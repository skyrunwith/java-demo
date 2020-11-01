package com.fzd.thread.design;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * copy-on-write 模式demo
 */
@Log4j2
public class CopyOnWriteDemoTest {

    @Test
    public void getRouter(){
        RouterTable routerTable = initRouter();
        routerTable.get("A").forEach(r -> {
            log.info(r.iface + " " + r.ip + " " + r.port);
        });
        routerTable.add(new Router("192.168.0.3", "B", 8081));
        routerTable.get("B").forEach(r -> {
            log.info(r.iface + " " + r.ip + " " + r.port);
        });
        routerTable.remove(new Router("192.168.0.3", "B", 8081));
        routerTable.get("B").forEach(r -> {
            log.info(r.iface + " " + r.ip + " " + r.port);
        });
    }

    private RouterTable initRouter(){
        RouterTable routerTable = new RouterTable();
        String[] services = new String[]{"A", "B", "C"};
        for(String service : services){
            for(int j = 0; j < services.length; j++){
                routerTable.add(new Router("192.168.0." + j, service, 8080));
            }
        }
        return routerTable;
    }

    class RouterTable {
        private ConcurrentHashMap<String, CopyOnWriteArraySet<Router>>
                map = new ConcurrentHashMap<>();

        public Set<Router> get(String key){
            return map.get(key);
        }

        public boolean remove(Router router){
            CopyOnWriteArraySet<Router> set = map.get(router.iface);
            return set != null && set.remove(router);
        }

        public void add(Router router){
            CopyOnWriteArraySet<Router> set = map.computeIfAbsent(router.iface, r -> new CopyOnWriteArraySet<>());
            set.add(router);
        }

    }

    final class Router{
        private final String ip;
        private final String iface;
        private final Integer port;

        public Router(String ip, String iface, Integer port) {
            this.ip = ip;
            this.iface = iface;
            this.port = port;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Router){
                Router r = (Router) o;
                return ip.equals(r.ip) && port.equals(r.ip) && iface.equals(r.iface);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, iface, port);
        }
    }
}
