import java.util.*;

public class Process extends Element {
    private int queue, maxqueue, failure;
    private double meanQueue;
    private double meanLoad;
    private final int channelsNumber;
    private ArrayList<Process> nextElements = new ArrayList<>();
    private ArrayList<Double> nextElementsProbabilities = new ArrayList<>();
    private final ArrayList<Double> tNextsList;

    public Process(double delay, int channelsNumber, String name) {
        super(delay);
        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
        this.meanLoad = 0.0;
        this.channelsNumber = channelsNumber;

        super.setName(name);

        this.tNextsList = new ArrayList<>();
        for (int i = 0; i < channelsNumber; i++) {
            this.tNextsList.add(Double.MAX_VALUE);
        }
    }
    @Override
    public void inAct() {
        if (super.getWorkingProcessors() < this.channelsNumber) {
            int freeChannel = this.findFreeChannel();

            super.setWorkingProcessors(this.getWorkingProcessors() + 1);

            this.tNextsList.set(freeChannel, super.getTcurr() + super.getDelay());
        } else {
            if (getQueue() < getMaxqueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();

        int channelIdx = this.getFinishedChannel();

        this.tNextsList.set(channelIdx, Double.MAX_VALUE);
        this.setWorkingProcessors(this.getWorkingProcessors() - 1);

        if (this.getQueue() > 0) {
            this.setQueue(this.getQueue() - 1);
            super.setWorkingProcessors(this.getWorkingProcessors() + 1);
            this.tNextsList.set(channelIdx, super.getTcurr() + super.getDelay());
        }

        Process nextProcess = this.findNextProcessInChain();
        if(nextProcess != null) {
            nextProcess.inAct();
        }
    }
    private Process findNextProcessInChain() {
        if(!this.nextElements.isEmpty()) {
            double rand = new Random().nextDouble();
            double cumulativeProbability = 0.0;

            for (int i = 0; i < this.nextElements.size(); i++) {
                cumulativeProbability += this.nextElementsProbabilities.get(i);
                if(rand < cumulativeProbability) {
                    return this.nextElements.get(i);
                }
            }

            return this.nextElements.get(this.nextElements.size() - 1);
        }

        return null;
    }

    public int getFailure() {
        return failure;
    }
    public int getQueue() {
        return queue;
    }
    public void setQueue(int queue) {
        this.queue = queue;
    }
    public int getMaxqueue() {
        return maxqueue;
    }
    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }
    @Override
    public double getTnext() {
        ArrayList<Double> sortedList = new ArrayList<>(tNextsList);
        Collections.sort(sortedList);
        return sortedList.get(0);
    }
    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println(" відмови = " + this.getFailure() + "; }");
    }
    @Override
    public void doStatistics(double delta) {
        this.meanQueue = getMeanQueue() + queue * delta;
        this.meanLoad = this.getMeanLoad() + this.getWorkingProcessors() * delta;
    }
    public double getMeanQueue() {
        return this.meanQueue;
    }
    public double getMeanLoad() {
        return this.meanLoad;
    }
    private int getFinishedChannel() {
        for (int i = 0; i < this.channelsNumber; i++) {
            if(this.tNextsList.get(i) == this.getTcurr()) {
                return i;
            }
        }

        return -1;
    }
    public void setNextElements(ArrayList<Process> processes, ArrayList<Double> probabilities) {
        if(probabilities.stream().mapToDouble(a -> a).sum() != 1.0) {
            throw new RuntimeException("The probabilities must sum into 1");
        }

        this.nextElements = processes;
        this.nextElementsProbabilities = probabilities;
    }

    private int findFreeChannel() {
        for (int i = 0; i < channelsNumber; i++) {
            if(this.tNextsList.get(i) == Double.MAX_VALUE) {
                return i;
            }
        }

        return -1;
    }
}