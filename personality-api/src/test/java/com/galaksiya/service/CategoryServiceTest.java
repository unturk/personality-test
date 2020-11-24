package com.galaksiya.service;

import com.galaksiya.constants.ErrorMessages;
import com.galaksiya.entity.Category;
import com.galaksiya.jersey.service.CategoryService;
import com.galaksiya.utils.RestServiceTestUtility;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Application;
import java.util.List;

import static com.galaksiya.constants.JsonProperties.*;
import static com.galaksiya.utils.TestConstants.TEST_CAT_CODE_1;
import static com.galaksiya.utils.TestConstants.TEST_CAT_NAME_1;
import static com.galaksiya.utils.TestUtils.addCategory;
import static com.galaksiya.utils.TestUtils.deleteAllCategories;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoryServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(CategoryService.class)
			.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
	}

	@After
	public void after() {
		deleteAllCategories();
	}

	@Test
	public void getCategories() {
		// Setup.
		Category category = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		Invocation.Builder request = target("categories").request();

		// Execute.
		JsonObject response = RestServiceTestUtility.sendGetRequest(request);

		// Assert.
		assertTrue("Response should include success property!", response.get(SUCCESS).getAsBoolean());
		JsonArray data = response.get(DATA).getAsJsonArray();
		List<Category> categories = new Gson().fromJson(data, new TypeToken<List<Category>>() {
		}.getType());
		assertTrue("Response categoryList size is not as expected!", categories.size() >= 1);
		assertTrue("Category list should contain saved category", categories.contains(category));
	}

	@Test
	public void getCategories_noCategory() {
		// Setup.
		Invocation.Builder request = target("categories").request();

		// Execute.
		javax.ws.rs.core.Response response = request.get();

		// Assert.
		JsonObject responseJson = new JsonParser().parse(response.readEntity(String.class)).getAsJsonObject();
		assertEquals("Response status is not as expected!", HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
		assertEquals("Failure message is not as expected!", ErrorMessages.GET_CATEGORIES_ERROR,
			responseJson.get(FAILURE).getAsString());
	}
}
