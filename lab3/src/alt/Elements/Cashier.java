package alt.Elements;

import alt.Other.Client;

import java.util.*;

public class Cashier extends Element {
    private final LinkedList<Client> queue;
    private int maxQueue;
    private double meanQueue, meanLoad;
    private Client client;
    private Cashier anotherCashier;
    private int switchesToAnotherCashier;

    public Cashier(String name, double delay, int maxQueue) {
        super(name);
        this.delay = delay;
        this.maxQueue = maxQueue;
        queue = new LinkedList<>();
        meanQueue = 0.0;
        meanLoad = 0.0;
        switchesToAnotherCashier = 0;
        client = null;
        tNext = Integer.MAX_VALUE;
    }

    @Override
    public void inAct(Client client) {
        if (this.client == null) {
            super.outAct();
            this.client = client;

            tNext = tCurrent + getDelay();
        } else {
            queue.add(client);
        }
    }
    @Override
    public void outAct() {
        tryToChangeCashier();

        Client processedClient = client;
        client = null;

        if (!queue.isEmpty()) {
            super.outAct();
            client = queue.pollFirst();

            tNext = tCurrent + getDelay();
        } else {
            tNext = Integer.MAX_VALUE;
        }

        if(nextElement != null) {
            nextElement.inAct(processedClient);
        }
    }

    @Override
    public void doStatistics(double delta) {
        this.meanQueue = meanQueue + queue.size() * delta;
        this.meanLoad = meanLoad + isCashierWorking() * delta;
    }

    public void setAnotherCashier(Cashier cashier) {
        anotherCashier = cashier;
    }
    public double getMeanQueue() {
        return this.meanQueue;
    }
    public double getMeanLoad() {
        return this.meanLoad;
    }
    public int getQueueSize() {
        return queue.size();
    }
    public boolean isQueueAvailable() {
        return queue.size() < maxQueue;
    }
    public int getAmountOfSwitchesToAnotherCashier() {
        return  switchesToAnotherCashier;
    }

    private void tryToChangeCashier() {
        if(queue.size() - anotherCashier.getQueueSize() >= 2) {
            switchesToAnotherCashier++;
            Client lastClientInQueue = queue.pollLast();

            anotherCashier.inAct(lastClientInQueue);
        }
    }
    private int isCashierWorking() {
        return client != null ? 1 : 0;
    }
}