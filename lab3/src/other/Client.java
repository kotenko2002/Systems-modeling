package other;

public class Client {
    private double startTime, endTime;
    private int type;

    public Client(double startTime, int type) {
        this.startTime = startTime;
        this.type = type;
        this.endTime = 0.0;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
