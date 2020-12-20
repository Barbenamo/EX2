package api;
/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
 * date:22/11/2020
 * this class implements edge data interface, and represents an edge on a directed weighted graph.
 *  ///////////////////////////////////
 *  characteristics and attributes:
 *  1. source integer: to indicate where the edge starts from.
 *  2. destination integer: to indicate where the edge ends.
 *  3. String info: for self uses.
 *  4. tag integer: for self uses.
 *  5. Double weight: to represent the weight of the edge.
 *	///////////////////////////////////
 * 
 */
public class EdgeData implements edge_data {
	private int src;
	private int dest;
	private String info;
	private int tag;
	private double weight;
	
	
	/**
	 * this constructor creates an edge and initiates its variables.
	 * @param src
	 * @param dest
	 * @param w
	 */
	public EdgeData(int src,int dest,double w) {
		this.src=src;
		this.dest=dest;
		this.weight=w;
		this.tag=Integer.MAX_VALUE;
		this.info="white";	
	}
	/**
	 * a copy constructor which creates an edge based on a different edge.
	 * @param edge
	 */
	public EdgeData(edge_data edge) {
		this.src=edge.getSrc();
		this.dest=edge.getDest();
		this.weight=edge.getWeight();
		this.tag=Integer.MAX_VALUE;
		this.info="white";
	}
	/**
	 * get source method.
	 * @return the key of the node which the edge starts from.
	 */
	@Override
	public int getSrc() {
		
		return this.src;
	}
	/**
	 * get destination method.
	 * @return the key of the node which the edge ends at.
	 */
	@Override
	public int getDest() {
		
		return this.dest;
	}
	/**
	 * a method to get the weight of the edge.
	 * @return Double type weight.
	 */
	@Override
	public double getWeight() {
		
		return this.weight;
	}
	/**
	 * get the info String of an edge.
	 * @return String that describes the info.(meta-data)
	 */
	@Override
	public String getInfo() {
		
		return this.info;
	}
	/**
	 * set the info of an edge.
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}
	/**
	 * get the tag of an edge.
	 * @return integer tag.
	 */
	@Override
	public int getTag() {
		
		return this.tag;
	}
	/**
	 * sets the tag of the edge.
	 * @param t
	 */
	@Override
	public void setTag(int t) {
		this.tag=t;
		
	}
	/**
	 * a boolean method to check whether two edges are equal to one another based on the source
	 * and destination.
	 * @return True: if they are the same, False: if they are different.
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof edge_data)) {
			return false;
		}
		edge_data edge=((EdgeData)o);
		if(edge.getSrc()!=this.getSrc() || edge.getDest()!=this.getDest() || edge.getWeight()!=this.getWeight()) {
			return false;
		}
		return true;
	}

}
