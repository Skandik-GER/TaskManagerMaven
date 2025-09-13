package com.iskander.manager;




import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.util.List;

public interface Manager{

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void removeTaskId(long id);

    void removeAllTasks();

    void removeAllEpic();

    void removeEpicId(long id);

    void removeAllSubtask();

    void removeSubtaskId(long id);


    List<Subtask> getSubtasksByEpic(long epicId);

    Task getTaskById(long id);

    Subtask getSubtaskById(long id);

    Epic getEpicById(long id);

    void updateTask(Task task);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask subtask);

    List<Task> getHistory();

    void validateTime(Task task);

}
