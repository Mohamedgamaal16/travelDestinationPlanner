import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, map, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment';
import type { Destination, DestinationRequest } from '../models/destination.model';
import type { SpringPage } from '../models/spring-page.model';

@Injectable({ providedIn: 'root' })
export class AdminDestinationService {
  private readonly http = inject(HttpClient);
  private readonly userBase = `${environment.apiUrl}/api/user/destinations`;
  private readonly adminBase = `${environment.apiUrl}/api/admin/destinations`;

  private suggestionsCache$: Observable<Destination[]> | null = null;

  /**
   * Lists either approved or disapproved-only destinations (matches UserDestinationServiceImpl search).
   */
  listForAdmin(
    name: string | undefined,
    approved: boolean,
    page: number,
    size: number,
    sort = 'id,asc',
  ): Observable<SpringPage<Destination>> {
    const term = name?.trim() ?? '';
    const sortParam = sort.includes(',') ? sort : `${sort},asc`;
    let params = new HttpParams().set('page', String(page)).set('size', String(size)).set('sort', sortParam);
    params = params.set('approved', String(approved));
    if (term) params = params.set('name', term);
    return this.http.get<SpringPage<Destination>>(`${this.userBase}/search`, { params }).pipe(
      map((p) => ({
        ...p,
        content: p.content.map((d) => ({ ...d, approved })),
      })),
    );
  }

  /** Always hits the network (e.g. picker dialog). */
  fetchSuggestions(): Observable<Destination[]> {
    return this.http.get<Destination[]>(`${this.adminBase}/suggestions`);
  }

  getSuggestionsCached(): Observable<Destination[]> {
    if (!this.suggestionsCache$) {
      this.suggestionsCache$ = this.fetchSuggestions().pipe(
        shareReplay({ bufferSize: 1, refCount: false }),
      );
    }
    return this.suggestionsCache$;
  }

  clearSuggestionsCache(): void {
    this.suggestionsCache$ = null;
  }

  add(dto: DestinationRequest): Observable<Destination> {
    return this.http.post<Destination>(this.adminBase, dto);
  }

  bulk(dtos: DestinationRequest[]): Observable<Destination[]> {
    return this.http.post<Destination[]>(`${this.adminBase}/bulk`, dtos);
  }

  approve(id: number): Observable<Destination> {
    return this.http.patch<Destination>(`${this.adminBase}/${id}/approve`, {});
  }

  disapprove(id: number): Observable<Destination> {
    return this.http.patch<Destination>(`${this.adminBase}/${id}/disapprove`, {});
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.adminBase}/${id}`, { responseType: 'text' });
  }
}
