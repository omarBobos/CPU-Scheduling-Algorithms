package Scheduler;

import Model.Process;
import java.util.ArrayList;
import java.util.List;

public class NonPreemptivePriorityScheduler {

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

            // ignore arrival completely
            for (Process p : list) {

                if (p.arrival <= time && !p.isFinished()) {

                    if (current == null ||
                            p.priority < current.priority ||
                            (p.priority == current.priority && p.arrival < current.arrival)) {

                        current = p;
                    }
                }
            }

            if (current == null) break;

            //  response time (arrival = 0)
            if (current.response == -1) {
                current.response = time;
            }

            // execute full
            while (current.remaining > 0) {
                gantt.add(current.id);
                current.remaining--;
                time++;
            }

            completed++;

            current.completion = time;

            // assume arrival = 0
            current.turnaround = current.completion;
            current.waiting = current.turnaround - current.burst;
        }

        return gantt;
    }
}