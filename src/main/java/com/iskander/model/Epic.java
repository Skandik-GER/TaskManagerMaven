package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class Epic extends Task {

    private  Map<Long, Subtask> subTasks;

    public Epic(String name, String describe) {
        super(name, describe, Status.NEW);
        subTasks = new HashMap<>();
    }



    public Duration getDuration() {
        if (subTasks.isEmpty()) {
            return Duration.ZERO;
        }
        LocalDateTime start = null;
        LocalDateTime end = null;

        for (Subtask subtask : subTasks.values()) {
            // и самым поздним окончанием среди всех подзадач.
            if (subtask.getStartTime() != null && subtask.getEndTime() != null) {
                if (start == null || subtask.getStartTime().isBefore(start)) {
                    start = subtask.getStartTime();
                }
                if (end == null || subtask.getEndTime().isAfter(end)) {
                    end = subtask.getEndTime();
                }
            }
        }

        return  Duration.between(start, end);
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }


    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public void setSubTasks(Map<Long, Subtask> subTasks) {
        this.subTasks = subTasks != null ? subTasks : new HashMap<>();
    }

    public Map<Long, Subtask> getSubTasks() {
        return Collections.unmodifiableMap(subTasks);
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

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", describe='" + getDescribe() + '\'' +
                ", name='" + getName() + '\'' +
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
