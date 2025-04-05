import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private registerUrl = 'http://127.0.0.1:8080/api/v1/auth/register';
  private baseUrl = 'http://127.0.0.1:8080/api/v1/users';

  constructor(private http: HttpClient) { }

  getAllUsers(){
    return this.http.get(`${this.baseUrl}`);
  }

  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${userId}`);
  }

  createUser(user: any): Observable<User> {
    const formData = new FormData();
    formData.append('full_name', user.fullName);
    formData.append('email', user.email);
    formData.append('password', user.password);
    formData.append('gender', user.gender);
    if (user.image) {
      formData.append('image', user.image);
    }
    return this.http.post<User>(`${this.registerUrl}`, formData);
  }

  updateUser(userId: string, user: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${userId}`, user);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${userId}`);
  }
}
