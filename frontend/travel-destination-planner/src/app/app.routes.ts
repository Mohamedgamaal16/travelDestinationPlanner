import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/role.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./shared/components/main-layout/main-layout.component').then(
        (m) => m.MainLayoutComponent,
      ),
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./shared/components/role-redirect/role-redirect.component').then(
            (m) => m.RoleRedirectComponent,
          ),
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/user/user-dashboard/user-dashboard.component').then(
            (m) => m.UserDashboardComponent,
          ),
      },
      {
        path: 'admin/dashboard',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/admin/admin-dashboard/admin-dashboard.component').then(
            (m) => m.AdminDashboardComponent,
          ),
      },
      {
        path: 'destinations',
        loadComponent: () =>
          import('./features/user/destinations/destination-list.component').then(
            (m) => m.DestinationListComponent,
          ),
      },
      {
        path: 'destinations/:id',
        loadComponent: () =>
          import('./features/user/destinations/destination-detail.component').then(
            (m) => m.DestinationDetailComponent,
          ),
      },
      {
        path: 'wishlist',
        loadComponent: () =>
          import('./features/user/wishlist/wishlist-page.component').then(
            (m) => m.WishlistPageComponent,
          ),
      },
      {
        path: 'admin/destinations',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/admin/admin-destinations/admin-destinations.component').then(
            (m) => m.AdminDestinationsComponent,
          ),
      },
      {
        path: 'admin/suggestions',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/admin/admin-suggestions/admin-suggestions.component').then(
            (m) => m.AdminSuggestionsComponent,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '/dashboard' },
];
