package com.iskander.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    final private long epicId;

    public Subtask(String name, String describe, long epicId, Status status, Duration duration, LocalDateTime startTime) {
        super(name, describe, status,duration,startTime);
        this.epicId = epicId;
    }


    public Subtask(long id, String name, String describe, long epicId, Status status,Duration duration,LocalDateTime startTime) {
        super(id, name, describe, status,duration,startTime);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }


    // RED: Критично! Изменение epicId после создания подзадачи++++
    // должно быть запрещено или как минимум сопровождаться
    // сложной логикой обновления в менеджере (старый эпик удаляет,
    // новый эпик добавляет). Лучше сделать поле final.


    // RED: Опасная реализация equals.++
    // 1. Она несовместима с hashCode(). hashcode считается только по epicId,
    //    а equals сравнивает по epicId, name, status, id.
    // 2. Для сравнения примитивов (long) нужно использовать ==, а не equals.
    // 3. Сравнение getId() написано верно (через ==), но оформлено странно.
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Subtask subtask = (Subtask) object;
        return getId() == subtask.getId() &&
                getEpicId() == subtask.getEpicId() &&
                Objects.equals(getName(), subtask.getName()) &&
                Objects.equals(getStatus(), subtask.getStatus());
    }

    // RED: Опасная реализация.++
    // Хеш-код считается только на основе epicId. Это значит, что все подзадачи
    // одного эпика будут иметь одинаковый хеш-код, даже если их id, name и status разные.
    // Это приведет к ужатной производительности в хеш-коллекциях и коллизиям.
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEpicId(), getName(), getStatus());
    }


    // YELLOW: лучше использовать геттеры вместо прямого обращения к полям++
    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() +
                ", status='" + getStatus() + '\'' +
                ", describe='" + getDescribe() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
