import { Component, AfterViewInit, ElementRef } from '@angular/core';
import { FooterComponent } from '../../layouts/footer/footer.component';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-content',
  standalone: true,
  templateUrl: './content.component.html',
  styleUrl: './content.component.css',
  imports: [FooterComponent, CommonModule],
})
export class ContentComponent implements AfterViewInit {
  panels = [1, 2, 3, 4,5,6];

  constructor(private el: ElementRef) {}

  getPanelBackground(panel: number): string {
    const images = [
      './assets/images/ab1.jpg',
      './assets/images/h7-in-5.jpg',
      './assets/images/ab2.jpg',
      './assets/images/image-600x400-2.jpg',
      './assets/images/project5.jpg',
      './assets/images/project3.jpg',
    ];
    return `url(${images[panel - 1]})`;
  }



  ngAfterViewInit() {
    const textContainers = this.el.nativeElement.querySelectorAll('.text-container');
    
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.1 });

    textContainers.forEach((container: Element) => {
      observer.observe(container);
    });
  }

}
