import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Person {
  id: number;
  lastName: string;
  firstName: string;
  email: string;
}

export interface Incident {
  id: number;
  title: string;
  description: string;
  severity: string;
  createdAt: string;
  owner: Person;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080/incidents';

  constructor(private http: HttpClient) {}

  searchIncidents(filters: {
    title?: string;
    description?: string;
    severity?: string;
    owner?: string;
  }): Observable<Incident[]> {
    let params = new HttpParams();
    if (filters.title) params = params.set('title', filters.title);
    if (filters.description) params = params.set('description', filters.description);
    if (filters.severity) params = params.set('severity', filters.severity);
    if (filters.owner) params = params.set('owner', filters.owner);
    return this.http.get<Incident[]>(this.baseUrl, { params });
  }
}
