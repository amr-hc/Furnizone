import { Routes } from '@angular/router';
import { ProductsViewComponent } from './products-view/products-view.component';
import { AllProductsComponent } from './all-products/all-products.component';
import { ProductsSearchComponent } from './products-search/products-search.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';

export const productroutes: Routes = [
  {
    path: '',
    component: ProductsViewComponent,
    children: [
      {
        path: '',
        redirectTo: 'product-list',
        pathMatch: 'full',
      },
      {
        path: 'product-list',
        component: AllProductsComponent,
      },
      {
        path: 'product-search',
        component: ProductsSearchComponent,
      },
      {
        path: 'product-detail/:id',
        component: ProductDetailComponent,
      },
    ],
  },
];
