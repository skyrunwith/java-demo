package com.fzd.thread.design.cow;

import lombok.Data;

import java.util.Objects;

@Data
public final class Router{
    private final String ip;
    // 服务名称
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
