public class Create extends Element {
    public Create(double delay, String name) {
        super(delay);
        super.setName(name);
        super.setTnext(0.0);
    }
    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());
        super.getNextElement().inAct();
    }
}