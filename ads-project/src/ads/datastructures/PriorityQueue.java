package ads.datastructures;

public interface PriorityQueue<Vertex> {
	
	public Vertex peek();
	
	public Vertex poll();
	
	public void add(Vertex v);
	
	public boolean isEmpty();
	
	//public void decreaseKey(Vertex v, long value);
	
	public void update(Vertex v, long value);

}
