package com.fzd;

import lombok.Data;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/9/27
 */
@Data
public class Pizza {

    private PizzaStatus status;

    public boolean isDeliverable(){
        return getStatus() == PizzaStatus.READY;
    }

    public enum PizzaStatus {
        ORDERED,
        READY,
        DELIVERED;
    }

}
