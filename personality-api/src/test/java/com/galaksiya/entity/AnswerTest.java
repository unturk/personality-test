package com.galaksiya.entity;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.*;
import static org.junit.Assert.*;

public class AnswerTest {

	private Answer answer;

	@Before
	public void setup() {
		Category category = new Category(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		Question question = new Question(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_2, category,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, null);
		AnswerGroup answerGroup = new AnswerGroup(UUID.randomUUID().toString());
		answer = new Answer(TEST_ANSWER_VALUE_1, question, answerGroup);
	}

	@Test
	public void toJson() {
		// Execute.
		JsonObject answerJson = answer.toJson();

		// Assert.
		assertNotNull("Serializing object to JSON has been failed!", answerJson);
		assertTrue("Serialized object must contain a Id field!", answerJson.has(ID));
		assertTrue("Serialized object must contain a value field!", answerJson.has(VALUE));
		assertTrue("Serialized object must contain a question field!", answerJson.has(QUESTION));
		assertTrue("Serialized object must contain an answer group field!", answerJson.has(ANSWER_GROUP));
	}

	@Test
	public void toJsonString() {
		// Execute.
		String str = answer.toJsonString();

		// Assert.
		assertNotNull("Serializing object to JSON string has been failed!", str);
	}

	@Test
	public void fromJson() {
		// Setup.
		String str = answer.toJsonString();

		// Execute.
		Answer answerFromJson = Answer.fromJson(str);

		// Assert.
		assertEquals("Converted object Id must be the same!", answer.getId(), answerFromJson.getId());
		assertEquals("Converted object value must be the same!", answer.getValue(), answerFromJson.getValue());
		assertEquals("Converted object question must be the same!", answer.getQuestion(),
			answerFromJson.getQuestion());
		assertEquals("Converted object answer group must be the same!", answer.getAnswerGroup(),
			answerFromJson.getAnswerGroup());
	}
}
