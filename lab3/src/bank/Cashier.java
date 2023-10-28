package bank;

import java.util.ArrayList;
import elements.Process;

public class Cashier extends Process {
    private double waitTime = 0.0, waitStart = 0.0, stateSum = 0.0;
    private int numOfSwaps = 0;

    private ArrayList<Cashier> otherLanes = new ArrayList<>();

    public Cashier(double delay, String type) {
        super(delay, type);
        super.setDistribution("norm");
    }

    @Override
    public void outAct() {
        super.outAct();

        for (Cashier c : otherLanes) {
            c.checkQueuesAndMove();
        }

        if (super.getState() == 0) {
            waitStart = getTcurr();
        }


    }

    @Override
    public void inAct() {
        if (super.getState() == 0) {
            waitTime += getTcurr() - waitStart;
        }
        super.inAct();

    }

    public void checkQueuesAndMove() {
        for (Cashier c : otherLanes) {
            if (this.getQueue() - c.getQueue() >= 2) {
                System.out.println("Move from " + this.getId() + " to " + c.getId());
                numOfSwaps++;
                c.inAct();
                break;
            }
        }
    }

    public void addLane(Cashier c) {
        otherLanes.add(c);
    }

    public double getWaitTime() {
        return waitTime;
    }

    public int getNumOfSwaps() {
        return numOfSwaps;
    }

    @Override
    public void doStatistics(double delta) {
        stateSum += delta * getState();
        super.doStatistics(delta);
    }

    public double getStateSum() {
        return stateSum;
    }
}
