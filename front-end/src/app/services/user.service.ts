import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://127.0.0.1:8080/api/v1/users';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}`);
  }

  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${userId}`);
  }

  createUser(user: any): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}`, user);
  }

  updateUser(userId: string, user: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${userId}`, user);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${userId}`);
  }
}
