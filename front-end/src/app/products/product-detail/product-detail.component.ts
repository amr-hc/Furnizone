import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
  imports: [CommonModule],
})
export class ProductDetailComponent implements OnInit {
  product: Product;

  defaultImageUrl: string =
    '../.././../assets/images/registration/sofa-7354949_1280.png';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    protected authService:AuthService
  ) {
    this.product = {} as Product;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      const productId = +id;
      this.productService.getProductById(productId).subscribe({
        next: (product: Product) => {
          this.product = product;
        },
        error: (error) => {
          console.error('Error fetching product details:', error);
        },
      });
    } else {
      console.error('Product id not found in route parameters');
    }
  }

  addToCart() {
    const userId = 1; // Assume a hardcoded user ID for now
    const quantity = 1; // Assume a hardcoded quantity for now
    this.productService.addToCart(userId, this.product.id, quantity).subscribe({
      next: (response) => {
        Swal.fire({
          title: 'Success!',
          text: 'Product added to cart successfully.',
          icon: 'success',
          confirmButtonText: 'OK',
        });
      },
      error: (error) => {
        Swal.fire({
          title: 'Error!',
          text: 'There was an error adding the product to the cart.',
          icon: 'error',
          confirmButtonText: 'OK',
        });
        console.error('Error adding product to cart:', error);
      },
    });
  }
  setDefaultImage(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = this.defaultImageUrl;
  }
}
