import { DecimalPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import type { Destination } from '../../../core/models/destination.model';
import { DestinationService } from '../../../core/services/destination.service';
import { WishlistService } from '../../../core/services/wishlist.service';
import { NotificationService } from '../../../core/services/notification.service';
import { apiErrorMessage } from '../../../core/utils/api-error';

@Component({
  selector: 'app-destination-list',
  imports: [
    DecimalPipe,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatChipsModule,
  ],
  templateUrl: './destination-list.component.html',
  styleUrl: './destination-list.component.css',
})
export class DestinationListComponent implements OnInit, OnDestroy {
  private readonly destinationsApi = inject(DestinationService);
  private readonly wishlistApi = inject(WishlistService);
  private readonly notify = inject(NotificationService);
  private readonly destroy$ = new Subject<void>();

  protected readonly loading = signal(false);
  protected readonly items = signal<Destination[]>([]);
  protected totalElements = 0;
  protected pageSize = 12;
  protected pageIndex = 0;
  protected sort = 'countryName,asc';

  readonly searchCtrl = new FormControl('', { nonNullable: true });

  ngOnInit(): void {
    this.searchCtrl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => {
        this.pageIndex = 0;
        this.load();
      });
    this.load();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onPage(e: PageEvent): void {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
    this.load();
  }

  sortChanged(): void {
    this.pageIndex = 0;
    this.load();
  }

  load(): void {
    this.loading.set(true);
    const name = this.searchCtrl.value.trim();
    const req =
      name.length > 0
        ? this.destinationsApi.search(name, true, this.pageIndex, this.pageSize, this.sort)
        : this.destinationsApi.list(this.pageIndex, this.pageSize, this.sort);
    req.subscribe({
      next: (page) => {
        this.items.set(page.content);
        this.totalElements = page.totalElements;
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.error(apiErrorMessage(err, 'Could not load destinations'));
      },
    });
  }

  addWishlist(d: Destination, ev: Event): void {
    ev.preventDefault();
    ev.stopPropagation();
    if (d.id == null) return;
    this.wishlistApi.add(d.id).subscribe({
      next: () => this.notify.success('Added to wishlist'),
      error: (err) => this.notify.error(apiErrorMessage(err, 'Wishlist update failed')),
    });
  }
}
