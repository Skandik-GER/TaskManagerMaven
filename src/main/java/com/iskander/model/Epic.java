package com.iskander.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Epic extends Task {

    private  Map<Long, Subtask> subTasks = new HashMap<>();

    public Epic(String name, String describe) {
        super(name, describe, Status.NEW);
        
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

            if (start != null && end != null) {
                totalDuration = totalDuration.plus(Duration.between(start, end));
            }
        }

        return totalDuration;
    }



    public void setSubTasks(Map<Long, Subtask> subTasks) {
        this.subTasks = subTasks;
        updateStatus();
    }

    // YELLOW
    // Метод лучше назвать getSubtasks
    public Map<Long, Subtask> getSubTasks() {
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
