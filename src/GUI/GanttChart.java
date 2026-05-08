package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GanttChart extends JFrame {

    private List<String> gantt;

    public GanttChart(List<String> gantt, String title) {
        this.gantt = gantt;

        setTitle(title);
        setSize(900, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new DrawPanel());

        setVisible(true);
    }

    class DrawPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            int x = 20;
            int y = 80;
            int width = 50;
            int height = 50;

            for (int i = 0; i < gantt.size(); i++) {

                String p = gantt.get(i);

                g2.drawRect(x, y, width, height);

                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(p);

                g2.drawString(p, x + (width - textWidth) / 2, y + 30);
                g2.drawString(String.valueOf(i), x, y + 70);

                x += width;
            }

            g2.drawString(String.valueOf(gantt.size()), x, y + 70);
        }
    }
}
