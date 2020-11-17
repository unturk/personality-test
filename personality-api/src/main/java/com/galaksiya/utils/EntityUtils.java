package com.galaksiya.utils;

import com.galaksiya.entity.HibernateEntity;
import com.google.gson.JsonArray;

import java.util.Collection;

public class EntityUtils {

	/**
	 * Converts given {@link Collection collection} of {@link com.galaksiya.entity.HibernateEntity} objects to jsonArray.
	 *
	 * @param entities {@link Collection} of {@link com.galaksiya.entity.HibernateEntity} objects.
	 * @return jsonArray that contains given entity json objects.
	 */
	public static JsonArray convertEntityToJsonArray(Collection<? extends HibernateEntity> entities) {
		JsonArray jsonArray = new JsonArray();
		if (entities != null) {
			entities.stream().map(HibernateEntity::toJson).forEach(jsonArray::add);
		}
		return jsonArray;
	}
}
