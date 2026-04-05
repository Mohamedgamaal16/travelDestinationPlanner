import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import type { SpringPage } from '../models/spring-page.model';
import type { WishlistAddRequest, WishlistEntry } from '../models/wishlist.model';

@Injectable({ providedIn: 'root' })
export class WishlistService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/api/user/wishlist`;

  list(page: number, size: number): Observable<SpringPage<WishlistEntry>> {
    const params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size))
      .set('sort', 'addedAt,desc');
    return this.http.get<SpringPage<WishlistEntry>>(this.base, { params });
  }

  add(destinationId: number): Observable<WishlistEntry> {
    const body: WishlistAddRequest = { destinationId };
    return this.http.post<WishlistEntry>(this.base, body);
  }

  removeByDestinationId(destinationId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${destinationId}`);
  }
}
