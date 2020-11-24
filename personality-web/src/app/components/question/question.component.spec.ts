import {ComponentFixture, TestBed} from '@angular/core/testing';

import {QuestionComponent} from './question.component';
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {Question} from "../../models/interfaces/question";

describe('QuestionComponent', () => {
	let component: QuestionComponent;
	let fixture: ComponentFixture<QuestionComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [QuestionComponent], schemas: [NO_ERRORS_SCHEMA],
		}).compileComponents();
	});

	beforeEach(() => {
		fixture = TestBed.createComponent(QuestionComponent);
		component = fixture.componentInstance;
		component.question = {questionType: 'SINGLE_CHOICE', details: '{}'} as Question;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
