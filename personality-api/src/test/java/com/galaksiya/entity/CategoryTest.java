package com.galaksiya.entity;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.galaksiya.constants.JsonProperties.ID;
import static com.galaksiya.constants.JsonProperties.NAME;
import static com.galaksiya.utils.TestConstants.TEST_CAT_CODE_1;
import static com.galaksiya.utils.TestConstants.TEST_CAT_NAME_1;
import static org.junit.Assert.*;
import static sun.plugin2.util.ParameterNames.CODE;

public class CategoryTest {

	private Category category;

	@Before
	public void setup() {
		category = new Category(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
	}

	@Test
	public void toJson() {
		// Execute.
		JsonObject categoryJson = category.toJson();

		// Assert.
		assertNotNull("Serializing object to JSON has been failed!", categoryJson);
		assertTrue("Serialized object must contain a Id field!", categoryJson.has(ID));
		assertTrue("Serialized object must contain a name field!", categoryJson.has(NAME));
		assertTrue("Serialized object must contain a order field!", categoryJson.has(CODE));
	}

	@Test
	public void toJsonString() {
		// Execute.
		String str = category.toJsonString();

		// Assert.
		assertNotNull("Serializing object to JSON string has been failed!", str);
	}

	@Test
	public void fromJson() {
		// Setup.
		String str = category.toJsonString();

		// Execute.
		Category categoryFromJson = Category.fromJson(str);

		// Assert.
		assertEquals("Converted object Id must be the same!", category.getId(), categoryFromJson.getId());
		assertEquals("Converted object name must be the same!", category.getName(), categoryFromJson.getName());
		assertEquals("Converted object code must be the same!", category.getCode(), categoryFromJson.getCode());
	}
}
