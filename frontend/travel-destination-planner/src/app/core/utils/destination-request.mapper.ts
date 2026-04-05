import type { Destination, DestinationRequest } from '../models/destination.model';

/** Maps API suggestion or row to a create payload (new rows default to disapproved / approved=false). */
export function destinationToCreateRequest(d: Destination): DestinationRequest {
  const body: DestinationRequest = {
    countryName: d.countryName,
    approved: false,
  };
  if (d.capital) body.capital = d.capital;
  if (d.region) body.region = d.region;
  if (d.population != null && d.population >= 0) body.population = d.population;
  if (d.currency) body.currency = d.currency;
  if (d.currencySymbol) body.currencySymbol = d.currencySymbol;
  if (d.flagUrl) body.flagUrl = d.flagUrl;
  return body;
}
