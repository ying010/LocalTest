package com.example.demo.download.task;

/**
 * @Package com.example.demo.download.task
 * @ClassName TaskExecutable
 * @Description TODO
 * @Author W.Z.King
 * @Date 2019/8/29 12:55
 */
public interface TaskExecutable {
    /**
     * 线程中执行的任务
     * @throws Exception
     * @Author W.Z.King
     * @Date 2019/8/29 12:55
     */
    void task() throws Exception;
}
