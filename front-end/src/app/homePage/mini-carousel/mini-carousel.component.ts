import { Component } from '@angular/core';
import { CarouselModule } from 'primeng/carousel';


@Component({
    selector: 'app-mini-carousel',
    standalone: true,
    templateUrl: './mini-carousel.component.html',
    styleUrls: ['./mini-carousel.component.css', '../carousel/carousel.component.css'],
    imports: [CarouselModule]
})
export class MiniCarouselComponent {
  images: any[] = [
    { src: './assets/images/armchair.png', alt: 'Armchair' },
    { src: './assets/images/bed.png', alt: 'Armchair' },
    { src: './assets/images/couch.png', alt: 'Armchair' },
    { src: './assets/images/floor-lamp.png', alt: 'Armchair' },
    { src: './assets/images/furniture.png', alt: 'Armchair' },
    { src: './assets/images/furniture (1).png', alt: 'Armchair' }
  
  ];
}
