import java.util.Arrays;
public class Brute {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments were given.");
            return;
        }
        In in = new In(args[0]);
        int N = in.readInt();         // Number of points
        Point[] p = new Point[N];
        Point min;
        Point max;
        Point[] pCollinear = new Point[4];
        int count;
        
        StdDraw.setXscale(0, 32276);
        StdDraw.setYscale(0, 32278);
        StdDraw.setPenRadius(.005);
        
        for (int i = 0; i < N; i++) {
            p[i] = new Point(in.readInt(), in.readInt());
            StdDraw.setPenColor(StdDraw.BLACK);
            p[i].draw();
        }
        
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                count = 1;
                if (p[j].compareTo(p[i]) == 0) {

                    continue;
                }

                for (int k = j  + 1; k < N; k++) { 
                    count = 2;
                    if ((p[k].compareTo(p[i]) == 0)
                       || (p[k].compareTo(p[j]) == 0)) {
                        continue;
                    }
                    if ((p[i].slopeTo(p[j]) != p[j].slopeTo(p[k]))) {
                        continue;
                    }

                    for (int l = k + 1; l < N; l++) {
                        count = 3;
                        if ((p[l].compareTo(p[i]) == 0)
                            || (p[l].compareTo(p[j]) == 0)
                            || (p[l].compareTo(p[k]) == 0)) {
                            
                            continue;
                        }

                        if ((p[i].slopeTo(p[j]) == p[j].slopeTo(p[k]))
                             && (p[j].slopeTo(p[k]) == p[k].slopeTo(p[l]))) {
                            count = 0;
                            pCollinear[count++] = p[i];
                            pCollinear[count++] = p[j];
                            pCollinear[count++] = p[k];
                            pCollinear[count++] = p[l];
                            Arrays.sort(pCollinear);
                            StdDraw.setPenRadius(.001);
                            StdDraw.setPenColor(StdDraw.BLUE);
                            pCollinear[0].drawTo(pCollinear[3]);
                            System.out.println(pCollinear[0].toString() + " -> "
                                               + pCollinear[1].toString() 
                                               + " -> " + pCollinear[2].toString()
                                               + " -> " + pCollinear[3].toString());

                        }
                    }
                }
            }
        }
        StdDraw.show(0);
    }
}
