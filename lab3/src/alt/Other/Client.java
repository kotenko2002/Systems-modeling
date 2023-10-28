package alt.Other;

public class Client {
    private double startTime, endTime;

    public Client(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double getTimeSpentInSystem() {
        return endTime - startTime;
    }
}
