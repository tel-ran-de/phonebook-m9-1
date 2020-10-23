import {HttpErrorResponse} from "@angular/common/http";

export function SubscriptionErrorHandle(error: HttpErrorResponse): string {

  if (error.status === 404 || error.status === 400 || error.status === 504)
    return 'Something bad happened, please try again later.';
  if (error.error instanceof ErrorEvent)
    return 'No internet connection';

  return error.error.message || 'If you have this error again, please contact us.';
}
