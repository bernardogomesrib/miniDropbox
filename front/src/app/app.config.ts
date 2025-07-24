import { ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { provideToastr, ToastrModule } from 'ngx-toastr';
import { provideAnimations } from '@angular/platform-browser/animations';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { keycloakHttpInterceptor } from '../../utils/http/keycloak-http.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([keycloakHttpInterceptor])),
    provideAnimations(),
    provideToastr({
      progressBar: true,
      tapToDismiss: true,
      timeOut: 3000,
      closeButton: true,
      positionClass: "toast-bottom-right",
      preventDuplicates: true,
      extendedTimeOut: 1000,
    }),
    provideAppInitializer(() => {
      const keycloakService = inject(KeycloakService);
      return keycloakService.init().catch(error => {
        console.error('Failed to initialize Keycloak:', error);
        console.warn('Application will continue without authentication');
        // Retorna uma promise resolvida para não bloquear a aplicação
        return Promise.resolve(false);
      });
    }),
  ],
};
