import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye, faCartPlus } from '@fortawesome/free-solid-svg-icons';
import { SpinnerComponent } from '../../layouts/spinner/spinner.component';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models/cart';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-all-products',
  templateUrl: './all-products.component.html',
  standalone: true,
  styleUrls: ['./all-products.component.css'],
  imports: [
    FormsModule,
    CommonModule,
    RouterLink,
    FontAwesomeModule,
    SpinnerComponent,
  ],
})
export class AllProductsComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  pagedProducts: Product[] = [];
  cartItems: Cart[] = [];
  currentPage = 1;
  pageSize = 16;
  faEye = faEye;
  faCartPlus = faCartPlus;
  isLoading = true;
  private cartsubscriptions: Subscription[] = [];
  private productsSubscription: Subscription | undefined;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    protected authService: AuthService
  ) {}

  ngOnInit() {
    this.loadProducts();
    this.loadCartItems();
  }

  loadProducts() {
    this.productsSubscription = this.productService
      .getProducts()
      .subscribe((products) => {
        this.products = products;
        this.setPage(1);
        this.isLoading = false;
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

  setPage(page: number) {
    let startIndex: number;
    let endIndex: number;

    if (page === 1) {
      startIndex = 0;
    } else {
      startIndex = (page - 1) * this.pageSize;
    }

    endIndex = Math.min(startIndex + this.pageSize, this.products.length);
    this.pagedProducts = this.products.slice(startIndex, endIndex);
    this.currentPage = page;
  }

  nextPage() {
    if (this.currentPage * this.pageSize < this.products.length) {
      this.setPage(this.currentPage + 1);
    }
  }
  addToCart(product_id: number) {
    const userString = localStorage.getItem('currentUser');
    const user_id = userString ? JSON.parse(userString).id : null;
    if (this.isCartItemExisting(product_id, user_id)) {
      console.log('cart item already exists');
      return;
    }
    const cartItem = this.createCartItem(product_id, user_id);
    this.addCartItemToBackend(cartItem);
  }

  isCartItemExisting(product_id: number, user_id: number): boolean {
    return this.cartItems.some(
      (obj) => obj.user_id === user_id && obj.product_id === product_id
    );
  }
  
  createCartItem(product_id: number, user_id: number): Cart {
    return {
      product_id,
      user_id,
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

  prevPage() {
    if (this.currentPage > 1) {
      this.setPage(this.currentPage - 1);
    }
  }

  ngOnDestroy() {
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
    this.cartsubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
