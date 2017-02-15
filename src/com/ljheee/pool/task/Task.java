package com.ljheee.pool.task;

/**
 *这个接口也比较简单，可以执行，可以取到执行结果
 *Task的实现可以是多种多样的
 */
public   interface  Task {    
    void  execute();   
    byte [] getResult();   
} 