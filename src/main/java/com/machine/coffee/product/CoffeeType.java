package com.machine.coffee.product;

/**
 * Created by rado on 06.02.2016.
 */
public enum CoffeeType {

    ESPRESSO("Espresso"),
    LATTE("Latte Macchiato"),
    CAPPUCCINO("Cappuccino"),
    UNKNOWN("Unknown product");


    private String type;

    CoffeeType(String type) {
        this.type = type;
    }

    public String getType() {return type; }

    public static int size() {return values().length;}
}
