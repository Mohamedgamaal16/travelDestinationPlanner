import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

function isAuthUrl(url: string): boolean {
  const path = url.replace(/^https?:\/\/[^/]+/, '');
  return (
    path.includes('/api/login') ||
    path.includes('/api/sign-up') ||
    path.includes('/api/refresh')
  );
}

function addBearer(req: Parameters<HttpInterceptorFn>[0], token: string | null) {
  if (!token || isAuthUrl(req.url)) return req;
  return req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const authedReq = addBearer(req, auth.getAccessToken());
  return next(authedReq).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status !== 401 || isAuthUrl(req.url)) {
        return throwError(() => err);
      }
      return auth.ensureRefreshed().pipe(
        switchMap(() => next(addBearer(req, auth.getAccessToken()))),
        catchError(() => throwError(() => err)),
      );
    }),
  );
};
