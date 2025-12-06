package com.iskander.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iskander.model.Epic;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskGson {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new GsonDurationAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .create();
}
