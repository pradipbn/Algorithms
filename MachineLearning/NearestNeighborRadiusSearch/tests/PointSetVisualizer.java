public class PointSetVisualizer {
    public static void main(String[] args) {
        In in = new In(args[0]);
		kdTree ktree = new kdTree();
		int count = 0;
        StdDraw.show(0);
        ////PointSET pSet = new PointSET();
        StdDraw.setXscale();
        StdDraw.setYscale();
		Point2D p;
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
		Point2D queryPoint = new Point2D(1, 1);
        while (!in.isEmpty()) {
			count++;
            p = new Point2D(in.readDouble(), in.readDouble());
			//pSet.insert(p);
			ktree.insert(p);
        }
		//System.out.println("Drawing:" + count);
		//System.out.println("KdTree:" + ktree.size());
		ktree.draw();
		StdDraw.show(50);
        //in = new In(args[0]);
        //while (!in.isEmpty()) {
		//	count++;
        //    p = new Point2D(in.readDouble(), in.readDouble());
		//	//pSet.insert(p);
		//	//System.out.println(ktree.contains(p));
        //}
		////StdDraw.clear();
        ////StdDraw.setPenColor(StdDraw.BLACK);
		////pSet.draw();
        //for (Point2D point : pSet.range(rect)) {
        //    System.out.println(point.x() + " " + point.y());
        //}

		//p = pSet.nearest(queryPoint);

		//if (p != null) {
		//	System.out.println("Nearest Point to (0.5,0.5):" + "(" + p.x() + "," + p.y() + ")");
		//} else {
		//	System.out.println("No search point found");
		//}
    }
}
