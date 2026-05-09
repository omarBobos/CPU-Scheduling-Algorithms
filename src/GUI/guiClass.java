package GUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Metrics.MetricsCalc;
import Model.Process;
import Scheduler.NonPreemptiveSJF;
import Scheduler.SJFScheduler;
import Scheduler.PriorityScheduler;
import Scheduler.NonPreemptivePriorityScheduler;

public class guiClass extends JFrame {

    JTextField txtId, txtArrival, txtBurst, txtPriority;
    JTable table;
    DefaultTableModel model;
    JTextArea area;

    public guiClass() {

        setTitle("Scheduling Algorithms");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ================= TOP PANEL =================
        JPanel top = new JPanel(new GridLayout(2, 5));

        txtId = new JTextField();
        txtArrival = new JTextField();
        txtBurst = new JTextField();
        txtPriority = new JTextField();

        top.add(new JLabel("PID"));
        top.add(new JLabel("Arrival"));
        top.add(new JLabel("Burst"));
        top.add(new JLabel("Priority"));
        top.add(new JLabel(""));

        top.add(txtId);
        top.add(txtArrival);
        top.add(txtBurst);
        top.add(txtPriority);

        JButton btnAdd = new JButton("Add");
        top.add(btnAdd);

        add(top, BorderLayout.NORTH);

        // TABLE ///
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"PID", "Arrival", "Burst", "Priority"});

        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);

        // TEXT AREA 
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane textScroll = new JScrollPane(area);
        textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // SPLIT PANE 
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, textScroll);
        split.setDividerLocation(300);

        add(split, BorderLayout.CENTER);

        //  BUTTONS 
        JPanel buttons = new JPanel();

        JButton btnRun = new JButton("Run");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        buttons.add(btnRun);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        add(buttons, BorderLayout.SOUTH);

        // ACTIONS//
        btnAdd.addActionListener(e -> addProcess());
        btnDelete.addActionListener(e -> deleteRow());
        btnClear.addActionListener(e -> clearAll());
        btnRun.addActionListener(e -> runAlgorithms());
    }

    // ADD PROCESS //
    void addProcess() {

        try {
            String id = txtId.getText().trim();
            String atText = txtArrival.getText().trim();
            String btText = txtBurst.getText().trim();
            String prText = txtPriority.getText().trim();

            // Empty Fields
            if (id.isEmpty() || atText.isEmpty() || btText.isEmpty() || prText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please complete all input fields before adding the process.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int arrival = Integer.parseInt(atText);
            int burst = Integer.parseInt(btText);
            int priority = Integer.parseInt(prText);

            // Negative values
            if (arrival < 0 || burst <= 0 || priority < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid input:\n"
                                + "- Arrival Time cannot be negative\n"
                                + "- Burst Time must be greater than 0\n"
                                + "- Priority cannot be negative",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            for (int i = 0; i < model.getRowCount(); i++) {

                if (model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Process ID \"" + id + "\" already exists.\n"
                                    + "Please enter a unique Process ID.",
                            "Duplicate Process",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            model.addRow(new Object[]{id, arrival, burst, priority});

            txtId.setText("");
            txtArrival.setText("");
            txtBurst.setText("");
            txtPriority.setText("");

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Arrival / Burst / Priority must be numbers.");
        }
    }

    // DELETE
    void deleteRow() {
        int row = table.getSelectedRow();
        if (row != -1)
            model.removeRow(row);
    }

    //  CLEAR
    void clearAll() {
        model.setRowCount(0);
        area.setText("");
    }

   
    List<Process> getProcesses() {

        List<Process> list = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {

            list.add(new Process(
                    model.getValueAt(i, 0).toString(),
                    Integer.parseInt(model.getValueAt(i, 1).toString()),
                    Integer.parseInt(model.getValueAt(i, 2).toString()),
                    Integer.parseInt(model.getValueAt(i, 3).toString())
            ));
        }

        return list;
    }

  
    void runAlgorithms() {

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Add processes first");
            return;
        }

        List<Process> original = getProcesses();

        List<Process> sjf = new ArrayList<>();
        List<Process> pri = new ArrayList<>();
        List<Process> nonPri = new ArrayList<>();
        List<Process> nonSJF = new ArrayList<>();

        for (Process p : original) {
            sjf.add(new Process(p.id, p.arrival, p.burst, p.priority));
            pri.add(new Process(p.id, p.arrival, p.burst, p.priority));
            nonPri.add(new Process(p.id, p.arrival, p.burst, p.priority));
            nonSJF.add(new Process(p.id, p.arrival, p.burst, p.priority));
        }

      
        List<String> g1 = SJFScheduler.run(sjf);
        List<String> g2 = PriorityScheduler.run(pri);
        List<String> g3 = NonPreemptivePriorityScheduler.run(nonPri);
        List<String> g4 = NonPreemptiveSJF.run(nonSJF);

        //  RESULTS
        String r1 = MetricsCalc.generate(sjf, "PREEMPTIVE SJF", String.join(" | ", g1));
        String r2 = MetricsCalc.generate(pri, "PREEMPTIVE PRIORITY", String.join(" | ", g2));
        String r3 = MetricsCalc.generate(nonPri, "NON-PREEMPTIVE PRIORITY", String.join(" | ", g3));
        String r4 = MetricsCalc.generate(nonSJF, "NON-PREEMPTIVE SJF", String.join(" | ", g4));

        //AVERAGES//
        double[] a1 = MetricsCalc.getAverages(sjf);
        double[] a2 = MetricsCalc.getAverages(pri);
        double[] a3 = MetricsCalc.getAverages(nonPri);
        double[] a4 = MetricsCalc.getAverages(nonSJF);

        double bestWT = a1[0];
        String bestWTAlgo = "Preemptive SJF";

        double bestTAT = a1[1];
        String bestTATAlgo = "Preemptive SJF";

        double bestRT = a1[2];
        String bestRTAlgo = "Preemptive SJF";

        // WT
        if (a2[0] < bestWT) { bestWT = a2[0]; bestWTAlgo = "Preemptive Priority"; }
        if (a3[0] < bestWT) { bestWT = a3[0]; bestWTAlgo = "Non-Preemptive Priority"; }
        if (a4[0] < bestWT) { bestWT = a4[0]; bestWTAlgo = "Non-Preemptive SJF"; }

        // TAT
        if (a2[1] < bestTAT) { bestTAT = a2[1]; bestTATAlgo = "Preemptive Priority"; }
        if (a3[1] < bestTAT) { bestTAT = a3[1]; bestTATAlgo = "Non-Preemptive Priority"; }
        if (a4[1] < bestTAT) { bestTAT = a4[1]; bestTATAlgo = "Non-Preemptive SJF"; }

        // RT
        if (a2[2] < bestRT) { bestRT = a2[2]; bestRTAlgo = "Preemptive Priority"; }
        if (a3[2] < bestRT) { bestRT = a3[2]; bestRTAlgo = "Non-Preemptive Priority"; }
        if (a4[2] < bestRT) { bestRT = a4[2]; bestRTAlgo = "Non-Preemptive SJF"; }

        String conclusion = "\n========== FINAL CONCLUSION ==========\n";

        conclusion += "Best Average Waiting Time: " + bestWTAlgo + "\n";
        conclusion += "Best Average Turnaround Time: " + bestTATAlgo + "\n";
        conclusion += "Best Response Time: " + bestRTAlgo + "\n\n";

        if (bestWTAlgo.contains("SJF")) {
            conclusion += "SJF performed better in minimizing waiting time due to shortest-job-first strategy.\n";
        }

        if (bestRTAlgo.contains("Preemptive")) {
            conclusion += "Preemptive scheduling improves response time because processes can be interrupted early.\n";
        }

        if (bestTATAlgo.equals(bestWTAlgo)) {
            conclusion += "The same algorithm optimized both WT and TAT, showing overall efficiency.\n";
        }

        // ================= COMPARISON =================
        String comparison = "\n========== COMPARISON SUMMARY ==========\n";

        comparison += String.format("%-30s%-15s%-15s%-15s\n",
                "Algorithm", "Avg WT", "Avg TAT", "Avg RT");

        comparison += "-------------------------------------------------------------\n";

        comparison += String.format("%-30s%-15.2f%-15.2f%-15.2f\n",
                "Preemptive SJF", a1[0], a1[1], a1[2]);

        comparison += String.format("%-30s%-15.2f%-15.2f%-15.2f\n",
                "Preemptive Priority", a2[0], a2[1], a2[2]);

        comparison += String.format("%-30s%-15.2f%-15.2f%-15.2f\n",
                "Non-Preemptive Priority", a3[0], a3[1], a3[2]);

        comparison += String.format("%-30s%-15.2f%-15.2f%-15.2f\n",
                "Non-Preemptive SJF", a4[0], a4[1], a4[2]);

        // DISPLAY 
        area.setText(r1 + "\n\n" + r2 + "\n\n" + r3 + "\n\n" + r4 + comparison + conclusion);

        // GANTT
        new GanttChart(g1, "SJF");
        new GanttChart(g2, "Priority");
        new GanttChart(g3, "Non-Preemptive Priority");
        new GanttChart(g4, "Non-Preemptive SJF");
    }
}
