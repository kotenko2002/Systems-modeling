import Elements.Create;
import Elements.Element;
import Elements.Process;
import Other.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        for (int N = 100; N < 1001; N+=100) {
            task(1000, 100, N, 10);
        }
    }

    public static void task(double simulationTime, int iterations, int N, int delay) {
        long avgTimeSim1 = 0;
        for (int oi = 1; oi < iterations; oi++) {
            Element.resetNextId();
            Create creator = new Create("CREATOR", delay);

            Process prevProcessor = new Process("PROCESSOR0", delay);
            creator.setNextElement(prevProcessor);

            ArrayList<Process> listOfProcesses = new ArrayList<>();
            listOfProcesses.add(prevProcessor);

            for (int i = 1; i < N; i++) {
                Process process = new Process("PROCESSOR" + i, delay);
                listOfProcesses.add(process);
                prevProcessor.addElement(new Pair(process, 1));

                prevProcessor = process;
            }
            Model model = new Model(new ArrayList<>(){{ add(creator); addAll(listOfProcesses); }});
            avgTimeSim1 += measureTime(() -> model.simulate(simulationTime));
        }

        long avgTimeSim2 = 0;
        for (int oi = 1; oi < iterations; oi++) {
            Element.resetNextId();
            Create creator = new Create("CREATOR", delay);

            Process prevProcessor = new Process("PROCESSOR0", delay);
            creator.setNextElement(prevProcessor);

            ArrayList<Process> listOfProcesses = new ArrayList<>();
            listOfProcesses.add(prevProcessor);

            for (int i = 1; i < N; i++) {
                Process process = new Process("PROCESSOR" + i, delay);
                listOfProcesses.add(process);
                prevProcessor.addElement(new Pair(process, 1));

                prevProcessor = process;
            }

            for (int i = 0; i < listOfProcesses.size(); i++) {
                Process process = listOfProcesses.get(i);

                for (int j = i+1; j < listOfProcesses.size(); j++) {
                    process.addElement(new Pair(listOfProcesses.get(j), 1));
                }
            }

            Model model = new Model(new ArrayList<>(){{ add(creator); addAll(listOfProcesses); }});
            avgTimeSim2 += measureTime(() -> model.simulate(simulationTime));
        }

        System.out.println("При N=" + N);
        System.out.println("Час симуляції №1: " + (avgTimeSim1 /(double)iterations));
        System.out.println("Час симуляції №2: " + (avgTimeSim2 /(double)iterations));
        System.out.println("----------------------------------");
    }

    private static long measureTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}