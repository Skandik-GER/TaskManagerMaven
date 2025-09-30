package com.iskander.manager;

import org.junit.jupiter.api.BeforeEach;

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