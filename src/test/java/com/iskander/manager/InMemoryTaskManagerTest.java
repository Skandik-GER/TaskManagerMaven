package com.iskander.manager;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager>{
    @BeforeEach
    public void initData(){
        super.initData();
        taskManager = (InMemoryTaskManager) Managers.getDefault();

    }
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}