import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {OrderService} from '../services/order.service';
import { Order } from '../models/order';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit{

  constructor(public OrderService: OrderService ,public router:Router) { }
  private ordersubscriptions: Subscription[] = [];
  orders : Order[]=this.OrderService.orders
  ngOnInit() {
    const ordersubscriptions =this.OrderService.getUserOrders().subscribe(data => {
      this.orders=data.data
    });

    this.ordersubscriptions.push(ordersubscriptions);
  }

  cancel(id : number,i :number) {
    const cancelsubscriptions = this.OrderService.cancel(id).subscribe(data=>console.log(data));
    this.orders[i].status = 'cancel';
    this.ordersubscriptions.push(cancelsubscriptions);
  }

  ngOnDestroy(){
    this.ordersubscriptions.forEach((sub) => sub.unsubscribe());
  }

}
