package com.galaksiya.operation;

import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.AnswerGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.ID;
import static com.galaksiya.utils.TestConstants.TEST_USER_KEY_1;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class AnswerGroupOperationTest {

	private AnswerGroup answerGroup1;
	private AnswerGroup answerGroup2;

	@Before
	public void setup() {
		answerGroup1 = addAnswerGroup(TEST_USER_KEY_1);
		answerGroup2 = addAnswerGroup(UUID.randomUUID().toString());
	}

	@After
	public void tearDown() {
		deleteAllAnswerGroups();
	}

	@Test
	public void getAnswerGroups() {
		// Execute.
		List answerGroups = getConnector().getObjects(null, AnswerGroup.class);

		// Assert.
		assertEquals("Retrieved answer groups size is not as expected", 2, answerGroups.size());
		assertTrue("Retrieved answer groups should contain category1", answerGroups.contains(answerGroup1));
		assertTrue("Retrieved answer groups should contain category2", answerGroups.contains(answerGroup2));
	}

	@Test
	public void getNotExistingAnswerGroup() {
		// Execute.
		AnswerGroup category = getConnector().getObject(ID, Integer.MAX_VALUE, AnswerGroup.class);

		// Assert.
		assertNull("Found object should have been null.", category);
	}

	@Test
	public void saveAnswerGroup() {
		// Setup.
		AnswerGroup answerGroup = new AnswerGroup(UUID.randomUUID().toString());

		// Execute.
		int savedObjectId = getConnector().saveObject(answerGroup);
		answerGroupsToDelete.add(savedObjectId);

		// Assert.
		AnswerGroup savedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, answerGroup.getId())),
				AnswerGroup.class);
		assertNotNull("Saved object id should not null", savedObject);
	}

	@Test
	public void updateAnswerGroup() {
		// Setup.
		answerGroup1.setUserKey("updatedKey");

		// Execute.
		getConnector().updateObject(answerGroup1);

		// Assert.
		AnswerGroup updatedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, answerGroup1.getId())),
				AnswerGroup.class);
		assertEquals("Updated object key is not as expected", answerGroup1.getUserKey(), updatedObject.getUserKey());
	}

	@Test
	public void deleteAnswerGroup() {
		// Execute.
		getConnector().deleteObject(answerGroup1);

		// Assert.
		AnswerGroup deletedObject = getConnector().getObject(
			new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, answerGroup1.getId())),
			AnswerGroup.class);
		assertNull("Saved object id should not null", deletedObject);
	}
}
