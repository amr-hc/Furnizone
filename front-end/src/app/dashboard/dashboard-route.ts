import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { AdminsComponent } from './admins/admins.component';
import { AddAdminFormComponent } from './admins/add-admin-form/add-admin-form.component';
import { UsersComponent } from './users/users.component';
import { ProductsComponent } from './products/products.component';
import { AddProductFormComponent } from './products/add-product-form/add-product-form.component';
import { EditProductFormComponent } from './products/edit-product-form/edit-product-form.component';
import { ShowProductComponent } from './products/show-product/show-product.component';
import { UserOrdersComponent } from './user-orders/user-orders.component';
import { AdminDetailsComponent } from './admins/admin-details/admin-details.component';
import { EditAdminFormComponent } from './admins/edit-admin-form/edit-admin-form.component';

export const adminRoutes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      { path: 'admins', component: AdminsComponent },
      { path: 'admins/add-admin', component: AddAdminFormComponent },
      { path: 'details', component: AdminDetailsComponent },
      { path: 'details/edit/:id', component: EditAdminFormComponent },
      { path: 'users', component: UsersComponent },
      { path: 'products', component: ProductsComponent },
      { path: 'products/add-product', component: AddProductFormComponent },
      {
        path: 'products/edit-product/:id',
        component: EditProductFormComponent,
      },
      { path: 'products/:id', component: ShowProductComponent },
      { path: 'orders', component: UserOrdersComponent },
    ],
  },
];
