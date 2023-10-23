import java.util.ArrayList;

public class Model {
    private ArrayList<Element> elements = new ArrayList<>();
    double tnext, tcurr;
    int event;

    public Model(ArrayList<Element> elements) {
        this.elements = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
    }

    public void simulate(double time) {
        while (tcurr < time) {
            tnext = Double.MAX_VALUE;
            for (Element e : elements) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                    event = e.getId();
                }
            }

            System.out.println("\nНастав час для події в " + elements.get(event).getName() + ", час = " + tnext);

            for (Element e : elements) {
                e.doStatistics(tnext - tcurr);
            }

            tcurr = tnext;

            for (Element e : elements) {
                e.setTcurr(tcurr);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTnext() == tcurr) {
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
            e.printResult();

            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("\tсередня довжина черги: " +
                        p.getMeanQueue() / tcurr
                        + ";\n" + "\tймовірність відмови: " +
                        Math.round((p.getFailure() / (double) (p.getQuantity()+p.getFailure())) * 100 * 10000) / (double)10000 + "%;\n" +
                        "\tсереднє завантаження процесора: " + p.getMeanLoad() / tcurr + ";"
                );
            }
            System.out.println("}\n");
        }
    }
}
