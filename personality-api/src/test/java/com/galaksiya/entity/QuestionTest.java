package com.galaksiya.entity;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.*;
import static org.junit.Assert.*;
import static sun.plugin2.util.ParameterNames.CODE;

public class QuestionTest {

	private Question question;

	@Before
	public void setup() {
		Category category = new Category(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		Question childQuestion = new Question(TEST_QUESTION_VALUE_2, TEST_QUESTION_DETAILS_1, category,
			QuestionType.NUMBER_RANGE);
		this.question = new Question(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_2, category,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, childQuestion);
	}

	@Test
	public void toJson() {
		// Execute.
		JsonObject questionJson = question.toJson();

		// Assert.
		assertNotNull("Serializing object to JSON has been failed!", questionJson);
		assertTrue("Serialized object must contain a Id field!", questionJson.has(ID));
		assertTrue("Serialized object must contain a value field!", questionJson.has(VALUE));
		assertTrue("Serialized object must contain a category field!", questionJson.has(CATEGORY));
		assertTrue("Serialized object must contain a child field!", questionJson.has(CHILD));
		assertTrue("Serialized object must contain a details field!", questionJson.has(DETAILS));
		assertTrue("Serialized object must contain a details field!", questionJson.has(QUESTION_TYPE));
	}

	@Test
	public void toJsonString() {
		// Execute.
		String str = question.toJsonString();

		// Assert.
		assertNotNull("Serializing object to JSON string has been failed!", str);
	}

	@Test
	public void fromJson() {
		// Setup.
		String str = question.toJsonString();

		// Execute.
		Question questionFromJson = Question.fromJson(str);

		// Assert.
		assertEquals("Converted object Id must be the same!", question.getId(), questionFromJson.getId());
		assertEquals("Converted object value must be the same!", question.getValue(), questionFromJson.getValue());
		assertEquals("Converted object question type must be the same!", question.getQuestionType(),
			questionFromJson.getQuestionType());
	}
}
