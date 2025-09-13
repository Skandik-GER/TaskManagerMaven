package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.util.*;
import java.util.concurrent.TimeoutException;


public class InMemoryTaskManager implements Manager{

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Long, Task> taskmap = new HashMap<>();
    protected final Map<Long, Subtask> subtaskmap = new HashMap<>();
    protected final Map<Long, Epic> epicmap = new HashMap<>();
    protected long nextId = 1;

    @Override
    public void createTask(Task task) {
        if(task.getId() == 0 ){
            task.setId(nextId);
        }
        taskmap.put(task.getId(), task);
        nextId++;
    }
    @Override
    public void createSubtask(Subtask subtask) {
        if(subtask.getId() == 0 ){
            subtask.setId(nextId);
        }
        long epicID = subtask.getEpicId();
        if (!epicmap.containsKey(epicID)) {
            System.out.println("Тысяча чертей!Не найден epicID ");
            return;
        }
        Epic epic = epicmap.get(epicID);
        subtaskmap.put(subtask.getId(), subtask);
        epic.addSubTask(subtask);
        nextId++;

    }
    @Override
    public void createEpic(Epic epic) {
        if(epic.getId() == 0 ){
            epic.setId(nextId);
        }
        nextId++;
        epicmap.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getTasks() {
        for(Task task: taskmap.values()){
            historyManager.add(task);
        }
        return new ArrayList<>(taskmap.values());
    }

    @Override
    public List<Epic> getEpics() {
        for(Epic epic: epicmap.values()){
            historyManager.add(epic);
        }
        return new ArrayList<>(epicmap.values());
    }
    @Override
    public List<Subtask> getSubtasks() {
        for(Subtask subtask : subtaskmap.values()){
            historyManager.add(subtask);
        }
        return new ArrayList<>(subtaskmap.values());
    }
    @Override
    public void removeTaskId(long id) {
        if (taskmap.containsKey(id)) {
            taskmap.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такой Id не найден");
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
    public void removeEpicId(long id) {
        Epic epic = epicmap.get(id);
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
        Epic epic = epicmap.get(subtask.getEpicId());
        epic.removeSubtaskById(id);
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
            System.err.println("Задача с ID " + id + " не найдена");
        }
        return taskmap.get(id);
    }
    @Override
    public Subtask getSubtaskById(long id) {
        historyManager.add(subtaskmap.get(id));
        return subtaskmap.get(id);
    }
    @Override
    public Epic getEpicById(long id) {
        historyManager.add(epicmap.get(id));
        return epicmap.get(id);
    }
    // RED+
    // В main показал тестовый сценарий, при котором произойдет баг
    // из-за этого метода
    @Override
    public void updateTask(Task task) {
        historyManager.add(task);
        taskmap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic oldEpic = epicmap.get(newEpic.getId());
        Map<Long, Subtask> subtasks = oldEpic.getSubTasks();
        newEpic.setSubTasks(subtasks);
        historyManager.add(newEpic);
        epicmap.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epicmap.get(subtask.getEpicId());
        epic.removeSubtaskById(subtask.getId());
        epic.addSubTask(subtask);
        historyManager.add(subtask);
        subtaskmap.put(subtask.getId(), subtask);
    }

    @Override
    public void validateTime(Task task){
        if(task.getStartTime() == null || task.getDuration() == null){
            return;
        }
        for(Task actTask : taskmap.values()){
            if(actTask.getStartTime().equals(task.getStartTime())){
                throw new RuntimeException();
            }
        }

        for(Subtask subtask : subtaskmap.values()){
            if(subtask.getStartTime().equals(task.getStartTime())){
                throw new RuntimeException();
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
