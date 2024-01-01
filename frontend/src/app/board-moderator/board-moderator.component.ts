import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Role, User } from '../models/user';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './board-moderator.component.html',
  styleUrls: ['./board-moderator.component.css']
})
export class BoardModeratorComponent implements OnInit {
  users: User[] = [];
  newUser!: User;
  currentUser!: User;
  roles: Role[] = [];
  errorMessage: string = '';
  selectedUser: User | null = null;

  constructor(private userService: UserService, private storageService: StorageService, private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.storageService.getUser();
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

  addNewUser() {
    this.router.navigateByUrl('/new-user');
  }
}
