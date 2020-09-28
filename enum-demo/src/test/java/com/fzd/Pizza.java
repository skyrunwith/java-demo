package com.fzd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/9/27
 */
@Data
public class Pizza {

    private PizzaStatus status;

    /**
     * EnumSet Demo, 枚举型set，内部顺序由枚举常量顺序决定
     * 实现类：RegularEnumSet、 JumboEnumSet
     */
    private static final EnumSet<PizzaStatus> undeliveredPizzaStatuses =
            EnumSet.of(PizzaStatus.READY, PizzaStatus.ORDERED);

    public Pizza() {
    }

    public Pizza(PizzaStatus status) {
        this.status = status;
    }

    public boolean isDeliverable(){
        return getStatus() == PizzaStatus.READY;
    }

    /**
     *
     */
    public void printTimeToDeliver(){
        System.out.println("Time to delivery is " + this.getStatus().getTimeToDelivery());
    }

    /**
     * 通过枚举类型过滤list
     * @param list
     * @return
     */
    public static List<Pizza> getAllUndelivered(List<Pizza> list){
        return list.stream().filter(item -> undeliveredPizzaStatuses.contains(item.getStatus())).collect(Collectors.toList());
    }

    /**
     * 利用 EnumMap key为枚举类型进行分组
     * @param pizzaList
     * @return
     */
    public static EnumMap<Pizza.PizzaStatus, List<Pizza>> groupPizzaByStatus(List<Pizza> pizzaList){
//        EnumMap<Pizza.PizzaStatus, List<Pizza>> enumMap = new EnumMap<PizzaStatus, List<Pizza>>(PizzaStatus.class);
//        Iterator<Pizza> iterator = pizzaList.iterator();
//        while (iterator.hasNext()){
//            Pizza pizza = iterator.next();
//            PizzaStatus pizzaStatus = pizza.getStatus();
//            if(enumMap.containsKey(pizzaStatus)){
//                enumMap.get(pizzaStatus).add(pizza);
//            }else {
//                List<Pizza> list = new ArrayList<>();
//                list.add(pizza);
//                enumMap.put(pizzaStatus, list);
//            }
//        }
//        return enumMap;

        //java8
        return pizzaList.stream().collect(Collectors.groupingBy(Pizza::getStatus
                , () -> new EnumMap<>(PizzaStatus.class), Collectors.toList()));
    }



    public void deliver(){
        if(isDeliverable()){
            EnumSingleton.getInstance().getStrategy().deliver(this);
            this.setStatus(PizzaStatus.DELIVERED);
        }
    }

    /**
     * 枚举类型定义
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum PizzaStatus {
        ORDERED(5){
            //枚举方法
            @Override
            public boolean isOrdered(){
                return true;
            }
        },
        READY(2){
            //枚举方法
            @Override
            public boolean isReady(){
                return true;
            }
        },
        DELIVERED(0){
            //枚举方法
            @Override
            public boolean isDelivered(){
                return true;
            }
        };
        //枚举变量
        private int timeToDelivery;

        public boolean isOrdered(){ return  false; }

        public boolean isReady(){ return false; }

        public boolean isDelivered(){ return false; }

        public int getTimeToDelivery(){
            return this.timeToDelivery;
        }

        PizzaStatus(int timeToDelivery){
            this.timeToDelivery = timeToDelivery;
        }
    }
}
