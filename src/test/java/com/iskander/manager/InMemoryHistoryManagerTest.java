package com.iskander.manager;

import com.iskander.model.Status;
import com.iskander.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void initData(){
        historyManager = new InMemoryHistoryManager();

        task1 = new Task(1,"Work","Working", Status.DONE, Duration.ofMinutes(10), LocalDateTime.now());
        task2 = new Task(2,"Chill","Chilling", Status.DONE, Duration.ofMinutes(20), LocalDateTime.now());

    }


    @Test
    void shouldAddTaskInHistory() {
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history,"History not t");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0,history.size());
    }

    @Test
    void shouldReturnHistory() {
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(history,historyManager.getHistory());
    }

    @Test
    void ShouldReturnNullWhenHistoryIsEmpty() {
        final List<Task> history = historyManager.getHistory();
        assertEquals(0,history.size());
    }

    @Test
    void shouldReturnNullWhenTaskIsInvalidId(){
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(3);
        final List<Task> history = historyManager.getHistory();

        assertEquals(2,history.size());
        assertFalse(history.contains(null),"Null");

    }
}