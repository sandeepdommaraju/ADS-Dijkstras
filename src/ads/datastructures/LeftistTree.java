package ads.datastructures;

import java.util.HashMap;
import java.util.Map;

import ads.datastructures.Graph.Vertex;

/**
 * LeftistTree is a priority queue
 * It is a min heap, the value of each node is smaller than its children
 * It has a unique property, at each node, 
 * 		the length of shortest external path traversing through left child 
 * 			should be greater than or equal to 
 * 		the length of the shortest external path traversed through right child
 */
public class LeftistTree implements PriorityQueue{
	
	static int N = 0;
	
	Node[] nodes;
	
	Node minNode; //root
	
	//Map<Integer, Node> internalMap;
	
	public class Node{
		
		Node parent;
		Node left;
		Node right;
		Vertex data;	// data contains the id of the vertex and the minimum distance from source
		int s; 			// length of the shortest external path from this Node
		
		public Node(Vertex v) {
			this.data = v;
			this.parent = null;
			this.left = null;
			this.right = null;
			this.s = 1;
		}
		
	}
	
	public void setVertices(int N){
		this.N= N;
		nodes = new Node[N];
	}

	/**
	 * returns the vertex with minimum value
	 */
	public Vertex getMin() {
		// TODO Auto-generated method stub
		if (minNode == null || minNode.data == null) return null;
		
		return minNode.data;
	}

	/**
	 * removes and returns the vertex with minimum value
	 * performs meld
	 */
	public Vertex removeMin() {
		// TODO Auto-generated method stub
		
		if (minNode == null || minNode.data == null) return null;
		
		Vertex retData = minNode.data;
		
		int minIdx = retData.id;
		
		minNode = meld(minNode.left, minNode.right);
		
		//internalMap.remove(minIdx);
		nodes[minIdx] = null;
		
		return retData;
	}

	/**
	 * adds the given vertex to the LeftistTree
	 */
	public void add(Vertex v) {
		// TODO Auto-generated method stub
		
		Node n = new Node(v);
		
		if (isEmpty()) {
			
			minNode = n;
			
			//internalMap = new HashMap<Integer, Node>();
			//internalMap.put(((ads.datastructures.Graph.Vertex) v).id, minNode);
			nodes[v.id] = minNode;
			
		} else {
			
			minNode = meld(minNode , n);
			//internalMap.put(((ads.datastructures.Graph.Vertex) v).id, n);
			nodes[v.id] = n;
		}
		
	}

	/**
	 * returns true if the LeftistTree is empty; returns false otherwise
	 */
	public boolean isEmpty() {
		
		return minNode == null;
	}


	/**
	 * changes the minimum distance of the given vertex to the value provided
	 */
	public void decreaseKey(Vertex v, long value) {
		
		remove(v);
		
		v.setValue(value);
		
		add(v);
		
	}
	
	/**
	 * performs melding operation
	 * @param n1
	 * @param n2
	 * @return
	 */
	private Node meld(Node n1, Node n2) {
		
		Node retNode;
		
		if (n1 == null || n2 == null) {
			retNode = n1 == null? n2 : n1;
			return retNode;
		}
		
		//Comparable<? super Vertex> key = (Comparable<? super Vertex>) n1.data;
		long key = n1.data.minDistance;
		
		Node p;
		Node m;
		
		if (key < n2.data.minDistance) {
			
			p = n1;
			m = meld(n1.right, n2);
			
		} else {
			
			p = n2;
			m = meld(n2.right, n1);
		}
		
		m.parent = p;
		
		int leftDegree = p.left == null? 0 : p.left.s;
		int rightDegree = m.s + 1;
		
		if (leftDegree < rightDegree) {
			
			p.right = p.left;
			p.left = m;
			
		} else {
			p.right = m;
		}
		
		p.s = Math.min(p.left==null?0:p.left.s, p.right==null?0:p.right.s) + 1;
		
		return p;
	}
	
	/**
	 * removes the given vertex from the LeftistTree and performs meld
	 * @param v
	 */
	public void remove(Vertex v) {
		
		int key = v.id;
		if (key == minNode.data.id) {
			removeMin();
		} else {
			//Node n = internalMap.get(key);
			Node n = nodes[key];
			
			Node p = n.parent;
			
			if (p.left == n)
				p.left = null;
			else if (p.right == n)
				p.right = null;
			
			if (n.left != null)
				n.left.parent = n.parent;
			if (n.right != null)
				n.right.parent = null;
			Node m = meld(n.left, n.right);
			minNode = meld(m, minNode);
			
			//internalMap.remove(key);
			nodes[key] = null;
		}
	}

}
