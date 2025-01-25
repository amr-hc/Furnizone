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
  pagedProducts: Product[] = [];
  cartItems: Cart[] = [];
  currentPage = 0;
  pageSize = 5;
  totalProductCount = 0;
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
    this.loadProducts(this.currentPage, this.pageSize);
    this.loadCartItems();
  }

  loadProducts(page: number, size: number) {
    this.productsSubscription = this.productService
      .getProducts(page, size)
      .subscribe((products) => {
        this.pagedProducts = products.content;
        this.totalProductCount = products.totalElements;
        this.isLoading = false;
      });
  }
  loadCartItems() {
    const cartSub = this.cartService
      .getAllCartItems()
      .subscribe((cartItems) => {
        this.cartItems = cartItems;
      });
    this.cartsubscriptions.push(cartSub);
  }

  setPage(page: number) {
    this.loadProducts(page, this.pageSize)
  }

  nextPage() {
    if (this.currentPage * this.pageSize < this.totalProductCount) {
      ++this.currentPage;
      this.setPage(this.currentPage);
    }
  }
  addToCart(product_id: number) {
    const userString = localStorage.getItem('currentUser');
    const user_id = userString ? JSON.parse(userString).id : null;
    if (this.isCartItemExisting(product_id, user_id)) {
      console.log('cart item already exists');
      return;
    }
    const cartItem = this.createCartItem(product_id);
    this.addCartItemToBackend(cartItem);
  }

  isCartItemExisting(product_id: number, user_id: number): boolean {
    return this.cartItems.some(
      (obj) => obj.user_id === user_id && obj.product_id === product_id
    );
  }
  
  createCartItem(product_id: number): Cart {
    return {
      product_id,
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
    if (this.currentPage > 0) {
      --this.currentPage;
      this.setPage(this.currentPage);
    }
  }

  ngOnDestroy() {
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
    this.cartsubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
