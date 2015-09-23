package ads.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ads.datastructures.Graph.Vertex;

public class FibonacciHeap implements PriorityQueue{
	
	static int N;
	
	//Map<Integer, Node> internalMap;
	
	Node[] nodes;
	
	Node minNode; //collection of min trees
	
	private class Node {
		
		int degree;
		Node child;
		Vertex data;
		Node leftSibling;
		Node rightSibling;
		Node parent;
		boolean childCut;
		
		public Node(Vertex data) {
			this.data = data;
			this.degree = 0;
			this.child = null;
			this.leftSibling = null;
			this.rightSibling = null;
			this.parent = null;
			this.childCut = false;
		}
	}
	
	public void setVertices(int N){
		this.N= N;
		nodes = new Node[N];
	}

	@Override
	public Vertex getMin() {
		// TODO Auto-generated method stub
		if (minNode == null || minNode.data == null) return null;
		return minNode.data;
	}

	@Override
	public Vertex removeMin() {
		// TODO Auto-generated method stub
		Node retNode = minNode;
		Node child = minNode.child;
		Node sibling = minNode.rightSibling;
		
		int topLevelSiblings = 100; //some big number
		if (sibling == minNode) {
			topLevelSiblings = 0;
		}
		
		int minIdx = minNode.data.id;
		
		minNode.leftSibling.rightSibling = minNode.rightSibling;
		minNode.rightSibling.leftSibling = minNode.leftSibling;
		minNode.leftSibling = minNode;
		minNode.rightSibling = minNode;
		
		Map<Integer, Node> table = new HashMap<Integer, Node>();
		pairwiseCombine(child, table);
		if (topLevelSiblings > 0)
			pairwiseCombine(sibling, table);
		combineTable(table);
		
		//internalMap.remove(minIdx);
		nodes[minIdx] = null;
		return retNode.data;
	}

	@Override
	public void add(Vertex v) {
		// TODO Auto-generated method stub
		if (isEmpty()) {
			minNode = new Node(v);
			minNode.rightSibling = minNode;
			minNode.leftSibling = minNode;
			//internalMap = new HashMap<Integer, Node>();
			//internalMap.put(v.id, minNode);
			nodes[v.id] = minNode;
		} else {
			Node x = new Node(v);
			x.leftSibling = x;
			x.rightSibling = x;
			meld(minNode, x);
			//internalMap.put(v.id,  x);
			nodes[v.id] = x;
		}
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return minNode == null;
	}

	//@Override
	public void decreaseKey(Vertex e, long value) {
		// TODO Auto-generated method stub
		
		//Node x = internalMap.get(e.id);
		Node x = nodes[e.id];
		
		Node p = x.parent;
		
		x.data.setValue(value);
		
		long key = x.data.minDistance;
				
		if (p!=null && key < p.data.minDistance) {
			
			cut (x, p);
			
			cascadeCut(p);
			
		} else if (p == null && key < minNode.data.minDistance) {
			
			minNode = x;
		}
	}
	
	public void remove(Node n) {
		
		decreaseKey(n.data, (long) Double.NEGATIVE_INFINITY);
		
		removeMin();
		
	}
	
	private void meld(Node f1, Node f2) {
		if (f1 == null || f2 == null) return;
		
		long key = f1.data.minDistance;
		
		/*if (f1.leftSibling == f1) {
			f1.leftSibling = f2.leftSibling;
		}*/
		f1.rightSibling.leftSibling = f2.leftSibling;
		f2.leftSibling.rightSibling = f1.rightSibling;
		f2.leftSibling = f1;
		f1.rightSibling = f2;
		
		if (key >= f2.data.minDistance) {
			minNode = f2;
		} else {
			minNode = f1;
		}
		
	}
	
	private void pairwiseCombine(Node child, Map<Integer, Node> table) {
		
		if (child == null) return;
		
		Node curr = child;
		
		while (curr != null) {
			
			Node next = curr.rightSibling;
			curr.leftSibling.rightSibling = curr.rightSibling;
			curr.rightSibling.leftSibling = curr.leftSibling;
			curr.leftSibling = curr;
			curr.rightSibling = curr;
			
			if (curr == next) {
				next = null;
			}
			
			curr.parent = null;
			
			int degree = curr.degree;
			
			while (true) {
				if (table.get(degree) == null) {
					curr.parent = null;
					table.put(degree, curr);
					break;
				} else {
					Node tmp = table.remove(degree);
					Node p;
					Node c;
					long key = tmp.data.minDistance;
					if (key >= curr.data.minDistance) {
						p = curr;
						c = tmp;
					} else {
						p = tmp;
						c = curr;
					}
					p.rightSibling = p;
					p.leftSibling = p;
					c.parent = p;
					p.parent = null;
					c.childCut = false;
					Node ch = p.child;
					if (ch == null) {
						p.child = c;
					} else {
						ch.rightSibling.leftSibling = c.leftSibling;
						c.leftSibling.rightSibling = ch.rightSibling;
						c.leftSibling = ch;
						ch.rightSibling = c;
					}
					p.degree++;
					//table.put(p.degree, p);
					degree = p.degree;
					curr = p;
				}
			}
			curr = next;
		}
		
	}
	
	private void combineTable(Map<Integer, Node> table) {
		
		int i=0;
		Node min = null;
		Node first = null;
		Node prev = null;
		Node curr = null;
		int minIdx = -1;
		for (Entry<Integer, Node> entry: table.entrySet()) {
			if (i==0) {
				min = entry.getValue();
				first = min;
				prev = min;
				minIdx = min.data.id;
				i++;
			} else {
				curr = entry.getValue();
				//prev = internalMap.get(minIdx);
				prev = nodes[minIdx];
				long key = curr.data.minDistance;
				if (key >= prev.data.minDistance) {
					min = prev;
					minIdx = prev.data.id;
				} else {
					min = curr;
					minIdx = curr.data.id;
				}
				
				curr.rightSibling.leftSibling = prev.leftSibling;
				prev.leftSibling.rightSibling = curr.rightSibling;
				prev.leftSibling = curr;
				curr.rightSibling = prev;
			}
		}
		//minNode = internalMap.get(minIdx);
		minNode = nodes[minIdx];
	}
	
	private void cascadeCut(Node x) {
		
		while (x.childCut && x.parent != null) {
			Node tmp = x.parent;
			cut(x, x.parent);
			x = tmp;
		}
		
		if (!x.childCut && x.parent != null)
			x.childCut = true;
	}
	
	private void cut(Node x, Node p) {
		x.leftSibling.rightSibling = x.rightSibling;
		x.rightSibling.leftSibling = x.leftSibling;
		p.degree--;
		
		if (p.degree == 0)
			p.child = null;
		
		if (p.child == x) {
			p.child = x.rightSibling;
		}
		
		x.leftSibling = x;
		x.rightSibling = x;
		
		minNode.rightSibling.leftSibling = x.leftSibling;
		x.leftSibling.rightSibling = minNode.rightSibling;
		x.leftSibling = minNode;
		minNode.rightSibling = x;
		
		x.childCut = false;
		x.parent = null;
		
		long key = x.data.minDistance;
		
		if (key < minNode.data.minDistance) {
			minNode = x;
		}
	}

}
