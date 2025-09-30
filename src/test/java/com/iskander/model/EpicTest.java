package com.iskander.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void shouldSetSubTasksInNewEpic() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask("Wake up","Standing up",epic.getId(),Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",epic.getId(),Status.NEW, Duration.ofMinutes(160), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);
        HashMap<Long,Subtask> subtaskHashMap = (HashMap<Long, Subtask>) epic.getSubTasks();
        Epic epic2 = new Epic("Evening","Routine");
        epic2.setSubTasks(subtaskHashMap);

        Assertions.assertEquals(2,epic2.getSubTasks().size());
    }

    @Test
    void shouldGetSubTasksFromEpic() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.NEW, Duration.ofMinutes(20), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        System.out.println(epic.getSubTasks());

        Assertions.assertEquals(2,epic.getSubTasks().size());
    }

    @Test
    void shouldRemoveSubtasksAllInEpic() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);
        epic.removeSubtasksAll();
        Assertions.assertEquals(0,epic.getSubTasks().size());
    }

    @Test
    void shouldRemoveSubtaskByIdInEpic() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(120), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);
        epic.removeSubtaskById(1);
        Assertions.assertEquals(1,epic.getSubTasks().size());
    }

    @Test
    void shouldAddSubTaskInEpic() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        epic.addSubTask(subtask);
        Assertions.assertEquals(1,epic.getSubTasks().size());
    }


    @Test
    void shouldReturnNullWhenEpicIsEmpty() {
        Epic epic = new Epic("Morning","Routine");

        Assertions.assertEquals(0,epic.getSubTasks().size());
    }

    @Test
    void shouldReturnStatusNewWhenSubtaskStatusNew() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(70), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        Assertions.assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    void shouldReturnStatusNewWhenSubtaskStatusDone() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.DONE, Duration.ofMinutes(70), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.DONE, Duration.ofMinutes(70), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        Assertions.assertEquals(Status.DONE,epic.getStatus());
    }

    @Test
    void shouldReturnStatusNewWhenSubtaskStatusNewAndDoneAndDuration() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.DONE, Duration.ofMinutes(25), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        Assertions.assertEquals(Status.IN_PROCESS,epic.getStatus());
        assertEquals(35,epic.getDuration().getSeconds()/60);
    }

    @Test
    void shouldReturnStatusNewWhenSubtaskStatusInProcess() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.IN_PROCESS, Duration.ofMinutes(40), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.IN_PROCESS, Duration.ofMinutes(70), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        Assertions.assertEquals(Status.IN_PROCESS,epic.getStatus());
    }
}