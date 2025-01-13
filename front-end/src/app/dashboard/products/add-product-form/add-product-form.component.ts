import { Component } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-product-form',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './add-product-form.component.html',
  styleUrl: './add-product-form.component.css'
})

export class AddProductFormComponent {
  newProduct: any = {};
  validationErrors: any = {};

  constructor(private productService: ProductService, private router: Router) {}

  cancelAddProduct() {
    this.router.navigate(['/adminDashboard/products']);
  }

  onFileChange(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      this.newProduct.image = file;
    }
  }

  submitAddProductForm() {
    const formData = new FormData();
    formData.append('title', this.newProduct.title);
    formData.append('description', this.newProduct.description);
    formData.append('price', this.newProduct.price);
    formData.append('stock', this.newProduct.stock);
    formData.append('image', this.newProduct.image);

    this.productService.createProduct(formData).subscribe(
      () => {
        this.router.navigate(['/adminDashboard/products']);
      },
      (error) => {
        this.validationErrors = error.error.errors;
      }
    );
  }
}
