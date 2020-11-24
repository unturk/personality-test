package com.galaksiya.operation;

import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.galaksiya.constants.JsonProperties.ID;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class CategoryOperationTest {

	private Category category1;
	private Category category2;

	@Before
	public void setup() {
		category1 = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		category2 = addCategory(TEST_CAT_NAME_2, TEST_CAT_CODE_2);
	}

	@After
	public void tearDown() {
		deleteAllCategories();
	}

	@Test
	public void getCategories() {
		// Execute.
		List categories = getConnector().getObjects(null, Category.class);

		// Assert.
		assertTrue("Retrieved Categories size is not as expected", categories.size() >= 2);
		assertTrue("Retrieved Categories should contain category1", categories.contains(category1));
		assertTrue("Retrieved Categories should contain category2", categories.contains(category2));
	}

	@Test
	public void getNotExistingCategory() {
		// Execute.
		Category category = getConnector().getObject(ID, Integer.MAX_VALUE, Category.class);

		// Assert.
		assertNull("Found object should have been null.", category);
	}

	@Test
	public void saveCategory() {
		// Setup.
		Category category = new Category(TEST_CAT_NAME_3, TEST_CAT_CODE_3);

		// Execute.
		int savedObjectId = getConnector().saveObject(category);
		categoriesToDelete.add(savedObjectId);

		// Assert.
		Category savedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, category.getId())),
				Category.class);
		assertNotNull("Saved object id should not null", savedObject);
	}

	@Test
	public void updateCategory() {
		// Setup.
		category1.setName(TEST_CAT_NAME_3);
		category1.setCode(TEST_CAT_CODE_3);

		// Execute.
		getConnector().updateObject(category1);

		// Assert.
		Category updatedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, category1.getId())),
				Category.class);
		assertEquals("Updated object name is not as expected", category1.getName(), updatedObject.getName());
		assertEquals("Updated object code is not as expected", category1.getCode(), updatedObject.getCode());
	}

	@Test
	public void deleteCategory() {
		// Execute.
		getConnector().deleteObject(category1);

		// Assert.
		Category deletedObject = getConnector().getObject(
			new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, category1.getId())),
			Category.class);
		assertNull("Saved object id should not null", deletedObject);
	}
}
