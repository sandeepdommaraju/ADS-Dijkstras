package ads.algos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ads.datastructures.FibonacciHeap;
import ads.datastructures.Graph;
import ads.datastructures.Graph.Edge;
import ads.datastructures.Graph.Vertex;
import ads.datastructures.LeftistTree;
import ads.datastructures.PriorityQueue;

/**
 * 
 * @author Sandeep
 * find shortest path from source to all other nodes in undirected graph
 *
 */
public class dijikstra {
	
	static Graph g;
	static Vertex source;
	static PriorityQueue<Vertex> pq = null;
	static int N = 0;
	static boolean[] visited = null;
	
	public void init() {
		
		// initialize - set dist(source) = 0; dist(rest of nodes) = BIGNUMBER
		source.minDistance = 0;
		// push nodes into priority-queue
		for (int i=1; i <= N; i++) {
			pq.add(g.vertices[i-1]);
		}
		
	}
	
	public void run() {
		
		while (!pq.isEmpty()) {
			
			Vertex currVertex = pq.poll();
			visited[currVertex.id] = true;
			
			List<Vertex> neighbors = currVertex.getNeighbors();
			
			for (Vertex nei: neighbors) {
				
				if (visited[nei.id]) {
					continue;
				}
				
				long dist_from_curr = currVertex.minDistance + currVertex.getEdgeLength(nei);
				
				if (dist_from_curr < nei.minDistance) {
					
					pq.update(nei, dist_from_curr);
					nei.minDistance = dist_from_curr;
					g.vertices[nei.id]= nei;
					
					//pq.remove(nei);
					//pq.add(nei);
				}
				
				
			}
			
		}
		
	}
	
	public static void printDistances() {
		
		for (int v=0; v<N; v++) {
			System.out.println(g.vertices[v].minDistance);
		}
	}
	
	public static void main(String[] args) {
		
		boolean randomMode = false;
		boolean leftTistTree = false;
		
		int n, d, x, m;
		
		String fileName = "";
		
		try {
	 			if (args[0].equals("-r")) {
					randomMode = true;
					n = Integer.parseInt(args[1]);
					d = Integer.parseInt(args[2]);
					x = Integer.parseInt(args[3]);
				} else if (args[0].equals("-l")){
					leftTistTree = true;
					fileName = args[1];
				} else if (args[0].equals("-f")){
					leftTistTree = false;
					fileName = args[1];
				} else {
					throw new Exception();
				}
		} catch(Exception e) {
			System.out.println("Invalid args !!");
			return;
		}
		
		if (randomMode) {
			
			//generateRandomGraph(n, d, x);
			
		} else {
			
			generateGraphFromFile(fileName);
		}
		
		long startTime = System.currentTimeMillis();
		
		dijikstra algo = new dijikstra();
		
		boolean leftistTree = true;
		
		if (leftistTree) {
			pq = new LeftistTree<Graph.Vertex>();
		} else {
			pq = new FibonacciHeap<Graph.Vertex>();
		}
		
		algo.init();
		algo.run();
		
		printDistances();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime - startTime));
		
	}
	
	public static void generateRandomGraph(int n, int d, int x) {
		
	}
	
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
			
			visited = new boolean[N];
			
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
