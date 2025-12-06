package com.iskander.manager;

import com.google.gson.reflect.TypeToken;
import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HttpTaskManager extends InMemoryTaskManager{
    private final KVTaskClient kvTaskClient;

    public HttpTaskManager(URL urlDataServer) {
        super();
        kvTaskClient = new KVTaskClient(urlDataServer);
    }
    public void loadFromServer() {
        Map<Long, TaskType> idToType = new HashMap<>();
        String response = kvTaskClient.load("tasks");
        List<Task> loadedTasks;
        if (!response.equals(""))
            loadedTasks = new ArrayList<>(List.of(
                    TaskGson.GSON.fromJson(response, Task[].class)));
        else {
            loadedTasks = Collections.emptyList();
        }
        for (Task task : loadedTasks) {
            super.updateTask(task);
            taskmap.put(task.getId(), task);
            prioritizedTasks.add(task);
            idToType.put(task.getId(), TaskType.TASK);
        }

        response = kvTaskClient.load("epics");
        List<Epic> loadedEpics;
        if (!response.equals("")) {
            loadedEpics = new ArrayList<>(List.of(
                    TaskGson.GSON.fromJson(response, Epic[].class)));
        }
        else {
            loadedEpics = Collections.emptyList();
        }
        for (Epic epic : loadedEpics) {
            super.updateId(epic.getId());
            taskmap.put(epic.getId(), epic);
            idToType.put(epic.getId(), TaskType.EPIC);
        }

        response = kvTaskClient.load("subtasks");
        List<Subtask> loadedSubtasks;
        if (!response.equals("")) {
            loadedSubtasks = new ArrayList<>(List.of(
                    TaskGson.GSON.fromJson(response, Subtask[].class)));
        }
        else {
            loadedSubtasks = Collections.emptyList();
        }
        for (Subtask subtask : loadedSubtasks) {
            super.updateId(subtask.getId());
            taskmap.put(subtask.getId(), subtask);

            long epicId = subtask.getEpicId();

            Epic epic = epicmap.get(epicId);
            epic.addSubTask(subtask);

            updateEpic(epic);
            prioritizedTasks.add(subtask);
            idToType.put(subtask.getId(), TaskType.SUBTASK);
        }

        response = kvTaskClient.load("history");
        List<Integer> historyTasksId;
        if (!response.equals("")) {
            historyTasksId = List.of(TaskGson.GSON.fromJson(
                    response, Integer[].class));
        }
        else {
            historyTasksId = Collections.emptyList();
        }
        for (Integer id : historyTasksId) {

            Task task;
            TaskType taskType = idToType.get(id);

            switch (taskType){
                case TASK:
                    task = getTaskById(id);
                    break;
                case EPIC:
                    task = getEpicById(id);
                    break;
                case SUBTASK:
                    task = getSubtaskById(id);
                    break;
                default:
                    throw new RuntimeException();
            }
            historyManager.add(task);
        }
    }



    public void save() {

        kvTaskClient.put("epics", TaskGson.GSON.toJson(epicmap));
        kvTaskClient.put("tasks", TaskGson.GSON.toJson(taskmap));
        kvTaskClient.put("subtasks", TaskGson.GSON.toJson(subtaskmap));
        kvTaskClient.put("history", TaskGson.GSON.toJson(historyManager));
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void removeTaskId(long id) {
        super.removeTaskId(id);
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
    }

    @Override
    public void removeEpicById(long id) {
        super.removeEpicById(id);
    }

    @Override
    public void removeSubtaskId(long id) {
        super.removeSubtaskId(id);
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
    }

    @Override
    public List<Subtask> getSubtasksByEpic(long epicId) {
        return super.getSubtasksByEpic(epicId);
    }

    @Override
    public Task getTaskById(long id) {
        return super.getTaskById(id);
    }

    @Override
    public Subtask getSubtaskById(long id) {
        return super.getSubtaskById(id);
    }

    @Override
    public Epic getEpicById(long id) {
        return super.getEpicById(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void validateTime(Task task) {
        super.validateTime(task);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
