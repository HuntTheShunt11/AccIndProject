import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Person {
  id: number;
  lastName: string;
  firstName: string;
  email: string;
}

export interface Incident {
  id: string;
  title: string;
  description: string;
  severity: string;
  createdAt: string;
  owner: Person;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // Current page number
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080/incidents';

  constructor(private http: HttpClient) {}

  searchIncidents(
    filters: { title?: string; description?: string; severity?: string; owner?: string },
    page = 0,
    size = 10
  ): Observable<Page<Incident>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    Object.entries(filters).forEach(([key, value]) => {
      if (value) {
        params = params.set(key, value);
      }
    });

    return this.http.get<Page<Incident>>(this.baseUrl, { params });
  }
}
