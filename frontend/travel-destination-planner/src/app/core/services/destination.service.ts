import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import type { Destination } from '../models/destination.model';
import type { SpringPage } from '../models/spring-page.model';

@Injectable({ providedIn: 'root' })
export class DestinationService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/api/user/destinations`;

  list(page: number, size: number, sort = 'id,asc'): Observable<SpringPage<Destination>> {
    let params = new HttpParams().set('page', page).set('size', size);
    const [prop, dir] = sort.split(',');
    params = params.set('sort', `${prop},${dir || 'asc'}`);
    return this.http.get<SpringPage<Destination>>(this.base, { params });
  }

  search(
    name: string | undefined,
    approved: boolean,
    page: number,
    size: number,
    sort = 'id,asc',
  ): Observable<SpringPage<Destination>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size));
    const [prop, dir] = sort.split(',');
    params = params.set('sort', `${prop},${dir || 'asc'}`);
    params = params.set('approved', String(approved));
    if (name?.trim()) {
      params = params.set('name', name.trim());
    }
    return this.http.get<SpringPage<Destination>>(`${this.base}/search`, { params }).pipe(
      map((p) => ({
        ...p,
        content: p.content.map((d) => ({ ...d, approved })),
      })),
    );
  }

  getById(id: number): Observable<Destination> {
    return this.http.get<Destination>(`${this.base}/${id}`).pipe(map((d) => ({ ...d, approved: true })));
  }
}
