import {ErrorResponse} from './error-response';

describe('ErrorResponse', () => {
	it('must create an instance', () => {
		const errorResponse = new ErrorResponse('Error Message');
		expect(errorResponse.message).toEqual('Error Message');
	});
});
