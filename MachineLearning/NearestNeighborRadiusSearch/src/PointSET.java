import java.util.Iterator;

public class PointSET {
	private SET<Point2D> pointSet;
	public PointSET() {
	  // construct an empty set of points
		pointSet = new SET<Point2D>();
	}
	public boolean isEmpty() {
	   // is the set empty?
		return pointSet.isEmpty();
	}
	public int size() {
	// number of points in the set
		return pointSet.size();
	}
	public void insert(Point2D p) {
	// add the point p to the set (if it is not already in the set)
		pointSet.add(p);
	}
	public boolean contains(Point2D p) {
	// does the set contain the point p?
		return pointSet.contains(p);
	}
	public void draw() {
	// draw all of the points to standard draw
		Iterator<Point2D> iter = pointSet.iterator();
		Point2D p;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		while (iter.hasNext()) {
			p = iter.next();
			StdDraw.point(p.x(), p.y());
		}
	}
	public Iterable<Point2D> range(RectHV rect) {
	// all points in the set that are inside the rectangle
		Iterator<Point2D> iter = pointSet.iterator();
        Point2D p;
		ResizingArrayStack<Point2D> pointStack =
						new ResizingArrayStack<Point2D>();
        while (iter.hasNext()) {
            p = iter.next();
            if (rect.contains(p)) {
                pointStack.push(p);
            }
        }

        return pointStack;
	}
	public Point2D nearest(Point2D p) {
	// a nearest neighbor in the set to p; null if set is empty
		if (p == null) {
			return null;
		}
		if (pointSet.isEmpty()) {
			return null;
		}
		Iterator<Point2D> iter = pointSet.iterator();
        Point2D searchPoint;
		Point2D minPoint = null;
		double minDistance = 0;
		double queryDistance;
		if (iter.hasNext()) {
			minPoint = iter.next();
			minDistance = minPoint.distanceSquaredTo(p);
		}

        while (iter.hasNext()) {
            searchPoint = iter.next();
			queryDistance = searchPoint.distanceSquaredTo(p);
			if (minDistance > queryDistance) { 
				minDistance = queryDistance;
				minPoint = searchPoint;
			}
        }

        return minPoint;
	}
}

