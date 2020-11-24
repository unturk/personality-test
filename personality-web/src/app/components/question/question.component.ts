import {Component, Input, OnInit} from '@angular/core';
import {Question} from "../../models/interfaces/question";

@Component({
	selector: 'app-question',
	templateUrl: './question.component.html',
	styleUrls: ['./question.component.scss']
})
export class QuestionComponent implements OnInit {

	@Input() question: Question;

	constructor() {
	}

	ngOnInit(): void {
		if (this.question) {
			const details = JSON.parse(this.question.details);
			this.question.options = details.options;
			this.question.range = details.range;
			this.question.condition = details.condition;
			this.question.showChild = false;
		}
	}

	updateQuestionStatus() {
		if (this.question.child && this.question.answer) {
			delete this.question.child.answer;
			let operator = this.question.condition.operator;
			let value = this.question.condition.value;
			this.question.showChild = operator === 'equals' && value === this.question.answer;
		}
	}
}
