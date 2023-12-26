import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role, User } from '../models/user';

const API_URL = 'http://localhost:8089/api/test/';

@Injectable({
  providedIn: 'root',
})
export class UserService {


  constructor(private http: HttpClient) { }

  private addTokenToHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  }

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL + 'all', { headers: this.addTokenToHeaders(), responseType: 'text' });
  }

  getUserBoard(): Observable<any> {
    return this.http.get(API_URL + 'user', { headers: this.addTokenToHeaders(), responseType: 'text' });
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(API_URL + 'mod', { headers: this.addTokenToHeaders(), responseType: 'text' });
  }


  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_URL + 'allUsers', { headers: this.addTokenToHeaders() });
  }

  updateUser(updatedUser: User): Observable<User> {
    const updateUrl = `${API_URL}updateUser/${updatedUser.id}`;
    const headers = this.addTokenToHeaders();
    return this.http.put<User>(updateUrl, updatedUser, { headers });
  }

  getRoles(): Observable<Role[]> {
    const updateUrl = `${API_URL}allRoles`;
    const headers = this.addTokenToHeaders();
    return this.http.get<Role[]>(updateUrl, { headers });
  }

  deleteUser(user: User): Observable<any> {
    const url = `${API_URL}deleteUser/${user.id}`;
    const headers = this.addTokenToHeaders();
    return this.http.delete(url, { headers });
  }
}