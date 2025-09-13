package com.iskander;

import com.iskander.manager.FileBackedTasksManager;


public class Main {
    public static void main(String[] args) {

//        Manager manager = new FileBackedTasksManager("resources/manager1/data.csv","resources/manager1/newData.csv","resources/manager1/history.csv");
//        Task task1 = new Task("Karowka", "Fall asleep ", Status.NEW);
//        Task task2 = new Task("sSleep", "Fall asleep ", Status.NEW);
////
//        Epic epic1 = new Epic("Backflip", "Jump with flip");
////
//        Subtask subtask1 = new Subtask("Sit", "sit", 1, Status.DONE);
//
//        manager.createEpic(epic1);
//        manager.createSubtask(subtask1);
//        manager.getSubtaskById(2);
//        manager.getEpicById(1);
//        manager.createTask(task1);
//        manager.createTask(task2);
//        System.out.println(manager);
//        System.out.println(manager.getHistory());
        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(
                "src/main/resources/manager2/data.csv",
                "src/main/resources/manager2/newData.csv",
                "src/main/resources/manager2/history.csv",
                "src/main/resources/manager2/newHistory.csv");
        System.out.println(fileBackedTasksManager1.getTasks());


        // Epic epic = new Epic("Sit", "sit");

        // RED
        // Сценарий, при котором случается баг
        // (проблема в методе обновления, задачу необходимо обновлять так же в истории, а то произойдет утечка)
//        manager.createTask(task1);
//        manager.getTaskById(1);
//        System.out.println(manager.getHistory());
//        manager.updateTask(updtask3);
//        System.out.println(manager.getHistory());
//        manager.removeAllTasks();
//        System.out.println(manager.getHistory());

//        manager.createSubtask(subtask1);
//        manager.createEpic(epic2);
//        manager.updateEpic(epic2);
//        System.out.println(manager.getHistory());


//        manager.createTask(task1);
//        manager.createTask(task2);
//        manager.updateTask(updtask3);
//
//
//        manager.createEpic(epic);
//        manager.createEpic(epic1);
//        manager.createEpic(epic2);
//
//        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//        manager.createSubtask(subtask3);
//        manager.createSubtask(subtask4);
//        manager.createSubtask(subtask5);
//        manager.createSubtask(subtask6);
//        manager.updateSubtask(subtask3);
//        System.out.println(manager.getHistory());
//
//        manager.getEpicById(epic1.getId());
//        manager.getEpicById(epic2.getId());
//
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//
//        manager.getSubtaskById(subtask1.getId());
//        manager.getSubtaskById(subtask2.getId());
//        manager.getSubtaskById(subtask3.getId());
//        manager.getSubtaskById(subtask4.getId());
//        System.out.println("----------------");
//        manager.createEpic(epic1);
//        manager.createTask(task1);
//        manager.createSubtask(subtask1);
//        manager.getSubtaskById(subtask1.getId());
//        System.out.println(manager.getHistory());
//        manager.createTask(task2);
//        manager.getTaskById(2);
//        System.out.println(manager.getHistory());
//        manager.removeTaskId(2);
//        manager.removeSubtaskId(subtask1.getId());
//        manager.getEpicById(epic1.getId());
//        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//        System.out.println(manager.getHistory());
//        manager.removeEpicId(epic1.getId());
//        System.out.println("---");
//        manager.removeEpicId(epic1.getId());
//        System.out.println(manager.getHistory());
//        manager.createEpic(epic2);
//        manager.createTask(task1);
//        manager.createTask(task2);
//        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//        System.out.println("____Tasks____");
//        manager.getTaskById(task1.getId());
//        System.out.println(manager.getHistory());
//        manager.getTaskById(task2.getId());
//        System.out.println(manager.getHistory());
//        manager.getTaskById(task1.getId());
//        System.out.println(manager.getHistory());
//        manager.removeTaskId(task1.getId());
//        System.out.println(manager.getHistory());
//        manager.removeTaskId(task2.getId());
//        System.out.println(manager.getHistory());
//        System.out.println("____SUBTASKS____");
//        manager.getSubtaskById(subtask1.getId());
//        System.out.println(manager.getHistory());
//        manager.getSubtaskById(subtask2.getId());
//        System.out.println(manager.getHistory());
//        System.out.println("____Epics____");
//        manager.getEpicById(epic2.getId());
//        System.out.println(manager.getHistory());
//        manager.removeEpicId(epic2.getId());
//        System.out.println(manager.getHistory());


    }
}