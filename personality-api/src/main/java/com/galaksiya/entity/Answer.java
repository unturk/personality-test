package com.galaksiya.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "\"Answer\"")
public class Answer implements Serializable, HibernateEntity {

	/**
	 * Integer identifier of this {@link Answer} instance to be used as primary key.
	 */
	@Expose
	@Id
	@Column(name = "\"id\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Field that represents answer value.
	 */
	@Expose
	@Column(name = "\"value\"", nullable = false)
	private String value;

	/**
	 * Field that represents the question this answer belongs to.
	 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"question\"", nullable = false)
	private Question question;

	/**
	 * Field that represents the answer group this answer belongs to.
	 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"answerGroup\"", nullable = false)
	private AnswerGroup answerGroup;

	/**
	 * Default constructor with no arguments.
	 */
	public Answer() {
	}

	/**
	 * Constructor.
	 *
	 * @see #value
	 * @see #question
	 * @see #answerGroup
	 */
	public Answer(String value, Question question, AnswerGroup answerGroup) {
		this.value = value;
		this.question = question;
		this.answerGroup = answerGroup;
	}

	/**
	 * Lazy fetch type constructor for the {@link Answer}.
	 *
	 * @param answer {@link Answer} instance to be initialized lazily.
	 */
	public Answer(Answer answer) {
		this.id = answer.getId();
		this.value = answer.getValue();
		Question question = answer.getQuestion();
		if (question != null && Hibernate.isInitialized(question)) {
			this.question = new Question(question, false);
		}
		AnswerGroup answerGroup = answer.getAnswerGroup();
		if (answerGroup != null && Hibernate.isInitialized(answerGroup)) {
			this.answerGroup = new AnswerGroup(answerGroup);
		}
	}

	/**
	 * @see #id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @see #id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @see #value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @see #value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see #question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @see #question
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @see #answerGroup
	 */
	public AnswerGroup getAnswerGroup() {
		return answerGroup;
	}

	/**
	 * @see #answerGroup
	 */
	public void setAnswerGroup(AnswerGroup answerGroup) {
		this.answerGroup = answerGroup;
	}

	/**
	 * Serializes this hierarchy to a basic JSON, excludes the object's field without the expose annotation.
	 *
	 * @return Serialized object string.
	 */
	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	/**
	 * Serializes this object to a basic JSON string, excludes the object's fields without the expose annotation.
	 *
	 * @return Serialized object as String.
	 */
	public String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(new Answer(this));
	}

	/**
	 * Static fromJson method for the {@link Answer}. Deserializes from Json String to {@link Answer} object,
	 * excludes the object's fields without the expose annotation.
	 *
	 * @param jsonStr Serialized object string.
	 * @return Category Object
	 */
	public static Answer fromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, Answer.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Answer answer = (Answer) o;
		return id == answer.id && Objects.equals(value, answer.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, value);
	}

	@Override
	public String toString() {
		return "Answer{" + "id=" + id + ", value='" + value + '\'' + '}';
	}
}
