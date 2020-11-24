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
@Table(name = "\"Question\"")
public class Question implements Serializable, HibernateEntity {

	/**
	 * Integer identifier of this {@link Question} instance to be used as primary key.
	 */
	@Expose
	@Id
	@Column(name = "\"id\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Field that represents exact value of Question.
	 */
	@Expose
	@Column(name = "\"value\"", nullable = false)
	private String value;

	/**
	 * Field that represents child of the Question.
	 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"category\"")
	private Category category;

	/**
	 * Field that represents child of this Question if any exists.
	 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"child\"")
	private Question child;

	/**
	 * Field that represents the type of question.
	 */
	@Expose
	@Column(name = "\"questionType\"")
	@Enumerated(EnumType.ORDINAL)
	private QuestionType questionType;

	/**
	 * Field that contains the details of the question in string format of JsonObject representation. For example; when
	 * {@link #questionType} is {@link QuestionType#SINGLE_CHOICE}, this field should contain options as an array. Also
	 * contains any existing conditions like {"operator":"equals", "value":"important"}.
	 */
	@Expose
	@Column(name = "\"details\"", length = 5000)
	private String details;

	/**
	 * Default constructor with no arguments.
	 */
	public Question() {
	}

	public Question(String value, String details, Category category, QuestionType questionType) {
		this(value, details, category, questionType, null);
	}

	/**
	 * Constructor.
	 *
	 * @see #value
	 * @see #category
	 * @see #questionType
	 * @see #child
	 */
	public Question(String value, String details, Category category, QuestionType questionType, Question child) {
		this.value = value;
		this.category = category;
		this.questionType = questionType;
		this.child = child;
		this.details = details;
	}

	/**
	 * Lazy fetch type constructor for the {@link Question}.
	 *
	 * @param question {@link Question} instance to be initialized lazily.
	 */
	public Question(Question question, boolean showChildQuestion) {
		this.id = question.getId();
		this.value = question.getValue();
		this.questionType = question.getQuestionType();
		this.details = question.getDetails();
		Category category = question.getCategory();
		if (category != null && Hibernate.isInitialized(category)) {
			this.category = new Category(category);
		}

		if (showChildQuestion) {
			Question child = question.getChild();
			if (child != null && Hibernate.isInitialized(child)) {
				this.child = new Question(child, false);
			}
		}
	}

	/**
	 * @see #id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @see #id
	 */
	public int getId() {
		return id;
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
	 * @see #category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @see #category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @see #child
	 */
	public void setChild(Question child) {
		this.child = child;
	}

	/**
	 * @see #child
	 */
	public Question getChild() {
		return child;
	}

	/**
	 * @see #questionType
	 */
	public QuestionType getQuestionType() {
		return questionType;
	}

	/**
	 * @see #questionType
	 */
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	/**
	 * @see #details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @see #details
	 */
	public void setDetails(String details) {
		this.details = details;
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
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(new Question(this, true));
	}

	/**
	 * Static fromJson method for the {@link Question}. Deserializes from Json String to {@link Question} object,
	 * excludes the object's fields without the expose annotation.
	 *
	 * @param jsonStr Serialized object string.
	 * @return Category Object
	 */
	public static Question fromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, Question.class);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Question question = (Question) o;
		return id == question.id && Objects
			.equals(value, question.value) && questionType == question.questionType && Objects
			.equals(details, question.details);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, value, questionType, details);
	}

	@Override
	public String toString() {
		return "Question{" + "id=" + id + ", value='" + value + '\'' + ", questionType=" + questionType + ", details" +
			"='" + details + '\'' + '}';
	}
}
