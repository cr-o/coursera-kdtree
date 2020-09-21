/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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
    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (!this.contains(p)) {
            points.add(p);
        }
    }
    public boolean contains(Point2D p) { // does the set contain point p?
        return points.contains(p);
    }
    public void draw() { // draw all points to standard draw
        while (points.iterator().hasNext()) {
            Point2D currPoint = points.iterator().next();
            currPoint.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        SET<Point2D> inRectangle = new SET<Point2D>();
        inRectangle.intersects(points);
        return inRectangle;
    }
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (points.size() == 0) {
            return null;
        }
        return p;
    }
    public static void main(String[] args) { // unit testing of the methods (optional)
    /*
    Corner cases
        Throw an IllegalArgumentException if any argument is null
    Performance requirements
        Support insert() and contains() in time proportional to the logarithm of the number of points in the set in the worst case
        Support nearest() and range() in time proportional to the number of points in the set

    * KdTreeGenerator
    * KdTreeVisualizer
    * NearestNeighborVisualizer
    */
    }
}
