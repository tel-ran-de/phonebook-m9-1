import {Injectable} from '@angular/core'
import {User} from "../model/user";
import {HttpClient} from '@angular/common/http'
import {Observable} from "rxjs";

@Injectable()
export class UserService {

  private readonly forgotPasswordPath = '/api/user/password/recovery/';
  private readonly resetPasswordPath = '/api/user/password/';
  private readonly userPath = '/api/user/';
  private readonly activationPath = '/api/user/activation/';
  private readonly loginPath = '/api/user/login';
  private readonly resetPasswordAuthUserPath = '/api/user/auth-password/';

  constructor(private http: HttpClient) {
  }

  newUserRegistration(user: User): Observable<User> {
    return this.http.post<User>(this.userPath, user);
  }

  sendRequestToConfirmRegistration(token: string): Observable<any> {
    return this.http.get<any>(`${this.activationPath}${token}`)
  }

  forgotPassword(userEmail: String): Observable<User> {
    return this.http.post<User>(this.forgotPasswordPath, {
      email: userEmail
    });
  }

  resetPassword(password: string, token: string): Observable<User> {
    return this.http.put<User>(this.resetPasswordPath, {
      password: password,
      token: token
    });
  }

  login(user: User): Observable<any> {
    return this.http.post<User>(this.loginPath, user, {observe: 'response'});
  }

  getUserData(): Observable<User> {
    return this.http.get<User>(this.userPath);
  };

  changePassAuthUser(newPassword: string): Observable<any> {
    return this.http.put(this.resetPasswordAuthUserPath, {password: newPassword});
  }
}
