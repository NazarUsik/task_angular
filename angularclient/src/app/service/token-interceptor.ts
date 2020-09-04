import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {KeycloakService} from './keycloak.service';
import {Observable} from 'rxjs';
import {from} from 'rxjs';
import {map, concatMap} from 'rxjs/operators';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private keycloak: KeycloakService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const tokenPromise: Promise<string> = this.keycloak.getToken();
    const tokenObservable: Observable<string> = from(tokenPromise);

    return tokenObservable.pipe(
      map(authToken => {
        req = req.clone({setHeaders: {Authorization: 'Bearer '.concat(authToken)}});
      }),
      concatMap(() => {
        return next.handle(req);
      })
    );
  }
}
