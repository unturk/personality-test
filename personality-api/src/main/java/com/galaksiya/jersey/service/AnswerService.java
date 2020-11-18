package com.galaksiya.jersey.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.db.TransactionObjects;
import com.galaksiya.entity.Answer;
import com.galaksiya.entity.AnswerGroup;
import com.galaksiya.exceptions.ServiceFailureException;
import com.galaksiya.logger.GLogger;
import com.galaksiya.logger.OperationLog;
import com.galaksiya.utils.RestUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.*;

@Path("answers")
@Produces({"application/json"})
public class AnswerService {

	/**
	 * GLogger instance.
	 */
	private final GLogger logger = new GLogger(getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAnswers(String data, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		OperationLog log = logger.startOperation("saveAnswers")
			.addField(IP_ADDRESS, RestUtils.getIpAddress(request)).addField(DATA, data);
		try {
			JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
			List<Answer> answers = parseAnswers(dataJson);
			String userKey = dataJson.has(USER_KEY) ? dataJson.get(USER_KEY).getAsString() : UUID.randomUUID()
				.toString();
			processAnswerTransaction(answers, userKey);
			response.addProperty(SUCCESS, true);
			log.succeed();
		} catch (Throwable t) {
			response = RestUtils.handleException(t, log);
		}
		return RestUtils.createResponseOK(response.toString());
	}

	/**
	 * Creates a {@link Session} instance and saves the given {@link List} of {@link Answer} along with {@link
	 * AnswerGroup} which is created via given user key parameter.
	 *
	 * @param answers List of answers to be saved.
	 * @param userKey User key to create {@link AnswerGroup} with.
	 */
	private void processAnswerTransaction(List<Answer> answers, String userKey) {
		DatabaseConnector connector = DatabaseConnector.getInstance();
		try (Session session = connector.openSession()) {
			final AnswerGroup answerGroup = new AnswerGroup(userKey);
			final TransactionObjects txObjects = new TransactionObjects();
			connector.saveObject(session, answerGroup, txObjects);

			answers.forEach(answer -> {
				answer.setAnswerGroup(answerGroup);
				connector.saveObject(session, answer, txObjects);
			});
			connector.doCombinedOperations(session, txObjects);
		}
	}

	@SuppressWarnings("UnstableApiUsage")
	private List<Answer> parseAnswers(JsonObject dataJson) throws ServiceFailureException {
		List<Answer> answerList;
		if (dataJson.has(ANSWERS)) {
			answerList = new Gson()
				.fromJson(dataJson.get(ANSWERS).getAsJsonArray(), new TypeToken<List<Answer>>() {
				}.getType());
			if (answerList == null || answerList.isEmpty()) {
				throw new ServiceFailureException(ErrorMessages.NO_ANSWER_EXISTS_ERROR);
			}
		} else {
			throw new ServiceFailureException(ErrorMessages.NO_ANSWER_EXISTS_ERROR);
		}
		return answerList;
	}
}
