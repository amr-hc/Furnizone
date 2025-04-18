import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private baseUrl = 'http://127.0.0.1:8080/api/v1';

  constructor(private http: HttpClient) {}

  getProducts(page: number, size: number): Observable<Page<Product>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Product>>(`${this.baseUrl}/products`, { params });
  }

  getProductsForAdmins(page: number, size: number): Observable<Page<Product>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Product>>(`${this.baseUrl}/products/admin`, { params });
  }

  getProductByName(title: string): Observable<Product[]> {
    const url = `${this.baseUrl}/products/search?title=${encodeURIComponent(title)}`;
    return this.http.get<Product[]>(url);
  
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
    return this.http.patch<Product>(
      `${this.baseUrl}/products/${productId}`,
      product
    );
  }

  deleteProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/products/${productId}`);
  }
}
