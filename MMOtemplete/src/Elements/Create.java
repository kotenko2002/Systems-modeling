package Elements;

public class Create extends Element {
    public Create(String name, double delay) {
        super(name, delay);
    }

    @Override
    public void outAct() {
        super.outAct();
        tNext = tCurrent + getDelay();
        super.getNextElement().inAct();
    }
}