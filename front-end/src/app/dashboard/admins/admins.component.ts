import { Component, OnInit } from '@angular/core';
import { Admin } from '../../models/admin';
import { AdminsService } from '../../services/admins.service';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-admins',
  templateUrl: './admins.component.html',
  standalone: true,
  imports: [CommonModule,RouterLink],
  styleUrls: ['./admins.component.css'],
})

export class AdminsComponent implements OnInit {
  admins: Admin[] = [];

  constructor(private adminService: AdminsService) {}

  ngOnInit(): void {
    this.loadAdmins();
  }

  loadAdmins() {
    this.adminService.getAdmins().subscribe((data: Admin[]) => {
      this.admins = data;
    });
  }

  deleteAdmin(id: number | undefined) {
    if (id !== undefined) {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      }).then((result) => {
        if (result.isConfirmed) {
          this.adminService.deleteAdmin(id).subscribe(() => {
            Swal.fire(
              'Deleted!',
              'The admin has been deleted.',
              'success'
            );
            this.loadAdmins();
          });
        }
      });
    } else {
      console.error('Admin ID is undefined.');
    }
  }
}
