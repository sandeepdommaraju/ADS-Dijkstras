package ads.datastructures;

import ads.datastructures.Graph.Vertex;

/**
 * Priority queue interface
 * There can be many implementations like minHeap, LeftisTree, FibonacciHeap, etc
 * @author Sandeep
 */
public interface PriorityQueue {
	
	public void setVertices(int N);
	/**
	 * 
	 * @return the vertex with minimum value, without removing it from the priority queue
	 */
	public Vertex getMin();
	
	/**
	 * 
	 * @return the vertex with minimum value, also removes it from priority queue
	 */
	public Vertex removeMin();
	
	/**
	 * 
	 * adds given vertex to the priority queue
	 */
	public void add(Vertex v);
	
	/**
	 * checks if the priority queue is empty
	 */
	public boolean isEmpty();
	
	/**
	 * update the value of the given vertex with given value
	 */
	public void decreaseKey(Vertex v, long value);

}
