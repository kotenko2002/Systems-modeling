package alt;

import alt.Elements.Create;
import alt.Elements.Cashier;
import alt.Elements.Despose;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) {
        bankTask();
    }

    public static void bankTask() {
        Create creator = new Create("CREATOR", 0.5);
        Cashier cashier1 = new Cashier("CASHIER1", 0.3, 3 );
        Cashier cashier2 = new Cashier("CASHIER2", 0.3, 3 );
        Despose despose = new Despose("DESPOSE");

        creator.setPriorityCashier(cashier1);
        creator.setNonPriorityCashier(cashier2);

        cashier1.setNextElement(despose);
        cashier1.setAnotherCashier(cashier2);

        cashier2.setNextElement(despose);
        cashier2.setAnotherCashier(cashier1);

        Model model = new Model(new ArrayList<>(){{add(creator); add(cashier1); add(cashier2); add(despose);}});
        int simulationTime = 10000;
        model.simulate(simulationTime);

        System.out.println("1) Cереднє завантаження" +
                "\n\tкасира №1: " + df.format(cashier1.getMeanLoad() / simulationTime) + ";" +
                "\n\tкасира №2: " + df.format(cashier2.getMeanLoad() / simulationTime) + ";");
        System.out.println("2) Середнє число клієнтів у банку: " +
                df.format((cashier1.getMeanLoad() + cashier1.getMeanQueue() + cashier1.getMeanLoad() + cashier1.getMeanQueue()) / simulationTime));
        System.out.println("3) " + df.format(simulationTime / (double)creator.getQuantity()));
        System.out.println("4) Середній час перебування клієнта в банку: " + df.format(despose.getAverageTimeClientStayInBank()) + "мс;");
        System.out.println("5) Cереднє число клієнтів у" +
                "\n\tчерзі №1: " + df.format(cashier1.getMeanQueue() / simulationTime) + ";" +
                "\n\tчерзі №2: " + df.format(cashier2.getMeanQueue() / simulationTime) + ";");
        System.out.println("6) Відсоток клієнтів, яким відмовлено в обслуговуванні: " +
                df.format((creator.getFailure() / (double)creator.getQuantity()) * 100) + "%;");
        System.out.println("7) Число змін під'їзних смуг" +
                "\n\tз каси №1 до каси №2: " + cashier1.getAmountOfSwitchesToAnotherCashier() + ";" +
                "\n\tз каси №2 до каси №1: " + cashier2.getAmountOfSwitchesToAnotherCashier() + ";");
    }
}