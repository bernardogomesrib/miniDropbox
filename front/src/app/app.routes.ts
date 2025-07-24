import { Routes } from '@angular/router';
import { MainComponent } from './paginas/main/main.component';
import { ExemploComponent } from './paginas/pagina-exemplo/exemplo/exemplo.component';

export const routes: Routes = [
  {
    path: '',
    component:MainComponent
  },
  // Rota de exemplo
  {
    path: '/exemplo',
    component: ExemploComponent
  }
];
