import { Component, NgModule } from '@angular/core';
import { AdminsService } from '../../../services/admins.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-admin-form',
  standalone: true,
  imports: [CommonModule,FormsModule,RouterLink],
  templateUrl: './add-admin-form.component.html',
  styleUrl: './add-admin-form.component.css',
})

export class AddAdminFormComponent {
  newAdmin: any = {};
  validationErrors: any = {};

  constructor(private adminsService: AdminsService, private router: Router) {}

  cancelAddAdmin() {
    this.router.navigate(['/adminDashboard/admins']);
  }

  onFileChange(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      this.newAdmin.image = file;
    }
  }

  submitAddAdminForm() {
    const formData = new FormData();
    formData.append('name', this.newAdmin.name);
    formData.append('email', this.newAdmin.email);
    formData.append('password', this.newAdmin.password);
    formData.append('image', this.newAdmin.image);

    this.adminsService.createAdmin(formData).subscribe(
      () => {
        this.router.navigate(['/adminDashboard/admins']);
      },
      (error) => {
        this.validationErrors = error.error.errors;
      }
    );
  }
}
