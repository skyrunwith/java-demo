package com.fzd.thread.design.balking;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoSaveEditor {
    // 文件是否被修改过
    boolean changed = false;
    /**
     * 定时任务线程池
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public void startAutoSave(){
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> autoSave(),
                5, 5, TimeUnit.SECONDS);
    }

    public void autoSave(){
        synchronized (this) {
            if (!changed) {
                return;
            }
            changed = false;
        }
        // 执行存盘操作
        this.execSave();
    }

    public void edit(){
        // 省略业务逻辑
        change();
    }

    private void change() {
        synchronized (this) {
            changed = true;
        }
    }

    private void execSave() {
    }
}
