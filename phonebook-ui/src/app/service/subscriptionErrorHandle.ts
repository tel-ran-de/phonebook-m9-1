import {HttpErrorResponse} from "@angular/common/http";

export function SubscriptionErrorHandle(error: HttpErrorResponse): string {
  if (!error.error.message && (error.status === 404 || error.status === 400 || error.status === 504))
    return 'code404_400';
  if (error.error instanceof ErrorEvent)
    return 'errorNoInternet';
  return error.error.message || 'noErrorMsg';
}
