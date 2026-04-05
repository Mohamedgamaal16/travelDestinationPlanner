import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-auth-toolbar',
  imports: [RouterLink, MatToolbarModule, MatButtonModule],
  templateUrl: './auth-toolbar.component.html',
  styleUrl: './auth-toolbar.component.css',
})
export class AuthToolbarComponent {}
