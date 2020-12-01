package com.fzd.thread.design.balking;

import com.fzd.thread.design.cow.Router;

import java.util.Set;
import java.util.concurrent.*;

/**
 * volatile 实现 balking模式
 */
public class RouterTable {
    // rt 是否被改过
    private volatile boolean changed = false;
    // key：接口名，value：路由集合
    private ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();
    // 将路由表写入本地的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    // 启动定时任务，rt 变更后写入本地，同一时刻只有一个线程会执行，所以volatile可以保证线程安全
    void startLocalServer(){
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> autoSave(),
                1, 1, TimeUnit.MINUTES);
    }

    // 自动保存路由表
    private void autoSave() {
        if(!changed){
            return;
        }
        changed = false;
        // 将路由表保存到本地
        saveLocal();
    }

    private void saveLocal() {
    }

    // 删除路由
    private void remove(Router router){
        Set<Router> routerSet = rt.get(router.getIface());
        if(routerSet != null){
            rt.remove(router.getIface());
            // 路由表发生变化
            changed = true;
        }
    }

    // 添加路由
    public void addRouter(Router router){
        Set<Router> routers = rt.computeIfAbsent(router.getIface(), r -> new CopyOnWriteArraySet<>());
        routers.add(router);
        // 路由表发生变化
        changed = true;
    }
}
