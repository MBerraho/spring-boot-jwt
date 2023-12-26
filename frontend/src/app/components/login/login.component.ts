// login.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  credentials = { email: '', password: '' };

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login(this.credentials).subscribe(response => {
      // Handle successful login
      this.authService.setAuthToken(response.token);

      // Check user role and navigate accordingly
      if (response.role === 'ADMIN') {
        this.router.navigate(['/admin-home']);
      } else if (response.role === 'USER') {
        this.router.navigate(['/user-home']);
      } else {
        this.router.navigate(['/default-home']); // Navigate to a default route
      }
    });
  }
}
