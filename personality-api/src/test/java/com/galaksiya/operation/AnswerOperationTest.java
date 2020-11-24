package com.galaksiya.operation;

import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.galaksiya.constants.JsonProperties.ID;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class AnswerOperationTest {

	private Answer answer1;
	private Answer answer2;
	private AnswerGroup answerGroup;
	private Question question;

	@Before
	public void setup() {
		Category category = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		answerGroup = addAnswerGroup(UUID.randomUUID().toString());
		question = addQuestion(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_1, category,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, null, 0);
		answer1 = addAnswer(TEST_ANSWER_VALUE_1, question, answerGroup);
		answer2 = addAnswer(TEST_ANSWER_VALUE_2, question, answerGroup);
	}

	@After
	public void tearDown() {
		deleteAllAnswers();
		deleteAllAnswerGroups();
		deleteAllQuestions();
		deleteAllCategories();
	}

	@Test
	public void getAnswers() {
		// Execute.
		List answers = getConnector().getObjects(null, Answer.class);

		// Assert.
		assertTrue("Retrieved answers size is not as expected", answers.size() >= 2);
		assertTrue("Retrieved answers should contain question", answers.contains(answer1));
		assertTrue("Retrieved answers should contain childQuestion", answers.contains(answer2));
	}

	@Test
	public void getNotExistingAnswer() {
		// Execute.
		Answer answer = getConnector().getObject(ID, Integer.MAX_VALUE, Answer.class);

		// Assert.
		assertNull("Found object should have been null.", answer);
	}

	@Test
	public void saveAnswer() {
		// Setup.
		Answer answer = new Answer(TEST_QUESTION_VALUE_3, question, answerGroup);

		// Execute.
		int savedObjectId = getConnector().saveObject(answer);
		answersToDelete.add(savedObjectId);

		// Assert.
		Answer savedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, answer.getId())),
				Answer.class);
		assertNotNull("Saved object id should not null", savedObject);
	}

	@Test
	public void updateAnswer() {
		// Setup.
		answer1.setValue(TEST_QUESTION_VALUE_3);

		// Execute.
		getConnector().updateObject(answer1);

		// Assert.
		Answer updatedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, answer1.getId())),
				Answer.class);
		assertEquals("Updated object value is not as expected", answer1.getValue(), updatedObject.getValue());
	}

	@Test
	public void deleteAnswer() {
		// Execute.
		getConnector().deleteObject(answer1);

		// Assert.
		Answer deletedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, question.getId())),
				Answer.class);
		assertNull("Saved object id should not null", deletedObject);
	}
}
