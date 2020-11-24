import {TestBed} from '@angular/core/testing';
import {QuestionService} from "./question.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('QuestionService', () => {
	beforeEach(() => TestBed.configureTestingModule({imports: [HttpClientTestingModule]}));

	it('should be created', () => {
		const service: QuestionService = TestBed.get(QuestionService);
		expect(service).toBeTruthy();
	});
});
