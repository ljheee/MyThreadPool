package com.ljheee.pool.threadpool;

import  java.util.LinkedList;   
import  java.util.Properties;

import com.ljheee.pool.executor.Executor;
import com.ljheee.pool.task.Task;

public   class  ThreadPool  implements  Pool{   
	
     private   boolean  isShut;   
     private  LinkedList pool;   
     private   int  size;
     
//     private   static  Properties prop = PropReader.getProperties( "webconfig.properties" );   
//     private   int  size = Integer.parseInt(prop.getProperty( "threadsperpage" ,  "3" ));   
     
     public  ThreadPool(int size){   
         // read configuration and set the   
         // content of pool by objects of Executor   
    	this.size = size;
        isShut =  false ; //set the status of pool to active   
        pool =  new  LinkedList();   
         for ( int  i =  0 ; i < size; i++){   
            Executor executor =  new  ExecutorImpl(); //new a executor thread   
            pool.add(executor); //add it to pool   
            ((ExecutorImpl)executor).start(); //start it   
        }   
    }   
     
     public   void  destroy() { //Destroy   
         synchronized (pool){   
            isShut =  true ; //set the status of pool to inactive   
            pool.notifyAll(); //notify all listener.   
            pool.clear(); //clear the list of threads   
        }   
    }   
  
     public  Executor getExecutor(){   
        Executor ret =  null ;   
         synchronized (pool){ //return if any.   
             if (pool.size() >  0 ){   
                ret = (Executor)pool.removeFirst();   
            } else {   
                 try  {   
                    pool.wait();   
                }  catch  (InterruptedException e) {   
                    e.printStackTrace();   
                }   
                ret = (Executor)pool.removeFirst();   
            }   
        }   
         return  ret;   
    }   
     
  
     /**
      *Executor接口的实现
      *作为ThreadPool的内部类  
      */
     private   class  ExecutorImpl  extends  Thread  implements  Executor{   
    	 
         private  Task task;   
         private  Object lock =  new  Object();   
         //private boolean loop = true;   
         public  ExecutorImpl(){}   
         public  Task getTask() {   
             return   this .task;   
        }   
  
         public   void  setTask(Task task) {   
             this .task = task;   
        }   
         public   void  startTask(){   
             //System.out.println("start here");   
             synchronized (lock){   
                lock.notify();   
            }   
        }   
         public   void  run(){   
             //get a task if any   
             //then run it   
             //then put self to pool   
             while (!isShut){   
                 synchronized (lock){   
                     try  {   
                        lock.wait(); //wait for resource   
                    }  catch  (InterruptedException e) {   
                        e.printStackTrace();   
                    }   
                }   
                getTask().execute(); //execute the task   
                 synchronized (pool){ //put it self to the pool when finish the task   
                    pool.addFirst(ExecutorImpl. this );   
                    pool.notifyAll();   
                }   
            }   
        } 
         
    }  
     
     
}