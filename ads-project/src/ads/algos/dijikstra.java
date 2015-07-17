package ads.algos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ads.datastructures.FibonacciHeap;
import ads.datastructures.Graph;
import ads.datastructures.Graph.Edge;
import ads.datastructures.Graph.Vertex;
import ads.datastructures.LeftistTree;
import ads.datastructures.PriorityQueue;

/**
 * 
 * @author Sandeep
 * find shortest path from source to all other nodes in undirected graph using Dijkstra's algorithm
 *
 */
public class dijikstra {
	
	static Graph g;
	static Vertex source;
	static PriorityQueue<Vertex> pq = null;
	static int N = 0;
	static int src = 0;
	static boolean[] visited = null;
	
	/**
	 * initialize 
	 * - mark all vertices as unvisited
	 * - set vertex distances with large number
	 * - insert into priority queue
	 * - make source distance as 0
	 * 
	 */
	public void init() {
		
		visited = new boolean[N];
		
		source = g.vertices[src];
		source.minDistance = 0;
		
		// push vertices into priority-queue
		for (int i=0; i < N; i++) {
			if (source.id != g.vertices[i].id) {
				g.vertices[i].minDistance = Long.MAX_VALUE;
			}
			pq.add(g.vertices[i]);
		}
		
	}
	
	/**
	 * run the dijstra's algorithm
	 * 
	 */
	public void run() {
		
		//terminate if priority queue is empty
		while (!pq.isEmpty()) {
			
			//fetch minimum
			Vertex currVertex = pq.removeMin();
			visited[currVertex.id] = true;
			
			List<Vertex> neighbors = currVertex.getNeighbors();
			
			for (Vertex nei: neighbors) {
				
				if (visited[nei.id]) {
					continue;
				}
				
				long dist_from_curr = currVertex.minDistance + currVertex.getEdgeLength(nei);
				
				//found a shorter distance; so update
				if (dist_from_curr < nei.minDistance) {
					
					pq.decreaseKey(nei, dist_from_curr);
					nei.minDistance = dist_from_curr;
					g.vertices[nei.id]= nei;
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * print the shortest distances from source
	 */
	public static void printDistances() {
		
		for (int v=0; v<N; v++) {
			System.out.println(g.vertices[v].minDistance);
		}
	}
	
	
	public static void main(String[] args) {
		
		boolean randomMode = false;
		boolean leftistTree = false;
		
		int n=0, x=0, m=0;
		double d =0;
		
		String fileName = "";
		
		
		// validate and read input arguments
		try {
	 			if (args[0].equals("-r")) {
					randomMode = true;
					n = Integer.parseInt(args[1]);
					d = Double.parseDouble(args[2]);
					x = Integer.parseInt(args[3]);
					
					if (n ==1000 && d==0.1){
						System.out.println("Can't form connected graph: Skip test case: n = "+ n +" d = " + d);
						throw new Exception();
					}
					
				} else if (args[0].equals("-l")){
					leftistTree = true;
					fileName = args[1];
				} else if (args[0].equals("-f")){
					leftistTree = false;
					fileName = args[1];
				} else {
					throw new Exception();
				}
		} catch(Exception e) {
			System.out.println("Invalid args !!");
			return;
		}
		
		// run in random mode
		if (randomMode) {
			
			long gStart = System.currentTimeMillis();
			while (true) {
				generateRandomGraph(n, d, x);
				if (g.isConnected()){
					break;
				} else {
					System.out.println("graph is not connected !!");
				}
			}
			long gEnd = System.currentTimeMillis();
			System.out.println("graph construction took: " + (gEnd - gStart) + " milli secs");
			
			dijikstra algo = new dijikstra();
			
			// run with LeftistTree
			pq = new LeftistTree<Graph.Vertex>();
			long startTime = System.currentTimeMillis();
			algo.init();
			algo.run();
			//printDistances();
			long endTime = System.currentTimeMillis();
			System.out.println("LeftistTree Time: " + (endTime - startTime) +" milli secs");
			
			pq = new FibonacciHeap<Graph.Vertex>();
			startTime = System.currentTimeMillis();
			algo.init();
			algo.run();
			//printDistances();
			endTime = System.currentTimeMillis();
			System.out.println("FibonacciHeap Time: " + (endTime - startTime) + " milli secs");
			
			
		} else { // run in user input mode
			
			generateGraphFromFile(fileName);
			
			dijikstra algo = new dijikstra();
			
			if (leftistTree) {
				pq = new LeftistTree<Graph.Vertex>();
			} else {
				pq = new FibonacciHeap<Graph.Vertex>();
			}
			
			long startTime = System.currentTimeMillis();
			algo.init();
			algo.run();
			printDistances();
			long endTime = System.currentTimeMillis();
			System.out.println("Time: " + (endTime - startTime) + " milli secs");
		}
		
		
		
	}
	
	/**
	 * constructs a graph
	 * @param n - number of vertices
	 * @param d - density percentage of the graph
	 * @param x - id of the source vertex 
	 */
	public static void generateRandomGraph(int n, double d, int x) {
		
		int maxEdges = n*(n-1)/2;
		int m =  ((int) (maxEdges*d))/100;
		
		System.out.println("Generating graph with " + n + " vertices and edge density: " + d + "%");
		System.out.println("Selecting #Edges: " + m + " out of #max" + maxEdges);
		
		N = n;
		
		g = new Graph();
		g.vertices = new Vertex[n];
		g.edges = new Edge[m];
		
		src = x;
		
		for (int v=0; v<n; v++) {
			g.vertices[v] = g.new Vertex(v, Long.MAX_VALUE);
		}
		
		List<String> strList = new ArrayList<String>();
		int e = 0;
		while( e < m) {
			Random r = new Random();
			int from = r.nextInt(n);
			int to = r.nextInt(n);
			//int to = 0;
			/*while(true) {
				to = r.nextInt(n);
				if (from!=to)
					break;
			}*/
			int dist = r.nextInt(1000) + 1;
			
			String str = from + "*" + to;
			if (strList.contains(str)){
				continue;
			}
			strList.add(str);
			
			g.edges[e++] = g.new Edge(from, to, dist);
			g.vertices[from].adjList.add(g.vertices[to]);
			g.vertices[to].adjList.add(g.vertices[from]);

		}
	}
	
	/**
	 * constructs a graph from the file provided
	 * @param fileName
	 */
	public static void generateGraphFromFile(String fileName) {
		
		BufferedReader br = null;
		 
		try {
 
			String line;
 
			br = new BufferedReader(new FileReader(fileName));
			
			line = br.readLine();
			int x = Integer.parseInt(line);
			line = br.readLine();
			String[] input = line.split(" ");
			int n = Integer.parseInt(input[0]);
			int m = Integer.parseInt(input[1]);
			
			N = n;
			src = x;
			
			g = new Graph();
			g.vertices = new Vertex[n];
			g.edges = new Edge[m];
			
			for (int v=0; v<n; v++) {
				g.vertices[v] = g.new Vertex(v, Long.MAX_VALUE);
			}
			
			int e = 0;
			while ((line = br.readLine()) != null) {
				input = line.split(" ");
				int from 	= Integer.parseInt(input[0]);
				int to 		= Integer.parseInt(input[1]);
				int dist 	= Integer.parseInt(input[2]);
				g.edges[e++] = g.new Edge(from, to, dist);
				g.vertices[from].adjList.add(g.vertices[to]);
				g.vertices[to].adjList.add(g.vertices[from]);
			}
			
			source = g.vertices[x];
			
			if (g.isConnected()) {
				System.out.println("graph is connected !!");
			}else{
				System.out.println("graph is not connected !!");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}

}
