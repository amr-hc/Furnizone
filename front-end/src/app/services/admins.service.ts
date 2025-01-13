import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Admin } from '../models/admin';

@Injectable({
  providedIn: 'root',
})
export class AdminsService {
  private apiUrl = 'http://127.0.0.1:8000/api/admins';

  constructor(private http: HttpClient) {}

  getAdmins(): Observable<Admin[]> {
    return this.http.get<Admin[]>(this.apiUrl);
  }

  getAdmin(id: number): Observable<Admin> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Admin>(url);
  }

  createAdmin(admin: FormData): Observable<Admin> {
    return this.http.post<Admin>(this.apiUrl, admin);
  }

  updateAdmin(id: number, admin: FormData): Observable<any> {
    const url = `${this.apiUrl}/${id}?_method=PATCH`;
    return this.http.post(url, admin);
  }

  deleteAdmin(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete(url);
  }
}
