import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const isUserGuard: CanActivateFn = (route, state) => {
  let authService = inject(AuthService);
  const router = inject(Router);
  if (authService.role != 'user') {
    router.navigateByUrl('unauthorized');
    return false;
  }
  return true;
};
