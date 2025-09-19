package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String describe;
    protected long id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String name, String describe, Status status,Duration duration,LocalDateTime startTime) {
        this(0, name, describe, status, duration, startTime);
    }

    public Task(long id, String name, String describe, Status status,Duration duration,LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }
    public Task(String name, String describe, Status status) {
        this.id = 0;
        this.name = name;
        this.describe = describe;
        this.status = status;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
    }
        // RED: Критично! Два поля (duration, startTime) остаются неинициализированными (null).++
        // Это приведет к NullPointerException в getEndTime().


    public void setId(long id) {
        this.id = id;
    }

    // YELLOW: Может вернуть null.++
    public LocalDateTime getStartTime() {
        if (startTime == null) {
            throw new IllegalStateException("Start time is not set");
        }
        return startTime;
    }

    // RED: Выбросит NPE, если startTime или duration == null.++
    public LocalDateTime getEndTime(){
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    // YELLOW: Может вернуть null.++
    public Duration getDuration() {
        if (duration == null) {
            throw new IllegalStateException("Duration is not set");
        }
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    // RED: Идентификатор не должен меняться после создания объекта.+=
    // Менеджер задач рассчитывает на его неизменность.

    public void setStatus(Status status) {
        this.status = status;
    }

    // YELLOW: В toString не выводятся поля duration и startTime,++
    // хотя они являются важной частью состояния объекта.
    // Это усложняет отладку. + лучше использовать геттеры++
    @Override
    public String toString() {
        return "Task{" +
                "name='" + getName() + '\'' +
                ", describe='" + getDescribe() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}

//    // Улучшенный конструктор
//    public Task(String name, String describe, Status status) {
//        this.name = name;
//        this.describe = describe;
//        this.status = status;
//        this.duration = Duration.ZERO; // Значение по умолчанию
//        this.startTime = LocalDateTime.now(); // или другое значение по умолчанию
//    }
//
//    // Защищенный метод getEndTime()
//    public LocalDateTime getEndTime() {
//        if (startTime == null || duration == null) {
//            return null;
//        }
//        return startTime.plus(duration);
//    }
