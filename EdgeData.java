package api;

public class EdgeData implements edge_data{
	private int src;
	private int dest;
	private double weight;
	private String info;
	private double tag;
	
	public EdgeData(int src, int dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.info = "white";
		this.tag = Integer.MAX_VALUE;
	}
	
	public EdgeData(edge_data edge) {
		this.src = edge.getSrc();
		this.dest = edge.getDest();
		this.weight = edge.getWeight();
		this.info = "white";
		this.tag = Integer.MAX_VALUE;
	}
	
	@Override
	public int getSrc() {
		return src;
	}

	@Override
	public int getDest() {
		return dest;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		info = s;
	}

	@Override
	public double getTag() {
		return tag;
	}

	@Override
	public void setTag(double t) {
		tag = t;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof edge_data))
			return false;
		edge_data edge = ((EdgeData)o);
		if(edge.getSrc() != this.getSrc() || edge.getDest() != this.getDest() || edge.getWeight() != this.getWeight())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "" + this.getSrc() + " -> " + this.getDest();
	}

}
