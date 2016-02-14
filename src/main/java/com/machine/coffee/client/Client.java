package com.machine.coffee.client;

import com.machine.coffee.product.Coffee;

/**
 * Created by rado on 06.02.2016.
 */
public class Client {

    private int index;
    private Coffee coffee;
    private long spendTimeToChoose;
    private long spendTimeToPay;
    private long spendTimeToTakeOrder;
    private long coffeeMachineIndex;
    private long summaryTime;

    public Client(int index) {
        this.index = index;
    }

    public Client(int index, Coffee coffee) {
        this.index = index;
        this.coffee = coffee;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public long getSpendTimeToChoose() {
        return spendTimeToChoose;
    }

    public void setSpendTimeToChoose(long spendTimeToChoose) {
        this.spendTimeToChoose = spendTimeToChoose;
    }

    public long getSpendTimeToPay() {
        return spendTimeToPay;
    }

    public void setSpendTimeToPay(long spendTimeToPay) {
        this.spendTimeToPay = spendTimeToPay;
    }

    public long getSpendTimeToTakeOrder() {
        return spendTimeToTakeOrder;
    }

    public void setSpendTimeToTakeOrder(long spendTimeToTakeOrder) {
        this.spendTimeToTakeOrder = spendTimeToTakeOrder;
    }

    public int getIndex() {return index;}

    public long getCoffeeMachineIndex() {
        return coffeeMachineIndex;
    }

    public void setCoffeeMachineIndex(long coffeeMachineIndex) {
        this.coffeeMachineIndex = coffeeMachineIndex;
    }

    public long getSummaryTime() {
        return summaryTime;
    }

    public void setSummaryTime(long summaryTime) {
        this.summaryTime = summaryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (index != client.index) return false;
        if (spendTimeToChoose != client.spendTimeToChoose) return false;
        if (spendTimeToPay != client.spendTimeToPay) return false;
        if (spendTimeToTakeOrder != client.spendTimeToTakeOrder) return false;
        if (coffee != null ? !coffee.equals(client.coffee) : client.coffee != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + (coffee != null ? coffee.hashCode() : 0);
        result = 31 * result + (int) (spendTimeToChoose ^ (spendTimeToChoose >>> 32));
        result = 31 * result + (int) (spendTimeToPay ^ (spendTimeToPay >>> 32));
        result = 31 * result + (int) (spendTimeToTakeOrder ^ (spendTimeToTakeOrder >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "index=" + index +
                ", coffee=" + coffee +
                ", spendTimeToChoose=" + spendTimeToChoose +
                ", spendTimeToPay=" + spendTimeToPay +
                ", spendTimeToTakeOrder=" + spendTimeToTakeOrder +
                ", coffeeMachineIndex=" + coffeeMachineIndex +
                '}';
    }
}
