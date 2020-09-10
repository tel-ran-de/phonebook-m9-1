import {Injectable} from '@angular/core'
import {User} from "../model/user";
import {HttpClient} from '@angular/common/http'
import {UserRecoveryPass} from "../model/userRecoveryPass";

@Injectable()
export class UserService {

  private readonly forgotPasswordPath = '/api/user/password/recovery/';
  private readonly resetPasswordPath = '/api/user/password/';
  private readonly userPath = '/api/user/';
  private readonly activationPath = '/api/user/activation/';
  private readonly loginPath = '/api/user/login';


  constructor(private http: HttpClient) {
  }

  newUserRegistration(user: User) {
    return this.http.post<User>(this.userPath, user);
  }

  sendRequestToConfirmRegistration(token: string) {
    return this.http.get(`${this.activationPath}${token}`)
  }

  forgotPassword(user: User) {
    return this.http.post<User>(this.forgotPasswordPath, user);
  }

  resetPassword(userRecoveryPass: UserRecoveryPass) {
    return this.http.put<User>(this.resetPasswordPath, userRecoveryPass);
  }



  login(user: User) {
    return this.http.post<User>(this.loginPath, user, {observe: 'response'});
  };

  getUserData() {
    return this.http.get<User>(this.userPath);
  }
}
