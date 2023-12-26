import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Role, User } from '../models/user';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {


  users: User[] = [];
  roles: Role[] = [];
  errorMessage: string = '';
  selectedUser: User | null = null;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe({
      next: (data: User[]) => {
        this.users = data;
      },
      error: (err) => {
        if (err.error) {
          try {
            const res = JSON.parse(err.error);
            this.errorMessage = res.message;
          } catch {
            this.errorMessage = `Error with status: ${err.status} - ${err.statusText}`;
          }
        } else {
          this.errorMessage = `Error with status: ${err.status}`;
        }
      }
    });
    this.userService.getRoles().subscribe({
      next: (data: Role[]) => {
        this.roles = data;
      },
      error: (err) => {
        if (err.error) {
          try {
            const res = JSON.parse(err.error);
            this.errorMessage = res.message;
          } catch {
            this.errorMessage = `Error with status: ${err.status} - ${err.statusText}`;
          }
        } else {
          this.errorMessage = `Error with status: ${err.status}`;
        }
      }
    });
  }

  update(user: User) {
    this.selectedUser = user;
  }

  delete(user: User) {
    this.userService.deleteUser(user).subscribe(
      () => {
        this.ngOnInit();
        this.selectedUser = null;
      },
      (error) => {
        console.error('Error updating user:', error);
      }
    );
  }

  save(updatedUser: User) {
    if (!updatedUser.roles || !Array.isArray(updatedUser.roles)) {
      updatedUser.roles = [];
    }
    this.userService.updateUser(updatedUser).subscribe(
      () => {
        this.ngOnInit();
        this.selectedUser = null;
      },
      (error) => {
        console.error('Error updating user:', error);
      }
    );
  }

  cancel() {
    this.selectedUser = null;
  }
}