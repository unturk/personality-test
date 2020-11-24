package com.galaksiya.jersey.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.entity.Category;
import com.galaksiya.exceptions.ServiceFailureException;
import com.galaksiya.logger.GLogger;
import com.galaksiya.logger.OperationLog;
import com.galaksiya.utils.EntityUtils;
import com.galaksiya.utils.RestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.galaksiya.constants.JsonProperties.IP_ADDRESS;

@Path("categories")
@Produces({"application/json"})
public class CategoryService {

	/**
	 * GLogger instance.
	 */
	private final GLogger logger = new GLogger(getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategories(@Context HttpServletRequest request) {
		Response response;
		OperationLog log = logger.startOperation("getCategories").addField(IP_ADDRESS,
			RestUtils.getIpAddress(request));
		try {
			List<Category> categories = DatabaseConnector.getInstance().getObjects(null, Category.class);
			if (categories != null && !categories.isEmpty()) {
				response = RestUtils.createResponseOK(EntityUtils.convertEntityToJsonArray(categories));
				log.succeed();
			} else {
				throw new ServiceFailureException(ErrorMessages.GET_CATEGORIES_ERROR);
			}
		} catch (Throwable t) {
			response = RestUtils.handleExceptionAsResponse(t, log);
		}
		return response;
	}
}
