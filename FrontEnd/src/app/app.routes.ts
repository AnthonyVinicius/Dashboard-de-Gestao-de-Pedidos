import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { OrderCreateComponent } from './pages/order-create/order-create.component';
import { OrderComponent } from './pages/order-list/order.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'orders',
    component: OrderComponent,
    canActivate: [authGuard],
  },
  {
    path: 'order/create',
    component: OrderCreateComponent,
    canActivate: [authGuard],
  },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];