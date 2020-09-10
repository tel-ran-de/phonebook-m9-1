import {HttpErrorResponse} from '@angular/common/http';
import {FormGroup} from "@angular/forms";

export class Utils {

  public subscriptionErrorHandle(error: HttpErrorResponse): string {
    let errorMessage: string;
    console.log(error)
    if (error.error instanceof ErrorEvent)
      return 'No internet connection';
    else errorMessage = error.error.message;

    if (errorMessage === null || !errorMessage)
      errorMessage = 'Error code: ' + error.status
        + '. If you have this error again, please contact us: support@phone-book.com'
    console.log(errorMessage)
    return errorMessage;

  }

  public confirmedPassValidator(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];
      if (matchingControl.errors && !matchingControl.errors.confirmedValidator) {
        return;
      }
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({confirmedValidator: true});
      } else {
        matchingControl.setErrors(null);
      }
    }
  }
}

