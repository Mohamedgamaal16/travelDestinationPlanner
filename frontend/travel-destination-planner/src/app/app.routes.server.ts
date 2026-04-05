import { RenderMode, ServerRoute } from '@angular/ssr';

/** Client render avoids prerender-time API calls (JWT, localhost API). */
export const serverRoutes: ServerRoute[] = [
  {
    path: '**',
    renderMode: RenderMode.Client,
  },
];
