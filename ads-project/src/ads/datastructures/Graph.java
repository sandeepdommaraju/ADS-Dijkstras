package ads.datastructures;

import java.util.ArrayList;
import java.util.List;

/*
 * Graph with adjacency list representation
 */
public class Graph {
	
	public Vertex[] vertices;
	public Edge[]   edges;
	
	
	public int getNumberOfVertices() {
		return vertices == null? 0 :vertices.length;
	}
	
	public class Vertex implements Comparable<Vertex>{

		public int id;
		public long minDistance;
		public List<Vertex> adjList;
		
		public Vertex(int id, long minDistance) {
			this.id = id;
			this.minDistance = minDistance;
			adjList = new ArrayList<Vertex>();
			
		}
		
		public void setValue(long value) {
			this.minDistance = value;
		}
		
		public int compareTo(Vertex other) {
			if (this.minDistance == other.minDistance) return 0;
			return this.minDistance - other.minDistance < 0? -1 : 1;
		}
		
		public List<Vertex> getNeighbors() {
			return adjList;
		}
		
		public long getEdgeLength(Vertex dst) {
			
			for (int e=0; e<edges.length; e++) {
				if ((edges[e].from == this.id && edges[e].to == dst.id) || (edges[e].from == dst.id && edges[e].to == this.id))
					return edges[e].getLength();
			}
			return 0;
		}

	}
	
	public class Edge {
		
		int from;
		int to;
		long weight;
		
		public Edge(int from , int to, long weight) {
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
		public long getLength(){
			return weight;
		}
		
		public Vertex getOtherVertex(Vertex curr) {
			Vertex other = null;
			return other;
		}
	}
	
	/*
	 * checks if the graph is connected or not
	 */
	public boolean isConnected() {
		
		System.out.println("checking connectivity!!");
		
		if (this.vertices.length == 0) return false;
		
		boolean[] vis = new boolean[this.vertices.length];
		
		// run depth first search
		dfs(this.vertices[0], vis);
		
		for (int i=0; i<vis.length; i++) {
			if (vis[i] == false)
				return false;
		}
		
		return true;
	}
	
	/*
	 * depth first search
	 */
	public void dfs(Vertex v, boolean[] vis) {
		vis[v.id] = true;
		List<Vertex> neis = v.getNeighbors();
		for (Vertex nei: neis) {
			if (!vis[nei.id])
				 dfs(nei, vis);
		}
	}
	
}
