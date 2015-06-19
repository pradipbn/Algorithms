import java.util.*;

public class SAP {
   // constructor takes a digraph (not necessarily a DAG)
   private Digraph iGraph;
   boolean graphProcessed; 
   private static final int INFINITY = Integer.MAX_VALUE;
   private int Ancestor;
   private int Length;

   public SAP(Digraph G) {
	   graphProcessed = false;
	   iGraph = new Digraph(G);
	}

   /*
	* TODO
	* Needs to enhancement to support v == w case
	*/
   private void processShortestAncestor(int v, int w) {
	    ST<Integer, Integer> st = new ST<Integer, Integer>();
		Digraph lGraph1 = new Digraph(iGraph.V());
		Digraph lGraph2 = new Digraph(iGraph.V());
		BreadthFirstDirectedPaths bfs1 =  new BreadthFirstDirectedPaths(lGraph1, v);
		BreadthFirstDirectedPaths bfs2 =  new BreadthFirstDirectedPaths(lGraph2, w);
		Queue<Integer> q1 = new Queue<Integer>();	
		Queue<Integer> q2 = new Queue<Integer>();	
		int commonNode = INFINITY;

		graphProcessed = true;
		q1.enqueue(v);
		q2.enqueue(w);
		while (true) {
			if (!q1.isEmpty()) {
				int m = q1.dequeue();
				for (int x : iGraph.adj(m)) {
					q1.enqueue(x);
					lGraph1.addEdge(m, x);

					//Check if there are any common nodes between bfs1 and bfs2
					//in the current level
					if (bfs2 != null && bfs2.hasPathTo(x)) {
						int distToCV = bfs1.distTo(x) + bfs2.distTo(x);
						commonNode = x;
						st.put(commonNode, distToCV);
						break;
					}
				}

				bfs1 = new BreadthFirstDirectedPaths(lGraph1, v);
				if (commonNode < INFINITY)  { break; }
			} else if (!q2.isEmpty()) {
				int n = q2.dequeue();
				for (int y : iGraph.adj(n)) {
					q2.enqueue(y);
					lGraph2.addEdge(n, y);

					//Check if there are any common nodes between bfs1 and bfs2
					//in the current level
					if (bfs1 != null && bfs1.hasPathTo(y)) {
						int distToCV = bfs1.distTo(y) + bfs2.distTo(y);
						commonNode = y;
						st.put(commonNode, distToCV);
						break;
					}
				}

				bfs2 = new BreadthFirstDirectedPaths(lGraph2, w);
				if (commonNode < INFINITY)  { break; }
			} else { break; }
		}
		// Process the graph one level lower until there is no common node
		//get EdgeTo from the upper one
		Stack<Integer> lowerLevelVertex = new Stack<Integer>();
		int lCV; // local common vertex variable
		int ldistToCV;
		if (commonNode < INFINITY && bfs1 != null && bfs2 != null) {
			for (int cv : bfs1.pathTo(commonNode)) {
				lowerLevelVertex.push(cv);
			}

			while (!lowerLevelVertex.isEmpty()) {
				lCV = lowerLevelVertex.pop();
				if (!bfs2.hasPathTo(lCV)) {
					break;
				}
				ldistToCV = bfs1.distTo(lCV) + bfs2.distTo(lCV);
				st.put(lCV, ldistToCV);
			}
		}
		Length = st.get(st.min()); 
		Ancestor = st.min();
   }

   private void processShortestAncestor(Iterable<Integer> v, Iterable<Integer> w) {
	    ST<Integer, Integer> st = new ST<Integer, Integer>();
		Digraph lGraph1 = new Digraph(iGraph.V());
		Digraph lGraph2 = new Digraph(iGraph.V());
		BreadthFirstDirectedPaths bfs1 =  new BreadthFirstDirectedPaths(lGraph1, v);
		BreadthFirstDirectedPaths bfs2 =  new BreadthFirstDirectedPaths(lGraph2, w);
		Queue<Integer> q1 = new Queue<Integer>();	
		Queue<Integer> q2 = new Queue<Integer>();	
		int commonNode = INFINITY;

		graphProcessed = true;
		for (int p : v) {
			//StdOut.println(p);
			q1.enqueue(p);
		}

		//StdOut.println(q1.isEmpty());
		for (int q : w) {
			//StdOut.println(q);
			q2.enqueue(q);
		}
		//StdOut.println(q2.isEmpty());
		while (true) {
			if (!q1.isEmpty()) {
				int m = q1.dequeue();
				for (int x : iGraph.adj(m)) {
					q1.enqueue(x);
					lGraph1.addEdge(m, x);

					//Check if there are any common nodes between bfs1 and bfs2
					//in the current level
					if (bfs2 != null && bfs2.hasPathTo(x)) {
						int distToCV = bfs1.distTo(x) + bfs2.distTo(x);
						commonNode = x;
						st.put(commonNode, distToCV);
						break;
					}
				}

				bfs1 = new BreadthFirstDirectedPaths(lGraph1, v);
				if (commonNode < INFINITY)  { break; }
			} else if (!q2.isEmpty()) {
				//StdOut.println(q2.isEmpty());
				int n = q2.dequeue();
				//StdOut.println(n);
				for (int y : iGraph.adj(n)) {
					q2.enqueue(y);
					lGraph2.addEdge(n, y);

					//Check if there are any common nodes between bfs1 and bfs2
					//in the current level
					if (bfs1 != null && bfs1.hasPathTo(y)) {
						int distToCV = bfs1.distTo(y) + bfs2.distTo(y);
						commonNode = y;
						st.put(commonNode, distToCV);
						break;
					}
				}

				bfs2 = new BreadthFirstDirectedPaths(lGraph2, w);
				if (commonNode < INFINITY)  { break; }
			} else { break; }
		}
		// Process the graph one level lower until there is no common node
		//get EdgeTo from the upper one
		Stack<Integer> lowerLevelVertex = new Stack<Integer>();
		int lCV; // local common vertex variable
		int ldistToCV;
		if (commonNode < INFINITY && bfs1 != null && bfs2 != null) {
			for (int cv : bfs1.pathTo(commonNode)) {
				lowerLevelVertex.push(cv);
			}

			while (!lowerLevelVertex.isEmpty()) {
				lCV = lowerLevelVertex.pop();
				if (!bfs2.hasPathTo(lCV)) {
					break;
				}
				ldistToCV = bfs1.distTo(lCV) + bfs2.distTo(lCV);
				st.put(lCV, ldistToCV);
			}
		}
		Length = st.get(st.min()); 
		Ancestor = st.min();
   }
   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
		if (!graphProcessed) {
			try {
				processShortestAncestor(v, w);
			} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
				Ancestor = -1;
				Length = -1;
			}
		}
		return Length;
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
		if (!graphProcessed) {
			try {
				processShortestAncestor(v, w);
			} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
				Ancestor = -1;
				Length = -1;
			}
		}
   		return Ancestor;
	}

   /*
	* TODO
	* Need to make length State machine driven
	* 1. Red: 	In this case, we should evaluate processShortestAncestor 
	* 			since the processShortestAncestor has not been evaluated
	* 			even once with the parameters.
	* 2. Green: Either length or ancestor has been called once with same parameters.
	* 			So, evalation not required.
	*/
   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (!graphProcessed) {
			try {
				processShortestAncestor(v, w);
			} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
				Ancestor = -1;
				Length = -1;
			}
		}
		return Length;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (!graphProcessed) {
			try {
				processShortestAncestor(v, w);
			} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
				Ancestor = -1;
				Length = -1;
			}
		}
   		return Ancestor;
   }

   // do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		ArrayList<Integer> array1 = new ArrayList<Integer>();
		array1.add(7);
		array1.add(4);
		array1.add(1);
		ArrayList<Integer> array2 = new ArrayList<Integer>();
		array2.add(5);
		array2.add(2);
		SAP sap = new SAP(G);
		StdOut.println("Ancestor(array):" + sap.ancestor(array1, array2));
		StdOut.println("Length(array):" + sap.length(array1, array2));
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			sap = new SAP(G);
			StdOut.println("Ancestor:" + sap.ancestor(v, w));
			StdOut.println("Length:" + sap.length(v, w));
		}
	}
}
