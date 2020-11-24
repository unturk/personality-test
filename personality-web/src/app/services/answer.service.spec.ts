import {TestBed} from '@angular/core/testing';
import {AnswerService} from "./answer.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AnswerService', () => {
	beforeEach(() => TestBed.configureTestingModule({imports: [HttpClientTestingModule]}));

	it('should be created', () => {
		const service: AnswerService = TestBed.get(AnswerService);
		expect(service).toBeTruthy();
	});
});
