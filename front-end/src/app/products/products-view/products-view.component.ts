import { Component } from '@angular/core';
import { NavbarComponent } from "../../layouts/navbar/navbar.component";
import { CarouselComponent } from "../../homePage/carousel/carousel.component";
import { AllProductsComponent } from "../all-products/all-products.component";
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from "../../layouts/footer/footer.component";


@Component({
    selector: 'app-products-view',
    standalone: true,
    templateUrl: './products-view.component.html',
    styleUrl: './products-view.component.css',
    imports: [NavbarComponent, CarouselComponent, AllProductsComponent, RouterOutlet, FooterComponent]
})

export class ProductsViewComponent {
    carouselImages = [
        { src: './assets/images/helo.jpg',height:'93vh', alt: 'Image 1' },
        { src: './assets/images/1 (3).jpg',height:'93vh', alt: 'Image 2' },
        { src: './assets/images/1 (2).jpg',height:'93vh', alt: 'Image 3' }
      ];
}
