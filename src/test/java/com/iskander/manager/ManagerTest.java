package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Status;
import com.iskander.model.Subtask;
import com.iskander.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest<T extends Manager> {
    protected T taskManager;
    protected Task task;
    protected Task task2;
    protected Task updTask;
    protected Subtask updSubTask;
    protected Epic epic;
    protected Epic epic2;
    protected Epic updEpic;

    protected abstract T createTaskManager();

    @BeforeEach
    void initData() {
        taskManager = createTaskManager();
        epic = new Epic("Chill", "Chilling");
        epic2 = new Epic("Work", "Working");


        task = new Task( 0,"Loot", "Looting", Status.IN_PROCESS, Duration.ofMinutes(10), LocalDateTime.now());
        task2 = new Task( "Camp", "Camping", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now());


        updTask = new Task( "Not Loot", "Chilling", Status.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        updSubTask = new Subtask( "Eat", "Eating", epic.getId(), Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        updEpic = new Epic("Watch Tv", "Watching");


    }


    @Test
    public void shouldCreateTask() {

        taskManager.createTask(task);

        assertNotNull(taskManager);
    }

    @Test
    void shouldCreateSubtask() {
        taskManager.createEpic(epic);
        Subtask subtask2 = new Subtask("SIt", "Siting", epic.getId(), Status.DONE, Duration.ofMinutes(40), LocalDateTime.now());
        taskManager.createSubtask(subtask2);

        assertNotNull(taskManager);
    }

    @Test
    void shouldCreateEpic() {
        taskManager.createEpic(epic);

        assertNotNull(taskManager);
    }

    @Test
    void shouldGetTasks() {
        taskManager.createTask(task);

        final List<Task> tasks = taskManager.getTasks();

        assertEquals("Loot", taskManager.getTasks().get(0).getName());
        assertNotNull(tasks);
    }

    @Test
    void shouldGetEpics() {
        taskManager.createEpic(epic);

        final List<Epic> epics = taskManager.getEpics();

        assertEquals("Chill", taskManager.getEpicById(1).getName());
        assertNotNull(epics);
    }

    @Test
    void shouldGetSubtasksFromList() {
        taskManager.createEpic(epic);
        Subtask subtaskWithEpic = new Subtask("Take Call", "Speaking", epic.getId(), Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.createSubtask(subtaskWithEpic);

        List<Subtask> subTasks = taskManager.getSubtasks();

        assertEquals("Take Call", taskManager.getSubtasks().get(0).getName());
        assertNotNull(subTasks);
    }

    @Test
    void shouldRemoveTaskId() {
        taskManager.createTask(task);
        assertNotNull(taskManager);
        taskManager.removeTaskId(1);
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void shouldRemoveAllTasks() {
        taskManager.createTask(task);
        Task task3 = new Task( "Camp", "Camping", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task3);
        assertNotNull(taskManager);
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void shouldRemoveAllEpic() {
        taskManager.createEpic(epic);
        assertNotNull(taskManager);
        taskManager.removeAllEpic();
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void removeEpicById() {
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        assertNotNull(taskManager);
        taskManager.removeEpicById(epic.getId());
        assertEquals(1, taskManager.getEpics().size());
        taskManager.removeEpicById(2);
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void removeAllSubtask() {
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("SIt", "Siting", epic.getId(), Status.DONE, Duration.ofMinutes(40), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Eat", "Eating", epic.getId(), Status.NEW, Duration.ofMinutes(70), LocalDateTime.now().plusHours(1));
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);
        assertEquals(130,epic.getDuration().getSeconds()/60);
        assertEquals(2, epic.getSubTasks().size());
        taskManager.removeAllSubtask();
        epic.removeSubtasksAll();
        assertEquals(0, epic.getSubTasks().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void removeSubtaskId() {
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Eat", "Eating", epic.getId(), Status.NEW, Duration.ofMinutes(70), LocalDateTime.now());
        Subtask subtask2 = new Subtask("SIt", "Siting", epic.getId(), Status.DONE, Duration.ofMinutes(70), LocalDateTime.now().plusHours(2));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        epic.addSubTask(subtask1);
        epic.addSubTask(subtask2);
        assertEquals(2, epic.getSubTasks().size());
        taskManager.removeSubtaskId(subtask1.getId());
        assertEquals(1, epic.getSubTasks().size());
    }

    @Test
    void getSubtasksByEpic() {
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Eat", "Eating", epic.getId(), Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Sit", "Siting", epic.getId(), Status.DONE, Duration.ofMinutes(60), LocalDateTime.now().plusHours(3));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());

        assertEquals(2, subtasks.size());
    }

    @Test
    void getTaskById() {
        taskManager.createTask(task);
        assertEquals("Loot", taskManager.getTaskById(1).getName());
    }

    @Test
    void getSubtaskById() {
        taskManager.createEpic(epic);

        Subtask subtaskWithEpic = new Subtask("Take Call", "Speaking", epic.getId(), Status.NEW, Duration.ofMinutes(170), LocalDateTime.now());
        taskManager.createSubtask(subtaskWithEpic);

        assertEquals("Take Call", taskManager.getSubtaskById(subtaskWithEpic.getId()).getName());
    }

    @Test
    void getEpicById() {
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        assertNotNull(taskManager.getEpics().size());
        assertEquals("Chill", taskManager.getEpicById(1).getName());
    }

    @Test
    void updateTask() {
        Task task3 = new Task( "Camp", "Camping", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task3);
        Task updTask1 = new Task(task3.getId(),"Not Loot", "Chilling", Status.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        taskManager.updateTask(updTask1);
        assertEquals("Not Loot", taskManager.getTaskById(task3.getId()).getName());
    }

    @Test
    void updateEpic() {
        taskManager.createEpic(epic);
        assertEquals("Chill", taskManager.getEpicById(epic.getId()).getName());
        taskManager.createEpic(updEpic);
        epic.setName(updEpic.getName());
        updEpic.setDescribe(updEpic.getDescribe());
        taskManager.updateEpic(epic);
        assertEquals("Watch Tv", taskManager.getEpicById(epic.getId()).getName());
    }

    @Test
    void updateSubtask() {
        taskManager.createEpic(epic);
        Subtask subtaskWithEpic = new Subtask("Take Call", "Speaking", epic.getId(), Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.createSubtask(subtaskWithEpic);

        assertEquals("Take Call", taskManager.getSubtaskById(subtaskWithEpic.getId()).getName());
        Subtask updatedSubtask = new Subtask(subtaskWithEpic.getId(), "Eat", "Eating", epic.getId(),
                Status.DONE, Duration.ofMinutes(10), LocalDateTime.now()
        );

        taskManager.updateSubtask(updatedSubtask);

        assertEquals("Eat", taskManager.getSubtaskById(subtaskWithEpic.getId()).getName());
        assertEquals(Status.DONE, taskManager.getSubtaskById(subtaskWithEpic.getId()).getStatus());
    }

    @Test
    void getHistory() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size());
        assertTrue(history.contains(task));
        assertTrue(history.contains(epic));
    }

    @Test
    void shouldReturnSubtasksAndDuration() {
        Epic epic = new Epic("Morning","Routine");
        Subtask subtask = new Subtask(1,"Wake up","Standing up",1,Status.DONE, Duration.ofMinutes(25), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2,"make Coffee","Making Coffee",1,Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        epic.addSubTask(subtask);
        epic.addSubTask(subtask2);

        Assertions.assertEquals(Status.IN_PROCESS,epic.getStatus());
        assertEquals(25,epic.getDuration().getSeconds()/60);
    }

    // RED
    // Было бы здорово так же проврить успешный случай создания задачи
    @Test
    void testValidateTimeWithSubtaskSameStartTime(){
        Task task = new Task("Task","Something",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now());
        taskManager.createTask(task);
        Epic epic = new Epic("Morning","Routine");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Wake up","Standing up",epic.getId(),Status.DONE, Duration.ofMinutes(10), LocalDateTime.now());
        assertThrows(RuntimeException.class,() -> taskManager.createTask(subtask));
    }

    @Test
    void testValidateTimeWithSameTimeShouldThrowException(){
        Task task = new Task("Task","Something",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now());
        taskManager.createTask(task);
        Task task1 = new Task("Potato","Digging",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> taskManager.createTask(task1));
    }

}