package com.iskander.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldGetTaskName() {
        Task task = new Task("Digging","Mining",Status.NEW);

        Assertions.assertEquals("Digging",task.getName());
    }

    @Test
    void shouldGetTaskDescribe() {
        Task task = new Task("Digging","Mining",Status.NEW);

        Assertions.assertEquals("Mining",task.getDescribe());
    }

    @Test
    void shouldGetTaskId() {
        Task task = new Task(1,"Digging","Mining",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());

        Assertions.assertEquals(1,task.getId());
    }

    @Test
    void shouldGetTaskStatus() {
        Task task = new Task(1,"Digging","Mining",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());

        Assertions.assertEquals(Status.NEW,task.getStatus());
    }

    @Test
    void shouldSetTaskName() {
        Task task = new Task(1,"Digging","Mining",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        task.setName("Chill");
        Assertions.assertEquals("Chill",task.getName());
    }

    @Test
    void shouldSetTaskDescribe() {
        Task task = new Task(1,"Digging","Mining",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        task.setDescribe("Chill");
        Assertions.assertEquals("Chill",task.getDescribe());
    }


    @Test
    void shouldSetTaskStatus() {
        Task task = new Task(1,"Digging","Mining",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        task.setStatus(Status.DONE);
        Assertions.assertEquals(Status.DONE,task.getStatus());
    }
}