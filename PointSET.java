import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET { // set of points in unit square, implemented using red-black BST
    private SET<Point2D> points;

    public PointSET() { // construct an empty set of points
        points = new SET<Point2D>();
    }

    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    public int size() { // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) { // add the point to the set
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        if (!this.contains(p)) {
            points.add(p);
        }
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        return points.contains(p);
    }

    public void draw() { // draw all points to standard draw
        while (points.iterator().hasNext()) {
            Point2D currPoint = points.iterator().next();
            currPoint.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        SET<Point2D> inRectangle = new SET<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                inRectangle.add(point);
            }
        }
        return inRectangle;
    }

    public Point2D nearest(Point2D p) { // nearest neighbor in the set to point p
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        if (points.size() == 0) { // null if the set is empty
            return null;
        }
        double minDistance = 1.0;
        double distanceBetween;
        Point2D nearestPoint = null;
        for (Point2D point : points) {
            distanceBetween = point.distanceSquaredTo(p);
            if (distanceBetween < minDistance) {
                nearestPoint = point;
                minDistance = distanceBetween;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)

    }
}
