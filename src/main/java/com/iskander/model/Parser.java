package com.iskander.model;

import java.time.format.DateTimeFormatter;

public class Parser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String toParse(Subtask task){
        return task.getName() + "," + task.getDescribe() + "," + task.getId() + "," + task.getStatus() + "," + task.getEpicId() + "," + task.getDuration().getSeconds() + "," + task.getStartTime().format(formatter);
    }
    public String toParse(Task task){
        return task.getName() + "," + task.getDescribe() + "," + task.getId() + "," + task.getStatus()+ "," + task.getDuration().getSeconds() + "," + task.getStartTime().format(formatter);
    }
    public String toParse(Epic epic){
        return epic.getName() + "," + epic.getDescribe() ;
    }
}
