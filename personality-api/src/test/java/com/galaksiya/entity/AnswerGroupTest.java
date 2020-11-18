package com.galaksiya.entity;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.TEST_CAT_CODE_1;
import static com.galaksiya.utils.TestConstants.TEST_CAT_NAME_1;
import static org.junit.Assert.*;
import static sun.plugin2.util.ParameterNames.CODE;

public class AnswerGroupTest {

	private AnswerGroup answerGroup;

	@Before
	public void setup() {
		answerGroup = new AnswerGroup("testKey");
	}

	@Test
	public void toJson() {
		// Execute.
		JsonObject categoryJson = answerGroup.toJson();

		// Assert.
		assertNotNull("Serializing object to JSON has been failed!", categoryJson);
		assertTrue("Serialized object must contain a Id field!", categoryJson.has(ID));
		assertTrue("Serialized object must contain a name field!", categoryJson.has(USER_KEY));
		assertTrue("Serialized object must contain a order field!", categoryJson.has(DATE));
	}

	@Test
	public void toJsonString() {
		// Execute.
		String str = answerGroup.toJsonString();

		// Assert.
		assertNotNull("Serializing object to JSON string has been failed!", str);
	}

	@Test
	public void fromJson() {
		// Setup.
		String str = answerGroup.toJsonString();

		// Execute.
		AnswerGroup answerGroupFromJson = AnswerGroup.fromJson(str);

		// Assert.
		assertEquals("Converted object Id must be the same!", answerGroup.getId(), answerGroupFromJson.getId());
		assertEquals("Converted object user key must be the same!", answerGroup.getUserKey(),
			answerGroupFromJson.getUserKey());
		assertEquals("Converted object date must be the same!", answerGroup.getDate(), answerGroupFromJson.getDate());
	}
}
