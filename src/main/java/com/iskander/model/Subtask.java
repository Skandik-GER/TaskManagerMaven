package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    final private long epicId;

    public Subtask(String name, String describe, long epicId, Status status, Duration duration, LocalDateTime startTime) {
        super(name, describe, status,duration,startTime);
        this.epicId = epicId;
    }


    public Subtask(long id, String name, String describe, long epicId, Status status,Duration duration,LocalDateTime startTime) {
        super(id, name, describe, status,duration,startTime);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Subtask subtask = (Subtask) object;
        return getId() == subtask.getId() &&
                getEpicId() == subtask.getEpicId() &&
                Objects.equals(getName(), subtask.getName()) &&
                Objects.equals(getStatus(), subtask.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEpicId(), getName(), getStatus());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() +
                ", status='" + getStatus() + '\'' +
                ", describe='" + getDescribe() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
