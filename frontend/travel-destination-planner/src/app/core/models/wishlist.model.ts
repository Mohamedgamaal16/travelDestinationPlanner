import type { Destination } from './destination.model';

export interface WishlistEntry {
  id: number;
  userId: number;
  destinationId: number;
  destination: Destination;
  addedAt: string;
}

export interface WishlistAddRequest {
  destinationId: number;
}
