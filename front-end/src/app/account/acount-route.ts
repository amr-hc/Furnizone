import { Routes } from '@angular/router';
import { RegisterComponent } from '../account/register/register.component';
import { LoginComponent } from '../account/login/login.component';
import { AccountViewComponent } from './account-view/account-view.component';


export const authRoutes: Routes = [
  {
    path: '',
    component: AccountViewComponent,
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full',
      },
      {
        path: 'registration',
        component: RegisterComponent,
      },
      { path: 'login',
       component: LoginComponent 
    },
      
    ],
  },
];
