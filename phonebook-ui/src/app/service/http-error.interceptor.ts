import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from "rxjs/operators";
import {Router} from "@angular/router";
import {TokenStorageService} from "./token-storage.service";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router, private tokenStorage: TokenStorageService) {
  }


  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401) {
        this.tokenStorage.signOut();
        this.router.navigate(['user/login']);
      }
      const error = 'unauthorized';
      return throwError(err);
    }))
  }
}
