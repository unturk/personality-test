import {Injectable, Injector} from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {ErrorResponse} from '../models/error-response';
import {IHttpResponse} from '../models/interfaces/i-http-response';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  public static injector: Injector;

  public static notifyUser(message: string, action?: string, options?: object): void {
    UtilityService.injector.get(MatSnackBar).open(message, action || 'OK', options || {
      duration: 5000,
      horizontalPosition: 'left'
    });
  }

  public static handleResponseFromService(result: IHttpResponse | ErrorResponse, successCallback: (result) => void,
                                          failureCallback?: (result) => void): void {
    if ((result as IHttpResponse).success) {
      successCallback(result);
    } else if ((result as IHttpResponse).failure) {
      // tslint:disable-next-line:no-unused-expression
      failureCallback && failureCallback(result);
      UtilityService.notifyUser((result as IHttpResponse).failure);
    } else if (result instanceof ErrorResponse) {
      UtilityService.notifyUser(result.message);
    }
  }

  public static pipeHttpResponseForError(response: Observable<IHttpResponse>): Promise<ErrorResponse | IHttpResponse> {
    return response.pipe(catchError((error: { message: string, status: number; data?: string }) =>
      of(new ErrorResponse(error.message, error.status, error.data))
    )).toPromise();
  }

}
