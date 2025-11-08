package com.iskander.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iskander.model.Task;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int PORT = 8080;
    HttpServer httpServer;
    public final Manager manager;
    private final Gson gson;


    public HttpTaskServer() throws IOException {
        this.manager = Managers.getDefault();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT),0);
        contexts();
    }
    private void contexts(){
        httpServer.createContext("/tasks/task",new TaskHandler(TaskType.TASK,manager));
        httpServer.createContext("/tasks/epic",new TaskHandler(TaskType.EPIC,manager));
        httpServer.createContext("/tasks/subtask",new TaskHandler(TaskType.SUBTASK,manager));
    }

    public void start(){
        httpServer.start();
        System.out.println("Server launched " + PORT);
    }
    public void stop(){
        httpServer.stop(0);
        System.out.println("Server is stopped");
    }
}

