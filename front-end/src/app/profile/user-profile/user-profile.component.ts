import { Component } from '@angular/core';
import { UserInfoComponent } from '../user-info/user-info.component';
import { OrdersComponent } from '../../orders/orders.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [UserInfoComponent, OrdersComponent],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {

}
