
import java.util.List;

public class JarvisMarch {

    ConvexHullVisualization F;

    public JarvisMarch(ConvexHullVisualization frame) {
                long startTime = System.currentTimeMillis();

        F = frame;
        computeConvexHull();
        create_hull();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        F.time.setText("Time Taken : " + elapsedTime);
    }

    public void computeConvexHull() {
        F.hull.clear(); 

        if (F.datapoints.size() < 3) {
            return;
        }

        Points startPoint = findStartPoint(F.datapoints);
        F.hull.add(startPoint);
        F.Anchors = startPoint;
        Points currentPoint = startPoint;

        do {
            Points nextPoint = findNextPoint(F.datapoints, currentPoint);
            F.hull.add(nextPoint);
            F.lines.add(new Line(currentPoint, nextPoint));
            F.canvas.repaint();
            currentPoint = nextPoint;
        } while (!currentPoint.equals(startPoint));

    }

    private Points findStartPoint(List<Points> points) {
        Points startPoint = points.get(0);

        for (Points point : points) {
            if (point.getX() < startPoint.getX()
                    || (point.getX() == startPoint.getX() && point.getY() < startPoint.getY())) {
                startPoint = point;
            }
        }

        return startPoint;
    }

    private Points findNextPoint(List<Points> points, Points currentPoint) {
        Points nextPoint = points.get(0);

        for (Points point : points) {
            if (point.equals(currentPoint)) {
                continue;
            }
            
            
            
            double orientation = orientation(currentPoint, nextPoint, point);

            if (nextPoint.equals(currentPoint) || orientation > 0
                    || (orientation == 0 && distance(currentPoint, point) > distance(currentPoint, nextPoint))) {
                nextPoint = point;
            }
             F.lines.add(new Line(currentPoint, nextPoint));
        }

        return nextPoint;
    }

    private double orientation(Points p, Points q, Points r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) {
            return 0; 
        }
        return (val > 0) ? 1 : -1; 
    }

    private double distance(Points p1, Points p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void create_hull() {


        F.canvas.repaint();
        for (int i = 0; i < F.hull.size() - 1; i++) {
            F.hulllines.add(new Line(F.hull.get(i), F.hull.get(i + 1)));
        }
        F.hulllines.add(new Line(F.hull.get(F.hull.size() - 1), F.hull.get(0)));
        F.canvas.repaint();
    }
}
