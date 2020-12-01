package com.fzd.thread.design.guardedsuspension;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String id;
    private String content;
}
