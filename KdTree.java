import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree { // set of points in unit square, implemented using 2d-tree; a generalization of a BST to two-dimensional keys
    private static class Node {
        private Point2D pt;
        private RectHV rect;
        private Node lessNode;
        private Node greaterNode;
    }

    private int treeSize = 0;
    private final Node root;

    public KdTree() { // construct an empty set of points
        root = new Node();
        root.pt = null;
        root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    public boolean isEmpty() { // is the set empty?
        if (treeSize == 0) {
            return true;
        }
        return false;
    }

    public int size() { // number of points in the set
        return treeSize;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        // Step 1
        // insert logic; no need to set up ReactHV for each node
        // best implemented by using private helper methods similar to BST.java
        if (contains(p)) {
            return;
        }
        if (root.pt == null) {
            root.pt = p;
        }
        else {
            Node newNode = new Node();
            newNode.pt = p;
            newNode.rect = null;
            insertBST(root, newNode, true);
        }
        treeSize += 1;
    }

    private Node insertBST(Node curr, Node add, boolean isVertical) {
        if (curr == null) {
            return add;
        }
        if (isVertical) {
            if (add.pt.x() < curr.pt.x()) {
                curr.lessNode = insertBST(curr.lessNode, add, false);
                if (curr.lessNode.rect == null) {
                    curr.lessNode.rect = new RectHV(curr.rect.xmin(), curr.rect.ymin(), curr.pt.x(), curr.rect.ymax());
                }
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, false);
                if (curr.greaterNode.rect == null) {
                    curr.greaterNode.rect = new RectHV(curr.pt.x(), curr.rect.ymin(), curr.rect.xmax(), curr.rect.ymax());
                }
            }
        }
        else {
            if (add.pt.y() < curr.pt.y()) {
                curr.lessNode = insertBST(curr.lessNode, add, true);
                if (curr.lessNode.rect == null) {
                    curr.lessNode.rect = new RectHV(curr.rect.xmin(), curr.rect.ymin(), curr.rect.xmax(), curr.pt.y());
                }
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, true);
                if (curr.greaterNode.rect == null) {
                    curr.greaterNode.rect = new RectHV(curr.rect.xmin(), curr.pt.y(), curr.rect.xmax(), curr.rect.ymax());
                }
            }
        }
        return curr;
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        // Step 2
        // use this to test that insert() was implemented properly
        // best implemented by using private helper methods similar to BST.java
        Node checkNode = new Node();
        checkNode.pt = p;
        if (root.pt == null) {
            return false;
        }
        return containsBST(root, checkNode, true);
    }

    private boolean containsBST(Node curr, Node add, boolean isVertical) {
        if (curr == null) {
            return false;
        }
        if (curr.pt.x() == add.pt.x() && curr.pt.y() == add.pt.y()) {
            return true;
        }
        boolean inLeft = false;
        boolean inRight = false;
        if (isVertical) {
            if (add.pt.x() < curr.pt.x()) {
                inLeft = containsBST(curr.lessNode, add, false);
            }
            else {
                inRight = containsBST(curr.greaterNode, add, false);
            }
        }
        else {
            if (add.pt.y() < curr.pt.y()) {
                inLeft = containsBST(curr.lessNode, add, true);
            }
            else {
                inRight = containsBST(curr.greaterNode, add, true);
            }
        }
        return inLeft || inRight;
    }

    public void draw() { // draw all points to standard draw
        draw(root, true);
    }

    private void draw(Node currNode, boolean isVertical) {
        // DF Traversal (inorder)
        if (currNode == null) {
            return;
        }
        draw(currNode.lessNode, !isVertical);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        currNode.pt.draw();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(currNode.pt.x(), currNode.rect.ymin());
            Point2D end = new Point2D(currNode.pt.x(), currNode.rect.ymax());
            start.drawTo(end);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(currNode.rect.xmin(), currNode.pt.y());
            Point2D end = new Point2D(currNode.rect.xmax(), currNode.pt.y());
            start.drawTo(end);
        }
        draw(currNode.greaterNode, !isVertical);
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        return findRange(root, rect, new LinkedList<>());
    }

    private LinkedList<Point2D> findRange(Node currNode, RectHV searchRect, LinkedList<Point2D> list) {
        if (currNode == null || currNode.pt == null) {
            return list;
        }
        if (searchRect.intersects(currNode.rect)) {
            if (searchRect.contains(currNode.pt)) {
                list.add(currNode.pt);
            }
            list = findRange(currNode.lessNode, searchRect, list);
            list = findRange(currNode.greaterNode, searchRect, list);
        }
        return list;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        return findNearest(root, p, new Point2D(Double.MAX_VALUE, Double.MAX_VALUE), Double.MAX_VALUE, true);
    }

    private Point2D findNearest(Node currNode, Point2D searchPoint, Point2D closestPoint, double closestSeen, boolean isVertical) {
        if (currNode == null) {
            return closestPoint;
        }
        if (currNode.pt == null) {
            return null;
        }
        double currDistance = currNode.pt.distanceSquaredTo((searchPoint));
        double searchCoordinate = 0.0;
        double currentCoordinate = 0.0;
        if (currDistance < closestSeen) {
            closestSeen = currDistance;
            closestPoint = currNode.pt;
        }
        if (isVertical) {
            searchCoordinate = searchPoint.x();
            currentCoordinate = currNode.pt.x();
        }
        else {
            searchCoordinate = searchPoint.y();
            currentCoordinate = currNode.pt.y();
        }
        if (searchCoordinate <= currentCoordinate) {
            Point2D updatedCheck = findNearest(currNode.lessNode, searchPoint, closestPoint, closestSeen, !isVertical);
            double leftCheck = updatedCheck.distanceSquaredTo(searchPoint);
            if (leftCheck < closestSeen) {
                closestPoint = updatedCheck;
                closestSeen = leftCheck;
            }
            if(currNode.greaterNode != null && currNode.greaterNode.rect.distanceSquaredTo(searchPoint) < closestSeen){
                updatedCheck = findNearest(currNode.greaterNode, searchPoint, closestPoint, closestSeen, !isVertical);
                double rightCheck = updatedCheck.distanceSquaredTo(searchPoint);
                if (rightCheck < closestSeen) {
                    closestPoint = updatedCheck;
                }
            }
        }
        else {
            Point2D updatedCheck = findNearest(currNode.greaterNode, searchPoint, closestPoint, closestSeen, !isVertical);
            double rightCheck = updatedCheck.distanceSquaredTo(searchPoint);
            if (rightCheck <= closestSeen) {
                closestPoint = updatedCheck;
                closestSeen = rightCheck;
            }
            if(currNode.lessNode != null && currNode.lessNode.rect.distanceSquaredTo(searchPoint) < closestSeen){
                updatedCheck = findNearest(currNode.lessNode, searchPoint, closestPoint, closestSeen, !isVertical);
                double leftCheck = updatedCheck.distanceSquaredTo(searchPoint);
                if (leftCheck < closestSeen) {
                    closestPoint = updatedCheck;
                }
            }
        }
        return closestPoint;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        /*
         * KdTreeGenerator
         * KdTreeVisualizer
         * NearestNeighborVisualizer
         */
        KdTree testPointTree = new KdTree();
        testPointTree.insert(new Point2D(1.0, 0.75));
        testPointTree.insert(new Point2D(0.75, 0.0));
        testPointTree.insert(new Point2D(0.375, 0.12));
        testPointTree.insert(new Point2D(0.375, 0.0));
        testPointTree.insert(new Point2D(0.75, 0.125));
        testPointTree.insert(new Point2D(1.0, 0.875));
        testPointTree.insert(new Point2D(0.0, 0.125));
        testPointTree.insert(new Point2D(0.25, 0.625));
        testPointTree.insert(new Point2D(0.25, 1.0));
        testPointTree.insert(new Point2D(0.5, 0.75));
        testPointTree.insert(new Point2D(0.0, 1.0));
        testPointTree.insert(new Point2D(0.375, 0.87));
        testPointTree.insert(new Point2D(0.0, 0.5));
        testPointTree.insert(new Point2D(0.25, 0.875));
        testPointTree.insert(new Point2D(0.0, 0.625));
        Point2D nearestPoint = testPointTree.nearest(new Point2D(0.375, 0.75));
        System.out.printf("%f, %f", nearestPoint.x(), nearestPoint.y());
    }
}
