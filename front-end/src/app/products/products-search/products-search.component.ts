import { Component, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { CarouselComponent } from '../../homePage/carousel/carousel.component';
import { SpinnerComponent } from '../../layouts/spinner/spinner.component';
import { NoResultComponent } from '../../layouts/no-result/no-result.component';
import { AllProductsComponent } from '../all-products/all-products.component';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models/cart';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-products-search',
  standalone: true,
  templateUrl: './products-search.component.html',
  styleUrl: './products-search.component.css',
  imports: [
    FormsModule,
    CommonModule,
    CarouselComponent,
    SpinnerComponent,
    NoResultComponent,
    AllProductsComponent,
  ],
})
export class ProductsSearchComponent implements OnDestroy {
  product: Product | null = null;
  isLoading = true;
  cartItems: Cart[] = [];
  private routeSubscription: Subscription | null = null;
  private productSubscription: Subscription | null = null;
  private cartsubscriptions: Subscription[] = [];

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    protected authService: AuthService
  ) {}

  ngOnInit() {
    this.routeSubscription = this.route.queryParams.subscribe((params) => {
      const title = params['title'];
      if (title) {
        this.productSubscription = this.productService
          .getProductByName(title)
          .subscribe({
            next: (products: Product[]) => {
              this.product = products[0];
              this.isLoading = false;
            },
            error: (error: any) => {
              console.error('Error fetching product:', error);
              this.isLoading = false;
            },
          });
      }
    });
  }

  loadCartItems() {
    const cartSub = this.cartService
      .getAllCartItems()
      .subscribe((cartItems) => {
        this.cartItems = cartItems.data;
      });
    this.cartsubscriptions.push(cartSub);
  }

  addToCart(product_id: number) {
    if (this.isCartItemExisting(product_id)) {
      console.log('cart item already exists');
      return;
    }
    const cartItem = this.createCartItem(product_id);
    this.addCartItemToBackend(cartItem);
  }

  isCartItemExisting(product_id: number): boolean {
    return this.cartItems.some(
      (obj) => obj.user_id === 1 && obj.product_id === product_id
    );
  }

  // TODO: Send only the product ID after finishing backend authentication.
  createCartItem(product_id: number): Cart {
    return {
      product_id,
      user_id: 1,
      quantity: 1,
      product: null,
      id: this.cartItems.length + 1,
    };
  }

  addCartItemToBackend(cartItem: Cart) {
    const addCartSub = this.cartService.addCartItem(cartItem).subscribe(
      (data) => {
        this.cartItems.push(cartItem);
        this.cartService.pushIteminCart();
      },
      (error) => {
        console.error('Error posting cart item', error);
      }
    );
    this.cartsubscriptions.push(addCartSub);
  }

  ngOnDestroy() {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
    if (this.productSubscription) {
      this.productSubscription.unsubscribe();
    }

    this.cartsubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
