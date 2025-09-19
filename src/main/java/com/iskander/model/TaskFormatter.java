package com.iskander.model;

import java.time.format.DateTimeFormatter;

//RED: Переименуйте класс и методы. Класс должен называться так, чтобы отражать его суть,
// например, TaskFormatter или TaskCsvSerializer.++
// Методы toParse лучше переименовать в format или serialize.
//
//YELLOW: Унифицируйте формат вывода для всех типов задач или четко задокументируйте отличия. Для Epic также стоит сериализовать id и status, как и для остальных задач, чтобы формат был предсказуемым.
//
// YELLOW: Рассмотрите возможность использования String.join или StringBuilder для конкатенации строк. Это сделает код чище и потенциально эффективнее.++

public class TaskFormatter {
    // RED: отсутсвуют модификаторы+
    // Критично: поле должно быть private. Также, если формат не должен меняться,
    // лучше добавить модификатор final и сделать его static (если он общий для всех экземпляров).
    // private static final DateTimeFormatter FORMATTER = ...+
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
    // Аналогичная проблема в методе toParse(Task task).+
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
