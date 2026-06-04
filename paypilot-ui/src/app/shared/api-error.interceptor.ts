import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable()
export class ApiErrorInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<unknown>, next: HttpHandler) {
    const authRequest = request.clone({
      setHeaders: {
        Authorization: 'Bearer dev-admin-token',
      },
    });
    return next.handle(authRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        const message = error.error?.details?.join(', ') || error.error?.message || error.message;
        return throwError(() => new Error(message));
      }),
    );
  }
}
