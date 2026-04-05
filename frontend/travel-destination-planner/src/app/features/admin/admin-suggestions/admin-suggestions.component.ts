import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import type { Destination } from '../../../core/models/destination.model';
import { AdminDestinationService } from '../../../core/services/admin-destination.service';
import { NotificationService } from '../../../core/services/notification.service';
import { sameDestination } from '../../../core/utils/destination-identity';
import { destinationToCreateRequest } from '../../../core/utils/destination-request.mapper';
import { apiErrorMessage } from '../../../core/utils/api-error';

@Component({
  selector: 'app-admin-suggestions',
  imports: [
    DecimalPipe,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './admin-suggestions.component.html',
  styleUrl: './admin-suggestions.component.css',
})
export class AdminSuggestionsComponent implements OnInit {
  private readonly adminApi = inject(AdminDestinationService);
  private readonly notify = inject(NotificationService);

  protected readonly loading = signal(false);
  protected readonly adding = signal(false);
  protected readonly items = signal<Destination[]>([]);

  readonly selection = new SelectionModel<Destination>(true, [], true, sameDestination);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.selection.clear();
    this.adminApi.fetchSuggestions().subscribe({
      next: (list) => {
        this.items.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.error(apiErrorMessage(err, 'Could not load suggestions'));
      },
    });
  }

  onCardCheckboxChange(d: Destination, checked: boolean): void {
    if (checked) {
      this.selection.select(d);
    } else {
      this.selection.deselect(d);
    }
  }

  clearSelection(): void {
    this.selection.clear();
  }

  addSelected(): void {
    const selected = this.selection.selected;
    if (!selected.length || this.adding()) return;
    const dtos = selected.map((d) => destinationToCreateRequest(d));
    this.adding.set(true);
    this.adminApi.bulk(dtos).subscribe({
      next: () => {
        this.adding.set(false);
        this.adminApi.clearSuggestionsCache();
        this.selection.clear();
        this.notify.success(
          selected.length === 1
            ? '1 destination added (disapproved — approve it in Manage destinations)'
            : `${selected.length} destinations added (disapproved — approve them in Manage destinations)`,
        );
      },
      error: (err) => {
        this.adding.set(false);
        this.notify.error(apiErrorMessage(err, 'Could not add destinations'));
      },
    });
  }
}
