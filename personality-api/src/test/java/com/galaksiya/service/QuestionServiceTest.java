package com.galaksiya.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.entity.Category;
import com.galaksiya.entity.Question;
import com.galaksiya.entity.QuestionType;
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

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class QuestionServiceTest extends JerseyTest {

	private static Category category1;
	private static Question question1;
	private static Question question2;

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(QuestionService.class)
			.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
	}

	@BeforeClass
	public static void beforeClass() {
		category1 = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		Category category2 = addCategory(TEST_CAT_NAME_2, TEST_CAT_CODE_2);
		question1 = addQuestion(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_1, category1,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, null, 0);
		question2 = addQuestion(TEST_QUESTION_VALUE_2, TEST_QUESTION_DETAILS_2, category2, QuestionType.NUMBER_RANGE,
			null, 0);
	}

	@AfterClass
	public static void afterClass() {
		deleteAllQuestions();
		deleteAllCategories();
	}

	@Test
	public void getQuestions() {
		// Setup.
		Invocation.Builder request = target("questions").request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<Question> questions = new Gson().fromJson(data, new TypeToken<List<Question>>() {
		}.getType());
		assertEquals("Response questionList size is not as expected!", 2, questions.size());
		assertTrue("Question list should contain saved question1", questions.contains(question1));
		assertTrue("Question list should contain saved question2", questions.contains(question2));
	}

	@Test
	public void getQuestions_specificCategory() {
		// Setup.
		Invocation.Builder request = target("questions").queryParam(CATEGORY, category1.getId()).request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<Question> questions = new Gson().fromJson(data, new TypeToken<List<Question>>() {
		}.getType());
		assertEquals("Response questionList size is not as expected!", 1, questions.size());
		assertTrue("Question list should contain saved question1", questions.contains(question1));
		assertFalse("Question list should contain saved question2", questions.contains(question2));
	}

	@Test
	public void getQuestions_nonexistentCategory() {
		// Setup.
		Invocation.Builder request = target("questions").queryParam(CATEGORY, Integer.MAX_VALUE).request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertEquals("Failure message is not as expected!", ErrorMessages.GET_QUESTIONS_ERROR,
			response.get(FAILURE).getAsString());
	}
}
