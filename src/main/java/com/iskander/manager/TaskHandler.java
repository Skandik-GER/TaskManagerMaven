package com.iskander.manager;

import com.google.gson.Gson;
import com.iskander.exception.EpicIdException;
import com.iskander.exception.OverloopTimeException;
import com.iskander.model.Epic;
import com.iskander.model.Subtask;
import com.iskander.model.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class TaskHandler implements HttpHandler {
    private final TaskType tasktype;
    private final Manager manager;
    private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskType tasktype, Manager manager) {
        this.tasktype = tasktype;
        this.manager = manager;

    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    writeResponse(exchange, "Метод не найден", 405);
            }
        } catch (Throwable e) {
            writeResponse(exchange, e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String response = null;
        URI uri = exchange.getRequestURI();
        String request = uri.getQuery();
        if (request != null && request.startsWith("id=")) {
            try {
                int id = Integer.parseInt(request.substring(3));
                switch (tasktype) {
                    case TASK:
                        response = TaskGson.GSON.toJson(manager.getTaskById(id));
                        break;
                    case EPIC:
                        response = TaskGson.GSON.toJson(manager.getEpicById(id));
                        break;
                    case SUBTASK:
                        response = TaskGson.GSON.toJson(manager.getSubtaskById(id));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tasktype);
                }
            } catch (NumberFormatException e) {
                writeResponse(exchange, "ID ERROR", 400);
                return;
            }
        } else {
            switch (tasktype) {
                case TASK:
                    response = TaskGson.GSON.toJson(manager.getTasks());
                    break;
                case EPIC:
                    response = TaskGson.GSON.toJson(manager.getEpics());
                    break;
                case SUBTASK:
                    response = TaskGson.GSON.toJson(manager.getSubtasks());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + tasktype);
            }
        }
        writeResponse(exchange, response, 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        try {
            Task task = switch (tasktype) {
                case TASK -> TaskGson.GSON.fromJson(body, Task.class);
                case EPIC -> TaskGson.GSON.fromJson(body, Epic.class);
                case SUBTASK -> TaskGson.GSON.fromJson(body, Subtask.class);
                default -> null;
            };

            if (task == null) {
                writeResponse(exchange, "Error", 400);
                return;
            }
            if (task.getId() != 0) {
                switch (tasktype) {
                    case TASK -> manager.updateTask(task);
                    case EPIC -> manager.updateEpic((Epic) task);
                    case SUBTASK -> manager.updateSubtask((Subtask) task);

                }
                writeResponse(exchange, "Обновился таск" + tasktype, 200);
            } else {
                switch (tasktype) {
                    case TASK -> manager.createTask(task);
                    case EPIC -> manager.createEpic((Epic) task);
                    case SUBTASK -> manager.createSubtask((Subtask) task);
                }
                writeResponse(exchange, "id: " + task.getId(), 201);
            }
        }catch (OverloopTimeException e){
            System.err.println(e.getMessage());
            writeResponse(exchange,"Задачи не должны перескаться по времени!",400);
        }catch (EpicIdException e){
            System.err.println(e.getMessage());
            writeResponse(exchange,"Такого EpicId не существует",400);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String request = uri.getQuery();

        if (request != null && request.startsWith("id=")) {
            try {
                int id = Integer.parseInt(request.substring(3));
                switch (tasktype) {
                    case TASK -> manager.removeTaskId(id);
                    case EPIC -> manager.removeEpicById(id);
                    case SUBTASK -> manager.removeSubtaskId(id);
                }
                writeResponse(exchange, "Задача удалена ", 200);
            } catch (NumberFormatException e) {
                writeResponse(exchange, "Ошибка в ID", 400);
            }
        } else {
            writeResponse(exchange, "ID неверен", 400);
        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private Optional<Integer> getId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

}
