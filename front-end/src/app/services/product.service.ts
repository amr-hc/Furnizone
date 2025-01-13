import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private baseUrl = 'http://127.0.0.1:8000/api';

  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/products`);
  }

  getProductByName(title: string): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/products/title/${title}`);
  }
  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/products/${productId}`);
  }
  addToCart(
    userId: number,
    productId: number,
    quantity: number
  ): Observable<any> {
    const cartItem = {
      user_id: userId,
      product_id: productId,
      quantity: quantity,
    };
    return this.http.post(`${this.baseUrl}/carts`, cartItem);
  }

  createProduct(product: FormData): Observable<Product> {
    return this.http.post<Product>(`${this.baseUrl}/products`, product);
  }

  updateProduct(productId: number, product: FormData): Observable<Product> {
    return this.http.post<Product>(
      `${this.baseUrl}/products/${productId}?_method=PATCH`,
      product
    );
  }

  deleteProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/products/${productId}`);
  }
}
