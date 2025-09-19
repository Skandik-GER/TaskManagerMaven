package com.iskander.manager;

import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;

import java.time.LocalDateTime;
import java.util.*;
// RED: Лишний импорт+


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

    // YELLOW: Метод изменяет входящий объект (устанавливает ему id).++
    // Это не всегда ожидаемо. Лучше создать новый объект внутри метода или
    // проверять, что id не установлен (равен 0). Но в целом решение рабочее.
    // YELLOW: Нет проверки на повторный добавление задачи с существующим id.
    // RED: Нет проверки на пересечение по времени (вызов validateTime) при создании задач+
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
    // RED: Нет проверки на пересечение по времени (вызов validateTime) при создании подзадачи.+
    @Override
    public void createSubtask(Subtask subtask) {
        if(subtask.getId() != 0 ){
            throw new IllegalArgumentException("Новая Подзадача должна иметь id = 0");
        }
        long epicID = subtask.getEpicId();
        if (!epicmap.containsKey(epicID)) {
            // RED: Заменить на исключение.+
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

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()+
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskmap.values());
    }

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()+
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicmap.values());
    }

    //RED: Нарушение инкапсуляции. Публичные методы getTasks(), getEpics(), getSubtasks()+
    // добавляют ВСЕ элементы в историю просмотров. Это абсолютно неверное поведение.
    // В историю должен попадать только тот объект, который запросили по отдельности (через get...ById).
    // Представьте, что вы просто выводите список всех задач, а они все разом попадают в историю,
    // затирая реальные последние просмотры.
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
            // RED: Заменить на исключение.+
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
        // GREEN: Верная логика удаления всех подзадач вместе с эпикам
        epicmap.clear();
        subtaskmap.clear();
    }
    // YELLOW: Лучше назвать метод removeEpicById
    @Override
    public void removeEpicById(long id) {
        Epic epic = epicmap.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с ID " + id + " не найден");
        }
        // RED: Нет проверки на то, что эпик с таким id существует (epic может быть null).+
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
            // RED: Заменить на исключение.+
            throw new IllegalArgumentException("Задача с ID " + id + " не найдена");
        }
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.+
        return taskmap.get(id);
    }
    @Override
    public Subtask getSubtaskById(long id) {
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.+
        Subtask subtask = subtaskmap.get(id);
        if (subtask == null) {
            throw new IllegalArgumentException("Подзадача с ID " + id + " не найдена");
        }
        historyManager.add(subtaskmap.get(id));
        return subtaskmap.get(id);
    }
    @Override
    public Epic getEpicById(long id) {
        // RED: Вернется null, если задачи нет. Лучше бросать исключение.+
        Epic epic = epicmap.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с ID " + id + " не найдена");
        }
        historyManager.add(epicmap.get(id));
        return epicmap.get(id);
    }

    @Override
    public void updateTask(Task task) {
        // YELLOW: Добавление в историю при обновлении - спорное решение.
        // RED: Нет проверки, что задача с таким id вообще существует в taskmap.+
        // RED: Нет проверки на пересечение по времени (validateTime).+
        if (!taskmap.containsKey(task.getId())) {
            throw new IllegalArgumentException("Задача с id " + task.getId() + " не существует");
        }
        validateTime(task);
        taskmap.put(task.getId(), task);

    }

    @Override
    public void updateEpic(Epic newEpic) {
        // RED: Нет проверки, что oldEpic существует.+
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
        // RED: Опасное решение!++
        // Потенциальная уязвимость в updateEpic.
        // Метод получает newEpic извне и полностью заменяет им существующий эпик.
        // Это опасный подход, так как клиентский код может передать объект с измененным состоянием.
        // Лучше обновлять только те поля старого эпика, которые должны меняться (например, название, описание),
        // но не список подзадач.
        historyManager.add(oldEpic); // YELLOW: Добавление в историю при обновлении.
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

    public void validateTime(Task task) {
        // RED: Алгоритм не проверяет пересечение по интервалам (время начала + продолжительность).+
        // Нужно проверять, что новый интервал [start, start+duration] не пересекается с существующими.
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

//Убрать добавление в историю из методов getTasks(), getEpics(), getSubtasks().
//
//-Добавить вызов validateTime() в createTask, createSubtask, updateTask, updateSubtask.
//-Исправить логику validateTime() для проверки пересечения интервалов, а не только времени начала.
//-Во всех методах, которые работают по ID (get...ById, remove...Id, update...),добавить проверку существования ID
// и выбрасывать соответствующее исключение вместо возврата null или вывода в консоль.
//-Переписать updateEpic чтобы он не заменял весь объект, а обновлял только необходимые поля (название, описание).
//-Исправить обработку ошибок в createSubtask (бросать исключение, а не возвращаться молча).ть
// обработку ошибок в createSubtask (бросать исключение, а не возвращаться молча).