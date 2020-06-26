import {Injectable} from '@angular/core';
import {User} from "./interface";
import {HttpClient} from '@angular/common/http';


@Injectable()
export class UserService {


 private recoveryUrl = 'http://localhost:8080/'; // routing incorrect!!!

  constructor(private http: HttpClient) {
  }

  recovery(user: User){
    return this.http.post<User>(this.recoveryUrl, user);
  }

}
