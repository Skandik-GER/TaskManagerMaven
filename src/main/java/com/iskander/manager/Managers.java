package com.iskander.manager;

public class Managers {
    private Managers() {
    }

    public static Manager getDefault(){
        return new InMemoryTaskManager();

    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static Manager getFileBackTasksManager(){
        return new FileBackedTasksManager("src/test/resources/manager1/data.csv",
                "src/test/resources/manager1/history.csv"
                );

    }

}
