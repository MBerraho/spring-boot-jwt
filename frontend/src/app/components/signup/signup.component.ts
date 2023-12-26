// signup.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  user = { fullName: '', email: '', password: '' };

  constructor(private authService: AuthService) {}

  signup() {
    this.authService.signup(this.user).subscribe(response => {
      // Handle successful signup, navigate to login, etc.
      console.log(response);
    });
  }
}
