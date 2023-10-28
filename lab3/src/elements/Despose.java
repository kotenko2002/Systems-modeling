package elements;

import other.Client;

public class Despose extends Element{
    private Client nextClient;
    private double meanTime = 0.0;

    public Despose() {
        super();
        super.setTnext(Double.MAX_VALUE);
    }

    public void inAct(Client client) {
        nextClient = client;

        super.inAct();
        if (super.getNextElement() != null)
            super.getNextElement().inAct();

        this.outAct();
    }

    public void outAct() {
        super.outAct();
        nextClient.setEndTime(getTcurr());
        meanTime += nextClient.getEndTime() - nextClient.getStartTime();
    }

    public double getMeanTime() {
        return meanTime;
    }
}
