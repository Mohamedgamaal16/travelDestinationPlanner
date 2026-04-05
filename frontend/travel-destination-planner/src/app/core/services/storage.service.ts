import { isPlatformBrowser } from '@angular/common';
import { inject, Injectable, PLATFORM_ID } from '@angular/core';

const ACCESS = 'tdp_access_token';
const REFRESH = 'tdp_refresh_token';
const ROLE = 'tdp_role';

@Injectable({ providedIn: 'root' })
export class StorageService {
  private readonly platformId = inject(PLATFORM_ID);

  getAccessToken(): string | null {
    return this.getItem(ACCESS);
  }

  setAccessToken(token: string): void {
    this.setItem(ACCESS, token);
  }

  getRefreshToken(): string | null {
    return this.getItem(REFRESH);
  }

  setRefreshToken(token: string): void {
    this.setItem(REFRESH, token);
  }

  getStoredRole(): string | null {
    return this.getItem(ROLE);
  }

  setStoredRole(role: string): void {
    this.setItem(ROLE, role);
  }

  clearAuth(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    localStorage.removeItem(ACCESS);
    localStorage.removeItem(REFRESH);
    localStorage.removeItem(ROLE);
  }

  private getItem(key: string): string | null {
    if (!isPlatformBrowser(this.platformId)) return null;
    return localStorage.getItem(key);
  }

  private setItem(key: string, value: string): void {
    if (!isPlatformBrowser(this.platformId)) return;
    localStorage.setItem(key, value);
  }
}
