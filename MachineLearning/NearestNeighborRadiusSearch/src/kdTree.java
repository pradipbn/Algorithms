import java.util.Comparator;

public class KdTree {
    private int size;
    //private Comparator<Point2D> cmpY;
    //private Comparator<Point2D> cmpX;
    private double xmax = 1;
    private double ymax = 1;
    private double xmin = 0;
    private double ymin = 0;

    private static class Node {
       private Point2D p;      // the point
       private RectHV rect;    // the axis-aligned rectangle corresponding to this node
       private Node lb;        // the left/bottom subtree
       private Node rt;        // the right/top subtree
       private char orientation;

       public Node(Point2D point) {
           p = point;
           rect = null;
           lb = null;
           rt = null;
       }
    }

    private Node kdNode;

    public KdTree() {
      // construct an empty set of points
      kdNode = null;
      size = 0;
    }
    public boolean isEmpty() {
       // is the set empty?
       if (kdNode == null) {
           return true;
       }
       return false;
    }
    public int size() {
        // number of points in the set
        return size;
    }
    public void insert(Point2D p) {
        // add the point p to the set (if it is not already in the set)
        // 1 - vertical orientation; 0 - horizontal orientation
        //RectHV rect = null;
        
        //System.out.println("Point-" + p.x() + "," + p.y());
        if (contains(p)) {
            return;
        }
        kdNode = insert(kdNode, p, 1, null, 0);
    }
    /*
     * Orientation refers to the parent orientation
     * Every child should reverse orientation passed by the parent
     */
    private Node insert(Node node, Point2D point, int orientation, 
                        Node parent, int cmp) {

        //if (orientation == 0) { orientation = 1; }
        //else if (orientation == 1) { orientation = 0; }

        if (node == null) {
            node = new Node(point);
            size++;
            //create a rect using the orientation
            node.orientation = (char)orientation;
            if (parent == null) {
                node.rect = new RectHV(xmin, ymin, xmax, ymax);
                return node;
            }
            double xpmin = parent.rect.xmin();
            double ypmin = parent.rect.ymin();
            double xpmax = parent.rect.xmax();
            double ypmax = parent.rect.ymax();

            if (orientation == 0) {
                //create a vertical split rect
                if (cmp == -1) {
                    //System.out.println("Left");
                    node.rect = new RectHV(xpmin, ypmin, parent.p.x(), ypmax);
                } else if (cmp == 1) {
                    //System.out.println("Right");
                    node.rect = new RectHV(parent.p.x(), ypmin, xpmax, ypmax);
                }
            } else if (orientation == 1) {
                //create a horizontal split rect
                if (cmp == -1) {
                    //System.out.println("Down");
                    node.rect = new RectHV(xpmin, ypmin, xpmax, parent.p.y());
                } else if (cmp == 1) {
                    //System.out.println("Up");
                    node.rect = new RectHV(xpmin, parent.p.y(), xpmax, ypmax);
                }
            }
            //System.out.println("xmin:" + node.rect.xmin() +
            //        " ymin:" + node.rect.ymin() + " xmax" + node.rect.xmax() +
            //        " ymax:" + node.rect.ymax());
            //System.out.println("Ornt" + node.orientation);
            //System.out.println("Point add-" + point.x() + "," + point.y());
            return node;
        }

        if (orientation == 0) {
            //check if the point lies on the up or down of the axis
            cmp = Point2D.Y_ORDER.compare(point, node.p);
            cmp = (cmp == 0) ? 1 : cmp;
            //System.out.println("Checking Up/ Down:" + cmp);
        } else if (orientation == 1) {
            //check if the point lies on the right or left of the axis
            //cmpX = point.X_ORDER;
            //System.out.println("Nodex:" + node.p.x() + "Nodey:" + node.p.y());
            cmp = Point2D.X_ORDER.compare(point, node.p);
            cmp = (cmp == 0) ? 1 : cmp;
            //System.out.println("Checking Right/ left:" + cmp);
        }

        orientation = (orientation != 0) ? 0 : 1;
        if (cmp == -1) {
           node.lb = insert(node.lb, point, orientation, node, cmp);
        } else if (cmp == 1) {
            //System.out.println("Node:" + node);
           node.rt = insert(node.rt, point, orientation, node, cmp);
        }

        //System.out.println();
        return node;
    }
    public boolean contains(Point2D p) {
    // does the set contain the point p?
        int orientation = 0;
        int cmp;
        Node node = kdNode;
        while (node != null) {
            if (node.p.equals(p)) {
                return true;
            }
            orientation = (orientation != 0) ? 0 : 1;
            if (orientation == 1) {
                cmp = Point2D.X_ORDER.compare(p, node.p);
                if (cmp == -1) {
                    node = node.lb;
                } else {
                    node = node.rt;
                }
                continue;
            } else if (orientation == 0) {
                cmp = Point2D.Y_ORDER.compare(p, node.p);
                if (cmp == -1) {
                    node = node.lb;
                } else {
                    node = node.rt;
                }
                continue;
            }
        }
        return false;
    }
    public void draw() {
    // draw all of the points to standard draw
        int orientation = 0;
        int count = 0;
        //int level = 1;
        StdDraw.setPenColor(StdDraw.BLACK);
        kdNode.rect.draw();
        for (Node node : levelOrder()) {
            //if (count > level) {
            //    level = level << 1;
            //    orientation = (orientation != 0) ? 0 : 1;
            //}
            if (node.orientation == 0) {
                //draw blue
                StdDraw.setPenRadius(.001);
                StdDraw.setPenColor(StdDraw.BLUE);
                //node.rect.draw();
                StdDraw.line(node.rect.xmin(), node.p.y(), 
                            node.rect.xmax(), node.p.y());
                StdDraw.setPenRadius(.01);
                StdDraw.setPenColor(StdDraw.BLACK);
                node.p.draw();
            } else {
                //draw red
                StdDraw.setPenRadius(.001);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(),
                            node.p.x(), node.rect.ymax());
                //node.rect.draw();
                StdDraw.setPenRadius(.01);
                StdDraw.setPenColor(StdDraw.BLACK);
                node.p.draw();
            }
            count++;
        }
        //System.out.println("draw():" + count);
    }

    private Iterable<Node> levelOrder() {
        Queue<Node> keys = new Queue<Node>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(kdNode);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        //System.out.println("Keys size:" + keys.size());
        return keys;
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points in the set that are inside the rectangle
        Queue<Node> queue = new Queue<Node>();
        //Queue<Node> searchNodes = new Queue<Node>();
        Queue<Point2D> rectPoints = new Queue<Point2D>();
        double xmaxLatest = rect.xmax();
        double xmin = rect.xmin();
        double ymaxLatest = rect.ymax();
        double ymin = rect.ymin();
        Point2D tempPoint;
        //boolean intersects;
        //boolean liesLeft;
        //boolean liesRight;
        //boolean liesUp;
        //boolean liesDown;
        queue.enqueue(kdNode);
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (node == null) continue;
            //double x    = node.p.x();
            //double y    = node.p.y();
            //if (node.orientation == 0) {
            //    y = node.p.y();
            //    if (
            //} else {
            //}
            if (node.rect.intersects(rect)) {
                //if ((x >= xmin) && (x <= xmax)
                //    && (y >= ymin) && (y <= ymax)) {
                if (rect.contains(node.p)) {
                    rectPoints.enqueue(node.p);
                }
                queue.enqueue(node.lb);
                queue.enqueue(node.rt);
            } else {
                if (node.orientation == 0) {
                    //Horizontal split
                    tempPoint = new Point2D(0, ymaxLatest);
                    int cmp = Point2D.Y_ORDER.compare(tempPoint, node.p);
                    if (cmp == -1) {
                        //Enque left tree
                        queue.enqueue(node.lb);
                    } else {
                        queue.enqueue(node.rt);
                    }
                } else {
                    //Horizontal split
                    tempPoint = new Point2D(xmaxLatest, 0);
                    int cmp = Point2D.X_ORDER.compare(tempPoint, node.p);
                    if (cmp == -1) {
                    //if (node.p.x() > xmax) {
                        //Enque left tree
                        queue.enqueue(node.lb);
                    } else {
                        queue.enqueue(node.rt);
                    }
                }
            }
        }
        return rectPoints;
    }
    private double searchPointX;
    private double searchPointY;
    public Point2D nearest(Point2D p) {
        Node nearestNode;
        if (kdNode == null) {
            return null;
        }
        searchPointX = p.x();
        searchPointY = p.y();
        nearestNode = nearestSearch(kdNode, null, p);
        
        return nearestNode.p;
    }
    private double nearestDistance = 0;// = null; 
    private Node nearestSearch(Node node, Node nearestNode, Point2D p) {
        if (node == null) {
            return nearestNode;
        }
        
        double squaredToNode = p.distanceSquaredTo(node.p);
        if ((nearestNode == null)
            || (squaredToNode < nearestDistance)) {
            //championNode = node;
            nearestDistance = squaredToNode;
            nearestNode     = node; 
            //return node;
            //nearestPoint = node.p;
            //node.champion = true;
        }
        if (node.orientation == 1) {
            int cmp = Point2D.X_ORDER.compare(p, node.p);
            double dx;
            if (cmp == -1) {
              nearestNode = nearestSearch(node.lb, nearestNode, p);  
              if (searchPointY >= node.rect.ymax()) {
                  dx = p.distanceSquaredTo(new Point2D(node.p.x(), node.rect.ymax()));
              } else if (searchPointY <= node.rect.ymin()) {
                  dx = p.distanceSquaredTo(new Point2D(node.p.x(), node.rect.ymin()));
              } else {
                  dx = searchPointX - node.p.x();
              }
              if (((dx * dx) < nearestNode.p.distanceSquaredTo(p))
                  || (nearestNode.p.equals(node.p))) {
                  nearestNode = nearestSearch(node.rt, nearestNode, p);
              }
            } else {
              nearestNode = nearestSearch(node.rt, nearestNode, p);  
              if (searchPointY >= node.rect.ymax()) {
                  dx = p.distanceSquaredTo(new Point2D(node.p.x(), node.rect.ymax()));
              } else if (searchPointY <= node.rect.ymin()) {
                  dx = p.distanceSquaredTo(new Point2D(node.p.x(), node.rect.ymin()));
              } else {
                  dx = searchPointX - node.p.x();
              }
              if (((dx * dx) < nearestNode.p.distanceSquaredTo(p)) 
                  || (nearestNode.p.equals(node.p))) {
                  nearestNode = nearestSearch(node.lb, nearestNode, p);
              }
            }
        } else {
            int cmp = Point2D.Y_ORDER.compare(p, node.p);
            double dy;
            if (cmp == -1) {
              nearestNode = nearestSearch(node.lb, nearestNode, p);  
              if (searchPointX >= node.rect.xmax()) {
                  dy = p.distanceSquaredTo(new Point2D(node.p.y(), node.rect.xmax()));
              } else if (searchPointX <= node.rect.xmin()) {
                  dy = p.distanceSquaredTo(new Point2D(node.p.y(), node.rect.xmin()));
              } else {
                  dy = searchPointY - node.p.y();
              }
              if (((dy * dy) < nearestNode.p.distanceSquaredTo(p))
                  || (nearestNode.p.equals(node.p))) {
                  nearestNode = nearestSearch(node.rt, nearestNode, p);
              }
            } else {
              nearestNode = nearestSearch(node.rt, nearestNode, p);  
              if (searchPointX >= node.rect.xmax()) {
                  dy = p.distanceSquaredTo(new Point2D(node.p.y(), node.rect.xmax()));
              } else if (searchPointX <= node.rect.xmin()) {
                  dy = p.distanceSquaredTo(new Point2D(node.p.y(), node.rect.xmin()));
              } else {
                  dy = searchPointY - node.p.y();
              }
              if (((dy * dy) < nearestNode.p.distanceSquaredTo(p))
                  || (nearestNode.p.equals(node.p))) {
                  nearestNode = nearestSearch(node.lb, nearestNode, p);
              }
            }
        }
        return nearestNode;
    }
    //public Point2D nearest(Point2D p) {
    //    // a nearest neighbor in the set to p; null if set is empty
    //    Node node;
    //    Point2D nearestPoint;
    //    double distanceSquaredToNearest;
    //    double distanceSquaredToSearch;
    //    if (kdNode == null) {
    //        return null;
    //    }
    //    Queue<Node> queue = new Queue<Node>();
    //    queue.enqueue(kdNode);
    //    nearestPoint = kdNode.p;
    //    distanceSquaredToNearest = p.distanceSquaredTo(nearestPoint);
    //    while (!queue.isEmpty()) {
    //        node = queue.dequeue();
    //        if (node == null) continue;
    //        if (node.p.equals(p)) {
    //            return node.p;
    //        }
    //        distanceSquaredToSearch  = p.distanceSquaredTo(node.p);
    //        if (node.orientation == 0) {
    //            if (distanceSquaredToSearch
    //                < distanceSquaredToNearest) {
    //                nearestPoint = node.p;
    //                distanceSquaredToNearest = p.distanceSquaredTo(nearestPoint);
    //                //queue.enqueue(node.lb);
    //                //queue.enqueue(node.rt);
    //            } //else {
    //                double normalDy = p.y() - node.p.y();
    //                if ((normalDy * normalDy) < distanceSquaredToNearest) {
    //                    queue.enqueue(node.lb);
    //                    queue.enqueue(node.rt);
    //                } else {
    //                    if (Point2D.Y_ORDER.compare(p, node.p) == -1) {
    //                        queue.enqueue(node.lb);
    //                    } else {
    //                        queue.enqueue(node.rt);
    //                    }
    //                }
    //            //}
    //        } else {
    //            if (distanceSquaredToSearch
    //                < distanceSquaredToNearest) {
    //                nearestPoint = node.p;
    //                distanceSquaredToNearest = p.distanceSquaredTo(nearestPoint);
    //                //queue.enqueue(node.lb);
    //                //queue.enqueue(node.rt);
    //            } //else {
    //                double normalDx = p.x() - node.p.x();
    //                if ((normalDx * normalDx) < distanceSquaredToNearest) {
    //                    queue.enqueue(node.lb);
    //                    queue.enqueue(node.rt);
    //                } else {
    //                    if (Point2D.X_ORDER.compare(p, node.p) == -1) {
    //                        queue.enqueue(node.lb);
    //                    } else {
    //                        queue.enqueue(node.rt);
    //                    }
    //                }
    //            //}
    //        }
    //    }
    //    return nearestPoint;
    //}
    //private Point2D nearestNode(Node node, Point2D point) {
    //    int cmp;    
    //    if (node == null) {
    //        return;
    //    }
    //    if (node.orientation == 0) {
    //            
    //    } else if (node.orientation == 1) {
    //        cmp = Point2D.X_ORDER.compare(point, node.p);    
    //        if (cmp == -1) {
    //        } else {
    //        }
    //    }
    //}
}
