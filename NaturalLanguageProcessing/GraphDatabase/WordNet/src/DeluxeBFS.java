public class DeluxeBFS {
	private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
	private int ancestor;
	private int ancestorLength;

	public class Ancestor implements Comparable<Ancestor> {
		int vertex;
		int length;

		public int compareTo(Ancestor that) {
			if (this.length < that.length) {
				return -1;
			} else if (this.length > that.length) {
				return 1;
			} else { return 0;
			}
		}
	}

	MinPQ<Ancestor> minPQ;

    public DeluxeBFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
		minPQ = new MinPQ<Ancestor>();
		ancestorLength = 0;
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, sources);
    }

    // BFS from multiple sources
    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }

        while (!q.isEmpty()) {
			int lAncestorLength = 0;
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                } else {
					Ancestor nAncestor = new Ancestor();	
					nAncestor.vertex = w;
					nAncestor.length = distTo[v] + distTo[w] + 1;
					minPQ.insert(nAncestor);
					q.enqueue(w);
				}
            }
        }
    }

	public int ancestor() {
		if (minPQ.isEmpty()) {
			return -1;
		} else {
			Ancestor sap = minPQ.min();
			return sap.vertex;
		}
	}
	
	public int length() {
		if (minPQ.isEmpty()) {
			return -1;
		} else {
			Ancestor sap = minPQ.min();
			return sap.length;
		}
	}
}
