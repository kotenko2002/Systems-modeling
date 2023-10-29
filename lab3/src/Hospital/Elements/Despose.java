package Hospital.Elements;

import Hospital.Other.Patient;

import java.util.ArrayList;

public class Despose extends Element {
    private final ArrayList<Patient> patients;

    public Despose(String name) {
        super(name);
        patients = new ArrayList<>();
        tNext = Integer.MAX_VALUE;
    }

    @Override
    public void inAct(Patient patient) {
        patient.setEndTime(tCurrent);
        patients.add(patient);
    }

    @Override
    public  int getQuantity() {
        return patients.size();
    }

    public double getAverageTimePatientStayInBank() {
        double avgTime = 0;

        for (Patient patient : patients) {
            avgTime += patient.getTimeSpentInSystem();
        }

        return avgTime / patients.size();
    }
}
