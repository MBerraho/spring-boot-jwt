// admin-auth.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';// Import your AuthService
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AdminAuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isAdmin = this.authService.isAdmin(); // Adjust this based on your AuthService logic

    if (isAdmin) {
      return true; // Allow access to the route
    } else {
      this.router.navigate(['/default-home']); // Redirect to a default route if not authorized
      return false;
    }
  }
}
