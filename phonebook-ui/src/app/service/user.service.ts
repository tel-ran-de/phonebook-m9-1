import {Injectable} from '@angular/core'
import {User} from "../model/user";
import {HttpClient, HttpResponse} from '@angular/common/http'
import {UserRecoveryPass} from "../model/userRecoveryPass";
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {TokenStorageService} from "./token-storage.service";

@Injectable()
export class UserService {

  private readonly forgotPasswordPath = '/api/user/password/recovery/';
  private readonly resetPasswordPath = '/api/user/password/';
  private readonly userPath = '/api/user/';
  private readonly activationPath = '/api/user/activation/';
  private readonly loginPath = '/api/user/login';


  constructor(private http: HttpClient, private router: Router, private tokenStorage: TokenStorageService) {
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

  private headerName = 'Access-Token';

  login(user: User) {
    return this.http.post<User>(this.loginPath, user, {observe: 'response'}).pipe(first())
      .subscribe(
        (data: HttpResponse<any>) => {
          this.tokenStorage.signOut();
          this.tokenStorage.saveToken(data.headers.get(this.headerName));
          this.router.navigate(['../home']).then();
        }
      )
  };

  getUserData() {
    return this.http.get<User>(this.userPath);
  }
}
