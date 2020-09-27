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

    @Test
    public void groupPizzaByStatus(){
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza(Pizza.PizzaStatus.READY));
        pizzas.add(new Pizza(Pizza.PizzaStatus.DELIVERED));
        pizzas.add(new Pizza(Pizza.PizzaStatus.ORDERED));
        pizzas.add(new Pizza(Pizza.PizzaStatus.READY));

        EnumMap<Pizza.PizzaStatus, List<Pizza>> enumMap = Pizza.groupPizzaByStatus(pizzas);
        assertTrue(enumMap.get(Pizza.PizzaStatus.READY).size() == 2);
        assertTrue(enumMap.get(Pizza.PizzaStatus.DELIVERED).size() == 1);
        assertTrue(enumMap.get(Pizza.PizzaStatus.ORDERED).size() == 1);
    }
}
