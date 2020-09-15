import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Router} from "@angular/router";
import {catchError} from "rxjs/operators";
import {TokenStorageService} from "../tokenHandle/token-storage.service";

@Injectable()
export class HttpError401Interceptor implements HttpInterceptor {

  constructor(private router: Router,
              private tokenStorage: TokenStorageService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url === '/api/user/login' && request.method === "POST")
      return next.handle(request);

    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401) {
        this.tokenStorage.signOut();
        this.router.navigate(['user/login']);
        throwError('unauthorized');
      }
      return throwError(err);
    }));
  }
}
