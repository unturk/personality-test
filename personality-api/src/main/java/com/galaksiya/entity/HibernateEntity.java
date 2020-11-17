package com.galaksiya.entity;

import com.google.gson.JsonObject;

public interface HibernateEntity {

    JsonObject toJson();

    String toJsonString();
}
