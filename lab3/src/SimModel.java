import bank.Cashier;

import elements.Create;
import elements.Despose;
import elements.Element;
import elements.Process;

import hospital.Reception;
import hospital.Lab;
import other.Client;
import other.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SimModel {
    public static void main(String[] args) {
        hospitalTask();
    }

    public static void task1() {
        Create c = new Create(1.0, "asd");
        Process p1 = new Process(1.0, "probs");
        Process p2 = new Process(1.0, "priors");
        Process p3 = new Process(1.0, "probs");
        Despose d = new Despose();
        System.out.println("id0 = " + c.getId() + " id1=" +
                p1.getId());
        c.setNextElement(p1);
        p1.addNext(p2, 1.0);
        p2.addNext(p3, 1);
        p2.addNext(p1, 2);
        p3.addNext(d, 0.6);
        p3.addNext(p2, 0.2);
        p3.addNext(p1, 0.2);
        p1.setMaxQueue(5);
        p2.setMaxQueue(5);
        p3.setMaxQueue(5);

        p1.setMaxWorkers(1);
        p2.setMaxWorkers(1);
        p3.setMaxWorkers(1);
        c.setName("CREATOR");
        p1.setName("PROCESSOR1");
        p2.setName("PROCESSOR2");
        p3.setName("PROCESSOR3");
        c.setDistribution("exp");
        p1.setDistribution("exp");
        p2.setDistribution("exp");
        p3.setDistribution("exp");
        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(p1);
        list.add(p2);
        list.add(p3);

        Model model = new Model(list);
        model.simulate(500.0);
    }
    public static void bankTask() {
        Create create = new Create(0.5, "priors");
        Cashier cashier1 = new Cashier(1, "probs");
        Cashier cashier2 = new Cashier(1, "probs");
        Despose despose = new Despose();

        create.setDistribution("exp");
        create.addNext(cashier1, 1);
        create.addNext(cashier2, 2);

        cashier1.setDelayDev(0.3);
        cashier1.addLane(cashier2);
        cashier1.setMaxQueue(3);
        cashier1.setMaxWorkers(1);
        cashier1.addNext(despose, 1.0);
        cashier1.inAct(new Client(0.0, 1));
        cashier1.inAct(new Client(0.0, 1));
        cashier1.inAct(new Client(0.0, 1));

        cashier2.setDelayDev(0.3);
        cashier2.addLane(cashier1);
        cashier2.setMaxQueue(3);
        cashier2.setMaxWorkers(1);
        cashier2.addNext(despose, 1.0);
        cashier2.inAct(new Client(0.0, 1));
        cashier2.inAct(new Client(0.0, 1));
        cashier2.inAct(new Client(0.0, 1));

        ArrayList<Element> list = new ArrayList<>(){{add(create); add(cashier1); add(cashier2); add(despose);}};
        Model model = new Model(list);
        double time = 1000.0;
        model.simulate(time);

        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Cashier) {
                Cashier c = (Cashier) e;
                System.out.println("Cashier #" + c.getId());
                System.out.println("1) " + c.getStateSum() / time);
                System.out.println("2) " + (c.getMeanQueue() + c.getStateSum()) / time);
                System.out.println("3) " + time / c.getQuantity());
                System.out.println("4) " + (c.getMeanQueue() / time + 2) * (time - c.getWaitTime()) / c.getQuantity());
                System.out.println("5) " + c.getMeanQueue() / time);
                System.out.println("6) " + c.getFailure() / (double) (c.getFailure() + c.getQuantity()));
                System.out.println("7) " + c.getNumOfSwaps());
            } else if (e instanceof Despose) {
                Despose d = (Despose) e;
                System.out.println("4 from Despose)   " + d.getMeanTime() / d.getQuantity());
                System.out.println("Quantity = " + d.getQuantity());
            }
        }
    }
    public static void hospitalTask() {
        HashMap<Integer, Double> probs = new HashMap<Integer, Double>() {{ put(1, 0.5); put(2, 0.1); put(3, 0.4); }};

        Create patientCreate = new Create(15.0, "probs", probs);
        patientCreate.setDistribution("exp");
        patientCreate.setName("create");

        HashMap<Integer, Integer> priorities = new HashMap<>() {{ put(1, 1); put(2, 2); put(3, 2); }};
        Reception reception = new Reception(priorities);
        reception.setName("reception");

        Process room = new Process(3.0, "probs");
        room.setName("room");
        room.setDistribution("unif");
        room.setDelayDev(8.0);
        room.setMaxWorkers(3);

        Process walkToRegistry = new Process(2.0, "probs");
        walkToRegistry.setName("walk1");
        walkToRegistry.setDistribution("unif");
        walkToRegistry.setDelayDev(5.0);

        Process registry = new Process(4.5, "probs");
        registry.setName("registry");
        registry.setDistribution("erlang");
        registry.setK(3);
        registry.setMaxWorkers(1);

        Lab lab = new Lab(4.0);
        lab.setName("lab");
        lab.setDistribution("erlang");
        lab.setK(2);
        lab.setMaxWorkers(2);

        Process walkToReception = new Process(2.0, "probs");
        walkToReception.setName("walk2");
        walkToReception.setDistribution("unif");
        walkToReception.setDelayDev(5.0);

        Despose despose = new Despose();
        despose.setName("despose");

        patientCreate.addNext(reception, 1.0);

        reception.setMaxWorkers(2);
        reception.setRoom(room);
        reception.setWalkToRegistry(walkToRegistry);

        room.addNext(despose, 1.0);

        walkToRegistry.addNext(registry, 1.0);

        registry.addNext(lab, 1.0);

        lab.setDespose(despose);
        lab.setWalkToReception(walkToReception);

        walkToReception.addNext(reception, 1.0);

        ArrayList<Element> list = new ArrayList<>() {
            {
                add(patientCreate);
                add(reception);
                add(room);
                add(walkToRegistry);
                add(registry);
                add(lab);
                add(walkToReception);
                add(despose);
            }
        };
        Model model = new Model(list);
        double time = 10_000.0;
        model.simulate(time);

        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Despose) {
                Despose d = (Despose) e;
                System.out.println("Mean time in hospital = " + (d.getMeanTime() / d.getQuantity()));
            }
        }
    }

    public static void lab4() {
        for (int N = 100; N < 1001; N+= 100) {
            Element.refreshNextId();
            Create create = new Create(10.0, "probs");
            create.setDistribution("exp");
            create.setName("create");

            ArrayList<Element> list = new ArrayList<>();
            list.add(create);

            Process prev = new Process(10.0, "probs");
            prev.setDistribution("exp");
            prev.setMaxWorkers(1);
            create.addNext(prev, 1.0);
            list.add(prev);

            for (int i = 0; i < N; i++) {
                Process p = new Process(10.0, "probs");
                p.setDistribution("exp");
                p.setMaxWorkers(1);
                prev.addNext(p, 1.0);
                list.add(p);
                prev = p;
            }

            for (int i = 1; i < list.size(); i++) {
                ((Process)list.get(i)).inAct(new Client(0.0, 1));
            }

            Model model = new Model(list);
            double time = 10_000.0;
            long startTime = System.currentTimeMillis();
            model.simulate(time);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) / 1_000.0);
        }

        for (int N = 100; N < 1001; N+= 100) {
            Element.refreshNextId();
            Create create = new Create(10.0, "probs");
            create.setDistribution("exp");
            create.setName("create");

            ArrayList<Element> list = new ArrayList<>();
            list.add(create);

            Process p = new Process(10.0, "probs");
            p.setDistribution("exp");
            p.setMaxWorkers(1);
            create.addNext(p, 1.0);
            list.add(p);

            for (int i = 0; i < N; i++) {
                p = new Process(10.0, "probs");
                p.setDistribution("exp");
                p.setMaxWorkers(1);
                for (int j = 1; j < list.size(); j++) {
                    ((Process)list.get(j)).addNext(p, 1.0);
                }
                list.add(p);
            }

            for (int i = 1; i < list.size(); i++) {
                ((Process)list.get(i)).inAct(new Client(0.0, 1));
            }

            Model model = new Model(list);
            double time = 10_000.0;
            long startTime = System.currentTimeMillis();
            model.simulate(time);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) / 1_000.0);
        }
    }
}