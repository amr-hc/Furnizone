

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {OrderService} from '../../services/order.service';
import { Order } from '../../models/order';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-user-orders',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './user-orders.component.html',
  styleUrl: './user-orders.component.css'
})
export class UserOrdersComponent {
  constructor(public OrderService: OrderService ,public router:Router) { }
  private ordersubscriptions: Subscription[] = [];
  orders : Order[]=this.OrderService.orders
  ngOnInit() {

    const ordersubscriptions = this.OrderService.getOrders().subscribe(data => {
      this.orders=data});
      this.ordersubscriptions.push(ordersubscriptions);
  }

  cancel(id : number,i :number) {
    const cancelsubscriptions = this.OrderService.cancel(id).subscribe(data=>console.log(data));
    this.orders[i].status = 'cancel';
    this.ordersubscriptions.push(cancelsubscriptions);
  }

  editStatus(e : any, id :number) {
    if(e.target.value == "cancel"){
      const editsubscriptions = this.OrderService.cancel(id).subscribe(data=>console.log(data));
      this.ordersubscriptions.push(editsubscriptions);

    }else if(e.target.value =="done"){
      const editsubscriptions = this.OrderService.done(id).subscribe(data=>console.log(data));
      this.ordersubscriptions.push(editsubscriptions);

    }
  }

  ngOnDestroy(){
    this.ordersubscriptions.forEach((sub) => sub.unsubscribe());
  }
}
