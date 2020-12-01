package com.fzd.thread.design.guardedsuspension;

import java.util.Objects;

public class GuardedService {
    /**
     * 发送消息
     * @param msg
     */
    public void send(Message msg){

    }

    /**
     * MQ 返回消息后调用该方法
     * @param msg
     */
    public void onMessage(Message msg){
        // 唤醒等待的线程
        GuardedObject.fireEvent(msg.getId(), msg);
    }

    void handleWebReq(){
        // 发送消息
        Message message = new Message("1", "test");
        // 等待 MQ 消息
        GuardedObject<Message> go = GuardedObject.create("1");
        send(message);
        Message result = go.get(Objects::nonNull);
        System.out.println(result.toString());
    }
}
