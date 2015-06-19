import java.util.Arrays;
public class Fast {
    private int index1;
    private int index2;
   
    private void joinSegments(Point[] p, Point tempPoint, int count) {
        int cIndex = 0;
        Point min, max;
        Point[] pCollinear = new Point[count + 1];
        pCollinear[cIndex++] = tempPoint;
        while (index1 != index2) {
            pCollinear[cIndex++] = p[index1];
            index1++;
        }
        index1--;
        Arrays.sort(pCollinear);
        if (pCollinear[0].compareTo(tempPoint) == 0) {
            int i;
            //print the array and draw the line
            StdDraw.setPenRadius(.001);
            StdDraw.setPenColor(StdDraw.BLUE); 
            for (i = 0; i < cIndex - 1; i++) {
                System.out.print(pCollinear[i] + " -> ");
            }
            System.out.println(pCollinear[i]);
            pCollinear[0].drawTo(pCollinear[cIndex - 1]);
            StdDraw.show(0);
        }
    }
    
    private void findLines(Point[] p, Point tempPoint) {
        index1 = 0;
        index2 = index1 + 1;
        int count = 1;
        Point min, max;

        while (index2 != p.length) {
            if (tempPoint.slopeTo(p[index1]) == tempPoint.slopeTo(p[index2])) {
                index2++;
                count++;
                if ((index2 == p.length) 
                     && (count >= 3)) {
                    joinSegments(p, tempPoint, count);
                }
            } else {
                if (count >= 3) {
                    
                    joinSegments(p, tempPoint, count);
                    count = 1;
                } else if (count > 1) {
                    count = 1;
                    index1 = index2 - 1;
                } else {
                    index1++;
                    index2++;
                }
            }            
            if ((index1 == p.length)
               || (index2 == p.length)) {
                break;
            }
        }
        return;
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments were given.");
            return;
        }

        In in = new In(args[0]);
        int N = in.readInt();         // Number of points
        Point[] p = new Point[N];
        final Point[] pFinal = new Point[N];
        Point min;
        Point max;
        Fast fsort = new Fast();
        
        StdDraw.setXscale(0, 32276);
        StdDraw.setYscale(0, 32278);
        StdDraw.setPenRadius(.005);
        StdDraw.setPenColor(StdDraw.BLACK);

        for (int i = 0; i < N; i++) {
            p[i] = new Point(in.readInt(), in.readInt());
            pFinal[i] = p[i];
            p[i].draw();
        }

        for (int i = 0; i < N; i++) {
            Arrays.sort(p, pFinal[i].SLOPE_ORDER);
            fsort.findLines(p, pFinal[i]);
        }
    }
}
