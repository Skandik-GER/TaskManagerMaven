package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
// RED: Лишний импорт
import java.util.Objects;


public class Epic extends Task {

    private  Map<Long, Subtask> subTasks = new HashMap<>();

    public Epic(String name, String describe) {
        super(name, describe, Status.NEW);
        // YELLOW: Неявно вызывается конструктор Task(name, describe, status),
        // который, как мы выяснили, оставляет duration и startTime равными null.
        // Для эпика это нормально, т.к. они вычисляются.
    }


    @Override
    public Duration getDuration() {
        if (subTasks.isEmpty()) {
            return Duration.ZERO;
        }

        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subTasks.values()) {
            LocalDateTime start = subtask.getStartTime();
            LocalDateTime end = subtask.getEndTime();
            // RED: Критичная ошибка в логике!
            // Суммируется продолжительность ВСЕХ подзадач подряд,
            // без учета их пересечений во времени.
            // Это не "duration эпика", а "суммарное время работы всех подзадач".
            // Длительность эпика - это разница между самым ранним началом
            // и самым поздним окончанием среди всех подзадач.
            if (start != null && end != null) {
                totalDuration = totalDuration.plus(Duration.between(start, end));
            }
        }

        return totalDuration;
    }
    // YELLOW: Также стоит переопределить getStartTime() и getEndTime().


    public void setSubTasks(Map<Long, Subtask> subTasks) {
        // YELLOW: Опасно. Лучше принимать коллекцию и копировать:
        // this.subTasks = new HashMap<>(subTasks);
        this.subTasks = subTasks;
        updateStatus();
    }

    public Map<Long, Subtask> getSubTasks() {
        // YELLOW: Нарушение инкапсуляции.
        // Возвращается mutable-коллекция, внешний код может её изменить.
        // Лучше вернуть Collections.unmodifiableMap(subTasks)
        // или новый HashMap<>(subTasks).
        return subTasks;
    }


    public void removeSubtasksAll() {
        subTasks.clear();
        status = Status.NEW;
    }

    public void removeSubtaskById(long id) {
        subTasks.remove(id);
        updateStatus();
    }


    public void addSubTask(Subtask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatus();
    }

    // YELLOW: лучше использовать геттеры вместо прямого обращения к полям
    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", describe='" + describe + '\'' +
                ", name='" + name + '\'' +
                ", subTasksSize=" + subTasks.size() +
                '}';
    }

    private void updateStatus() {
        boolean hasNew = false;
        boolean hasInProcess = false;
        boolean hasDone = false;

        for (Subtask subTask : subTasks.values()) {
            switch (subTask.getStatus()) {
                case NEW -> hasNew = true;
                case IN_PROCESS -> hasInProcess = true;
                case DONE -> hasDone = true;
            }
        }

        if (hasInProcess || (hasNew && hasDone)) {
            status = Status.IN_PROCESS;
        } else if (hasNew) {
            status = Status.NEW;
        } else if (hasDone) {
            status = Status.DONE;
        } else {
            status = Status.NEW;
        }
    }
}
