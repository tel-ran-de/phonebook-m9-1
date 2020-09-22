import {HttpErrorResponse} from "@angular/common/http";

export function SubscriptionErrorHandle(error: HttpErrorResponse): string {
  let errorMessage: string;
  if (error.error instanceof ErrorEvent)
    return 'No internet connection';
  else errorMessage = error.error.message;

  if (errorMessage === null || !errorMessage)
    errorMessage = 'Error code: ' + error.status
      + '. If you have this error again, please contact us: support@phone-book.com'
  return errorMessage;
}
