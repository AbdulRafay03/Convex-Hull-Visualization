
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Timer;

public class QuickElimination {

    ConvexHullVisualization F;

    public QuickElimination(ConvexHullVisualization frame) {
        long startTime = System.currentTimeMillis();
        F = frame;
        F.canvas.repaint();
        calculateConvexHull();
        create_hull();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        F.time.setText("Time Taken : " + elapsedTime);

    }

    private void calculateConvexHull() {
        if (F.datapoints.size() < 3) {
            // Convex hull requires at least 3 points
            return;
        }

        // Convert the List<Points> to int[][]
        int[][] pointsArray = new int[F.datapoints.size()][2];
        for (int i = 0; i < F.datapoints.size(); i++) {
            pointsArray[i][0] = F.datapoints.get(i).getX();
            pointsArray[i][1] = F.datapoints.get(i).getY();
        }

        // Call the FindConvexHull method from the provided code
        int[][] convexHullArray = FindConvexHull(pointsArray);

        // Convert the result back to List<Points>
        F.hull.clear();
        for (int i = 0; i < convexHullArray.length; i++) {
            int x = convexHullArray[i][0];
            int y = convexHullArray[i][1];
            F.hull.add(new Points(x, y));
        }

        // Sort the convex hull points by their coordinates
        F.hull.sort(Comparator.comparingInt(Points::getX).thenComparingInt(Points::getY));
    }

    public int[][] FindConvexHull(int[][] pointsList) {
        int n = pointsList.length;
        int small = 0;

        // Find the point with the lowest y-coordinate (and leftmost if tied)
        for (int i = 1; i < n; i++) {
            if (pointsList[i][1] < pointsList[small][1]
                    || (pointsList[i][1] == pointsList[small][1] && pointsList[i][0] < pointsList[small][0])) {
                small = i;
            }
        }
//        F.Anchors = F.datapoints.get(small);
        if (n == 1) {
            return new int[][]{{-1, -1}};
        }

        int[] t = pointsList[small];
        pointsList[small] = pointsList[0];
        pointsList[0] = t;

        quickSort(pointsList, 1, n - 1, true);

        int[] stack = new int[n];
        int p = 1;
        stack[0] = 0;
        stack[1] = n - 1;
        Points P1 = new Points(pointsList[0][0], pointsList[0][1]);
        Points P2 = new Points(pointsList[n - 1][0], pointsList[n - 1][1]);
        F.lines.add(new Line(P1, P2));

        for (int i = n - 2; i > 0; i--) {
            long prod = -1;
            Points pi = new Points(pointsList[i][0], pointsList[i][1]);

            while (prod <= 0 && p > 0) {
                int[] p1 = pointsList[stack[p]];
                int[] p2 = pointsList[stack[p - 1]];
                Points Pp1 = new Points(pointsList[p][0], pointsList[p][1]);
                Points Pp2 = new Points(pointsList[p - 1][0], pointsList[p - 1][1]);
                F.lines.add(new Line(pi, Pp2));
                F.lines.add(new Line(pi, Pp1));
                long y1 = p1[1] - p2[1];
                long x1 = p1[0] - p2[0];
                long x2 = pointsList[i][0] - p1[0];
                long y2 = pointsList[i][1] - p1[1];

                prod = x1 * y2 - x2 * y1;

                if (prod <= 0) {
                    p--;
                }
            }
            p++;
            stack[p] = i;
        }

        if (p + 1 <= 2) {
            return new int[][]{{-1, -1}};
        }

        int[][] ans = new int[p + 1][2];
        for (int i = p; i >= 0; i--) {
            ans[i] = pointsList[stack[i]];
        }

        quickSort(ans, 0, p, false);

        // Apply Quick Elimination
        List<int[]> convexHullList = new ArrayList<>();
        convexHullList.add(ans[0]);

        for (int i = 1; i <= p; i++) {
            if (ans[i][0] != ans[i - 1][0] || ans[i][1] != ans[i - 1][1]) {
                convexHullList.add(ans[i]);
            }
        }

        return convexHullList.toArray(new int[0][]);
    }

    public static void quickSort(int arr[][], int low, int end, boolean flag) {
        if (low >= end) {
            return;
        }
        int p = -1;
        if (flag) {
            p = partition(arr, low, end);
        } else {
            p = part(arr, low, end);
        }
        quickSort(arr, low, p - 1, flag);
        quickSort(arr, p + 1, end, flag);
    }

    private static int part(int arr[][], int low, int end) {
        int p = low;
        for (int i = low + 1; i <= end; i++) {
            if ((arr[i][0] < arr[p][0]) || (arr[i][0] == arr[p][0] && arr[i][1] < arr[p][1])) {
                low++;
                int t[] = arr[low];
                arr[low] = arr[i];
                arr[i] = t;
            }
        }
        int t[] = arr[low];
        arr[low] = arr[p];
        arr[p] = t;
        return low;
    }

    private static int partition(int arr[][], int low, int end) {
        int p = low;
        double a1 = angle(arr[low], arr[0]);
        for (int i = low + 1; i <= end; i++) {
            double a2 = angle(arr[i], arr[0]);
            if (a1 < a2) {
                low++;
                int t[] = arr[low];
                arr[low] = arr[i];
                arr[i] = t;
            }
        }
        int t[] = arr[low];
        arr[low] = arr[p];
        arr[p] = t;
        return low;
    }

    private static double angle(int p1[], int p2[]) {
        double x = p1[0] - p2[0];
        double y = p1[1] - p2[1];
        return -(x / Math.sqrt(x * x + y * y));
    }

    private Points findAnchorPoint(List<Points> points) {
        Points anchor = points.get(0);
        for (Points point : points) {
            if (point.getY() > anchor.getY() || (point.getY() == anchor.getY()
                    && point.getX() > anchor.getX())) {
                anchor = point;
            }
        }
        F.Anchors = anchor;
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

        F.canvas.repaint();
        for (int i = 0; i < F.hull.size() - 1; i++) {
            F.hulllines.add(new Line(F.hull.get(i), F.hull.get(i + 1)));
        }
        F.hulllines.add(new Line(F.hull.get(F.hull.size() - 1), F.hull.get(0)));

        F.canvas.repaint();

    }

}
