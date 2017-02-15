package com.ljheee.pool.threadpool;

import com.ljheee.pool.executor.Executor;

public   interface  Pool { //池接口   
    Executor getExecutor();   
     void  destroy();   
}