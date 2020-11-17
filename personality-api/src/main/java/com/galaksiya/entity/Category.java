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

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "\"Category\"")
public class Category implements Serializable, HibernateEntity {

	/**
	 * Integer identifier of this {@link Category} instance to be used as primary key.
	 */
	@Expose
	@Id
	@Column(name = "\"id\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Field that represents exact name of Category.
	 */
	@Expose
	@Column(name = "\"name\"", nullable = false)
	private String name;

	/**
	 * Field that represents code name of the category.
	 */
	@Expose
	@Column(name = "\"code\"", unique = true, nullable = false)
	private String code;

	/**
	 * Default constructor with no arguments.
	 */
	public Category() {
	}

	/**
	 * Constructor.
	 *
	 * @see #name
	 * @see #code
	 */
	public Category(String name, String code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * Lazy fetch type constructor for the {@link Category}.
	 *
	 * @param category {@link Category} instance to be initialized lazily.
	 */
	public Category(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.code = category.getCode();
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
	 * @see #name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see #code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @see #code
	 */
	public void setCode(String code) {
		this.code = code;
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
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(new Category(this));
	}

	/**
	 * Static fromJson method for the {@link Category}. Deserializes from Json String to {@link Category} object,
	 * excludes the object's fields without the expose annotation.
	 *
	 * @param jsonStr Serialized object string.
	 * @return Category Object
	 */
	public static Category fromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, Category.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Category category = (Category) o;
		return id == category.id && Objects.equals(name, category.name) && Objects.equals(code, category.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, code);
	}

	@Override
	public String toString() {
		return "Category{" + "id=" + id + ", name='" + name + '\'' + ", code='" + code + '\'' + '}';
	}
}
