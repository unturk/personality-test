import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorResponse} from "../models/error-response";
import {IHttpResponse} from "../models/interfaces/i-http-response";
import {UtilityService} from "./utility.service";
import {endpoints} from "../constants/endpoints.constant";
import {Category} from "../models/interfaces/category";
import {Answer} from "../models/interfaces/answer";

@Injectable({
	providedIn: 'root'
})
export class AnswerService {

	constructor(private http: HttpClient) {
	}

	saveAnswers(answers: Answer[]): Promise<ErrorResponse | IHttpResponse> {
		return UtilityService.pipeHttpResponseForError(this.http.post<IHttpResponse>(endpoints.answers(), {answers}));
	}
}
