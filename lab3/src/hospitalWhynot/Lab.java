package hospitalWhynot;

import javafx.util.Pair;
import other.Client;
import elements.Process;
import elements.Despose;

public class Lab extends Process {
    private Despose despose;
    private Process walkToReception;

    public Lab(double delay) {
        super(delay, "");
    }

    public void setDespose(Despose despose) {
        this.despose = despose;
    }

    public void setWalkToReception(Process walkToReception) {
        this.walkToReception = walkToReception;
    }

    @Override
    public void outAct() {
        incrQuantity();
        Client client = nextClients.poll().getValue();
        if (nextClients.peek() != null) {
            super.setTnext(nextClients.peek().getKey());
        } else {
            super.setTnext(Double.MAX_VALUE);
        }
        super.setState(super.getState() - 1);

        if (client.getType() == 2) {
            client.setType(1);
            walkToReception.inAct(client);
        } else {
            despose.inAct(client);
        }

        if (getQueue() > 0) {
            client = queue.remove(0);
            super.setState(super.getState() + 1);
            nextClients.add(new Pair<>(super.getTcurr() + this.getDelay(client), client));
            super.setTnext(nextClients.peek().getKey());
        }
    }
}
