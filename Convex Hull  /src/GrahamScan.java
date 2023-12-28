
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

public class GrahamScan {

    Points p;
    ConvexHullVisualization F;

    public GrahamScan(ConvexHullVisualization Frame) {
        long startTime = System.currentTimeMillis();
        F = Frame;
        computeConvexHull();
        create_hull();
        F.canvas.repaint();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        F.time.setText("Time Taken : " + elapsedTime);
    }

    public void computeConvexHull() {

        F.hull.clear();

        if (F.datapoints.size() < 3) {
            return;
        }

        Points anchor = findAnchorPoint(F.datapoints);
        sortPointsByPolarAngle(F.datapoints, anchor);
        F.Anchors = anchor;
        Stack<Points> stack = new Stack<>();
        stack.push(F.datapoints.get(0));
        stack.push(F.datapoints.get(1));
        F.lines.add(new Line(stack.elementAt(stack.size() - 2), stack.peek()));

        for (int i = 2; i < F.datapoints.size(); i++) {
            p = F.datapoints.get(i);
            while (stack.size() > 1 && orientation(stack.elementAt(stack.size() - 2),
                    stack.peek(), F.datapoints.get(i)) <= 0) {
                stack.pop();
                F.lines.add(new Line(stack.elementAt(stack.size() - 2), stack.peek()));
                F.canvas.repaint();
            }
            p = F.datapoints.get(i);
            stack.push(F.datapoints.get(i));
            F.lines.add(new Line(stack.elementAt(stack.size() - 2), stack.peek()));

        }

        F.hull.addAll(stack);
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

    private int orientation(Points p, Points q, Points r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) {
            return 0;
        }
        return (val > 0) ? 1 : -1;

    }

    public void create_hull() {

        for (int i = 0; i < F.hull.size() - 1; i++) {
            F.hulllines.add(new Line(F.hull.get(i), F.hull.get(i + 1)));
        }
        F.hulllines.add(new Line(F.hull.get(F.hull.size() - 1), F.hull.get(0)));

        Timer timerInner = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                F.canvas.repaint();
            }

        });
        timerInner.setRepeats(false);
        timerInner.start();
    }
}
