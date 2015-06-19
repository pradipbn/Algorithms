public class WordNet {

   private Digraph hprNymsGraph;
   private ST<String, MinPQ<Integer>> st = new ST<String, MinPQ<Integer>>(); //For synsets
   private ST<Integer, String> rSt = new ST<Integer, String>();
   private SAP sap;

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
	   In synsetsIn = new In(synsets);
	   In hypernymsIn = new In(hypernyms);

	   String line;
	   String[] array;

	   int numSynsetIds = 0;

	   while (!synsetsIn.isEmpty()) {
		   String[] iString;
		   MinPQ<Integer> id;
		   
		   line = synsetsIn.readLine();
		   array = line.split(",");
		   numSynsetIds++;
		   iString = array[1].split(" ");

		   rSt.put(Integer.parseInt(array[0]), array[1]);
		   for (String s : iString) {
			   id = st.get(s);
			   if (id == null) {
				   MinPQ<Integer> newId = new MinPQ<Integer>();
				   newId.insert(Integer.parseInt(array[0]));
				   st.put(s, newId);
			   } else {
				   id.insert(Integer.parseInt(array[0]));
			   }
		   }

		   //for (String s : array) {
		   //    StdOut.print(s);
		   //}
		   //StdOut.println();
	   }
	   
	   //StdOut.println(numSynsetIds); 
	   hprNymsGraph = new Digraph(numSynsetIds);
	   
	   while (!hypernymsIn.isEmpty()) {
		   int i = 0;
		   int id = 0;
		   line = hypernymsIn.readLine();
		   array = line.split(",");
		   //StdOut.println(array.length);
		   for (String s : array) {
			   if (i == 0) {
				   id = Integer.parseInt(s);
			   } else {
				   hprNymsGraph.addEdge(id, Integer.parseInt(s)); 
			   }
			   i++;
		       //StdOut.print(s);
		   }
		   //StdOut.println();
	   }
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
	   return st.keys();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
	   StdOut.println("Number of nouns:" + st.size());
	   return st.contains(word);
   }

   /*
	* TODO
	* Need to call the SAP just once
	*/
   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
	   sap = new SAP(hprNymsGraph);
	   return sap.length(st.get(nounA), st.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
	   sap = new SAP(hprNymsGraph);
	   int ancestor = sap.ancestor(st.get(nounA), st.get(nounB));
	   return rSt.get(ancestor);
   }

   // do unit testing of this class
   public static void main(String[] args) {
	   WordNet wNet = new WordNet(args[0], args[1]);
	   StdOut.println(wNet.isNoun("September_12"));
	   StdOut.println(wNet.isNoun("A'man"));
	   StdOut.println(wNet.distance("worm", "bird"));
	   StdOut.println(wNet.distance("white_marlin", "mileage"));
	   StdOut.println(wNet.distance("Black_Plague", "black_marlin"));
	   StdOut.println(wNet.distance("American_water_spaniel", "histology"));
	   StdOut.println(wNet.distance("Brown_Swiss", "barrel_roll"));
	   StdOut.println(wNet.sap("worm", "bird"));
	   StdOut.println(wNet.sap("individual", "edible_fruit"));

	   //for (String s : wNet.nouns()) {
	   //    StdOut.println(s);
	   //}
	   //
	   //String line;
	   //while (!in.isEmpty()) {
	   //    System.out.println(in.readLine());
	   //    //System.out.println("Hello World\n" + args[0]);
	   //}
   }
}
