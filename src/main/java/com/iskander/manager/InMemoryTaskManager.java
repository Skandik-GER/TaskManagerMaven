package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.util.*;
// RED: Лишний импорт
import java.util.concurrent.TimeoutException;

//GREEN: Хорошая организация кода. Четкое разделение на методы, логичные названия.
// Использование HistoryManager для управления историей — это правильное и хорошее архитектурное решение.
//GREEN: Правильная реализация удаления из истории в методах remove....
// Этому часто уделяют мало внимания, но здесь это сделано верно.


public class InMemoryTaskManager implements Manager{

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    // YELLOW: Имена переменных 'taskmap', 'subtaskmap', 'epicmap' лучше
    // сделать более понятными: 'tasks', 'subtasks', 'epics'.
    protected final Map<Long, Task> taskmap = new HashMap<>();
    protected final Map<Long, Subtask> subtaskmap = new HashMap<>();
    protected final Map<Long, Epic> epicmap = new HashMap<>();
    // GREEN: Правильно реализован механизм генерации ID.
    protected long nextId = 1;

    // YELLOW: Метод изменяет входящий объект (устанавливает ему id).
    // Это не всегда ожидаемо. Лучше создать новый объект внутри метода или
    // проверять, что id не установлен (равен 0). Но в целом решение рабочее.
    // YELLOW: Нет проверки на повторный добавление задачи с существующим id.
    // RED: Нет проверки на пересечение по времени (вызов validateTime) при создании задач
    @Override
    public void createTask(Task task) {
        if(task.getId() == 0 ){
            task.setId(nextId);
        }
        taskmap.put(task.getId(), task);
        nextId++;
    }
    // RED: Нет проверки на пересечение по времени (вызов validateTime) при создании подзадачи.
    @Override
    public void createSubtask(Subtask subtask) {
        if(subtask.getId() == 0 ){
            subtask.setId(nextId);
        }
        long epicID = subtask.getEpicId();
        if (!epicmap.containsKey(epicID)) {
            // RED: Заменить на исключение.
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

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
    @Override
    public List<Task> getTasks() {
        for(Task task: taskmap.values()){
            historyManager.add(task);
        }
        return new ArrayList<>(taskmap.values());
    }

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
    @Override
    public List<Epic> getEpics() {
        for(Epic epic: epicmap.values()){
            historyManager.add(epic);
        }
        return new ArrayList<>(epicmap.values());
    }

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
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
            // RED: Заменить на исключение.
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
        // GREEN: Верная логика удаления всех подзадач вместе с эпикам
        epicmap.clear();
        subtaskmap.clear();
    }
    // YELLOW: Лучше назвать метод removeEpicById
    @Override
    public void removeEpicId(long id) {
        Epic epic = epicmap.get(id);
        // RED: Нет проверки на то, что эпик с таким id существует (epic может быть null).
        Map<Long,Subtask> subtasks = epic.getSubTasks();
        for(Long subtaskId : subtasks.keySet()){
            historyManager.remove(subtaskId);
            subtaskmap.remove(subtaskId); // GREEN: Корректное удаление подзадач из своей мапы.
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
            // RED: Заменить на исключение.
            System.err.println("Задача с ID " + id + " не найдена");
        }
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.
        return taskmap.get(id);
    }
    @Override
    public Subtask getSubtaskById(long id) {
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.
        historyManager.add(subtaskmap.get(id));
        return subtaskmap.get(id);
    }
    @Override
    public Epic getEpicById(long id) {
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.
        historyManager.add(epicmap.get(id));
        return epicmap.get(id);
    }

    @Override
    public void updateTask(Task task) {
        // YELLOW: Добавление в историю при обновлении - спорное решение.
        // RED: Нет проверки, что задача с таким id вообще существует в taskmap.
        // RED: Нет проверки на пересечение по времени (validateTime).
        historyManager.add(task);
        taskmap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        // RED: Нет проверки, что oldEpic существует.
        Epic oldEpic = epicmap.get(newEpic.getId());
        Map<Long, Subtask> subtasks = oldEpic.getSubTasks();
        newEpic.setSubTasks(subtasks); // RED: Опасное решение!
        // Потенциальная уязвимость в updateEpic.
        // Метод получает newEpic извне и полностью заменяет им существующий эпик.
        // Это опасный подход, так как клиентский код может передать объект с измененным состоянием.
        // Лучше обновлять только те поля старого эпика, которые должны меняться (например, название, описание),
        // но не список подзадач.
        historyManager.add(newEpic); // YELLOW: Добавление в историю при обновлении.
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
        // RED: Алгоритм не проверяет пересечение по интервалам (время начала + продолжительность).
        // Нужно проверять, что новый интервал [start, start+duration] не пересекается с существующими.
        if(task.getStartTime() == null || task.getDuration() == null){
            return;
        }
        for(Task actTask : taskmap.values()){
            if(actTask.getStartTime().equals(task.getStartTime())){
                // RED: Проверка только на точное совпадение времени начала.
                throw new RuntimeException(); // YELLOW: Использование слишком общего исключения.
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

//Убрать добавление в историю из методов getTasks(), getEpics(), getSubtasks().
//
//-Добавить вызов validateTime() в createTask, createSubtask, updateTask, updateSubtask.
//-Исправить логику validateTime() для проверки пересечения интервалов, а не только времени начала.
//-Во всех методах, которые работают по ID (get...ById, remove...Id, update...),добавить проверку существования ID
// и выбрасывать соответствующее исключение вместо возврата null или вывода в консоль.
//-Переписать updateEpic чтобы он не заменял весь объект, а обновлял только необходимые поля (название, описание).
//-Исправить обработку ошибок в createSubtask (бросать исключение, а не возвращаться молча).ть
// обработку ошибок в createSubtask (бросать исключение, а не возвращаться молча).