public class Element {
    private String name;
    private double tnext;
    private double delayMean, delayDev;
    private String distribution;
    private int quantity;
    private double tcurr;
    private int workingProcessors;
    private Element nextElement;
    private static int nextId=0;
    private int id;

    public Element(){
        tnext = Double.MAX_VALUE;
        delayMean = 1.0;
        distribution = "exp";
        tcurr = tnext;
        workingProcessors =0;
        nextElement=null;
        id = nextId;
        nextId++;
        name = "element"+id;
    }

    public Element(double delay) {
        tnext = 0.0;
        delayMean = delay;
        distribution = "";
        tcurr = tnext;
        workingProcessors = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(String nameOfElement, double delay){
        tnext = 0.0;
        delayMean = delay;
        distribution = "exp";
        tcurr = tnext;
        workingProcessors =0;
        nextElement=null;
        id = nextId;
        nextId++;
        name = nameOfElement;
    }

    public double getDelay() {
        double delay = getDelayMean();
        String distribution = getDistribution().toLowerCase();

        switch (distribution) {
            case "exp":
                delay = FunRand.Exp(getDelayMean());
                break;
            case "norm":
                delay = FunRand.Norm(getDelayMean(), getDelayDev());
                break;
            case "unif":
                delay = FunRand.Unif(getDelayMean(), getDelayDev());
                break;
        }

        return delay;
    }

    public double getDelayDev() {
        return delayDev;
    }
    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }
    public String getDistribution() {
        return distribution;
    }
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTcurr() {
        return tcurr;
    }
    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
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
        return tnext;
    }
    public void setTnext(double tnext) {
        this.tnext = tnext;
    }
    public double getDelayMean() {
        return delayMean;
    }
    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void printResult(){
        System.out.println("\tкількість: " + quantity + ";");
    }
    public void printInfo(){
        System.out.print(getName() + ": {" + " працюючих процесорів: " + workingProcessors +
                "; кількість: " + quantity+
                "; tnext: " + tnext);

         if(getName().contains("CREATOR")) {
             System.out.println(" }");
         }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void doStatistics(double delta){
    }
}
