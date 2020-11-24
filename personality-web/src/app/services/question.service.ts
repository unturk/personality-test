import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorResponse} from "../models/error-response";
import {IHttpResponse} from "../models/interfaces/i-http-response";
import {UtilityService} from "./utility.service";
import {endpoints} from "../constants/endpoints.constant";
import {Category} from "../models/interfaces/category";

@Injectable({
	providedIn: 'root'
})
export class QuestionService {

	constructor(private http: HttpClient) {
	}

	fetchQuestions(category: Category): Promise<ErrorResponse | IHttpResponse> {
		return UtilityService.pipeHttpResponseForError(this.http.get<IHttpResponse>(endpoints.questions(category.id)));
	}
}
