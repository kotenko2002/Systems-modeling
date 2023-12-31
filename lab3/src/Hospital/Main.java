package Hospital;

import Hospital.Elements.*;
import Hospital.Elements.Processes.Lab;
import Hospital.Elements.Processes.Process;
import Hospital.Elements.Processes.Reception;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) {
        hospitalTask();
    }

    public static void hospitalTask() {
        //Initialization
        Create creator = new Create("CREATOR");
        creator.setExpDistribution(15);

        Reception reception = new Reception("RECEPTION", 2);

        Process room = new Process("ROOM", 3);
        room.setUnifDistribution(3, 8);

        Process walkToRegistry = new Process("WALK_TO_REGISTRY");
        walkToRegistry.setUnifDistribution(2, 5);

        Process registry = new Process("REGISTRY", 3);
        registry.setErlangDistribution(4.5, 3);

        Lab lab = new Lab("LAB", 2);
        lab.setErlangDistribution(4, 2);

        Process walkToReception = new Process("WALK_TO_RECEPTION");
        walkToReception.setUnifDistribution(2, 5);

        Despose despose = new Despose("DESPOSE");

        //Dependency injection
        creator.setNextElement(reception);

        reception.setWalkToRegistry(walkToRegistry);
        reception.setRoom(room);
        room.setNextElement(despose);

        walkToRegistry.setNextElement(registry);
        registry.setNextElement(lab);

        lab.setWalkToReception(walkToReception);
        lab.setDespose(despose);

        walkToReception.setNextElement(reception);

        //Simulation
        Model model = new Model(new ArrayList<>(){{
            add(creator);
            add(reception);
            add(room);
            add(walkToRegistry);
            add(registry);
            add(lab);
            add(walkToReception);
            add(despose);
        }});
        int simulationTime = 10000;
        model.simulate(simulationTime);

        System.out.println("Час, проведений хворим у системі: " + df.format(despose.getAverageTimePatientStayInBank()));
        System.out.println("\tЧас, проведений хворим першого типу системі: " + df.format(despose.getAverageTimePatientStayInBankByType(1)));
        System.out.println("\tЧас, проведений хворим другого типу системі: " + df.format(despose.getAverageTimePatientStayInBankByType(2)));
        System.out.println("\tЧас, проведений хворим третього типу системі: " + df.format(despose.getAverageTimePatientStayInBankByType(3)));

        System.out.println("Інтервал між прибуттями хворих у лабораторію: " + df.format(simulationTime / (double)creator.getQuantity()));
    }
}
