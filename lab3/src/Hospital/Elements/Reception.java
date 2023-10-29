package Hospital.Elements;

import Hospital.Other.Patient;

public class Reception extends Process {
    private Process room;
    private Process walkToRegistry;

    public Reception(String name, int workersAmount) {
        super(name, workersAmount);
    }

    @Override
    public double getDelay(Patient patient) {
        if(patient == null) {
            throw new RuntimeException();
        }
        switch (patient.getType()) {
            case 1:
                return 15;
            case 2:
                return 40;
            case 3:
                return 30;
        }

        throw new RuntimeException("Пацієнт неіснуючого типу");
    }

    public void setRoom(Process room) {
        this.room = room;
    }
    public void setWalkToRegistry(Process walkToRegistry) {
        this.walkToRegistry = walkToRegistry;
    }

    @Override
    protected Patient getPatientFromQueue() {
        for (Patient patient : queue) {
            if (patient.getType() == 1) {
                queue.remove(patient);
                return patient;
            }
        }

        return queue.poll();
    }
    @Override
    protected void goToNextElement(Patient patient) {
        if (patient.getType() == 1) {
            room.inAct(patient);
        } else {
            walkToRegistry.inAct(patient);
        }
    }
}
