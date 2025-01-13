import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { Product } from '../../../models/product';
import { Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-product-form.component.html',
  styleUrls: ['./edit-product-form.component.css'],
})
export class EditProductFormComponent implements OnInit {
  productId!: number;
  product: Product = {
    id: 0,
    title: '',
    description: '',
    price: 0,
    image: '',
    stock: 0,
    image_url: '',
  };
  error!: string;
  routeParamsSubscription!: Subscription;
  validationErrors: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.routeParamsSubscription = this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.productId = +params.get('id')!;
          return this.productService.getProductById(this.productId);
        })
      )
      .subscribe(
        (data: Product) => {
          this.product = data;
        },
        (error: any) => {
          this.error = 'Failed to load product details';
        }
      );
  }

  ngOnDestroy(): void {
    this.routeParamsSubscription.unsubscribe();
  }

  onSubmit(): void {
    const formData = new FormData();

    formData.append('title', this.product.title || '');
    formData.append('description', this.product.description || '');
    formData.append(
      'price',
      this.product.price !== null && this.product.price !== undefined
        ? this.product.price.toString()
        : ''
    );
    formData.append(
      'stock',
      this.product.stock !== null && this.product.stock !== undefined
        ? this.product.stock.toString()
        : ''
    );

    if (
      typeof this.product.image === 'object' &&
      this.product.image instanceof File
    ) {
      formData.append('image', this.product.image);
    }

    this.productService.updateProduct(this.productId, formData).subscribe(
      () => {
        this.router.navigate(['/adminDashboard/products']);
      },
      (error: any) => {
        this.validationErrors = error.error.errors;
        this.error = 'Failed to update product';
      }
    );
  }

  onFileChange(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      this.product.image = file;
    }
  }

  goBack(): void {
    this.router.navigate(['/adminDashboard/products']);
  }
}
