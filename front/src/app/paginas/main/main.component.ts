
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrpcRastreioService } from '../servicos/grpc-rastreio.service';
import { ToastrService } from 'ngx-toastr';
import { KeycloakService } from '../../../../utils/keycloak/keycloak.service';
import { Carro } from './util/types';

@Component({
  selector: 'app-main',
  imports: [CommonModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent implements OnInit {
  // Variáveis para dados e posição...
  carros: Carro[] = [];
  constructor(
    private toaster: ToastrService, 
    private service: GrpcRastreioService,
    public keycloakService: KeycloakService
  ) { }
  posicaoX: number = -1;
  posicaoY: number = -1;
  tempX: number = 0;
  tempY: number = 0;
  listaIds: string[] = [];
  ngOnInit(): void {
    // Verificar se há token na URL após redirect do Keycloak
    const hasTokenInUrl = this.keycloakService.processTokenFromUrl();
    
    console.log('Authentication status on init:', {
      hasTokenInUrl,
      authenticated: this.keycloakService.isAuthenticated,
      tokenValid: this.keycloakService.isAuthenticated ? this.keycloakService.isTokenValid : false,
      userName: this.keycloakService.isAuthenticated ? this.keycloakService.fullName : 'Not authenticated',
      hasManualToken: this.keycloakService.hasManualToken()
    });
  }

  // Método para login manual
  doLogin() {
    console.log('Initiating manual login...');
    console.log('Current authentication state:', {
      isAuthenticated: this.keycloakService.isAuthenticated,
      hasManualToken: this.keycloakService.hasManualToken()
    });
    this.keycloakService.login();
  }

  // Método para logout
  doLogout() {
    this.keycloakService.logout();
  }
  private intervalId: any;

}
