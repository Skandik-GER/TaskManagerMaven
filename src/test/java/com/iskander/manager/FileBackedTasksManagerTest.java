package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Status;
import com.iskander.model.Subtask;
import com.iskander.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager> {

    private String testFile;
    private String testFile2;
    private String testHist;
    private String testHist2;
    private Task task;
    private Task task2;
    private Epic epic;
    private Subtask subtask;


    @Override
    protected FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(testFile, testFile2);
    }

    @BeforeEach
    void initData() {
        super.initData();
        testFile = "src/test/resources/manager1/data.csv";
        testFile2 = "src/test/resources/manager1/newData.csv";

        testHist = "src/test/resources/manager1/history.csv";
        testHist2 = "src/test/resources/manager1/newHist.csv";
        taskManager = (FileBackedTasksManager) Managers.getFileBackTasksManager();

        task = new Task( "Work", "Working", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        task2 = new Task( "Smoke", "Smoking", Status.IN_PROCESS,Duration.ofMinutes(20), LocalDateTime.now());

        epic = new Epic("Work", "Work Routine");

        subtask = new Subtask( "Take Call", "Speaking", epic.getId(), Status.NEW,Duration.ofMinutes(10),
                LocalDateTime.now());
    }
    @AfterEach
    void data(){
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(testFile2)) {
            writer.write("");

        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter( testHist)) {
            writer.write("");

        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter( testHist2)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла: " + e.getMessage());
        }

    }

    @Test
    void shouldSaveTasksToFile() {
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(3, "Take Call", "Speaking", epic.getId(), Status.NEW,Duration.ofMinutes(10),
                LocalDateTime.now());
        taskManager.createSubtask(subtask1);

        taskManager.save();

        try {
            String fileNames = Files.readString(Path.of(testFile));
            System.out.println(fileNames);
            assertTrue(fileNames.contains("Work"), "All good");
            assertTrue(fileNames.contains("Smoke"), "All good");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldSaveTasksFromFile() {
        taskManager.createEpic(epic);
        Task actTask = new Task( "Work", "Working", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusHours(1));
        Task actTask2 = new Task( "Smoke", "Smoking", Status.IN_PROCESS,Duration.ofMinutes(20), LocalDateTime.now());
        taskManager.createTask(actTask);
        taskManager.createTask(actTask2);
        Subtask subtask1 = new Subtask("Take Call", "Speaking", epic.getId(), Status.NEW,Duration.ofMinutes(10),
                LocalDateTime.now().plusHours(2));
        taskManager.createSubtask(subtask1);
        taskManager.save();


        FileBackedTasksManager loadManager = FileBackedTasksManager.loadFromFile(testFile, testFile2, testHist, testHist2);
        assertEquals(2, loadManager.getTasks().size());
        assertEquals(1, loadManager.getSubtasks().size());

        try {
            String fileNames = Files.readString(Path.of(testFile));
            assertTrue(fileNames.contains("Work"), "All good");
            assertTrue(fileNames.contains("Smoke"), "All good");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldLoadEmptyFile() {
        taskManager.save();

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(testFile,testFile2,testHist
                ,testHist2);
        assertTrue(loadedManager.getTasks().isEmpty());
        assertTrue(loadedManager.getSubtasks().isEmpty());
        assertTrue(loadedManager.getEpics().isEmpty());

    }

}