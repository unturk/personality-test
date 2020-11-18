package com.galaksiya.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "\"AnswerGroup\"")
public class AnswerGroup implements Serializable, HibernateEntity {

	/**
	 * Integer identifier of this {@link AnswerGroup} instance to be used as primary key.
	 */
	@Expose
	@Id
	@Column(name = "\"id\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Either user filled value that represents the user filled this group or random uuid for anonymity.
	 */
	@Expose
	@Column(name = "\"userKey\"", nullable = false)
	private String userKey;

	/**
	 * Field that represents the date the questions are answered.
	 */
	@Expose
	@Column(name = "\"date\"", nullable = false)
	private long date;

	/**
	 * Default constructor with no arguments.
	 */
	public AnswerGroup() {
	}

	/**
	 * Constructor.
	 *
	 * @see #userKey
	 * @see #date
	 */
	public AnswerGroup(String userKey) {
		this.userKey = userKey;
		this.date = System.currentTimeMillis();
	}

	/**
	 * Lazy fetch type constructor for the {@link AnswerGroup}.
	 *
	 * @param answerGroup {@link AnswerGroup} instance to be initialized lazily.
	 */
	public AnswerGroup(AnswerGroup answerGroup) {
		this.id = answerGroup.getId();
		this.userKey = answerGroup.getUserKey();
		this.date = answerGroup.getDate();
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
	 * @see #userKey
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * @see #userKey
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * @see #date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @see #date
	 */
	public void setDate(long dateMs) {
		this.date = dateMs;
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
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(new AnswerGroup(this));
	}

	/**
	 * Static fromJson method for the {@link AnswerGroup}. Deserializes from Json String to {@link AnswerGroup} object,
	 * excludes the object's fields without the expose annotation.
	 *
	 * @param jsonStr Serialized object string.
	 * @return Category Object
	 */
	public static AnswerGroup fromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, AnswerGroup.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AnswerGroup that = (AnswerGroup) o;
		return id == that.id && date == that.date && Objects.equals(userKey, that.userKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userKey, date);
	}

	@Override
	public String toString() {
		return "AnswerGroup{" + "id=" + id + ", userKey='" + userKey + '\'' + ", date=" + date + '}';
	}
}
