import {Component, OnInit} from '@angular/core';
import {Category} from "../../models/interfaces/category";
import {CategoryService} from "../../services/category.service";
import {UtilityService} from "../../services/utility.service";
import {IHttpResponse} from "../../models/interfaces/i-http-response";
import {MatSelectChange} from "@angular/material/select";
import {QuestionService} from "../../services/question.service";
import {Question} from "../../models/interfaces/question";
import {MatDialogRef} from "@angular/material/dialog";
import {AnswerService} from "../../services/answer.service";

@Component({
	selector: 'app-personality-test-dialog',
	templateUrl: './personality-test-dialog.component.html',
	styleUrls: ['./personality-test-dialog.component.scss']
})
export class PersonalityTestDialogComponent implements OnInit {

	private _selectedCategory: Category;

	/**
	 * Array of categories to be selected.
	 */
	private _categories: Category[];

	/**
	 * Array of categories to be selected.
	 */
	private _questions: Question[];

	constructor(private categoryService: CategoryService, private questionService: QuestionService,
				private answerService: AnswerService, public dialogRef: MatDialogRef<PersonalityTestDialogComponent>) {
	}

	ngOnInit() {
		setTimeout(() => this.loadCategories());
	}

	private loadCategories = async () => {
		const result = await this.categoryService.fetchCategories();
		UtilityService.handleResponseFromService(result, (response: IHttpResponse) => {
			this.categories = response.data;
		});
	}

	private loadQuestions = async () => {
		const result = await this.questionService.fetchQuestions(this.selectedCategory);
		UtilityService.handleResponseFromService(result, (response: IHttpResponse) => {
			const data = response.data;
			const childIds = data.reduce(function (arr, datum) {
				datum.child && arr.push(datum.child.id);
				return arr;
			}, []);
			for (let i = 0; i < data.length; i++) {
				if (childIds.indexOf(data[i].id) !== -1) {
					data.splice(i, 1);
				}
			}
			this.questions = data;
		});
	}

	selectCategory($event: MatSelectChange) {
		this.selectedCategory = $event.value;
		this.loadQuestions();
	}

	get categories(): Category[] {
		return this._categories;
	}

	set categories(value: Category[]) {
		this._categories = value;
	}

	get selectedCategory(): Category {
		return this._selectedCategory;
	}

	set selectedCategory(value: Category) {
		this._selectedCategory = value;
	}

	get questions(): Question[] {
		return this._questions;
	}

	set questions(value: Question[]) {
		this._questions = value;
	}

	async saveAnswers() {
		if (!this.selectedCategory) {
			UtilityService.notifyUser('You must select a category to answer the questions!');
		} else if (this.questions.some(question => !question.answer || (question.showChild && !question.child.answer)).valueOf()) {
			UtilityService.notifyUser('You must answer all questions in selected category!');
		} else {
			let answers = [];
			this.questions.forEach(question => {
				answers.push({question: {id: question.id}, value: question.answer.toString()});
				question.showChild && answers.push({
					question: {id: question.child.id},
					value: question.child.answer.toString()
				});
			})
			const result = await this.answerService.saveAnswers(answers);
			UtilityService.handleResponseFromService(result, (response: IHttpResponse) => {
				UtilityService.notifyUser('Your personality test has been saved successfully!', 'OK');
				this.dialogRef.close();
			});
		}
	}
}
