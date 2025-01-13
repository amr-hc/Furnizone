import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MiniCarouselComponent } from "../mini-carousel/mini-carousel.component";


@Component({
    selector: 'app-carousel',
    standalone: true,
    templateUrl: './carousel.component.html',
    styleUrl: './carousel.component.css',
    imports: [CommonModule, MiniCarouselComponent]
})
export class CarouselComponent {

  @Input() images2: { src: string,height:string, alt: string }[] = [];


}
