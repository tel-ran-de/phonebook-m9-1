import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenStorageService} from "./token-storage.service";

const TOKEN_HEADER_KEY = 'Access-Token';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  urlsToNotUse: Array<any>;

  constructor(private tokenStorage: TokenStorageService) {
    this.urlsToNotUse = [
      {method: 'POST', endpoint: '/api/user/'},
      {method: 'GET', endpoint: '/api/user/activation/'},
      {method: 'POST', endpoint: '/api/user/password/recovery/'},
      {method: 'PUT', endpoint: '/api/user/password/'},
      {method: 'POST', endpoint: '/api/user/login'},
    ];
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    if (this.isInvalidRequestForInterceptor(req.url, req.method))
      return next.handle(authReq);

    const token = this.tokenStorage.getToken();
    if (token != null)
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, token)});

    return next.handle(authReq);
  }

  private isInvalidRequestForInterceptor(requestUrl: string, method: string): boolean {
    for (let address of this.urlsToNotUse)
      if (address.method === method && address.endpoint === requestUrl)
        return true;
    return false;
  }
}
