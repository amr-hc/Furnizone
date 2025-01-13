import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-edit-info',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './edit-info.component.html',
  styleUrl: './edit-info.component.css',
})
export class EditInfoComponent implements OnInit, OnDestroy {
  currentUser: any;
  sub: Subscription | null = null;
  editForm = this.fb.group({
    full_name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    gender: ['', [Validators.required]],
    image: ['', [Validators.required]],
  });
  constructor(
    protected authService: AuthService,
    private fb: FormBuilder,
    private userService: UserService
  ) {}
  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }
  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe((user) => {
      this.currentUser = user;
    });
    this.editForm.patchValue({
      full_name: this.currentUser.full_name,
      email: this.currentUser.email,
      gender: this.currentUser.gender,
    });
  }

  onSubmit() {
    if (!this.editForm.valid) {
      this.editForm.markAllAsTouched();
      return;
    }
    console.log(this.editForm.value);
    
    // this.sub = this.userService
    //   .updateUser(this.currentUser.id, this.editForm.value)
    //   .subscribe((data) => {
    //     localStorage.setItem('currentUser', JSON.stringify(data.user));
    //     this.authService.currentUser = data.user;
    //   });
  }

  get fullName() {
    return this.editForm.get('full_name');
  }
  get email() {
    return this.editForm.get('email');
  }
  get gender() {
    return this.editForm.get('gender');
  }
  get image() {
    return this.editForm.get('image');
  }
}
