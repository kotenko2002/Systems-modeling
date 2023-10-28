package elements;

import javafx.util.Pair;
import other.Client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Process extends Element {
    protected int maxQueue, failure, maxWorkers;
    protected final PriorityQueue<Pair<Double, Client>> nextClients;
    private ArrayList<Element> nextElements = null;
    private ArrayList<Double> nextElementsProbs = null;
    private PriorityQueue<Pair<Process, Integer>> nextProcessesPrioritised = null;
    protected ArrayList<Client> queue = new ArrayList<>();
    protected HashMap<Integer, Integer> prioritiesOfQueue = null;
    private double meanQueue;


    public Process(double delay, String type) {
        super(delay);
        super.setTnext(Double.MAX_VALUE);
        nextClients = new PriorityQueue<>(Comparator.comparingDouble(Pair::getKey));
        nextElements = new ArrayList<>();
        if (type == "probs") {
            nextElementsProbs = new ArrayList<>();
        } else if (type == "priors") {
            nextProcessesPrioritised = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        }
        maxQueue = Integer.MAX_VALUE;
        maxWorkers = Integer.MAX_VALUE;
        meanQueue = 0.0;
    }


    public Process(double delay, String type, HashMap<Integer, Integer> prioritiesOfQueue) {
        super(delay);
        super.setTnext(Double.MAX_VALUE);
        nextClients = new PriorityQueue<>(Comparator.comparingDouble(Pair::getKey));
        nextElements = new ArrayList<>();
        if (type.equals("probs")) {
            nextElementsProbs = new ArrayList<>();
        } else if (type.equals("priors")) {
            nextProcessesPrioritised = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        }
        this.prioritiesOfQueue = prioritiesOfQueue;
        maxQueue = Integer.MAX_VALUE;
        maxWorkers = Integer.MAX_VALUE;
        meanQueue = 0.0;
    }


    public void inAct(Client client) {
        if (super.getState() < maxWorkers) {
            super.setState(super.getState() + 1);
            nextClients.add(new Pair<>(super.getTcurr() + this.getDelay(client), client));
            super.setTnext(nextClients.peek().getKey());
        } else {
            if (getQueue() < getMaxQueue()) {
                queue.add(client);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        Client client = nextClients.poll().getValue();
        if (nextClients.peek() != null) {
            super.setTnext(nextClients.peek().getKey());
        } else {
            super.setTnext(Double.MAX_VALUE);
        }
        super.setState(super.getState() - 1);

        if (nextElements.size() != 0) {
            if (nextElementsProbs != null) {
                double prob = Math.random() * getSumOfProbs();
                double cumSum = 0.0;
                for (int i = 0; i < nextElementsProbs.size(); i++) {
                    if (prob <= cumSum + nextElementsProbs.get(i)) {
                        Element element = nextElements.get(i);
                        if (element instanceof Process) {
                            ((Process) element).inAct(client);
                        } else if (element instanceof Despose) {
                            ((Despose) element).inAct(client);
                        } else {
                            element.inAct();
                        }
                        break;
                    }
                    cumSum += nextElementsProbs.get(i);
                }
            } else {
                int maxQueue = nextProcessesPrioritised.peek().getKey().getQueue();
                Process toGo = nextProcessesPrioritised.peek().getKey();
                for (Pair<Process, Integer> pair : nextProcessesPrioritised) {
                    Process p = pair.getKey();
                    if (p.getQueue() < maxQueue && p.getQueue() < p.getMaxQueue()) {
                        maxQueue = p.getQueue();
                        toGo = p;
                    }
                }
                toGo.inAct(client);
            }
        }

        if (getQueue() > 0) {
            if (prioritiesOfQueue != null) {
                Comparator<Client> comparator = Comparator.comparingInt(o -> prioritiesOfQueue.get(o.getType()));
                queue.sort(comparator);
            }
            client = queue.remove(0);
            super.setState(super.getState() + 1);
            nextClients.add(new Pair<>(super.getTcurr() + this.getDelay(client), client));
            super.setTnext(nextClients.peek().getKey());
        }
    }

    public void addNext(Element e, Double prob) {
        nextElements.add(e);
        nextElementsProbs.add(prob);
    }

    public void addNext(Process e, Integer priority) {
        nextElements.add(e);
        nextProcessesPrioritised.add(new Pair<>(e, priority));
    }


    public double getDelay(Client client) {
        return super.getDelay();
    }

    public double getSumOfProbs() {
        double sum = 0.0;
        for (int i = 0; i < nextElementsProbs.size(); i++) {
            sum += nextElementsProbs.get(i);
        }
        return sum;
    }

    public int getFailure() {
        return failure;
    }

    public int getQueue() {
        return queue.size();
    }


    public int getMaxQueue() {
        return maxQueue;
    }

    public void setMaxQueue(int maxqueue) {
        this.maxQueue = maxqueue;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("queue = " + this.getQueue());
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + getQueue() * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}
