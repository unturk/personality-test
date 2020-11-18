package com.galaksiya.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.Answer;
import com.galaksiya.entity.Category;
import com.galaksiya.entity.Question;
import com.galaksiya.entity.QuestionType;
import com.galaksiya.jersey.service.AnswerService;
import com.galaksiya.jersey.service.QuestionService;
import com.galaksiya.utils.RestServiceTestUtility;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Application;
import java.util.List;
import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.LazyMethodConstants.GET_ANSWER_GROUP;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class AnswerServiceTest extends JerseyTest {

	private static Question question1;
	private static Question question2;

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(AnswerService.class)
			.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
	}

	@BeforeClass
	public static void beforeClass() {
		Category category = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		question1 = addQuestion(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_1, category,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, null, 0);
		question2 = addQuestion(TEST_QUESTION_VALUE_2, TEST_QUESTION_DETAILS_2, category, QuestionType.NUMBER_RANGE,
			null, 0);
	}

	@AfterClass
	public static void afterClass() {
		deleteAllAnswers();
		deleteAllAnswerGroups();
		deleteAllQuestions();
		deleteAllCategories();
	}

	@Test
	public void saveAnswers() {
		// Setup.
		Invocation.Builder request = target("answers").request();
		JsonObject requestJson = prepareAnswerRequestJson(TEST_USER_KEY_1,
			new Answer(TEST_ANSWER_VALUE_1, question1, null), new Answer(TEST_ANSWER_VALUE_3, question2, null));

		// Execute.
		JsonObject response = RestServiceTestUtility.sendPostRequest(request, requestJson);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		final GQueryFilter filter = new GQueryFilter(
			new GQueryFilterPart("answerGroup.userKey").setOperator(Operators.EQUAL, TEST_USER_KEY_1));
		List<Answer> savedAnswers = getConnector().getObjects(filter, Answer.class, GET_ANSWER_GROUP);
		assertEquals("Response questionList size is not as expected!", 2, savedAnswers.size());
		savedAnswers.forEach(answer -> answersToDelete.add(answer.getId()));
		answerGroupsToDelete.add(savedAnswers.get(0).getAnswerGroup().getId());
	}

	@Test
	public void saveAnswers_noAnswer() {
		// Setup.
		Invocation.Builder request = target("answers").request();
		JsonObject requestJson = prepareAnswerRequestJson(UUID.randomUUID().toString());

		// Execute.
		JsonObject response = RestServiceTestUtility.sendPostRequest(request, requestJson);

		// Assert.
		assertEquals("Failure message is not as expected!", ErrorMessages.NO_ANSWER_EXISTS_ERROR,
			response.get(FAILURE).getAsString());
	}

	private JsonObject prepareAnswerRequestJson(String userKey, Answer... answers) {
		JsonObject requestJson = new JsonObject();
		requestJson.addProperty(USER_KEY, userKey);
		JsonArray answerArray = new JsonArray();
		for (Answer answer : answers) {
			JsonObject answerJson = new JsonObject();
			answerJson.addProperty(VALUE, answer.getValue());

			JsonObject questionJson = new JsonObject();
			questionJson.addProperty(ID, answer.getQuestion().getId());
			answerJson.add(QUESTION, questionJson);
			answerArray.add(answerJson);
		}
		requestJson.add(ANSWERS, answerArray);
		return requestJson;
	}

}
