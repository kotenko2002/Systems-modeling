import java.text.DecimalFormat;
import java.util.ArrayList;

import Elements.Element;
import Elements.Process;

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
                if (e.getTnext() < tNext) {
                    tNext = e.getTnext();
                    event = e.getId();
                }
            }

            System.out.println("\nНастав час для події в " + elements.get(event).getName() + ", час = " + df.format(tNext));

            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }

            tCurrent = tNext;

            for (Element e : elements) {
                e.setTcurr(tCurrent);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTnext() == tCurrent) {
                    e.outAct();
                }
            }

            printInfo();
        }
        printResult();
    }
    public void printInfo() {
        for (Element e : elements) {
            e.printInfo();
        }
    }
    public void printResult() {
        System.out.println("\n-----------------------РЕЗУЛЬТАТ-----------------------");

        for (Element e : elements) {
            System.out.println(e.getName() + ": {");

            System.out.println("\tкількість: " + e.getQuantity() + ";");

            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("\tсередня довжина черги: " +
                    p.getMeanQueue() / tCurrent
                    + ";\n" + "\tймовірність відмови: " +
                    Math.round((p.getFailure() / (double) (p.getQuantity()+p.getFailure())) * 100 * 10000) / (double)10000 + "%;\n" +
                    "\tсереднє завантаження процесора: " + p.getMeanLoad() / tCurrent + ";"
                );
            }
            System.out.println("}\n");
        }
    }
}
