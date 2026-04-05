import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, finalize, map, of, shareReplay, switchMap, tap, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import type { AppUserRole } from '../models/user-role';
import type { AuthResponse, LoginRequest, SignUpRequest } from '../models/auth.model';
import { StorageService } from './storage.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(StorageService);
  private readonly router = inject(Router);
  private readonly platformId = inject(PLATFORM_ID);

  /** Resolved after login / app init; ADMIN can access `/api/admin/**`. */
  readonly role = signal<AppUserRole | null>(null);

  private refresh$: Observable<void> | null = null;

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      const stored = this.storage.getStoredRole();
      if (stored === 'ADMIN' || stored === 'USER') {
        this.role.set(stored);
      }
    }
  }

  getAccessToken(): string | null {
    return this.storage.getAccessToken();
  }

  isLoggedIn(): boolean {
    return !!this.getAccessToken();
  }

  isAdmin(): boolean {
    return this.role() === 'ADMIN';
  }

  /** Home dashboard: admin vs regular user. */
  defaultHomePath(): string {
    return this.isAdmin() ? '/admin/dashboard' : '/dashboard';
  }

  login(body: LoginRequest): Observable<void> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/api/login`, body).pipe(
      tap((r) => this.persistTokens(r)),
      switchMap(() => this.probeRoleHttp()),
      map(() => void 0),
    );
  }

  signUp(body: SignUpRequest): Observable<void> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/api/sign-up`, body).pipe(
      tap((r) => this.persistTokens(r)),
      tap(() => {
        this.role.set('USER');
        this.storage.setStoredRole('USER');
      }),
      map(() => void 0),
    );
  }

  logout(): void {
    this.storage.clearAuth();
    this.role.set(null);
    void this.router.navigate(['/login']);
  }

  persistTokens(r: AuthResponse): void {
    this.storage.setAccessToken(r.accessToken);
    this.storage.setRefreshToken(r.refreshToken);
  }

  /**
   * Single-flight refresh used by the HTTP interceptor.
   */
  ensureRefreshed(): Observable<void> {
    if (this.refresh$) {
      return this.refresh$;
    }
    const rt = this.storage.getRefreshToken();
    if (!rt) {
      this.logout();
      return throwError(() => new Error('No refresh token'));
    }
    this.refresh$ = this.http
      .post<AuthResponse>(`${environment.apiUrl}/api/refresh`, { refreshToken: rt })
      .pipe(
        tap((r) => this.persistTokens(r)),
        map(() => void 0),
        catchError((err) => {
          this.logout();
          return throwError(() => err);
        }),
        finalize(() => {
          this.refresh$ = null;
        }),
        shareReplay(1),
      );
    return this.refresh$;
  }

  /** Call after restoring session to know if navbar should show admin links. */
  hydrateRoleIfNeeded(): Observable<void> {
    if (!isPlatformBrowser(this.platformId) || !this.isLoggedIn()) {
      return of(void 0);
    }
    if (this.role()) {
      return of(void 0);
    }
    return this.probeRoleHttp();
  }

  private probeRoleHttp(): Observable<void> {
    return this.http.get<unknown[]>(`${environment.apiUrl}/api/admin/destinations/suggestions`).pipe(
      tap(() => {
        this.role.set('ADMIN');
        this.storage.setStoredRole('ADMIN');
      }),
      map(() => void 0),
      catchError((err: { status?: number }) => {
        if (err.status === 403) {
          this.role.set('USER');
          this.storage.setStoredRole('USER');
          return of(void 0);
        }
        this.role.set('USER');
        this.storage.setStoredRole('USER');
        return of(void 0);
      }),
    );
  }
}
