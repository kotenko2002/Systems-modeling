package Bank.Elements;

import Bank.Other.Client;

import java.util.ArrayList;

public class Despose extends Element {
    private final ArrayList<Client> clients;
    public Despose(String name) {
        super(name);
        clients = new ArrayList<>();
        tNext = Double.MAX_VALUE;
    }

    @Override
    public void inAct(Client client) {
        client.setEndTime(tCurrent);
        clients.add(client);
    }

    @Override
    public  int getQuantity() {
        return clients.size();
    }

    public double getAverageTimeClientStayInBank() {
        double avgTime = 0;

        for (Client client : clients) {
            avgTime += client.getTimeSpentInSystem();
        }

        return avgTime / clients.size();
    }
}
