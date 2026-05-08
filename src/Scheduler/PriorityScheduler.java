package Scheduler;

import Model.Process;
import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {

    public static List<String> run(List<Process> list) {

        for (Process p : list) {
            p.reset();
        }

        int time = 0;
        int completed = 0;
        int n = list.size();

        List<String> gantt = new ArrayList<>();

        while (completed < n) {

            Process current = null;

            for (Process p : list) {

                if (p.arrival <= time && !p.isFinished()) {

                    if (current == null ||
                            p.priority < current.priority ||
                            (p.priority == current.priority && p.arrival < current.arrival)) {

                        current = p;
                    }
                }
            }

            if (current == null) {
                gantt.add("Idle");
                time++;
                continue;
            }

            if (current.response == -1) {
                current.response = time - current.arrival;
            }

            gantt.add(current.id);

            current.remaining--;
            time++;

            if (current.isFinished()) {

                completed++;

                current.completion = time;

                current.turnaround = current.completion - current.arrival;

                current.waiting = current.turnaround - current.burst;
            }
        }

        return gantt;
    }
}