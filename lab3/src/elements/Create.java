package elements;

import javafx.util.Pair;
import other.Client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Create extends Element {
    protected Client nextClient;
    private ArrayList<Element> nextElements = null;
    private ArrayList<Double> nextElementsProbs = null;
    private PriorityQueue<Pair<Process, Integer>> nextProcessesPrioritised = null;

    protected ArrayList<Integer> types = new ArrayList<>();
    protected ArrayList<Double> probsOfTypes = new ArrayList<>();

    public Create(double delay, String type) {
        super(delay);
        super.setTnext(0.0);

        nextClient = new Client(0.0, 1);

        nextElements = new ArrayList<>();
        if (type == "probs") {
            nextElementsProbs = new ArrayList<>();
        } else if (type == "priors"){
            nextProcessesPrioritised = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        }
    }

    public Create(double delay, String type, HashMap<Integer, Double> probsOfTypes) {
        super(delay);
        super.setTnext(0.0);

        probsOfTypes.forEach((t, p) -> {
            this.types.add(t);
            this.probsOfTypes.add(p);
        });

        double prob = Math.random() * getSumOfTypeProbs();
        double cumSum = 0.0;
        for (int i = 0; i < probsOfTypes.size(); i++) {
            if (prob <= cumSum + this.probsOfTypes.get(i)) {
                nextClient = new Client(0.0, types.get(i));
                break;
            }
            cumSum += this.probsOfTypes.get(i);
        }

        nextElements = new ArrayList<>();
        if (type.equals("probs")) {
            nextElementsProbs = new ArrayList<>();
        } else if (type.equals("priors")){
            nextProcessesPrioritised = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        }
    }


    @Override
    public void outAct() {
        super.outAct();
        if (nextElements.size() != 0) {
            if (nextElementsProbs != null) {
                double prob = Math.random() * getSumOfProbs();
                double cumSum = 0.0;
                for (int i = 0; i < nextElementsProbs.size(); i++) {
                    if (prob <= cumSum + nextElementsProbs.get(i)) {
                        Element element = nextElements.get(i);
                        if (element instanceof Process) {
                            ((Process)element).inAct(nextClient);
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
                toGo.inAct(nextClient);
            }
        }

        Client client = getNextClient();

        super.setTnext(client.getStartTime());
        if (probsOfTypes.size() > 0) {

            double prob = Math.random() * getSumOfTypeProbs();
            double cumSum = 0.0;
            for (int i = 0; i < probsOfTypes.size(); i++) {
                if (prob <= cumSum + probsOfTypes.get(i)) {
                    client.setType(types.get(i));
                    break;
                }
                cumSum += probsOfTypes.get(i);
            }
        }
        nextClient = client;

    }

    public double getSumOfProbs() {
        double sum = 0.0;
        for (int i = 0; i < nextElementsProbs.size(); i++) {
            sum += nextElementsProbs.get(i);
        }
        return sum;
    }

    public double getSumOfTypeProbs() {
        double sum = 0.0;
        for (int i = 0; i < probsOfTypes.size(); i++) {
            sum += probsOfTypes.get(i);
        }
        return sum;
    }

    public void addNext(Element e, Double prob) {
        nextElements.add(e);
        nextElementsProbs.add(prob);
    }

    public void addNext(Process p, Integer priority) {
        nextElements.add(p);
        nextProcessesPrioritised.add(new Pair<>(p, priority));
    }

    public Client getNextClient() {
        double time = getTcurr() + super.getDelay();
        return new Client(time, 1);
    }

}
