package com.galaksiya.jersey.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.Answer;
import com.galaksiya.entity.AnswerGroup;
import com.galaksiya.exceptions.ServiceFailureException;
import com.galaksiya.logger.GLogger;
import com.galaksiya.logger.OperationLog;
import com.galaksiya.utils.EntityUtils;
import com.galaksiya.utils.RestUtils;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.galaksiya.constants.JsonProperties.*;

@Path("answerGroups")
@Produces({"application/json"})
public class AnswerGroupService {

	/**
	 * GLogger instance.
	 */
	private final GLogger logger = new GLogger(getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnswerGroups(@QueryParam(USER_KEY) String userKey, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		OperationLog log = logger.startOperation("getAnswerGroups")
			.addField(IP_ADDRESS, RestUtils.getIpAddress(request)).addField(USER_KEY, userKey);
		try {
			List<AnswerGroup> answerGroups = DatabaseConnector.getInstance()
				.getObjects(new GQueryFilter(new GQueryFilterPart(USER_KEY).setOperator(Operators.EQUAL, userKey)),
					AnswerGroup.class);
			if (answerGroups != null) {
				response.addProperty(SUCCESS, true);
				response.add(DATA, EntityUtils.convertEntityToJsonArray(answerGroups));
				log.succeed();
			} else {
				throw new ServiceFailureException(ErrorMessages.GET_ANSWER_GROUPS_ERROR);
			}
		} catch (Throwable t) {
			response = RestUtils.handleException(t, log);
		}
		return RestUtils.createResponseOK(response.toString());
	}

	@GET
	@Path("{id}/answers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnswersOfAnswerGroup(@PathParam (ID) int answerGroupId, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		OperationLog log = logger.startOperation("getAnswersOfAnswerGroup")
			.addField(IP_ADDRESS, RestUtils.getIpAddress(request)).addField(ID, answerGroupId);
		try {
			List<Answer> answers = DatabaseConnector.getInstance()
				.getObjects(new GQueryFilter(new GQueryFilterPart("answerGroup.id").setOperator(Operators.EQUAL,
					answerGroupId)), Answer.class);
			if (answers != null && answers.size() != 0) {
				response.addProperty(SUCCESS, true);
				response.add(DATA, EntityUtils.convertEntityToJsonArray(answers));
				log.succeed();
			} else {
				throw new ServiceFailureException(ErrorMessages.GET_ANSWERS_ERROR);
			}
		} catch (Throwable t) {
			response = RestUtils.handleException(t, log);
		}
		return RestUtils.createResponseOK(response.toString());
	}

}
