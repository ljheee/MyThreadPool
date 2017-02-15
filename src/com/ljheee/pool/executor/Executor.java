package com.ljheee.pool.executor;

import com.ljheee.pool.task.Task;

/**
 *执行器接口   
 */
public   interface  Executor { 
    void  setTask(Task task);   
   Task getTask();   
    void  startTask();   
}
