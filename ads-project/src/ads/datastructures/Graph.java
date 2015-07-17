package ads.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	public Vertex[] vertices;
	public Edge[]   edges;
	
	
	public int getNumberOfVertices() {
		return vertices == null? 0 :vertices.length;
	}
	
	
	public int getEdgeLength(Vertex a, Vertex b) {
		
		return 0;
	}
	

	public class Vertex extends Node implements Comparable<Vertex>{

		public List<Vertex> adjList;
		
		public Vertex(int id, long minDistance) {
			super(id, minDistance);
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
			
			/*for (int i=0; i<adjList.size(); i++) {
				if (adjList.get(i).id == dst.id)
					return 
			}*/
			
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
	
}
