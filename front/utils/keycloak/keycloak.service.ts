import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import Keycloak from 'keycloak-js';
import { environment } from '../../src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;
  private _initialized: boolean = false;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
  }

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      });
    }
    return this._keycloak;
  }

  // Método para verificar se o Keycloak está acessível
  private async checkKeycloakAvailability(): Promise<boolean> {
    try {
      const realmUrl = `${environment.keycloak.url}/realms/${environment.keycloak.realm}`;
      console.log('Checking Keycloak availability at:', realmUrl);
      
      const response = await this.http.get(realmUrl, { 
        responseType: 'json',
        observe: 'response' 
      }).toPromise();
      
      console.log('Keycloak is available:', response?.status === 200);
      return response?.status === 200;
    } catch (error) {
      console.error('Keycloak is not available:', error);
      return false;
    }
  }

  async init() {
    // Evitar múltiplas inicializações
    if (this._initialized) {
      console.log('Keycloak already initialized, returning current state');
      return this.keycloak.authenticated || false;
    }

    try {
      console.log('Initializing Keycloak with config:', {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      });

      // Verificar se o Keycloak está disponível primeiro
      const isKeycloakAvailable = await this.checkKeycloakAvailability();
      if (!isKeycloakAvailable) {
        console.warn('Keycloak server is not available. Skipping authentication.');
        this._initialized = true;
        return false;
      }

      // Tentar inicialização única com configuração simplificada
      const authenticated = await this.keycloak.init({
        onLoad: 'check-sso',
        checkLoginIframe: false,
        enableLogging: true,
        flow: 'implicit',
        responseMode: 'fragment',
        redirectUri: window.location.origin + '/'
      });
      
      this._initialized = true;
      console.log('Keycloak authenticated:', authenticated);
      
      return authenticated;
      
    } catch (error) {
      console.error('Keycloak initialization failed:', error);
      this._initialized = true; // Marcar como inicializado mesmo com erro
      console.log('Authentication will be completely manual');
      return false;
    }
  }

  async login() {
    console.log('Manual login initiated');
    
    // Se o Keycloak não foi inicializado corretamente, usar URL manual
    if (!this._initialized || !this.keycloak.authenticated) {
      const loginUrl = this.buildLoginUrl();
      console.log('Using manual redirect to:', loginUrl);
      window.location.href = loginUrl;
      return;
    }
    
    // Tentar login normal se o Keycloak estiver funcionando
    try {
      await this.keycloak.login({
        redirectUri: window.location.origin + '/'
      });
    } catch (error) {
      console.error('Normal login failed, using fallback:', error);
      const loginUrl = this.buildLoginUrl();
      console.log('Using manual redirect to:', loginUrl);
      window.location.href = loginUrl;
    }
  }

  // Método para construir URL de login manualmente (funciona sem Web Crypto API)
  private buildLoginUrl(): string {
    const baseUrl = `${environment.keycloak.url}/realms/${environment.keycloak.realm}/protocol/openid-connect/auth`;
    const params = new URLSearchParams({
      client_id: environment.keycloak.clientId,
      redirect_uri: window.location.origin + '/',
      response_type: 'token',
      scope: 'openid profile email',
      state: Math.random().toString(36).substring(2, 15), // State simples sem crypto
      nonce: Math.random().toString(36).substring(2, 15)  // Nonce simples sem crypto
    });
    
    return `${baseUrl}?${params.toString()}`;
  }

  // Método melhorado para processar token da URL
  processTokenFromUrl(): boolean {
    const hash = window.location.hash;
    if (hash && hash.includes('access_token')) {
      console.log('Found access token in URL hash');
      
      // Extrair parâmetros do hash
      const params = new URLSearchParams(hash.substring(1));
      const accessToken = params.get('access_token');
      const tokenType = params.get('token_type');
      const expiresIn = params.get('expires_in');
      
      if (accessToken) {
        console.log('Access token extracted:', {
          tokenType,
          expiresIn,
          tokenLength: accessToken.length
        });
        
        // Se o Keycloak não conseguiu processar, armazenar manualmente
        if (!this.keycloak.authenticated) {
          console.log('Storing token manually since Keycloak failed to process it');
          // Armazenar no localStorage como backup
          localStorage.setItem('access_token', accessToken);
          localStorage.setItem('token_type', tokenType || 'Bearer');
          localStorage.setItem('expires_in', expiresIn || '3600');
          localStorage.setItem('token_timestamp', Date.now().toString());
        }
        
        // Limpar a URL para não ficar com o token visível
        window.history.replaceState({}, document.title, window.location.pathname);
        
        return true;
      }
    }
    return false;
  }

  // Método para verificar se temos token manual
  hasManualToken(): boolean {
    const token = localStorage.getItem('access_token');
    const timestamp = localStorage.getItem('token_timestamp');
    const expiresIn = localStorage.getItem('expires_in');
    
    if (token && timestamp && expiresIn) {
      const tokenAge = (Date.now() - parseInt(timestamp)) / 1000; // em segundos
      const expiresInSec = parseInt(expiresIn);
      
      if (tokenAge < expiresInSec) {
        console.log('Valid manual token found');
        return true;
      } else {
        console.log('Manual token expired, clearing...');
        this.clearManualToken();
      }
    }
    
    return false;
  }

  // Método para limpar token manual
  clearManualToken(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('token_type');
    localStorage.removeItem('expires_in');
    localStorage.removeItem('token_timestamp');
  }

  // Override dos getters para incluir token manual
  get isAuthenticated(): boolean {
    return this.keycloak.authenticated || this.hasManualToken();
  }

  get userId(): string {
    if (this.keycloak.authenticated) {
      return this.keycloak?.tokenParsed?.sub as string;
    }
    // Para token manual, retornar um ID genérico
    return this.hasManualToken() ? 'manual-user' : '';
  }

  get isTokenValid(): boolean {
    if (this.keycloak.authenticated) {
      return !this.keycloak.isTokenExpired();
    }
    return this.hasManualToken();
  }

  get fullName(): string {
    if (this.keycloak.authenticated) {
      return this.keycloak.tokenParsed?.['name'] as string;
    }
    // Para token manual, retornar um nome genérico
    return this.hasManualToken() ? 'Usuário Autenticado' : '';
  }

  logout() {
    // Limpar tanto o Keycloak quanto o token manual
    this.clearManualToken();
    
    if (this.keycloak.authenticated) {
      return this.keycloak.logout({
        redirectUri: window.location.origin + '/'
      });
    } else {
      // Se não tem Keycloak, apenas recarregar a página
      window.location.reload();
      return Promise.resolve();
    }
  }

  accountManagement() {
    return this.keycloak.accountManagement();
  }

  // Método para debug
  getKeycloakInfo() {
    return {
      authenticated: this.keycloak.authenticated,
      token: this.keycloak.token ? 'Present' : 'Not present',
      refreshToken: this.keycloak.refreshToken ? 'Present' : 'Not present',
      tokenExpired: this.keycloak.isTokenExpired(),
      config: {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      }
    };
  }
}
