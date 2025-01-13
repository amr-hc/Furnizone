import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminsService } from '../../../services/admins.service';
import { Admin } from '../../../models/admin';
import { Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-admin-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-admin-form.component.html',
  styleUrls: ['./edit-admin-form.component.css'],
})
export class EditAdminFormComponent implements OnInit, OnDestroy {
  adminId!: number;
  admin: Admin = {
    id: 0,
    name: '',
    email: '',
    image: '',
    image_url: '',
    password: '',
  };
  error!: string;
  routeParamsSubscription!: Subscription;
  validationErrors: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private adminsService: AdminsService
  ) {}

  ngOnInit(): void {
    this.routeParamsSubscription = this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.adminId = +params.get('id')!;
          return this.adminsService.getAdmin(this.adminId);
        })
      )
      .subscribe(
        (data: Admin) => {
          this.admin = data;
        },
        (error: any) => {
          this.error = 'Failed to load admin details';
        }
      );
  }

  ngOnDestroy(): void {
    this.routeParamsSubscription.unsubscribe();
  }

  onSubmit(): void {
    const formData = new FormData();

    formData.append('name', this.admin.name || '');
    formData.append('email', this.admin.email || '');
    if (this.admin.password) {
      formData.append('password', this.admin.password);
    }

    if (
      typeof this.admin.image === 'object' &&
      this.admin.image instanceof File
    ) {
      formData.append('image', this.admin.image);
    }

    this.adminsService.updateAdmin(this.adminId, formData).subscribe(
      () => {
        this.router.navigate(['/adminDashboard/admins']);
      },
      (error: any) => {
        this.validationErrors = error.error.errors;
        this.error = 'Failed to update admin';
      }
    );
  }

  onFileChange(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      this.admin.image = file;
    }
  }

  goBack(): void {
    this.router.navigate(['/adminDashboard/admins']);
  }
}
