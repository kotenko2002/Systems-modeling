package Elements;

import Other.FunRand;

public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;
    protected Element nextElement;

    private double delay, delayDev;
    private String distribution;
    private int workingProcessors;

    private static int nextId=0;
    private int id;

    public Element(String name, double delay) {
        this.name = name;
        this.delay = delay;
        tNext = 0.0;
        tCurrent = tNext;
        nextElement = null;
        id = nextId;
        nextId++;
    }

    public double getDelay() {
        switch (distribution) {
            case "exp":
                delay = FunRand.Exp(delay);
                break;
            case "norm":
                delay = FunRand.Norm(delay, delayDev);
                break;
            case "unif":
                delay = FunRand.Unif(delay, delayDev);
                break;
        }

        return delay;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setTcurr(double tcurr) {
        this.tCurrent = tcurr;
    }
    public int getWorkingProcessors() {
        return workingProcessors;
    }
    public void setWorkingProcessors(int workingProcessors) {
        this.workingProcessors = workingProcessors;
    }
    public Element getNextElement() {
        return nextElement;
    }
    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void inAct() {
    }
    public void outAct(){
        quantity++;
    }
    public double getTnext() {
        return tNext;
    }
    public void setTnext(double tnext) {
        this.tNext = tnext;
    }
    public int getId() {
        return id;
    }
    public void printInfo(){
        System.out.print(name + ": {" + " працюючих процесорів: " + workingProcessors +
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
