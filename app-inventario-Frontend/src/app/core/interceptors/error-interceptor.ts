import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(

    catchError((error: HttpErrorResponse) => {

      const currentUrl = router.url;
      if (currentUrl !== '/error-conexion') {
        localStorage.setItem('last-url', currentUrl);
      }
      // ===========================
      // ERROR: SIN CONEXIÓN (0)
      // ===========================
      if (error.status === 0) {
        router.navigate(['/error-conexion']);
        return throwError(() => error);
      }

      // ===========================
      // ERROR: SERVIDOR CAÍDO (503)
      // ===========================
      if (error.status === 503) {
        router.navigate(['/error-conexion']);
        return throwError(() => error);
      }

      // ===========================
      // ERROR: NO AUTORIZADO (401)
      // ===========================
      if (error.status === 401) {
        // Token inválido o expirado
        router.navigate(['/login'], {
          queryParams: { expired: true }
        });
        return throwError(() => error);
      }

      // ===========================
      // ERROR: PROHIBIDO (403)
      // ===========================
      if (error.status === 403) {
        router.navigate(['/acceso-denegado']);
        return throwError(() => error);
      }

      // ===========================
      // ERROR: SERVIDOR (500)
      // ===========================
      if (error.status === 500) {
        router.navigate(['/error-servidor']);
        return throwError(() => error);
      }

      // Cualquier otro error no previsto
      return throwError(() => error);
    })
  );
};
