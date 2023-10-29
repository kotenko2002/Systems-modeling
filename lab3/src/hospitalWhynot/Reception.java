package hospitalWhynot;

import javafx.util.Pair;
import other.Client;
import elements.Process;

import java.util.Comparator;
import java.util.HashMap;

public class Reception extends Process {
    private Process room;
    private Process walkToRegistry;

    public Reception(HashMap<Integer, Integer> prioritiesOfQueue) {
        super(0.0, "", prioritiesOfQueue);
        setName("Reception" + getId());
    }

    public void setRoom(Process room) {
        this.room = room;
    }

    public void setWalkToRegistry(Process walkToRegistry) {
        this.walkToRegistry = walkToRegistry;
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

        if (client.getType() == 1) {
            room.inAct(client);
        } else {
            walkToRegistry.inAct(client);
        }

        if (getQueue() > 0) {
            Comparator<Client> comparator = Comparator.comparingInt(o -> prioritiesOfQueue.get(o.getType()));
            queue.sort(comparator);
            client = queue.remove(0);
            super.setState(super.getState() + 1);
            nextClients.add(new Pair<>(super.getTcurr() + this.getDelay(client), client));
            super.setTnext(nextClients.peek().getKey());
        }
    }

    @Override
    public double getDelay(Client client) {
        int type = client.getType();
        if (type == 1) {
            return 15;
        } else if (type == 2) {
            return 40;
        } else {
            return 30;
        }
    }
}
