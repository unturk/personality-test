package com.galaksiya.operation;

import com.galaksiya.db.gquery.GQueryFilter;
import com.galaksiya.db.gquery.GQueryFilterPart;
import com.galaksiya.db.gquery.Operators;
import com.galaksiya.entity.Category;
import com.galaksiya.entity.Question;
import com.galaksiya.entity.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.galaksiya.constants.JsonProperties.ID;
import static com.galaksiya.utils.LazyMethodConstants.GET_CATEGORY;
import static com.galaksiya.utils.TestConstants.*;
import static com.galaksiya.utils.TestUtils.*;
import static org.junit.Assert.*;

public class QuestionOperationTest {

	private Category category1;
	private Question question;
	private Question childQuestion;

	@Before
	public void setup() {
		category1 = addCategory(TEST_CAT_NAME_1, TEST_CAT_CODE_1);
		childQuestion = addQuestion(TEST_QUESTION_VALUE_2, TEST_QUESTION_DETAILS_2, category1,
			QuestionType.NUMBER_RANGE, null, null);
		question = addQuestion(TEST_QUESTION_VALUE_1, TEST_QUESTION_DETAILS_1, category1,
			QuestionType.CONDITIONAL_SINGLE_CHOICE, childQuestion, 0);
	}

	@After
	public void tearDown() {
		deleteAllQuestions();
		deleteAllCategories();
	}

	@Test
	public void getCategories() {
		// Execute.
		List questions = getConnector().getObjects(null, Question.class);

		// Assert.
		assertTrue("Retrieved questions size is not as expected", questions.size() >= 2);
		assertTrue("Retrieved questions should contain question", questions.contains(question));
		assertTrue("Retrieved questions should contain childQuestion", questions.contains(childQuestion));
	}

	@Test
	public void getNotExistingQuestion() {
		// Execute.
		Question question = getConnector().getObject(ID, Integer.MAX_VALUE, Question.class);

		// Assert.
		assertNull("Found object should have been null.", question);
	}

	@Test
	public void saveQuestion() {
		// Setup.
		Question question = new Question(TEST_QUESTION_VALUE_3, "", category1, QuestionType.SINGLE_CHOICE);

		// Execute.
		int savedObjectId = getConnector().saveObject(question);
		questionsToDelete.add(savedObjectId);

		// Assert.
		Question savedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, question.getId())),
				Question.class);
		assertNotNull("Saved object id should not null", savedObject);
	}

	@Test
	public void updateQuestion() {
		// Setup.
		Category category2 = addCategory(TEST_CAT_NAME_2, TEST_CAT_CODE_2);
		childQuestion.setValue(TEST_QUESTION_VALUE_3);
		childQuestion.setCategory(category2);

		// Execute.
		getConnector().updateObject(childQuestion);

		// Assert.
		Question updatedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, childQuestion.getId())),
				Question.class, GET_CATEGORY);
		assertEquals("Updated object name is not as expected", childQuestion.getValue(), updatedObject.getValue());
		assertEquals("Updated object category is not as expected", category2, updatedObject.getCategory());
	}

	@Test
	public void deleteQuestion() {
		// Execute.
		getConnector().deleteObject(question);

		// Assert.
		Question deletedObject = getConnector()
			.getObject(new GQueryFilter(new GQueryFilterPart(ID).setOperator(Operators.EQUAL, question.getId())),
				Question.class);
		assertNull("Saved object id should not null", deletedObject);
	}
}
