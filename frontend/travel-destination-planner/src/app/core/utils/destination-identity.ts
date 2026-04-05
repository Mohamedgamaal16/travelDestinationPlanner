import type { Destination } from '../models/destination.model';

/** Stable identity for suggestion rows (no DB id). */
export function sameDestination(a: Destination, b: Destination): boolean {
  return (
    a.countryName === b.countryName &&
    (a.region ?? '') === (b.region ?? '') &&
    (a.capital ?? '') === (b.capital ?? '')
  );
}
