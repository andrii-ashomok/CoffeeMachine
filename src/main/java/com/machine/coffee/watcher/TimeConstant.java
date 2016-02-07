package com.machine.coffee.watcher;

/**
 * Created by rado on 06.02.2016.
 */
public class TimeConstant {

    private long timePickFavorite;
    private long timePayCredit;
    private long timePayCash;
    private long timeFindCup;
    private long timePutCup;
    private long timePickType;
    private long timeLeave;
    private long timeFullOrder;


    public TimeConstant(long timePickFavorite, long timePayCredit, long timePayCash,
                        long timeFindCup, long timePutCup,
                        long timePickType, long timeLeave, long timeFullOrder) {
        this.timePickFavorite = timePickFavorite;
        this.timePayCredit = timePayCredit;
        this.timePayCash = timePayCash;
        this.timeFindCup = timeFindCup;
        this.timePutCup = timePutCup;
        this.timePickType = timePickType;
        this.timeLeave = timeLeave;
        this.timeFullOrder = timeFullOrder;
    }

    public long getTimePickFavorite() {
        return timePickFavorite;
    }

    public void setTimePickFavorite(long timePickFavorite) {
        this.timePickFavorite = timePickFavorite;
    }

    public long getTimePayCredit() {
        return timePayCredit;
    }

    public void setTimePayCredit(long timePayCredit) {
        this.timePayCredit = timePayCredit;
    }

    public long getTimePayCash() {
        return timePayCash;
    }

    public void setTimePayCash(long timePayCash) {
        this.timePayCash = timePayCash;
    }

    public long getTimeFindCup() {
        return timeFindCup;
    }

    public void setTimeFindCup(long timeFindCup) {
        this.timeFindCup = timeFindCup;
    }

    public long getTimePutCup() {
        return timePutCup;
    }

    public void setTimePutCup(long timePutCup) {
        this.timePutCup = timePutCup;
    }

    public long getTimePickType() {
        return timePickType;
    }

    public void setTimePickType(long timePickType) {
        this.timePickType = timePickType;
    }

    public long getTimeLeave() {
        return timeLeave;
    }

    public void setTimeLeave(long timeLeave) {
        this.timeLeave = timeLeave;
    }

    public long getTimeFullOrder() {
        return timeFullOrder;
    }

    public void setTimeFullOrder(long timeFullOrder) {
        this.timeFullOrder = timeFullOrder;
    }
}
