import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import type { DestinationRequest } from '../../../core/models/destination.model';

@Component({
  selector: 'app-add-destination-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
  ],
  templateUrl: './add-destination-dialog.component.html',
  styleUrl: './admin-dialog-shared.css',
})
export class AddDestinationDialogComponent {
  private readonly fb = inject(FormBuilder);
  private readonly ref = inject(MatDialogRef<AddDestinationDialogComponent, DestinationRequest | null>);

  readonly form = this.fb.nonNullable.group({
    countryName: ['', [Validators.required, Validators.maxLength(100)]],
    capital: ['', Validators.maxLength(100)],
    region: ['', Validators.maxLength(100)],
    population: [null as number | null, Validators.min(0)],
    currency: ['', Validators.maxLength(50)],
    currencySymbol: ['', Validators.maxLength(10)],
    flagUrl: ['', [Validators.maxLength(500), Validators.pattern(/^(|https?:\/\/.+)$/)]],
    approved: [false],
  });

  cancel(): void {
    this.ref.close(null);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const body: DestinationRequest = {
      countryName: v.countryName,
      capital: v.capital || undefined,
      region: v.region || undefined,
      population: v.population ?? undefined,
      currency: v.currency || undefined,
      currencySymbol: v.currencySymbol || undefined,
      flagUrl: v.flagUrl || undefined,
      approved: v.approved,
    };
    this.ref.close(body);
  }
}
