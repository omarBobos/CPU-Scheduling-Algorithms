package Metrics;

import Model.Process;
import java.util.List;

public class MetricsCalc {

    public static String generate(List<Process> list, String title, String gantt) {

        StringBuilder result = new StringBuilder();

        result.append("========== ").append(title).append(" ==========\n");
        result.append("Gantt Chart:\n").append(gantt).append("\n\n");

        if (list == null || list.isEmpty()) {
            result.append("No processes to display.\n");
            return result.toString();
        }

        result.append(String.format("%-10s%-10s%-10s%-10s\n",
                "PID", "WT", "TAT", "RT"));

        for (Process p : list) {

            result.append(String.format("%-10s%-10d%-10d%-10d\n",
                    p.id, p.waiting, p.turnaround, p.response));
        }

        double[] avg = getAverages(list);

        result.append("\n---------------------------------\n");
        result.append(String.format("Average WT  = %.2f\n", avg[0]));
        result.append(String.format("Average TAT = %.2f\n", avg[1]));
        result.append(String.format("Average RT  = %.2f\n", avg[2]));

        return result.toString();
    }

    public static double[] getAverages(List<Process> list) {

        double avgWT = 0, avgTAT = 0, avgRT = 0;

        for (Process p : list) {
            avgWT += p.waiting;
            avgTAT += p.turnaround;
            avgRT += p.response;
        }

        int n = list.size();

        return new double[]{
                avgWT / n,
                avgTAT / n,
                avgRT / n
        };
    }
}
