package com.iskander.model;

import java.time.format.DateTimeFormatter;

public class TaskFormatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String serialize(Subtask task) {
        if (task == null) throw new IllegalArgumentException("Подзадача не может быть пуста");

        String durationStr = "null";
        if (task.getDuration() != null) {
            durationStr = String.valueOf(task.getDuration().toMinutes());
        }

        String startTimeStr = "null";
        if (task.getStartTime() != null) {
            startTimeStr = task.getStartTime().format(formatter);
        }

        return String.join(",",
                task.getName(),
                task.getDescribe(),
                String.valueOf(task.getId()),
                task.getStatus().name(),
                String.valueOf(task.getEpicId()),
                durationStr,
                startTimeStr
        );
    }

    public String serialize(Task task) {
        if (task == null) throw new IllegalArgumentException("Задча не может быть пуста");

        String durationStr = "null";
        if (task.getDuration() != null) {
            durationStr = String.valueOf(task.getDuration().toMinutes());
        }

        String startTimeStr = "null";
        if (task.getStartTime() != null) {
            startTimeStr = task.getStartTime().format(formatter);
        }

        return String.join(",",
                task.getName(),
                task.getDescribe(),
                String.valueOf(task.getId()),
                task.getStatus().name(),
                durationStr,
                startTimeStr
        );
    }
    public String serialize(Epic epic) {
        if (epic == null) throw new IllegalArgumentException("Эпик не может быть пуст");

        return String.join(",",
                epic.getName(),
                epic.getDescribe(),
                String.valueOf(epic.getId()),
                epic.getStatus().name()
        );
    }


}
