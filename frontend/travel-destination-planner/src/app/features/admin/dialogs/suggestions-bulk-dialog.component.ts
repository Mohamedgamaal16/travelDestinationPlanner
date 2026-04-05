import { DecimalPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { SelectionModel } from '@angular/cdk/collections';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { Subject, takeUntil } from 'rxjs';
import type { Destination, DestinationRequest } from '../../../core/models/destination.model';
import { AdminDestinationService } from '../../../core/services/admin-destination.service';
import { NotificationService } from '../../../core/services/notification.service';
import { sameDestination } from '../../../core/utils/destination-identity';
import { destinationToCreateRequest } from '../../../core/utils/destination-request.mapper';
import { apiErrorMessage } from '../../../core/utils/api-error';

@Component({
  selector: 'app-suggestions-bulk-dialog',
  imports: [
    DecimalPipe,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTableModule,
  ],
  templateUrl: './suggestions-bulk-dialog.component.html',
  styleUrl: './suggestions-bulk-dialog.component.css',
})
export class SuggestionsBulkDialogComponent implements OnInit, OnDestroy {
  private readonly adminApi = inject(AdminDestinationService);
  private readonly notify = inject(NotificationService);
  private readonly ref = inject(MatDialogRef<SuggestionsBulkDialogComponent, DestinationRequest[] | null>);
  private readonly destroy$ = new Subject<void>();

  protected readonly loading = signal(true);
  protected readonly suggestions = signal<Destination[]>([]);

  readonly filterCtrl = new FormControl('', { nonNullable: true });
  protected pageIndex = 0;
  protected pageSize = 12;

  readonly selection = new SelectionModel<Destination>(true, [], true, sameDestination);

  readonly displayedColumns = ['select', 'marker', 'flag', 'country', 'capital', 'region', 'population'] as const;

  ngOnInit(): void {
    this.adminApi.fetchSuggestions().subscribe({
      next: (list) => {
        this.suggestions.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.error(apiErrorMessage(err, 'Could not load suggestions'));
      },
    });

    this.filterCtrl.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.pageIndex = 0;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  filtered(): Destination[] {
    const q = this.filterCtrl.value.trim().toLowerCase();
    const all = this.suggestions();
    if (!q) return all;
    return all.filter(
      (d) =>
        d.countryName.toLowerCase().includes(q) ||
        (d.capital && d.capital.toLowerCase().includes(q)) ||
        (d.region && d.region.toLowerCase().includes(q)),
    );
  }

  paged(): Destination[] {
    const f = this.filtered();
    const start = this.pageIndex * this.pageSize;
    return f.slice(start, start + this.pageSize);
  }

  onPage(e: PageEvent): void {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
  }

  isPageAllSelected(): boolean {
    const page = this.paged();
    return page.length > 0 && page.every((r) => this.selection.isSelected(r));
  }

  isPagePartiallySelected(): boolean {
    const page = this.paged();
    const n = page.filter((r) => this.selection.isSelected(r)).length;
    return n > 0 && n < page.length;
  }

  masterToggle(): void {
    if (this.isPageAllSelected()) {
      this.paged().forEach((r) => this.selection.deselect(r));
    } else {
      this.paged().forEach((r) => this.selection.select(r));
    }
  }

  cancel(): void {
    this.ref.close(null);
  }

  addSelected(): void {
    const selected = this.selection.selected;
    if (!selected.length) return;
    this.ref.close(selected.map((d) => destinationToCreateRequest(d)));
  }
}
