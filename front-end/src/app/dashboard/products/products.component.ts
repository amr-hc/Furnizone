import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { Page } from '../../models/page';

@Component({
  selector: 'app-product',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
  standalone: true,
  imports: [CommonModule,RouterLink],
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  totalElements = 0;
  currentPage = 0;
  pageSize = 10;


  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe(
      (data: Page<Product>) => {
        this.products = data.content;
        this.totalElements = data.totalElements;

      },
      (error: any) => {
        console.error('Failed to fetch products', error);
      }
    );
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadProducts();
  }


  deleteProduct(productId: number): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.productService.deleteProduct(productId).subscribe(
          () => {
            this.products = this.products.filter(
              (product) => product.id !== productId
            );
            Swal.fire(
              'Deleted!',
              'The product has been deleted.',
              'success'
            );
          },
          (error: any) => {
            console.error('Failed to delete product', error);
          }
        );
      }
    });
  }
}
