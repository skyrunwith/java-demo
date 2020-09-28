package com.fzd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class EnumSetTest {

    @Test
    public void getAllUndelivered(){
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza(Pizza.PizzaStatus.READY));
        pizzas.add(new Pizza(Pizza.PizzaStatus.DELIVERED));
        pizzas.add(new Pizza(Pizza.PizzaStatus.ORDERED));
        pizzas.add(new Pizza(Pizza.PizzaStatus.READY));

        List<Pizza> undeliveredPizza = Pizza.getAllUndelivered(pizzas);
        System.out.println(undeliveredPizza);
    }
}
