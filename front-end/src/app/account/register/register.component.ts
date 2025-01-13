import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent implements OnInit,OnDestroy {
  sub:Subscription | null = null;
  registerForm = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: [
      '',
      [Validators.required, Validators.pattern(/^[a-zA-Z0-9]{8,}$/)],
    ],
    gender: ['', [Validators.required]],
  });
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    protected router: Router
  ) {}
  ngOnDestroy(): void {
    this.sub?.unsubscribe()
  }

  ngOnInit() {}

  onSubmit() {
    if (!this.registerForm.valid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    let { firstName, lastName, email, password, gender } =
      this.registerForm.value;
    const body = {
      full_name: `${firstName} ${lastName}`,
      email,
      password,
      gender,
    };
    this.sub=this.userService.createUser(body).subscribe((user) => {
      this.router.navigateByUrl('/login');
    });
  }

  get firstName() {
    return this.registerForm.get('firstName');
  }
  get lastName() {
    return this.registerForm.get('lastName');
  }
  get email() {
    return this.registerForm.get('email');
  }
  get password() {
    return this.registerForm.get('password');
  }
  get gender() {
    return this.registerForm.get('gender');
  }
}
