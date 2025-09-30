package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            throw new IllegalStateException("Start time is not set");
        }
        return startTime;
    }

    public LocalDateTime getEndTime(){
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

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

    public void setStatus(Status status) {
        this.status = status;
    }

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
