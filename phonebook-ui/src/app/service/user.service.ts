import { Injectable } from '@angular/core';
import {RegistrationUser} from "../model/registration-user.model";
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly registrationUrl = 'api/user';
  private readonly activationUrl = 'api/user/activation/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  registerNewUser(user: RegistrationUser): Observable<any>  {
    return this.http.post(this.registrationUrl, user, this.httpOptions)
      .pipe(
        catchError(this.handleRegistrationErrors)
      );
  }

  private handleRegistrationErrors(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      // console.error('An error occurred:', error.error.message);
      return throwError('Network error; please try again later.');
    }
    else if (error.status == 400) {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      // console.error(
      //   `Backend returned code ${error.status}, ` +
      //   `body was: ${error.error}`);
      return throwError('User already exist');
    }
    else {
      return throwError('Something bad happened; please try again later.'); //404,500
    }
  }

  activateUser(token: string): Observable<any> {
    return this.http.get(this.activationUrl + token)
      .pipe(
        catchError(this.handleActivationErrors)
      );
  }

  private handleActivationErrors(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      return throwError('Network error, please try again later.');
    }
    else if (error.status == 400) {
      return throwError('Invalid token');//todo
    }
    else {
      return throwError('Something bad happened, please try again later.'); //404,500
    }
  }
}
