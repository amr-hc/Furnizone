import { Routes } from '@angular/router';
import { HomeComponent } from './homePage/home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthGuard } from './guards/auth.guard';
import { isAdminGuard } from './guards/is-admin.guard';
import { isUserGuard } from './guards/is-user.guard';
import { AboutComponent } from './about/about.component';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: 'about',
    component: AboutComponent,
  },
  {
    path: 'products',
    loadChildren: () =>
      import('./products/product.route').then((m) => m.productroutes),
    canActivate: [AuthGuard],
  },
  {
    path: 'profile',
    loadChildren: () =>
      import('./profile/user.route').then((m) => m.userRoutes),
    canActivate: [isUserGuard],
  },
  {
    path: 'account',
    loadChildren: () =>
      import('./account/acount-route').then((m) => m.authRoutes),
  },
  {
    path: 'cart',
    loadChildren: () => import('./cart/cart-route').then((m) => m.cartRoutes),
    canActivate: [AuthGuard],
  },
  {
    path: 'adminDashboard',
    loadChildren: () =>
      import('./dashboard/dashboard-route').then((m) => m.adminRoutes),
    canActivate: [isAdminGuard],
  },

  {
    path: '**',
    component: NotFoundComponent,
  },
];
