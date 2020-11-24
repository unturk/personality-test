/**
 * A generic http call response. Backing server responses will be in this format.
 */
export interface IHttpResponse {

	/**
	 * This result must be true iff the request has been successfully completed. If the success field is false or
	 * absent, it's admissible to thing the back-end procedure of the sent request is failed.
	 */
	success?: boolean;

	/**
	 * Any kind of data sent by the backing server will be hold in the this property. It may be and object, an array,
	 * or any primitive field.
	 */
	data?: any;

	/**
	 * On failed requests some informative message can be send to the clients. These informative messages will be kept
	 * with this property.
	 */
	failure?: string;

	/**
	 * On failed requests at authentication filter phase, if the request rejected as unauthorized, failure message is sent on
	 * unauthorized field as well.
	 */
	unauthorized?: string;

}
