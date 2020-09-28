import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree { // set of points in unit square, implemented using 2d-tree; a generalization of a BST to two-dimensional keys
    private static class Node {
        private Point2D pt;
        private RectHV rect;
        private Node lessNode;
        private Node greaterNode;
    }

    private int treeSize = 0;
    private Node root;

    public KdTree() { // construct an empty set of points
        root = new Node();
        root.pt = null;
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
            root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        }
        else {
            Node newNode = new Node();
            newNode.pt = p;
            insertBST(root, newNode, null, true, false);
        }
        treeSize += 1;
    }

    private Node insertBST(Node curr, Node add, Node parent, boolean isVertical, boolean isLess) {
        if (curr == null) {
            // Step 3
            // set up RectHV for each node
            if (isVertical) {
                if (isLess) { // on bottom
                    add.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.pt.y());
                }
                else { // on top
                    add.rect = new RectHV(parent.rect.xmin(), parent.pt.y(), parent.rect.xmax(), parent.rect.xmax());
                }
            }
            else {
                if (isLess) { // on left
                    add.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.pt.x(), parent.rect.ymax());
                }
                else { // on right
                    add.rect = new RectHV(parent.pt.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
            return add;
        }

        if (isVertical) {
            if (curr.pt.x() < add.pt.x()) {
                curr.lessNode = insertBST(curr.lessNode, add, curr, false, true);
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, curr, false, false);
            }
        }
        else {
            if (curr.pt.y() < add.pt.y()) {
                curr.lessNode = insertBST(curr.lessNode, add, curr, true, true);
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, curr, true, false);
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
        if (root == null) {
            return false;
        }
        return containsBST(root, checkNode, true);
    }

    private boolean containsBST(Node curr, Node add, boolean isVertical) {
        if (curr == null) {
            return false;
        }
        boolean inLeft = false;
        boolean inRight = false;
        if (isVertical) {
            if (curr.pt.x() < add.pt.x()) {
                inLeft = containsBST(curr.lessNode, add, false);
            }
            else if (curr.pt.x() > add.pt.x()) {
                inRight = containsBST(curr.greaterNode, add, false);
            }
            else {
                return true;
            }
        }
        else {
            if (curr.pt.y() < add.pt.y()) {
                inLeft = containsBST(curr.lessNode, add, true);
            }
            else if (curr.pt.y() < add.pt.y()) {
                inRight = containsBST(curr.greaterNode, add, true);
            }
            else {
                return true;
            }
        }
        return inLeft || inRight;
    }

    public void draw() { // draw all points to standard draw
        draw(root, true);
    }

    public Node draw(Node currNode, boolean isVertical) {
        // Step 4
        // test rectangles
        if (currNode == null) {
            throw new IllegalArgumentException("Current node cannot be null");
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        currNode.pt.draw();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(currNode.pt.x(), currNode.rect.ymin());
            Point2D end = new Point2D(currNode.pt.x(), currNode.rect.ymax());
            start.drawTo(end);
            if (currNode.lessNode != null) {
                currNode.lessNode = draw(currNode.lessNode, false);
            }
            if (currNode.greaterNode != null) {
                currNode.greaterNode = draw(currNode.greaterNode, false);
            }
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(currNode.rect.xmin(), currNode.pt.y());
            Point2D end = new Point2D(currNode.rect.xmax(), currNode.pt.y());
            start.drawTo(end);
            if (currNode.lessNode != null) {
                currNode.lessNode = draw(currNode.lessNode, false);
            }
            if (currNode.greaterNode != null) {
                currNode.greaterNode = draw(currNode.greaterNode, false);
            }
        }
        return currNode;
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        // To find all points contained in a given query rectangle,
        // start at the root and recursively search for points in both subtrees using the following pruning rule:
        // if the query rectangle does not intersect the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees).
        // A subtree is searched only if it might contain a point contained in the query rectangle.
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        return findNearest(root, p, new Point2D(Double.MAX_VALUE, Double.MAX_VALUE), true);
    }

    private Point2D findNearest(Node currNode, Point2D searchPoint, Point2D closestSeen, boolean isVertical) {
        // To find a closest point to a given query point,
        // start at the root and recursively search in both subtrees using the following pruning rule:
        // if the closest point discovered so far is closer
        // than the distance between the query point and the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees).
        // That is, search a node only only if it might contain a point that is closer than the best one found so far.
        // The effectiveness of the pruning rule depends on quickly finding a nearby point.
        // To do this, organize the recursive method so that when there are two possible subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line
        // as the query point as the first subtree to explore—
        // the closest point found while exploring the first subtree may enable pruning of the second subtree
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        /*
         * KdTreeGenerator
         * KdTreeVisualizer
         * NearestNeighborVisualizer
         */
    }
}
