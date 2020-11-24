import {Category} from "./category";

export interface Question {
	id?: number;

	value: string;

	category: Category;

	child?: Question;

	showChild?: boolean;

	questionType: string;

	details: string;

	answer?: string;

	options?: string[];

	range?: {to: number, from: number};

	condition?: {operator: string, value: string};

}
