package Bank.Elements;

import Bank.Other.Client;

public class Create extends Element {
    private Cashier priorityCashier;
    private Cashier nonPriorityCashier;
    private int failure;

    public Create(String name, double delay) {
        super(name);
        this.delay = delay;
    }

    @Override
    public void outAct() {
        super.outAct();
        tNext = tCurrent + getDelay();

        Cashier nextCashier = getNextCashier();
        if(nextCashier.isQueueAvailable()) {
            nextCashier.inAct(new Client(tCurrent));
        } else {
            failure++;
        }
    }

    public void setPriorityCashier(Cashier cashier) {
        priorityCashier = cashier;
    }

    public void setNonPriorityCashier(Cashier cashier) {
        nonPriorityCashier = cashier;
    }

    public int getFailure() {
        return failure;
    }

    private Cashier getNextCashier() {
        int pcqSize = priorityCashier.getQueueSize();
        int npcqSize = nonPriorityCashier.getQueueSize();

        if(pcqSize == npcqSize) {
            return priorityCashier;
        }

        return (pcqSize < npcqSize) ? priorityCashier : nonPriorityCashier;
    }
}