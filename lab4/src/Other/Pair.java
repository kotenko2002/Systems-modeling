package Other;

import Elements.Element;

public class Pair {
    private Element element;
    private double probability;

    public Pair(Element element, double probability) {
        this.element = element;
        this.probability = probability;
    }

    public Element getElement() {
        return element;
    }
    public double getProbability() {
        return probability;
    }
}
