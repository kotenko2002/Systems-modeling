package alt.Elements;

import alt.Other.Client;
import alt.Other.FunRand;

public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;
    protected Element nextElement;
    protected double delay;

    private static int nextId = 0;
    private int id;

    public Element(String name) {
        this.name = name;
        tNext = 0.0;
        tCurrent = tNext;
        nextElement = null;
        id = nextId;
        nextId++;
    }

    public double getDelay() {
        return FunRand.Exp(delay);
    }

    public int getQuantity() {
        return quantity;
    }
    public void setTCurrent(double tCurrent) {
        this.tCurrent = tCurrent;
    }
    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void inAct(Client client) {
    }
    public void outAct(){
        quantity++;
    }
    public double getTNext() {
        return tNext;
    }
    public int getId() {
        return id;
    }
    public void printInfo(){
        System.out.print(name + ": {" + " працюючих процесорів: " + 1 +
                "; кількість: " + quantity+
                "; tnext: " + tNext);

         if(name.contains("CREATOR")) {
             System.out.println(" }");
         }
    }
    public String getName() {
        return name;
    }

    public void doStatistics(double delta){
    }
}
