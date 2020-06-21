import { Injectable } from '@angular/core';
import {RegistrationUser} from "../model/registration-user.model";
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private registrationUrl = 'api/user/registration';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  registerNewUser(user: RegistrationUser): Observable<any>  {
    return this.http.post(this.registrationUrl, user, this.httpOptions)
  }
}
