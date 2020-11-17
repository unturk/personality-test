package com.galaksiya.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;


public class RestServiceTestUtility {

	private static final JsonParser parser = new JsonParser();

	private static JsonParser getParser() {
		return parser;
	}

	public static JsonObject sendGetRequest(Invocation.Builder request) {
		return getParser().parse(request.get(String.class)).getAsJsonObject();
	}

	public static JsonObject sendDeleteRequest(Invocation.Builder request) {
		return getParser().parse(request.delete(String.class)).getAsJsonObject();
	}

	public static JsonObject sendPostRequest(Invocation.Builder request, JsonObject requestJson) {
		return getParser().parse(request.post(Entity.json(requestJson.toString()), String.class)).getAsJsonObject();
	}

	public static JsonObject sendPutRequest(Invocation.Builder request, JsonObject requestJson) {
		return getParser().parse(request.put(Entity.json(requestJson.toString()), String.class)).getAsJsonObject();
	}

}
