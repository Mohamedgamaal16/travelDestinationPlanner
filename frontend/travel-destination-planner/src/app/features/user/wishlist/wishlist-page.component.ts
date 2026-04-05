import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import type { WishlistEntry } from '../../../core/models/wishlist.model';
import { WishlistService } from '../../../core/services/wishlist.service';
import { NotificationService } from '../../../core/services/notification.service';
import { apiErrorMessage } from '../../../core/utils/api-error';

@Component({
  selector: 'app-wishlist-page',
  imports: [
    DatePipe,
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './wishlist-page.component.html',
  styleUrl: './wishlist-page.component.css',
})
export class WishlistPageComponent implements OnInit {
  private readonly wishlistApi = inject(WishlistService);
  private readonly notify = inject(NotificationService);

  protected readonly loading = signal(false);
  protected readonly entries = signal<WishlistEntry[]>([]);
  protected totalElements = 0;
  protected pageSize = 10;
  protected pageIndex = 0;

  readonly displayedColumns = ['flag', 'country', 'capital', 'addedAt', 'actions'] as const;

  ngOnInit(): void {
    this.load();
  }

  onPage(e: PageEvent): void {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.wishlistApi.list(this.pageIndex, this.pageSize).subscribe({
      next: (page) => {
        this.entries.set(page.content);
        this.totalElements = page.totalElements;
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.error(apiErrorMessage(err, 'Could not load wishlist'));
      },
    });
  }

  remove(destinationId: number): void {
    this.wishlistApi.removeByDestinationId(destinationId).subscribe({
      next: () => {
        this.notify.success('Removed from wishlist');
        this.load();
      },
      error: (err) => this.notify.error(apiErrorMessage(err, 'Could not remove')),
    });
  }
}
