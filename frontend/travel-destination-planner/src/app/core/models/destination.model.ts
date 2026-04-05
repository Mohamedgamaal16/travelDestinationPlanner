export interface Destination {
  /** Present for persisted destinations; omitted/null for API suggestions. */
  id?: number | null;
  countryName: string;
  capital?: string | null;
  region?: string | null;
  population?: number | null;
  currency?: string | null;
  currencySymbol?: string | null;
  flagUrl?: string | null;
  /** Set client-side when merging search results; API DTO omits this field. */
  approved?: boolean;
}

export interface DestinationRequest {
  countryName: string;
  capital?: string;
  region?: string;
  population?: number;
  currency?: string;
  currencySymbol?: string;
  flagUrl?: string;
  approved?: boolean;
}
