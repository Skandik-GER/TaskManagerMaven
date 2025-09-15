package com.iskander.model;

import java.time.format.DateTimeFormatter;
//RED: Переименуйте класс и методы. Класс должен называться так, чтобы отражать его суть,
// например, TaskFormatter или TaskCsvSerializer.
// Методы toParse лучше переименовать в format или serialize.
//
//YELLOW: Унифицируйте формат вывода для всех типов задач или четко задокументируйте отличия. Для Epic также стоит сериализовать id и status, как и для остальных задач, чтобы формат был предсказуемым.
//
// YELLOW: Рассмотрите возможность использования String.join или StringBuilder для конкатенации строк. Это сделает код чище и потенциально эффективнее.

public class Parser {
    // RED: отсутсвуют модификаторы
    // Критично: поле должно быть private. Также, если формат не должен меняться,
    // лучше добавить модификатор final и сделать его static (если он общий для всех экземпляров).
    // private static final DateTimeFormatter FORMATTER = ...
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String toParse(Subtask task){
        // RED: Вызов task.getDuration().getSeconds() и task.getStartTime().format(...)
        // выбросит NPE, если getDuration() или getStartTime() вернут null.
        return task.getName() + "," + task.getDescribe() + "," + task.getId() + "," + task.getStatus() + "," + task.getEpicId() + "," + task.getDuration().getSeconds() + "," + task.getStartTime().format(formatter);
    }
    // Аналогичная проблема в методе toParse(Task task).
    public String toParse(Task task){
        return task.getName() + "," + task.getDescribe() + "," + task.getId() + "," + task.getStatus()+ "," + task.getDuration().getSeconds() + "," + task.getStartTime().format(formatter);
    }
    public String toParse(Epic epic){
        // YELLOW: Формат строки для Epic отличается от формата для Task и Subtask.
        // Это может вызвать проблемы при чтении файла, так как парсер (например, в FileBackedTasksManager)
        // будет ожидать одинаковое количество полей для каждого типа.
        return epic.getName() + "," + epic.getDescribe() ;
    }
}
