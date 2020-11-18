package com.galaksiya.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.entity.*;
import com.galaksiya.jersey.service.AnswerGroupService;
import com.galaksiya.utils.RestServiceTestUtility;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Application;
import java.util.List;
import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

@SuppressWarnings("UnstableApiUsage")
public class AnswerGroupServiceTest extends JerseyTest {

	private AnswerGroup answerGroup1;
	private AnswerGroup answerGroup2;
	private Answer answer;

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(AnswerGroupService.class)
			.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
	}

	@Before
	public void before() {
		answerGroup1 = addAnswerGroup(TEST_USER_KEY_1);
		answerGroup2 = addAnswerGroup(UUID.randomUUID().toString());
		Category category = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		Question question = addQuestion(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_1, category,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, null, 0);
		answer = addAnswer(TEST_ANSWER_VALUE_1, question, answerGroup1);
	}

	@After
	public void after() {
		deleteAllAnswers();
		deleteAllAnswerGroups();
		deleteAllQuestions();
		deleteAllCategories();
	}

	@Test
	public void getAnswerGroupAnswers() {
		// Setup.
		Invocation.Builder request = target(String.format("answerGroups/%s/answers", answerGroup1.getId())).request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<Answer> answers = new Gson().fromJson(data, new TypeToken<List<Answer>>() {
		}.getType());
		assertEquals("Response answers size is not as expected!", 1, answers.size());
		assertTrue("Answer list should contain saved answer", answers.contains(answer));
	}

	@Test
	public void getAnswerGroupAnswers_noAnswer() {
		// Setup.
		Invocation.Builder request = target(String.format("answerGroups/%s/answers", answerGroup2.getId())).request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertEquals("Failure message is not as expected!", ErrorMessages.GET_ANSWERS_ERROR,
			response.get(FAILURE).getAsString());
	}

	@Test
	public void getAnswerGroups() {
		// Setup.
		Invocation.Builder request = target("answerGroups").request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<AnswerGroup> answerGroups = new Gson().fromJson(data, new TypeToken<List<AnswerGroup>>() {
		}.getType());
		assertEquals("Response categoryList size is not as expected!", 2, answerGroups.size());
		assertTrue("Answer group list should contain saved answerGroup1", answerGroups.contains(answerGroup1));
		assertTrue("Answer group list should contain saved answerGroup2", answerGroups.contains(answerGroup2));
	}

	@Test
	public void getAnswerGroups_withFilter() {
		// Setup.
		Invocation.Builder request = target("answerGroups").queryParam(USER_KEY, answerGroup1.getUserKey()).request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<AnswerGroup> answerGroups = new Gson().fromJson(data, new TypeToken<List<AnswerGroup>>() {
		}.getType());
		assertEquals("Response categoryList size is not as expected!", 1, answerGroups.size());
		assertTrue("Answer group list should contain saved answerGroup1", answerGroups.contains(answerGroup1));
		assertFalse("Answer group list should not contain saved answerGroup2", answerGroups.contains(answerGroup2));
	}

	@Test
	public void getAnswerGroups_noData() {
		// Setup.
		DatabaseConnector.getInstance().deleteObject(answerGroup1);
		DatabaseConnector.getInstance().deleteObject(answerGroup2);
		Invocation.Builder request = target("answerGroups").request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		assertEquals("Response categoryList size is not as expected!", 0, data.size());
	}
}
