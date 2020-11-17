package com.galaksiya.jersey.interceptor;

import com.galaksiya.config.ApplicationConfig;
import com.galaksiya.logger.GLogger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

@Provider
public class RequestInterceptor implements ContainerResponseFilter, WriterInterceptor {

	/**
	 * Logger instance.
	 */
	private final GLogger logger = new GLogger(RequestInterceptor.class);

	/**
	 * Http header value for Accept-Encoding
	 */
	private static final String GZIP = "gzip";

	/**
	 * The after filter. When this filter gets work on a request, created context instance is cleared in response
	 * filter and adds content encoding as GZIP if request accepts with their headers.
	 *
	 * @param requestContext  Context of the request which the filter is currently working on.
	 * @param responseContext Context of the response created for the request which the filter is currently working on.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		try {
			addContentEncodingGZIP(requestContext, responseContext);
			addCORSHeaders(responseContext);
		} catch (Exception e) {
			logger.error("Error occurred during intercepting response!", e);
		}
	}

	/**
	 * If CORS usage is enabled in conf, adds proper headers to the response to enable CORS requests on the web.
	 *
	 * @param responseContext Context of the response created for the request which the filter is currently working on.
	 */
	private void addCORSHeaders(ContainerResponseContext responseContext) {
		if (ApplicationConfig.getInstance().isCorsEnabled()) {
			MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();
			if (!responseHeaders.containsKey("Access-Control-Allow-Origin")) {
				responseHeaders.add("Access-Control-Allow-Origin", ApplicationConfig.getInstance().getCorsOriginUrl());
			}
			if (!responseHeaders.containsKey("Access-Control-Allow-Methods")) {
				responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
			}
			if (!responseHeaders.containsKey("Access-Control-Allow-Headers")) {
				responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
			}
			if (!responseHeaders.containsKey("Access-Control-Allow-Credentials")) {
				responseHeaders.add("Access-Control-Allow-Credentials", "true");
			}
		}
	}

	/**
	 * If client has requested with HttpHeader of Accept-Encoding: gzip, then do gzip encoding and put
	 * Content-Encoding: gzip as part of response headers.
	 *
	 * @param requestContext  Context of the request which the filter is currently working on.
	 * @param responseContext Context of the response created for the request which the filter is currently working on.
	 */
	private void addContentEncodingGZIP(ContainerRequestContext requestContext,
		ContainerResponseContext responseContext) {
		final MultivaluedMap<String, String> headers = requestContext.getHeaders();
		if (headers != null && headers.get(HttpHeaders.ACCEPT_ENCODING) != null) {
			for (final String header : headers.get(HttpHeaders.ACCEPT_ENCODING)) {
				if (header.contains(GZIP)) {
					responseContext.getHeaders().add(HttpHeaders.CONTENT_ENCODING, GZIP);
					break;
				}
			}
		}
	}

	/**
	 * Interceptor method wrapping calls to {@link MessageBodyWriter#writeTo} method. The parameters of the wrapped
	 * method called are available from {@code context}. Implementations of this method SHOULD explicitly call {@link
	 * WriterInterceptorContext#proceed} to invoke the next interceptor in the chain.
	 * <p>
	 * For the response, there is only one output stream i.e. written to by jersey after all the interceptors have
	 * invoked context.proceed() in their aroundWriteTo method.
	 *
	 * @param context Response context that contains {@link OutputStream}.
	 */
	@Override
	public void aroundWriteTo(WriterInterceptorContext context) {
		setGZIPEncodedOutputStream(context);
	}

	/**
	 * Gets response's headers and checks that it contains {@link HttpHeaders#CONTENT_ENCODING} key. If contains this
	 * key gets {@link HttpHeaders#CONTENT_ENCODING} value and checks that it contains gzip as related value. Then sets
	 * {@link GZIPOutputStream gzipped output stream} to actual context stream. If any {@link IOException} occurs while
	 * setting gzipped output stream, logs this as an error with their exception.
	 *
	 * @param context Response context that is used for checking headers and setting gzipped output stream.
	 */
	private void setGZIPEncodedOutputStream(WriterInterceptorContext context) {
		MultivaluedMap<String, Object> headers = context.getHeaders();
		if (headers != null && headers.containsKey(HttpHeaders.CONTENT_ENCODING) && headers.get(
			HttpHeaders.CONTENT_ENCODING).contains(GZIP)) {
			final OutputStream outputStream = context.getOutputStream();
			try {
				// When we wrap the base output stream in a GZIPOutputStream, the response will be GZIP encoded.
				context.setOutputStream(new GZIPOutputStream(outputStream));
				context.proceed();
			} catch (IOException e) {
				logger.error("An error occurred while setting gzipped output stream.", e);
			}
		}
	}
}
