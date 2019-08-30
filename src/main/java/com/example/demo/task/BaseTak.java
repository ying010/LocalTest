package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Package com.example.demo.task
 * @ClassName BaseTak
 * @Description TODO
 * @Author W.Z.King
 * @Date 2019/8/29 12:54
 */
public class BaseTak implements Runnable {
    private Logger logger = LoggerFactory.getLogger(BaseTak.class);
    private TaskExecutable taskExecutable;

    public BaseTak(TaskExecutable taskExecutable) {
        this.taskExecutable = taskExecutable;
    }

    @Override
    public void run() {
        try {
            taskExecutable.task();
        } catch (Exception e) {
            logger.error("BaseTask(err)", e);
        }
    }
}
