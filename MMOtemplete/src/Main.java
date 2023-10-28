import Elements.Create;
import Elements.Process;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        task3();
    }

    public static void task3() {
        Create creator = new Create("CREATOR", 1);
        Process processor1 = new Process("PROCESSOR1", 1, 1 );
        Process processor2 = new Process("PROCESSOR2", 1, 1 );
        Process processor3 = new Process("PROCESSOR3", 1, 1 );

        creator.setDistribution("exp");
        creator.setNextElement(processor1);

        processor1.setDistribution("exp");
        processor1.setMaxqueue(5);
        processor1.setNextElements(new ArrayList<>(){{add(processor2);}}, new ArrayList<>(){{add(1.0);}});

        processor2.setDistribution("exp");
        processor2.setMaxqueue(5);
        processor2.setNextElements(new ArrayList<>(){{add(processor3); add(processor1);}}, new ArrayList<>(){{add(0.5); add(0.5);}});

        processor3.setDistribution("exp");
        processor3.setMaxqueue(5);

        Model model = new Model(new ArrayList<>(){{add(creator); add(processor1); add(processor2); add(processor3);}});
        model.simulate(10000);
    }
}