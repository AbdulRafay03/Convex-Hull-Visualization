
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class brute_force {

    ConvexHullVisualization F;

    public brute_force(ConvexHullVisualization frame) {
        long startTime = System.currentTimeMillis();
        F = frame;
        F.hull.clear();
        Collections.sort(frame.datapoints,
                Comparator.comparingInt(Points::getY));
        find_hull();
     
        create_hull();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        F.time.setText("Time Taken : " + elapsedTime);

    }

    private static double orientation(Points p1, Points p2, Points p3) {
        return (p2.getX() - p1.getX()) * (p3.getY() - p1.getY())
                - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
    }

    public void find_hull() {
        for (int i = 0; i < F.datapoints.size(); i++) {
            for (int j = 0; j < F.datapoints.size(); j++) {
                if (i == j) {
                    continue;
                }
                Points p1 = F.datapoints.get(i);
                Points p2 = F.datapoints.get(j);

                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        F.lines.add(new Line(p1, p2));
                        F.canvas.repaint();
                    }
                });
                timer.setRepeats(false);  // Set to false to execute only once
                timer.start();

                boolean valid = true;
                for (int k = 0; k < F.datapoints.size(); k++) {
                    if (k != i && k != j) {
                        Points testPoint = F.datapoints.get(k);

                        Timer timerInner = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                F.lines.add(new Line(p1, p2));
                                
                                F.canvas.repaint();
                                F.lines.remove(F.lines.size() - 1);
                            }
                        });
                        timerInner.setRepeats(false);
                        timerInner.start();

                        double orientation = orientation(p1, p2, testPoint);

                        if (orientation < 0) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    if (!F.hull.contains(p1)) {
                        F.hull.add(p1);
                    }
                    if (!F.hull.contains(p2)) {
                        F.hull.add(p2);
                    }
                    Timer timerRemove = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (F.lines.size() > 1) {
                                F.lines.remove(F.lines.size() - 1);
                                F.canvas.repaint();
                            }

                        }
                    });

                    timerRemove.setRepeats(false);
                    //timerRemove.setInitialDelay(1000 * F.datapoints.size());  // Delay removal until after inner loops
                    timerRemove.start();
                }
            }

        }

    }

    private Points findAnchorPoint(List<Points> points) {
        Points anchor = points.get(0);
        for (Points point : points) {
            if (point.getY() > anchor.getY() || (point.getY() == anchor.getY()
                    && point.getX() > anchor.getX())) {
                anchor = point;
            }
        }
        return anchor;
    }

    private void sortPointsByPolarAngle(List<Points> points, final Points anchor) {
        Collections.sort(points, new Comparator<Points>() {
            @Override
            public int compare(Points p1, Points p2) {
                double angle1 = Math.atan2(p1.getY() - anchor.getY(), p1.getX() - anchor.getX());
                double angle2 = Math.atan2(p2.getY() - anchor.getY(), p2.getX() - anchor.getX());

                if (angle1 > angle2) {
                    return -1;
                }
                if (angle1 < angle2) {
                    return 1;
                }

                double distance1 = distance(anchor, p1);
                double distance2 = distance(anchor, p2);
                return Double.compare(distance1, distance2);
            }
        });
    }

    private double distance(Points p1, Points p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void create_hull() {

        Points anchor = findAnchorPoint(F.hull);

        sortPointsByPolarAngle(F.hull, anchor);

        F.lines.clear();
        F.canvas.repaint();
        for (int i = 0; i < F.hull.size() - 1; i++) {
            F.hulllines.add(new Line(F.hull.get(i), F.hull.get(i + 1)));
        }
        F.hulllines.add(new Line(F.hull.get(F.hull.size() - 1), F.hull.get(0)));

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                F.canvas.repaint();
            }
        });
        timer.setRepeats(false);  // Set to false to execute only once
        timer.start();

    }
}
