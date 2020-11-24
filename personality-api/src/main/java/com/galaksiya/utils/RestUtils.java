package com.galaksiya.utils;

import com.galaksiya.config.ApplicationConfig;
import com.galaksiya.constants.WebConstants;
import com.galaksiya.exceptions.ServiceFailureException;
import com.galaksiya.logger.OperationLog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import static com.galaksiya.constants.ErrorMessages.*;
import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.constants.WebConstants.RESOURCES_PATH;

public class RestUtils {

	/**
	 * Creates an {@link ServerConnector HTTP server connector} for the given server.
	 *
	 * @param server to create the connector for.
	 * @return Created connector.
	 */
	public static ServerConnector createConnector(Server server) {
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(ApplicationConfig.getInstance().getWebPort());
		server.addConnector(connector);
		return connector;
	}

	/**
	 * Extracted method for status 200 responses reuse.
	 *
	 * @param object Response's status(200)'s entity parameter.
	 * @return Response status code 200 with entity object.
	 */
	public static Response createResponseOK(JsonElement object) {
		final JsonObject response = new JsonObject();
		response.addProperty(SUCCESS, true);
		if (object != null) {
			response.add(DATA, object);
		}
		return createResponse(response.toString(), Response.Status.OK);
	}

	public static Response handleExceptionAsResponse(Throwable t, OperationLog operationLog) {
		JsonObject jsonResponse = handleException(t, operationLog);
		int statusCode = jsonResponse.has(STATUS) ? jsonResponse.get(STATUS).getAsInt() : Response.Status.INTERNAL_SERVER_ERROR
			.getStatusCode();
		return createResponse(jsonResponse.toString(), Response.Status.fromStatusCode(statusCode));
	}

	public static Response createResponse(Object entity, Response.Status status) {
		return Response.status(status).entity(entity).build();
	}

	/**
	 * Creates and returns a {@link Server} instance from configuration parameters.
	 *
	 * @return Created {@link Server} instance.
	 */
	public static Server createServer() {
		ApplicationConfig config = ApplicationConfig.getInstance();
		return new Server(
			new QueuedThreadPool(config.getMaxThreadCount(), config.getMinThreadCount(), config.getIdleTimeout()));
	}

	/**
	 * Creates a {@link WebAppContext} with descriptor as static web xml path and base resources.
	 *
	 * @return created {@link WebAppContext} instance.
	 */
	public static WebAppContext createWebAppContext() {
		WebAppContext root = new WebAppContext();
		root.setContextPath("/");
		root.setDescriptor(WebConstants.WEB_XML_PATH);
		root.setBaseResource(Resource.newClassPathResource(RESOURCES_PATH));
		return root;
	}

	/**
	 * Gets given field from json object as Long.
	 *
	 * @param json  Json Object.
	 * @param field Field name.
	 * @return fieldVal Field value.
	 */
	public Long getFieldAsLong(JsonObject json, String field) {
		Long fieldVal = null;
		if (json != null) {
			if (json.has(field)) {
				JsonElement jsonElement = json.get(field);
				if (jsonElement != null && jsonElement.isJsonPrimitive()) {
					if (jsonElement.getAsJsonPrimitive().isNumber()) {
						fieldVal = jsonElement.getAsLong();
					}
				}
			}
		}
		return fieldVal;
	}

	/**
	 * Extracts ip address from given request.
	 *
	 * @param request Http servlet request.
	 * @return ipAddress ip address of the request.
	 */
	public static String getIpAddress(HttpServletRequest request) {
		return request != null ? request.getRemoteAddr() : "0.0.0.0";
	}

	/**
	 * Handling various exception types helper method.
	 *
	 * @param t Exception
	 * @return failure message as String.
	 */
	public static JsonObject handleException(Throwable t, OperationLog log) {
		String msg = UNEXPECTED_ERROR;
		int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

		if (t instanceof ServiceFailureException) {
			ServiceFailureException serviceFailure = (ServiceFailureException) t;
			msg = serviceFailure.getErrorMessage();
		} else if (t instanceof ServiceException) {
			msg = DATABASE_CONNECTION_ERR;
		} else if (t instanceof PersistenceException) {
			if (t.getCause() instanceof GenericJDBCException) {
				String causeMsg = t.getCause().getMessage();
				if (causeMsg.contains("Unable to acquire JDBC Connection")) {
					msg = UNABLE_TO_ACQUIRE_DATABASE_CONNECTION_ERROR;
				}
			} else {
				msg = UNEXPECTED_ERROR;
			}
		} else if (t instanceof JsonSyntaxException) {
			msg = INCOMPATIBLE_JSON_FORMAT_MSG;
		}

		if (log != null) {
			log.addField(REASON, msg).fail(t);
		}

		JsonObject response = new JsonObject();
		response.addProperty(FAILURE, msg);
		response.addProperty(STATUS, status);
		return response;
	}

	/**
	 * Gets given field from json object as Booean.
	 *
	 * @param json  Json Object.
	 * @param field Field name.
	 * @return fieldVal Field value.
	 */
	public Boolean getFieldAsBoolean(JsonObject json, String field) {
		Boolean fieldVal = null;
		if (json.has(field)) {
			JsonElement jsonElement = json.get(field);
			if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()) {
				fieldVal = jsonElement.getAsBoolean();
			}
		}
		return fieldVal;
	}

	/**
	 * Gets given field from json object as String.
	 *
	 * @param json  Json Object.
	 * @param field Field name.
	 * @return fieldVal Field value.
	 */
	public String getFieldAsString(JsonObject json, String field) {
		String fieldVal = null;
		if (json.has(field)) {
			JsonElement jsonElement = json.get(field);
			if (jsonElement != null && jsonElement.isJsonPrimitive()) {
				if (jsonElement.getAsJsonPrimitive().isString()) {
					fieldVal = jsonElement.getAsString();
				}
			}
		}
		return fieldVal;
	}

	/**
	 * Gets given jsonObj's id as integer.
	 *
	 * @param jsonObj   Given {@link JsonObject} that id to be elected.
	 * @param fieldName Related field name that contains id object.
	 * @return ids
	 */
	public Integer getIdFieldFromObject(JsonObject jsonObj, String fieldName) {
		Integer id = null;
		if (jsonObj.has(fieldName)) {
			JsonElement jsElement = jsonObj.get(fieldName);
			if (jsElement.isJsonObject()) {
				JsonObject jsonObject = jsElement.getAsJsonObject();
				if (jsonObject.has(ID)) {
					JsonElement idElement = jsonObject.get(ID);
					if (idElement.isJsonPrimitive()) {
						id = idElement.getAsInt();
					}
				}
			}
		}
		return id;
	}

}
