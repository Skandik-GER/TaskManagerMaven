package com.iskander;

import com.iskander.manager.FileBackedTasksManager;
import com.iskander.manager.HttpTaskServer;
import com.iskander.model.Status;
import com.iskander.model.Task;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.manager.createTask(new Task("Work","working", Status.NEW));
        httpTaskServer.start();

    }
}