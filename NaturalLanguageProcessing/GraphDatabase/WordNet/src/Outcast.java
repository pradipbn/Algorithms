public class Outcast {
	private WordNet iWordNet;
	private String outcast = null;
	private int dMax = 0;
	public Outcast(WordNet wordnet) {
		// constructor takes a WordNet object
		iWordNet = wordnet;
	}         
	/*
	 * TODO
	 * Use to DP to save computation
	 */
	public String outcast(String[] nouns) {  
		// given an array of WordNet nouns, return an outcast
		int d = 0;
		int distance = 0;
		for (String s : nouns) {
			for (String iS : nouns) {
				if (s.equals(iS)) {
					continue;
				}

				distance = iWordNet.distance(s, iS);
				if (distance >= 0) {
					d += distance;
				}
			}
			//StdOut.println(d);
			if (d > dMax) {
				dMax = d;
				outcast = s;
			}
			d = 0;
		}

		dMax = 0; //reset the Max for later use
		return outcast;
	}

	public static void main(String[] args) {
		// see test client below
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
