import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { OrderComponent } from './pages/order-list/order.component';
import { OrderCreateComponent } from './pages/order-create/order-create.component';

export const routes: Routes = [

    { path: "login", component: LoginComponent },
    { path: "register", component: RegisterComponent },
    {path: "dashboard", component: DashboardComponent},
    {path: 'order/create', component: OrderCreateComponent,},
    {path: 'orders',component: OrderComponent},
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' },
];
