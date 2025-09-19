package com.iskander.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void shouldGetEpicIdFromSubtask() {
        Subtask subtask = new Subtask(1,"Drink","Drinkning Soda",1,Status.DONE, Duration.ofMinutes(10), LocalDateTime.now());

        Assertions.assertEquals(1,subtask.getEpicId());
    }

    @Test
    void shouldSetEpicIdToSubtask() {
        Epic epic = new Epic("Chill","Chilling");
        long epicId = epic.getId();

        Subtask subtask = new Subtask("Drink", "Drinking Soda", epicId, Status.DONE,
                Duration.ofMinutes(50), LocalDateTime.now());

        assertEquals(epicId, subtask.getEpicId());

    }
}