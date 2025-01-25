import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Subject, Observable, Subscription } from 'rxjs';
import { Cart } from '../models/cart';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  path: string = 'http://127.0.0.1:8080/api/v1/carts';
  public cartArray: Cart[] = [];
  public cartLength: number = 0;
  cartLengthSubject: Subject<number> = new Subject();
  private cartsubscriptions: Subscription[] = [];
  constructor(private http: HttpClient, protected authService: AuthService) {
    if (authService.role && authService.role !== 'admin') {
      this.updateCartLength();
    }
  }

  private updateCartLength() {
    const getCartItemLength = this.getAllCartItems().subscribe((data) => {
      this.cartArray = data.data;
      // this.cartLength = this.cartArray.length;
      this.cartLengthSubject.next(this.cartLength);
    });
    this.cartsubscriptions.push(getCartItemLength);
  }

  getAllCartItems() {
    return this.http.get<any>(this.path);
  }

  getOneCart(id: number) {
    return this.http.get<Cart>(`${this.path}${id}`);
  }

  addCartItem(cart: any) {
    return this.http.post(this.path, cart);
  }

  updateCart(cart: Cart) {
    return this.http.patch(`${this.path}${cart.id}`, cart);
  }

  deleteCartItem(id: number) {
    const options = {
      body: { user_id: 1 },
    };
    return this.http.delete(`${this.path}/${id}`, options);
  }

  pushIteminCart() {
    this.cartLength++;
    this.cartLengthSubject.next(this.cartLength);
  }

  removeItemfromCart() {
    this.cartLength--;
    this.cartLengthSubject.next(this.cartLength);
  }

  loginCase() {
    this.updateCartLength();
  }

  getIteminCart(): Observable<any> {
    return this.cartLengthSubject.asObservable();
  }

  ngOnDestroy(): void {
    this.cartsubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
