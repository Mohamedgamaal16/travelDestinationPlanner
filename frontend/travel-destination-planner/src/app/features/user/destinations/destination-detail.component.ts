import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import type { Destination } from '../../../core/models/destination.model';
import { DestinationService } from '../../../core/services/destination.service';
import { WishlistService } from '../../../core/services/wishlist.service';
import { NotificationService } from '../../../core/services/notification.service';
import { apiErrorMessage } from '../../../core/utils/api-error';

@Component({
  selector: 'app-destination-detail',
  imports: [
    DecimalPipe,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './destination-detail.component.html',
  styleUrl: './destination-detail.component.css',
})
export class DestinationDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly destinationsApi = inject(DestinationService);
  private readonly wishlistApi = inject(WishlistService);
  private readonly notify = inject(NotificationService);

  protected readonly loading = signal(true);
  protected readonly destination = signal<Destination | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isFinite(id)) {
      this.loading.set(false);
      return;
    }
    this.destinationsApi.getById(id).subscribe({
      next: (d) => {
        this.destination.set(d);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.error(apiErrorMessage(err, 'Destination not found'));
      },
    });
  }

  addWishlist(): void {
    const d = this.destination();
    if (!d || d.id == null) return;
    this.wishlistApi.add(d.id).subscribe({
      next: () => this.notify.success('Added to wishlist'),
      error: (err) => this.notify.error(apiErrorMessage(err, 'Could not add to wishlist')),
    });
  }
}
