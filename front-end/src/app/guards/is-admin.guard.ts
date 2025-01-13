import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const isAdminGuard: CanActivateFn = (route, state) => {
  let authService = inject(AuthService);
  const router = inject(Router);
  if (authService.role != 'admin') {
    router.navigateByUrl('unauthorized');
    return false;
  }
  return true;
};
