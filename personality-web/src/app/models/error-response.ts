export class ErrorResponse {
	private _message: string;
	private _status: number;
	private _data: string;

	constructor(message: string, status = 200, data?: string) {
		this._message = message;
		this._status = status;
		this._data = data;
	}

	get message(): string {
		return this._message;
	}

	set message(value: string) {
		this._message = value;
	}

	get status(): number {
		return this._status;
	}

	set status(value: number) {
		this._status = value;
	}

	get data(): string {
		return this._data;
	}

	set data(value: string) {
		this._data = value;
	}
}
