import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

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
        Node newNode = root.pt == null ? root : new Node();
        newNode.pt = p;

        if (treeSize == 0) {
            insertBST(root, newNode, true);
        }
        treeSize += 1;
        // Step 3
        // set up RectHV for each node
    }

    private Node insertBST(Node curr, Node add, boolean isVertical) {
        if (curr == null) {
            return add;
        }
        if (isVertical) {
            if (curr.pt.x() < add.pt.x()) {
                curr.lessNode = insertBST(curr.lessNode, add, false);
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, false);
            }
        }
        else {
            if (curr.pt.y() < add.pt.y()) {
                curr.lessNode = insertBST(curr.lessNode, add, true);
            }
            else {
                curr.greaterNode = insertBST(curr.greaterNode, add, true);
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
            else {
                inRight = containsBST(curr.greaterNode, add, false);
            }
        }
        else {
            if (curr.pt.y() < add.pt.y()) {
                inLeft = containsBST(curr.lessNode, add, true);
            }
            else {
                inRight = containsBST(curr.greaterNode, add, true);
            }
        }
        return inLeft || inRight;
    }

    public void draw() { // draw all points to standard draw
        // Step 4
        // test rectangles

        // black points, red vertical, blue horizontal
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
