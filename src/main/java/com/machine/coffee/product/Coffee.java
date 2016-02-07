package com.machine.coffee.product;

/**
 * Created by rado on 06.02.2016.
 */
public class Coffee {

    private CoffeeType coffeeType;
    private int price;
    private long cockTime;

    public Coffee() {
        coffeeType=CoffeeType.UNKNOWN;
        price=0;
        cockTime=0;
    }

    public Coffee(CoffeeType coffeeType, int price, long cockTime) {
        this.coffeeType = coffeeType;
        this.price = price;
        this.cockTime = cockTime;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(CoffeeType coffeeType) {
        this.coffeeType = coffeeType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getCockTime() {
        return cockTime;
    }

    public void setCockTime(long cockTime) {
        this.cockTime = cockTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coffee coffee = (Coffee) o;

        if (Float.compare(coffee.cockTime, cockTime) != 0) return false;
        if (Float.compare(coffee.price, price) != 0) return false;
        if (coffeeType != coffee.coffeeType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = coffeeType != null ? coffeeType.hashCode() : 0;
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (cockTime != +0.0f ? Float.floatToIntBits(cockTime) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "coffeeType=" + coffeeType +
                ", price=" + price +
                ", cockTime=" + cockTime +
                '}';
    }
}
