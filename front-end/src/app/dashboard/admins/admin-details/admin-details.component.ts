import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminsService } from '../../../services/admins.service';
import { HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-details',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './admin-details.component.html',
  styleUrls: ['./admin-details.component.css'],
})
export class AdminDetailsComponent implements OnInit {
  admin: any;
  isAdmin: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private adminsService: AdminsService,
    protected authService: AuthService
  ) {}

  ngOnInit() {
    const currentUser = localStorage.getItem('currentUser');
    const role = this.authService.role;
    if (currentUser && role) {
      const user = JSON.parse(currentUser);
      if (role === 'admin') {
        this.admin = user;
        this.isAdmin = true;
      }
    }
  }

  editAccount() {
    this.router.navigate(['/adminDashboard/details/edit', this.admin.id]);
  }

  deleteAccount() {
    if (
      confirm(
        'Are you sure you want to delete your account? This action cannot be undone.'
      )
    ) {
      this.adminsService
        .deleteAdmin(this.admin.id)
        .pipe(
          catchError((error) => {
            this.errorMessage = 'There was an error deleting the account.';
            console.error('Error deleting account:', error);
            return of(null);
          })
        )
        .subscribe((response) => {
          if (response !== null) {
            console.log('Account deleted successfully');
            localStorage.removeItem('currentUser');
            localStorage.removeItem('role');
            this.router.navigate(['/account/login']);
          }
        });
    }
  }
}
