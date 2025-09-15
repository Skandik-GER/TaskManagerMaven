package com.iskander.manager;


import com.iskander.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// RED:Нарушение SRP (Single Responsibility Principle).
// Класс FileBackedTasksManager знает слишком много: он и управляет задачами, и парсит CSV,
// и сериализует/десериализует объекты.
// Логику парсинга (fromString, fromStringEpic, historyFromString) действительно лучше вынести
// в отдельный утилитарный класс.

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Parser parser = new Parser();
    private final String to;
    private final String toHist;


    public FileBackedTasksManager(String to, String toHist) {
        this.to = to;
        this.toHist = toHist;
    }


    public void save() {
        try (FileWriter fileWriter = new FileWriter(to)) {
            StringBuilder stringBuilder = new StringBuilder("type,name,description,id,status,duration,startTime\n");
            for (Epic epic : epicmap.values()) {
                stringBuilder.append("EPIC,").append(parser.toParse(epic)).append("\n");
            }
            for (Task task : taskmap.values()) {
                stringBuilder.append("TASK,").append(parser.toParse(task)).append("\n");
            }
            for (Subtask subtask : subtaskmap.values()) {
                stringBuilder.append("SUBTASK,").append(parser.toParse(subtask)).append("\n");
            }

            fileWriter.write(stringBuilder.toString().trim());

        } catch (IOException e) {
            System.out.println("Ошибка во время чтения файла.");
        }

        try (FileWriter fileWriter = new FileWriter(toHist)) {
            fileWriter.write(historyToString());
        } catch (IOException e) {
            System.out.println("Ошибка чтения");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getTasks() {
        save();
        return super.getTasks();
    }


    @Override
    public List<Epic> getEpics() {
        save();
        return super.getEpics();
    }

    @Override
    public List<Subtask> getSubtasks() {
        save();
        return super.getSubtasks();
    }

    @Override
    public void removeTaskId(long id) {
        super.removeTaskId(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeEpicId(long id) {
        super.removeEpicId(id);
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public void removeSubtaskId(long id) {
        super.removeSubtaskId(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasksByEpic(long epicId) {
        save();
        return super.getSubtasksByEpic(epicId);
    }

    @Override
    public Task getTaskById(long id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Subtask getSubtaskById(long id) {
        save();
        return super.getSubtaskById(id);
    }

    @Override
    public Epic getEpicById(long id) {
        save();
        return super.getEpicById(id);
    }

    private String historyToString() {
        final StringBuilder sb = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            Long id = task.getId();
            sb.append(id).append(",");
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static List<Long> historyFromString(String value) {
        if(value == null){
            return Collections.emptyList();
        }
        final List<Long> history = new ArrayList<>();
        String[] values = value.split(",");
        for (String number : values) {
            long num = Long.parseLong(number);
            history.add(num);
        }
        return history;

    }

    private static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        long id = Long.parseLong(parts[3]);
        Status status = Status.valueOf(parts[4]);
        // RED: Потенциальная ошибка. Парсится 5-й элемент (parts[5]) как duration,
        // но в строке для Подзадачи parts[5] - это epicId.
        // Видно, что методы парсинга скопированы и не адаптированы под разное количество полей.
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[5]));
        LocalDateTime startTime = LocalDateTime.parse(parts[6],formatter);
        return new Task(id, name, describe, status,duration,startTime);
    }

    private static Epic fromStringEpic(String value) {
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        return new Epic(name, describe);
    }

    private static Subtask fromStringSubtask(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] parts = value.split(",");
        String name = parts[1];
        String describe = parts[2];
        long id = Long.parseLong(parts[3]);
        Status status = Status.valueOf(parts[4]);
        long epicId = Long.parseLong(parts[5]);
        // RED: Ошибка! parts[5] парсится дважды.
        // Сначала в epicId, потом в duration. Это выбросит исключение.
        // Нужно parts[6] для duration.
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[5]));
        LocalDateTime startTime = LocalDateTime.parse(parts[7],formatter);
        return new Subtask(id, name, describe, epicId, status,duration,startTime);
    }

    public static FileBackedTasksManager loadFromFile(String path, String newPath, String hist, String toHist) {
        FileBackedTasksManager manager = new FileBackedTasksManager(newPath, toHist);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(path))) {
            fileReader.readLine();
            String line;
            while ((line = fileReader.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                TaskType taskType = TaskType.valueOf(data[0].trim().toUpperCase());
                switch (taskType) {
                    case TASK:
                        Task task = fromString(line);
                        manager.createTask(task);
                        break;
                    case EPIC:
                        Epic epic = fromStringEpic(line);
                        manager.createEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = fromStringSubtask(line);
                        manager.createSubtask(subtask);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        } catch (IllegalArgumentException e) {
            System.err.println("ОШИБКА: неизвестный тип задачи");
        }

        try (BufferedReader historyReader = new BufferedReader(new FileReader(hist))) {
            String historyLine = historyReader.readLine();
            List<Long> histIds = historyFromString(historyLine);
            for (long id : histIds) {
                if (manager.taskmap.containsKey(id)) {
                    manager.historyManager.add(manager.taskmap.get(id));
                } else if (manager.epicmap.containsKey(id)) {
                    manager.historyManager.add(manager.epicmap.get(id));
                } else if (manager.subtaskmap.containsKey(id)) {
                    manager.historyManager.add(manager.subtaskmap.get(id));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла истории: " + e.getMessage());
        }
        // RED: Лишний вызов? Мы только что загрузили данные и вызвали save() для каждой задачи.
        manager.save();
        return manager;
    }
}
