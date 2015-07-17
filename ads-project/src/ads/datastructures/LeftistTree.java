package ads.datastructures;

import java.util.HashMap;
import java.util.Map;


public class LeftistTree<Vertex> implements PriorityQueue<Vertex>{
	
	Node minNode; //root
	
	Map<Integer, Node> internalMap;
	
	public class Node{
		
		Node parent;
		Node left;
		Node right;
		Vertex data;
		int s;
		
		public Node(Vertex v) {
			this.data = v;
			this.parent = null;
			this.left = null;
			this.right = null;
			this.s = 1;
		}
		
	}

	@Override
	public Vertex peek() {
		// TODO Auto-generated method stub
		if (minNode == null || minNode.data == null) return null;
		
		return minNode.data;
	}

	@Override
	public Vertex poll() {
		// TODO Auto-generated method stub
		
		if (minNode == null || minNode.data == null) return null;
		
		Vertex retData = minNode.data;
		
		int minIdx = ((ads.datastructures.Graph.Vertex) retData).id;
		
		minNode = meld(minNode.left, minNode.right);
		
		internalMap.remove(minIdx);
		
		return retData;
	}

	@Override
	public void add(Vertex v) {
		// TODO Auto-generated method stub
		
		Node n = new Node(v);
		
		if (isEmpty()) {
			
			minNode = n;
			
			internalMap = new HashMap<Integer, Node>();
			internalMap.put(((ads.datastructures.Graph.Vertex) v).id, minNode);
			
		} else {
			
			minNode = meld(minNode , n);
			internalMap.put(((ads.datastructures.Graph.Vertex) v).id, n);
		}
		
	}

	@Override
	public boolean isEmpty() {
		
		return minNode == null;
	}

	//@Override
	public void decreaseKey(Vertex v, long value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Vertex v, long value) {
		// TODO Auto-generated method stub
		
		remove(v);
		
		((ads.datastructures.Graph.Vertex) v).setValue(value);
		
		add(v);
		
	}
	
	private Node meld(Node n1, Node n2) {
		
		Node retNode;
		
		if (n1 == null || n2 == null) {
			retNode = n1 == null? n2 : n1;
			return retNode;
		}
		
		Comparable<? super Vertex> key = (Comparable<? super Vertex>) n1.data;
		
		Node p;
		Node m;
		
		if (key.compareTo(n2.data) < 0) {
			
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
	
	public void remove(Vertex v) {
		
		int key = ((ads.datastructures.Graph.Vertex) v).id;
		if (key == ((ads.datastructures.Graph.Vertex) minNode.data).id) {
			poll();
		} else {
			Node n = internalMap.get(key);
			/*if (n.left != null)
				n.left.parent = n.parent;
			if (n.right != null)
				n.right.parent = null;
			
			n.parent.right = n.left;
			
			Node curr = n.parent;
			Node prev = n.left;
			
			while (curr != minNode) {
				
				int leftDegree = 0;
				int rightDegree = 0;
				
				if (curr.left == prev) {
					
					leftDegree = prev.s;
					rightDegree = curr.right == null ? 0 : curr.right.s;
					
				} else if (curr.right == prev) {
					
					rightDegree = prev.s;
					leftDegree = curr.left == null ? 0 : curr.left.s;
				}
				
				if (leftDegree == rightDegree) 
					break;
				
				curr.s--;
				prev = curr;
				curr = curr.parent;
			}
			
			minNode = meld(minNode, n.right);*/
			
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
			
			internalMap.remove(key);
		}
	}

}
