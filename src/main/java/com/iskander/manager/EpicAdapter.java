package com.iskander.manager;

import com.google.gson.*;
import com.iskander.model.Epic;
import com.iskander.model.Subtask;

import java.lang.reflect.Type;

public class EpicAdapter implements  JsonDeserializer<Epic> {

    public Epic deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Epic epic = new Epic(
                jsonObject.get("name").getAsString(),
                jsonObject.get("describe").getAsString()
        );

        // Устанавливаем ID если есть
        if (jsonObject.has("id")) {
            // через рефлексию или сеттер
        }

        // Десериализуем подзадачи
        if (jsonObject.has("subTasks")) {
            JsonArray subTasksArray = jsonObject.getAsJsonArray("subTasks");
            for (JsonElement element : subTasksArray) {
                Subtask subtask = context.deserialize(element, Subtask.class);
                epic.addSubTask(subtask);
            }
        }

        return epic;
    }
}
