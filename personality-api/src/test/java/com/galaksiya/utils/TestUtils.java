package com.galaksiya.utils;

import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.entity.*;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

	private final static DatabaseConnector connector = DatabaseConnector.getInstance();

	public static List<Integer> categoriesToDelete = new ArrayList<>();
	public static List<Integer> questionsToDelete = new ArrayList<>();
	public static List<Integer> answerGroupsToDelete = new ArrayList<>();
	public static List<Integer> answersToDelete = new ArrayList<>();

	public static AnswerGroup addAnswerGroup(String userKey) {
		AnswerGroup answerGroup = new AnswerGroup(userKey);
		int id = connector.saveObject(answerGroup);
		answerGroupsToDelete.add(id);
		answerGroup.setId(id);
		return answerGroup;
	}

	public static void deleteAllAnswerGroups() {
		answerGroupsToDelete.forEach(answerGroupId -> {
			try {
				connector.deleteObject(answerGroupId, AnswerGroup.class);
			} catch (Exception ignored) {
			}
		});
		answerGroupsToDelete.clear();
	}

	public static Category addCategory(String name, String code) {
		Category category = new Category(name, code);
		int id = connector.saveObject(category);
		categoriesToDelete.add(id);
		category.setId(id);
		return category;
	}

	public static void deleteAllCategories() {
		categoriesToDelete.forEach(categoryId -> {
			try {
				connector.deleteObject(categoryId, Category.class);
			} catch (Exception ignored) {
			}
		});
		categoriesToDelete.clear();
	}

	public static Answer addAnswer(String value, Question question, AnswerGroup answerGroup) {
		Answer answer = new Answer(value, question, answerGroup);
		int id = connector.saveObject(answer);
		answersToDelete.add(id);
		answer.setId(id);
		return answer;
	}

	public static void deleteAllAnswers() {
		answersToDelete.forEach(answerId -> {
			try {
				connector.deleteObject(answerId, Answer.class);
			} catch (Exception ignored) {
			}
		});
		answersToDelete.clear();
	}

	public static Question addQuestion(String value, String details, Category category, QuestionType questionType,
		Question child, Integer index) {
		Question question = new Question(value, details, category, questionType, child);
		int id = connector.saveObject(question);
		if (index != null) {
			questionsToDelete.add(index, id);
		} else {
			questionsToDelete.add(id);
		}
		question.setId(id);
		return question;
	}

	public static void deleteAllQuestions() {
		questionsToDelete.forEach(questionId -> {
			try {
				connector.deleteObject(questionId, Question.class);
			} catch (Exception ignored) {
			}
		});
		questionsToDelete.clear();
	}

	public static DatabaseConnector getConnector() {
		return connector;
	}
}
