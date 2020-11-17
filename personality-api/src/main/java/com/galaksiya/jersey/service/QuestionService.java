package com.galaksiya.jersey.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.Category;
import com.galaksiya.entity.Question;
import com.galaksiya.exceptions.ServiceFailureException;
import com.galaksiya.logger.GLogger;
import com.galaksiya.logger.OperationLog;
import com.galaksiya.utils.EntityUtils;
import com.galaksiya.utils.RestUtils;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.LazyMethodConstants.GET_CHILD;

@Path("questions")
@Produces({"application/json"})
public class QuestionService {

	/**
	 * GLogger instance.
	 */
	private final GLogger logger = new GLogger(getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestions(@QueryParam(CATEGORY) Integer categoryId, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		OperationLog log = logger.startOperation("getQuestions").addField(IP_ADDRESS,
			RestUtils.getIpAddress(request)).addField(CATEGORY, categoryId);
		try {
			GQueryFilter filter = new GQueryFilter(
				new GQueryFilterPart("category.id").setOperator(Operators.EQUAL, categoryId));
			List<Question> questions = DatabaseConnector.getInstance().getObjects(filter, Question.class, GET_CHILD);
			if (questions != null && !questions.isEmpty()) {
				response.addProperty(SUCCESS, true);
				response.add(DATA, EntityUtils.convertEntityToJsonArray(questions));
				log.succeed();
			} else {
				throw new ServiceFailureException(ErrorMessages.GET_QUESTIONS_ERROR);
			}
		} catch (Throwable t) {
			response = RestUtils.handleException(t, log);
		}
		return RestUtils.createResponseOK(response.toString());
	}
}
