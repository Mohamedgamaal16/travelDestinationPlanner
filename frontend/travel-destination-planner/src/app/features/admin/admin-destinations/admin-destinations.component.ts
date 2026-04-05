import { DecimalPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import {
  MatButtonToggleChange,
  MatButtonToggleModule,
} from '@angular/material/button-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog } from '@angular/material/dialog';
import type { Destination } from '../../../core/models/destination.model';
import { AdminDestinationService } from '../../../core/services/admin-destination.service';
import { NotificationService } from '../../../core/services/notification.service';
import { apiErrorMessage } from '../../../core/utils/api-error';
import { AddDestinationDialogComponent } from '../dialogs/add-destination-dialog.component';
import { SuggestionsBulkDialogComponent } from '../dialogs/suggestions-bulk-dialog.component';

@Component({
  selector: 'app-admin-destinations',
  imports: [
    DecimalPipe,
    ReactiveFormsModule,
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatButtonToggleModule,
    MatSelectModule,
  ],
  templateUrl: './admin-destinations.component.html',
  styleUrl: './admin-destinations.component.css',
})
export class AdminDestinationsComponent implements OnInit, OnDestroy {
  private readonly adminApi = inject(AdminDestinationService);
  private readonly notify = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroy$ = new Subject<void>();

  protected readonly loading = signal(false);
  protected readonly rows = signal<Destination[]>([]);
  protected totalElements = 0;
  protected pageSize = 15;
  protected pageIndex = 0;
  protected sort = 'id,asc';
  /** Approved vs disapproved only (matches UserDestinationServiceImpl). Default: disapproved list. */
  protected approvedToggle: 'yes' | 'no' = 'no';

  readonly searchCtrl = new FormControl('', { nonNullable: true });
  readonly displayedColumns = [
    'id',
    'flag',
    'country',
    'capital',
    'region',
    'population',
    'status',
    'actions',
  ] as const;

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

  private mapToggleToApproved(): boolean {
    return this.approvedToggle === 'yes';
  }

  onApprovedChange(e: MatButtonToggleChange): void {
    this.approvedToggle = e.value as 'yes' | 'no';
    this.pageIndex = 0;
    this.load();
  }

  onPage(e: PageEvent): void {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
    this.load();
  }

  sortChanged(value: string): void {
    this.sort = value;
    this.pageIndex = 0;
    this.load();
  }

  load(): void {
    this.loading.set(true);
    const name = this.searchCtrl.value.trim() || undefined;
    this.adminApi
      .listForAdmin(name, this.mapToggleToApproved(), this.pageIndex, this.pageSize, this.sort)
      .subscribe({
        next: (page) => {
          this.rows.set(page.content);
          this.totalElements = page.totalElements;
          this.loading.set(false);
        },
        error: (err) => {
          this.loading.set(false);
          this.notify.error(apiErrorMessage(err, 'Could not load destinations'));
        },
      });
  }

  openAdd(): void {
    this.dialog
      .open(AddDestinationDialogComponent, { width: '520px', autoFocus: false })
      .afterClosed()
      .subscribe((dto) => {
        if (!dto) return;
        this.adminApi.add(dto).subscribe({
          next: () => {
            this.adminApi.clearSuggestionsCache();
            this.notify.success('Destination created');
            this.load();
          },
          error: (err) => this.notify.error(apiErrorMessage(err, 'Create failed')),
        });
      });
  }

  openSuggestionsBulk(): void {
    this.dialog
      .open(SuggestionsBulkDialogComponent, { width: 'min(920px, 96vw)', maxWidth: '96vw', autoFocus: false })
      .afterClosed()
      .subscribe((list) => {
        if (!list?.length) return;
        this.adminApi.bulk(list).subscribe({
          next: () => {
            this.adminApi.clearSuggestionsCache();
            this.notify.success('Bulk import completed');
            this.load();
          },
          error: (err) => this.notify.error(apiErrorMessage(err, 'Bulk import failed')),
        });
      });
  }

  approve(row: Destination): void {
    if (row.id == null) return;
    this.adminApi.approve(row.id).subscribe({
      next: () => {
        this.adminApi.clearSuggestionsCache();
        this.notify.success('Approved');
        this.load();
      },
      error: (err) => this.notify.error(apiErrorMessage(err, 'Approve failed')),
    });
  }

  disapprove(row: Destination): void {
    if (row.id == null) return;
    this.adminApi.disapprove(row.id).subscribe({
      next: () => {
        this.adminApi.clearSuggestionsCache();
        this.notify.success('Disapproved');
        this.load();
      },
      error: (err) => this.notify.error(apiErrorMessage(err, 'Disapprove failed')),
    });
  }

  delete(row: Destination): void {
    if (row.id == null) return;
    if (!confirm(`Delete ${row.countryName}?`)) return;
    this.adminApi.delete(row.id).subscribe({
      next: () => {
        this.adminApi.clearSuggestionsCache();
        this.notify.success('Deleted');
        this.load();
      },
      error: (err) => this.notify.error(apiErrorMessage(err, 'Delete failed')),
    });
  }
}
