import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorResponse} from "../models/error-response";
import {IHttpResponse} from "../models/interfaces/i-http-response";
import {UtilityService} from "./utility.service";
import {endpoints} from "../constants/endpoints.constant";

@Injectable({
	providedIn: 'root'
})
export class CategoryService {

	constructor(private http: HttpClient) {
	}

	/**
	 * Sends a paginated request to retrieve ads. If page and/or count parameter is not given, 1 and 10 is used
	 * respectively as default values. Returns the resulting Promise which either consists of ErrorResponse or IHttpResponse.
	 * @param {number} page - the number of active page.
	 * @param {number} count - the count of ads per page.
	 */
	fetchCategories(): Promise<ErrorResponse | IHttpResponse> {
		return UtilityService.pipeHttpResponseForError(this.http.get<IHttpResponse>(endpoints.categories()));
	}
}
