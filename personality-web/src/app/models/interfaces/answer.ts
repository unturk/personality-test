import {Question} from "./question";

export interface Answer {
	id?: number;

	question: Question | {id:number};

	value: string;
}
