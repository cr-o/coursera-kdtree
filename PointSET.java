/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET { // set of points in unit square, implemented using red-black BST
    private SET PointSET;
    public PointSET(){ // construct an empty set of points
        PointSET = new SET();
    }
    public boolean isEmpty(){ // is the set empty?
        return PointSET.isEmpty();
    }
    public int size(){ // number of points in the set
        return PointSET.size();
    }
    public void insert(Point2D p){ // add the point to the set (if it is not already in the set)
        PointSET.add(p);
    }
    public boolean contains(Point2D p){ // does the set contain point p?
        return PointSET.contains(p);
    }
    public void draw(){ // draw all points to standard draw

    }
    public Iterable<Point2D> range(RectHV rect){ // all points that are inside the rectangle (or on the boundary)

    }
    public Point2D nearest(Point2D p){ // a nearest neighbor in the set to point p; null if the set is empty

    }
    public static void main(String[] args){ // unit testing of the methods (optional)
    /*
    * KdTreeGenerator
    * KdTreeVisualizer
    * NearestNeighborVisualizer
    */
    }
}
