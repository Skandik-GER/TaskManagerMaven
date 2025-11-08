package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskParserUtilits {

    public static List<Long> historyFromString(String value) {
        if(value == null){
            return Collections.emptyList();
        }
        final List<Long> history = new ArrayList<>();
        String[] values = value.split(",");
        for (String number : values) {
            long num = Long.parseLong(number);
            history.add(num);
        }
        return history;

    }

    public static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        long id = Long.parseLong(parts[3]);
        Status status = Status.valueOf(parts[4]);
        // RED: Потенциальная ошибка. Парсится 5-й элемент (parts[5]) как duration,
        // но в строке для Подзадачи parts[5] - это epicId.
        // Видно, что методы парсинга скопированы и не адаптированы под разное количество полей.
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[6]));
        LocalDateTime startTime = LocalDateTime.parse(parts[7],formatter);
        return new Task(id, name, describe, status,duration,startTime);
    }

    public static Epic fromStringEpic(String value) {
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        return new Epic(name, describe);
    }

    public static Subtask fromStringSubtask(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        long id = Long.parseLong(parts[3]);
        Status status = Status.valueOf(parts[4]);
        long epicId = Long.parseLong(parts[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[6]));
        LocalDateTime startTime = LocalDateTime.parse(parts[7],formatter);
        return new Subtask(id, name, describe, epicId, status,duration,startTime);
    }
}
