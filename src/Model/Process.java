package Model;

public class Process {

    public String id;

    public int arrival;
    public int burst;
    public int priority;

    public int remaining;
    public int completion;
    public int waiting;
    public int turnaround;
    public int response;

    public Process(String id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;

        reset();
    }

    public void reset() {
        this.remaining = burst;
        this.completion = 0;
        this.waiting = 0;
        this.turnaround = 0;
        this.response = -1;
    }

    public Process copy() {
        return new Process(id, arrival, burst, priority);
    }

    public boolean isFinished() {
        return remaining == 0;
    }
}