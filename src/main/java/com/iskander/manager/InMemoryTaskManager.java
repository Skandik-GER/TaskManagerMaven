package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements Manager{

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Long, Task> taskmap = new HashMap<>();
    protected final Map<Long, Subtask> subtaskmap = new HashMap<>();
    protected final Map<Long, Epic> epicmap = new HashMap<>();

    protected long nextId = 1;

    @Override
    public void createTask(Task task) {
        if(task.getId() != 0 ){
            throw new IllegalArgumentException("Новая задача должна иметь id = 0");
        }
        validateTime(task);
        task.setId(nextId);
        taskmap.put(nextId, task);
        nextId++;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if(subtask.getId() != 0 ){
            throw new IllegalArgumentException("Новая Подзадача должна иметь id = 0");
        }
        long epicID = subtask.getEpicId();
        if (!epicmap.containsKey(epicID)) {
            throw new IllegalArgumentException("Тысяча чертей!Не найден epicID ");
        }
        validateTime(subtask);
        Epic epic = epicmap.get(epicID);
        subtask.setId(nextId);
        subtaskmap.put(nextId, subtask);
        epic.addSubTask(subtask);
        nextId++;

    }
    @Override
    public void createEpic(Epic epic) {
        if(epic.getId() != 0 ){
            throw new IllegalArgumentException("Новая задача должна иметь id = 0");
        }
        epic.setId(nextId);
        epicmap.put(epic.getId(), epic);
        nextId++;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskmap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicmap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskmap.values());
    }

    @Override
    public void removeTaskId(long id) {
        if (taskmap.containsKey(id)) {
            taskmap.remove(id);
            historyManager.remove(id);
        } else {
            throw new IllegalArgumentException("Такой Id не найден");
        }
    }

    @Override
    public void removeAllTasks() {
        for(Task task : taskmap.values()){
            historyManager.remove(task.getId());
        }
        taskmap.clear();
    }


    @Override
    public void removeAllEpic() {
        for(Epic epic : epicmap.values()){
            historyManager.remove(epic.getId());
        }
        for(Subtask subtask : subtaskmap.values()){
            historyManager.remove(subtask.getId());
        }
        epicmap.clear();
        subtaskmap.clear();
    }

    @Override
    public void removeEpicById(long id) {
        Epic epic = epicmap.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с ID " + id + " не найден");
        }

        Map<Long,Subtask> subtasks = epic.getSubTasks();
        for(Long subtaskId : subtasks.keySet()){
            historyManager.remove(subtaskId);
            subtaskmap.remove(subtaskId);
        }
        epicmap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllSubtask() {
        for (Epic epic : epicmap.values()) {
            epic.removeSubtasksAll();
        }
        for (Subtask subtask : subtaskmap.values()) {
            historyManager.remove(subtask.getId());
        }
        subtaskmap.clear();
    }
    @Override
    public void removeSubtaskId(long id) {
        Subtask subtask = subtaskmap.get(id);
        if (subtask == null) {
            return;
        }

        Epic epic = epicmap.get(subtask.getEpicId());
        if (epic != null) {
            epic.removeSubtaskById(id);
        }

        subtaskmap.remove(id);
        historyManager.remove(id);

    }

    @Override
    public List<Subtask> getSubtasksByEpic(long epicId) {
        Epic epic = epicmap.get(epicId);
        for(Subtask subtask: epic.getSubTasks().values()){
            historyManager.add(subtask);
        }
        return new ArrayList<>(epic.getSubTasks().values());
    }
    @Override
    public Task getTaskById(long id) {
        if (taskmap.get(id) != null) {
            historyManager.add(taskmap.get(id));
        } else {
            throw new IllegalArgumentException("Задача с ID " + id + " не найдена");
        }
        return taskmap.get(id);
    }
    @Override
    public Subtask getSubtaskById(long id) {
        Subtask subtask = subtaskmap.get(id);
        if (subtask == null) {
            throw new IllegalArgumentException("Подзадача с ID " + id + " не найдена");
        }
        historyManager.add(subtaskmap.get(id));
        return subtaskmap.get(id);
    }
    @Override
    public Epic getEpicById(long id) {
        Epic epic = epicmap.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с ID " + id + " не найдена");
        }
        historyManager.add(epicmap.get(id));
        return epicmap.get(id);
    }

    @Override
    public void updateTask(Task task) {
        if (!taskmap.containsKey(task.getId())) {
            throw new IllegalArgumentException("Задача с id " + task.getId() + " не существует");
        }
        validateTime(task);
        taskmap.put(task.getId(), task);

    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (newEpic == null) {
            throw new IllegalArgumentException("Epic cannot be null");
        }
        Epic oldEpic = epicmap.get(newEpic.getId());
        if (oldEpic == null) {
            throw new IllegalArgumentException("Эпик с id " + newEpic.getId() + " не существует");
        }
        Map<Long, Subtask> subtasks = oldEpic.getSubTasks();

        oldEpic.setName(newEpic.getName());
        oldEpic.setDescribe(newEpic.getDescribe());

        oldEpic.setSubTasks(subtasks);
        historyManager.add(oldEpic);
        epicmap.put(oldEpic.getId(),oldEpic);
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epicmap.get(subtask.getEpicId());
        epic.removeSubtaskById(subtask.getId());
        epic.addSubTask(subtask);
        historyManager.add(subtask);
        subtaskmap.put(subtask.getId(), subtask);
    }

    // RED
    // Повторяющий код. Лучше все задачи запихнуть в общий список всех задач и по ним пробежаться
    public void validateTime(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return;
        }

        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = taskStart.plus(task.getDuration());
        for (Task actTask : taskmap.values()) {
            if (actTask.getStartTime() != null && actTask.getDuration() != null && !actTask.getDuration().isZero()) {
                LocalDateTime existingStart = actTask.getStartTime();
                LocalDateTime existingEnd = existingStart.plus(actTask.getDuration());

                boolean isOverlap = !(taskEnd.isBefore(existingStart) || taskStart.isAfter(existingEnd));
                if (isOverlap) {
                    throw new RuntimeException();
                }
            }
        }

        for (Subtask subtask : subtaskmap.values()) {
            if (subtask.getStartTime() != null && subtask.getDuration() != null && !subtask.getDuration().isZero()) {
                LocalDateTime existingStart = subtask.getStartTime();
                LocalDateTime existingEnd = existingStart.plus(subtask.getDuration());

                boolean isOverlap = !(taskEnd.isBefore(existingStart) || taskStart.isAfter(existingEnd));
                if (isOverlap) {
                    throw new RuntimeException();
                }
            }
        }
    }
    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "taskmapSize=" + taskmap.size() +
                ", subtaskmapSize=" + subtaskmap.size() +
                ", epicmapSize=" + epicmap.size() +
                ", historySize=" + historyManager.getHistory().size() +
                ", nextIdSize=" + nextId +
                '}';
    }

}