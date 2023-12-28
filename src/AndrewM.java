
import java.util.Collections;
import java.util.Comparator;

public class AndrewM {

    ConvexHullVisualization F;

    public AndrewM(ConvexHullVisualization frame) {
                long startTime = System.currentTimeMillis();

        F = frame;
        computeConvexHull();
        F.canvas.repaint();
        create_hull();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        F.time.setText("Time Taken : " + elapsedTime);
    }

    public void computeConvexHull() {
        F.hull.clear(); // Clear the convex hull list for re-computation

        if (F.datapoints.size() < 3) {
            return;
        }

        // Sort points lexicographically
        Collections.sort(F.datapoints, Comparator.comparing(Points::getX).thenComparing(Points::getY));

        // Build lower hull
        for (Points point : F.datapoints) {
            while (F.hull.size() >= 2
                    && orientation(F.hull.get(F.hull.size() - 2), F.hull.get(F.hull.size() - 1), point) <= 0) {

                F.lines.add(new Line(F.hull.get(F.hull.size() - 1), point));
                F.hull.remove(F.hull.size() - 1);
            }
            F.hull.add(point);
        }

        // Build upper hull
        int upperHullStart = F.hull.size() + 1; // Save the start index for the upper hull
        for (int i = F.datapoints.size() - 2; i >= 0; i--) {
            Points point = F.datapoints.get(i);
            while (F.hull.size() >= upperHullStart
                    && orientation(F.hull.get(F.hull.size() - 2), F.hull.get(F.hull.size() - 1), point) <= 0) {
                F.hull.remove(F.hull.size() - 1);
            }
            F.hull.add(point);
        }

        // Remove redundant endpoint
        F.hull.remove(F.hull.size() - 1);

        F.canvas.repaint();
    }

    private int orientation(Points p, Points q, Points r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) {
            return 0; // Collinear
        }
        return (val > 0) ? 1 : -1; // Clockwise or counterclockwise
    }

    public void create_hull() {

        for (int i = 0; i < F.hull.size() - 1; i++) {
            
            F.hulllines.add(new Line(F.hull.get(i), F.hull.get(i + 1)));
        }
        F.hulllines.add(new Line(F.hull.get(F.hull.size() - 1), F.hull.get(0)));
        
        F.canvas.repaint();
    }
}
