import { Component, OnDestroy, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { CommonModule } from '@angular/common';
import { Cart } from '../../models/cart';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
})
export class CartComponent implements OnInit, OnDestroy {
  cartItems: Cart[] = [];
  private cartsubscriptions: Subscription[] = [];
  total: number = 0;

  constructor(
    private cartService: CartService,
    private OrderService: OrderService
  ) {}

  updateTotal() {
    this.total = this.cartItems.reduce((sum, item) => {
      if (item.product) {
        return (sum ?? 0) + item.product.price * item.quantity;
      }
      return sum;
    }, 0);
  }
  ngOnInit(): void {
    const ALLCartsSub = this.cartService.getAllCartItems().subscribe(
      (data) => {
        this.cartItems = data.data;
        this.updateTotal();
      },
      (error) => {
        console.error('Error fetching cart items', error);
      }
    );
    this.cartsubscriptions.push(ALLCartsSub);
    
  }

  increaseQuantity(cart: Cart) {
    cart.quantity += 1;
    this.updateTotal();
    this.updateCartItemInBackend(cart);
  }

  decreaseQuantity(cart: Cart) {
    if (cart.quantity > 1) {
      cart.quantity -= 1;
      this.updateTotal();
      this.updateCartItemInBackend(cart);
    }
  }

  updateCartItemInBackend(cart: Cart) {
    const increaseCartQuantity = this.cartService
      .updateCart(cart)
      .subscribe((data) => {});
    this.cartsubscriptions.push(increaseCartQuantity);
  }

  removeCartItem(id: number) {
    const removeCartItem = this.cartService
      .deleteCartItem(id)
      .subscribe((data) => {
        this.cartService.removeItemfromCart();
      });
    this.cartsubscriptions.push(removeCartItem);
    this.cartItems = this.cartItems.filter((item) => item.id !== id);
    this.updateTotal();
  }

  createOrder() {
    this.cartItems.forEach((itemm) => {
      this.cartService.removeItemfromCart();
    });
    this.cartItems.length = 0;
    const orderSub = this.OrderService.createOrder().subscribe();
    this.cartsubscriptions.push(orderSub);
  }

  ngOnDestroy() {
    this.cartsubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
