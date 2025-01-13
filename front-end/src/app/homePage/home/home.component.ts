import { Component} from '@angular/core';
import { CarouselComponent } from '../carousel/carousel.component';
import { ContentComponent } from '../content/content.component';
import { FooterComponent } from "../../layouts/footer/footer.component";

@Component({
    selector: 'app-home',
    standalone: true,
    templateUrl: './home.component.html',
    styleUrl: './home.component.css',
    imports: [CarouselComponent, ContentComponent, FooterComponent]
})
export class HomeComponent {
  carouselImages = [
    { src: './assets/images/slider-2-1.webp',height:'100vh', alt: 'Image 1' },
    { src: './assets/images/slider-2-2.webp',height:'100vh', alt: 'Image 2' },
    { src: './assets/images/service-backgroundimage.jpg',height:'100vh', alt: 'Image 3' }
  ];

}
