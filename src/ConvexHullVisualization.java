

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class ConvexHullVisualization extends JFrame {

    private static final int POINT_RADIUS = 5;
//    boolean dp = false;
    JPanel canvas = new JPanel() {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            put_points(g);
            draw_line(g);
        }
    };
    String Algo_name;
    Points Anchors;
    Graphics g;
    Font F = new Font("Arial", Font.BOLD, 14);
    JButton rand = new JButton();
    JButton clear = new JButton();
    JButton viz = new JButton();
    JLabel cord = new JLabel("(0, 0)");
    JLabel time = new JLabel("Time Taken : ");

    Random random = new Random();

    String[] algs = {"Brute Force", "Jarvis March", "Graham Scan",
        "Quick Elimination", "Andrew's Monotone Chain"};
    JComboBox<String> algos = new JComboBox<>(algs);

    public ArrayList<Points> datapoints = new ArrayList<>();
    public ArrayList<Points> org_datapoints = new ArrayList<>();
    public ArrayList<JLabel> Datapoint_cords = new ArrayList<>();
    public ArrayList<Points> hull = new ArrayList<>();
    public ArrayList<Line> lines = new ArrayList<>();
    public ArrayList<Line> hulllines = new ArrayList<>();

    public ConvexHullVisualization() {
        setTitle("Convex Hull Visualization");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel v = new JLabel();

        canvas.setBounds(150, 50, 700, 400);
        canvas.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        g = canvas.getGraphics();
        canvas.paintComponents(g);

        algos.setBounds(200, 500, 120, 30);
        algos.setFont(F);

        rand.setBounds(400, 500, 120, 30);
        rand.setText("Generate");
        rand.setFont(F);

        clear.setBounds(600, 500, 120, 30);
        clear.setText("Clear");
        clear.setFont(F);

        viz.setBounds(400, 550, 120, 30);
        viz.setText("Visualize");
        viz.setFont(F);

        cord.setBounds(500, 30, 200, 20);
        cord.setForeground(Color.BLACK);
        cord.setVisible(true);
//        add(cord, BorderLayout.NORTH);

        time.setBounds(10, 10, 200, 20);
        time.setForeground(Color.black);
        time.setVisible(true);

        add_actions();
        add(canvas);
        add(algos);
        add(rand);
        add(clear);
        add(viz);
        add(time);

    }

    private void add_actions() {
        viz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (datapoints.isEmpty()) {
                    viz.setBorder(BorderFactory.createLineBorder(Color.RED));
                } else {
                    viz.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    hull.clear();
                    lines.clear();
                    hulllines.clear();
                    Anchors = null;
                    Algo_name = algos.getSelectedItem().toString();
                    if (Algo_name.equals("Brute Force")) {
                        brute_force bf = new brute_force(ConvexHullVisualization.this);
                    } else if (Algo_name.equals("Jarvis March")) {
                        JarvisMarch jm = new JarvisMarch(ConvexHullVisualization.this);
                    } else if (Algo_name.equals("Graham Scan")) {
                        GrahamScan jm = new GrahamScan(ConvexHullVisualization.this);
                    } else if (Algo_name.equals("Andrew's Monotone Chain")) {
                        AndrewM jm = new AndrewM(ConvexHullVisualization.this);
                    } else if (Algo_name.equals("Quick Elimination")) {
                        QuickElimination qe = new QuickElimination(ConvexHullVisualization.this);
                    }
                }

                for (JLabel j : Datapoint_cords) {
                    ConvexHullVisualization.this.remove(j);
                }
                Datapoint_cords.clear();
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();
                for (Points p : hull){
                }
                int i = 0;
                for (Points p : org_datapoints) {
                    Datapoint_cords.add(new JLabel("(" + p.getX() + "," + p.getY() + ")"));
                    Datapoint_cords.get(i).setVisible(true);
                    Datapoint_cords.get(i).setBounds(10, 40 + (20 * i), 200, 20);

                    if (conts(p)) {
                        Datapoint_cords.get(i).setForeground(Color.GREEN);
                    } else {
                        Datapoint_cords.get(i).setForeground(Color.BLACK);
                    }
                    ConvexHullVisualization.this.add(Datapoint_cords.get(i));
                    i++;
                }
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();
            }
            
            public boolean conts(Points p){
                for (Points h : hull){
                    if (h.getX() == p.getX() && h.getY() == p.getY()){
                        return true;
                    }
                }
                return false;
            }
        });

        rand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Anchors = null;
                datapoints.clear();
                hull.clear();
                lines.clear();
                hulllines.clear();
                org_datapoints.clear();
                

                for (JLabel j : Datapoint_cords) {
                    ConvexHullVisualization.this.remove(j);
                }
                Datapoint_cords.clear();
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();

                for (int i = 0; i < 20; i++) {

                    int x = random.nextInt(701);
                    int y = random.nextInt(401);
                    datapoints.add(new Points(x, y));
                    Datapoint_cords.add(new JLabel("(" + x + "," + y + ")"));
                }
                org_datapoints.addAll(datapoints);
                int i = 0;
                for (JLabel j : Datapoint_cords) {
                    j.setBounds(10, 40 + (20 * i), 200, 20);
                    j.setForeground(Color.BLACK);
                    j.setVisible(true);
                    ConvexHullVisualization.this.add(j);
                    i++;
                }
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();

                canvas.repaint();
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Anchors = null;
                datapoints.clear();
                lines.clear();
                hull.clear();
                hulllines.clear();
                canvas.repaint();

                for (JLabel j : Datapoint_cords) {
                    ConvexHullVisualization.this.remove(j);
                }
                org_datapoints.clear();
                Datapoint_cords.clear();
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (int) e.getPoint().getX();
                int y = (int) e.getPoint().getY();
                datapoints.add(new Points(x, y));
                org_datapoints.add(new Points(x, y));

                Datapoint_cords.add(new JLabel("(" + x + "," + y + ")"));

                int i = 0;
                for (JLabel j : Datapoint_cords) {
                    j.setBounds(10, 40 + (20 * i), 200, 20);
                    j.setForeground(Color.BLACK);
                    j.setVisible(true);
                    ConvexHullVisualization.this.add(j);
                    i++;
                }
                ConvexHullVisualization.this.revalidate();
                ConvexHullVisualization.this.repaint();

                canvas.repaint();

            }

         
        });
    }

   

    public void put_points(Graphics g) {
        g.setColor(Color.blue);
        for (Points p : datapoints) {
            int x = p.x;
            int y = p.y;

            g.fillOval(x - POINT_RADIUS, y - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        g.setColor(Color.red);
        for (Points p : hull) {
            int x = p.x;
            int y = p.y;

            g.fillOval(x - POINT_RADIUS, y - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }
        if (Anchors != null) {
            g.setColor(Color.green);
            g.fillOval(Anchors.x - POINT_RADIUS, Anchors.y - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);

        }

    }

    public void draw_line(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        for (Line l : lines) {
            Points s = l.start;
            Points e = l.end;
            g.drawLine(s.x, s.y, e.x, e.y);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.GREEN);
        for (Line l : hulllines) {
            Points s = l.start;
            Points e = l.end;
            g2d.drawLine(s.x, s.y, e.x, e.y);

        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConvexHullVisualization visualization = new ConvexHullVisualization();
            visualization.setVisible(true);
        });
    }

}
