package Bank;

import Bank.Elements.Create;
import Bank.Elements.Element;
import Bank.Elements.Cashier;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Model {
    private final DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<Element> elements;
    private double tNext, tCurrent;
    private int event;

    public Model(ArrayList<Element> elements) {
        this.elements = elements;
        tNext = 0.0;
        tCurrent = tNext;
        event = 0;
    }

    public void simulate(double time) {
        while (tCurrent < time) {
            tNext = Double.MAX_VALUE;
            for (Element e : elements) {
                if (e.getTNext() < tNext) {
                    tNext = e.getTNext();
                    event = e.getId();
                }
            }

            System.out.println("It's time for event in " + elements.get(event).getName() + ", time = " + tNext);
            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }

            tCurrent = tNext;
            for (Element e : elements) {
                e.setTCurrent(tCurrent);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTNext() == tCurrent) {
                    e.outAct();
                }
            }
        }

        printResult();
    }

    public void printResult() {
        System.out.println("\n-----------------------РЕЗУЛЬТАТ-----------------------");

        for (Element e : elements) {
            System.out.println(e.getName() + ": {");

            System.out.println("\tкількість: " + e.getQuantity() + ";");

            if (e instanceof Create) {
                Create c = (Create) e;
                System.out.println("\tпередано на обробку: " + (e.getQuantity() - c.getFailure()) + ";");
                System.out.println("\tвідмови: " + c.getFailure() + ";");
            }
            if (e instanceof Cashier) {
                Cashier c = (Cashier) e;
                System.out.println("\tсереднє завантаження процесора: " + df.format(c.getMeanLoad() / tCurrent) + ";");
                System.out.println("\tсередня довжина черги: " + df.format(c.getMeanQueue() / tCurrent) + ";");
                System.out.println("\tкількість переходів до іншої каси: " + c.getAmountOfSwitchesToAnotherCashier() + ";");
            }
            System.out.println("}\n");
        }
    }
}
