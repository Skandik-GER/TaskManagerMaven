package com.iskander.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private long epicId;

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

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Subtask subtask = (Subtask) object;
        return getEpicId() == subtask.getEpicId() && getName().equals(subtask.getName())
                && getStatus().equals(subtask.getStatus()) && getId() == (subtask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEpicId());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", status='" + status + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
