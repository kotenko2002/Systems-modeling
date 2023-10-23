import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        task2();
    }

    public static void task1() {
        Create creator = new Create(1, "CREATOR");
        Process processor = new Process(1, 1, "PROCESSOR");

        creator.setDistribution("exp");
        creator.setNextElement(processor);

        processor.setDistribution("exp");
        processor.setMaxqueue(5);

        Model model = new Model(new ArrayList<>(){{add(creator); add(processor);}});
        model.simulate(10000);
    }

    public static void task2() {
        Create creator = new Create(1, "CREATOR");
        Process processor1 = new Process(1, 1, "PROCESSOR1");
        Process processor2 = new Process(1, 1, "PROCESSOR2");
        Process processor3 = new Process(1, 1, "PROCESSOR3");

        creator.setDistribution("exp");
        creator.setNextElement(processor1);

        processor1.setDistribution("exp");
        processor1.setMaxqueue(5);
        processor1.setNextElements(new ArrayList<>(){{add(processor2);}}, new ArrayList<>(){{add(1.0);}});

        processor2.setDistribution("exp");
        processor2.setMaxqueue(5);
        processor2.setNextElements(new ArrayList<>(){{add(processor3);}}, new ArrayList<>(){{add(1.0);}});

        processor3.setDistribution("exp");
        processor3.setMaxqueue(5);

        Model model = new Model(new ArrayList<>(){{add(creator); add(processor1); add(processor2); add(processor3);}});
        model.simulate(10000);
    }

    public static void task3() {
        Create creator = new Create(1, "CREATOR");
        Process processor1 = new Process(1, 1, "PROCESSOR1");
        Process processor2 = new Process(0.5, 1, "PROCESSOR2");
        Process processor3 = new Process(0.3, 1, "PROCESSOR3");

        creator.setDistribution("exp");
        creator.setNextElement(processor1);

        processor1.setDistribution("exp");
        processor1.setMaxqueue(5);
        processor1.setNextElements(new ArrayList<>(){{add(processor2);}},new ArrayList<>(){{add(1.0);}});

        processor2.setDistribution("exp");
        processor2.setMaxqueue(5);
        processor2.setNextElements(new ArrayList<>(){{add(processor3); add(processor1);}}, new ArrayList<>(){{add(0.5); add(0.5);}});

        processor3.setDistribution("exp");
        processor3.setMaxqueue(5);

        Model model = new Model(new ArrayList<>(){{add(creator); add(processor1); add(processor2); add(processor3);}});
        model.simulate(10000);
    }
}
