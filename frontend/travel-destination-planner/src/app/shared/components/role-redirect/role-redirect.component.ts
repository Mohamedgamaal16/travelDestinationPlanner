import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/** Sends `/` to the correct dashboard based on role. */
@Component({
  selector: 'app-role-redirect',
  standalone: true,
  template: '',
})
export class RoleRedirectComponent implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    void this.router.navigateByUrl(this.auth.defaultHomePath(), { replaceUrl: true });
  }
}
