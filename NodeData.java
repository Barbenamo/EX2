package api;

import java.util.Collection;
import java.util.HashMap;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
 * date:22/11/2020
 * this class implements the node_data interface and represents an vertex on an directed weighted graph
 * ///////////////////////////////////
 *  characteristics and attributes:
 *  1. each node has a unique integer key to id him.
 *  2. each node has an String info, mainly used in various algorithms for color or other indication.
 *  3. each node has an integer tag,  for different uses.
 *  4. the vertex has his weight, later to be used in DFS algorithms.
 *  5. the vertex has it geo_location type location.
 *  6. because of the directional functionality, each node has his parent node.(for self uses)
 *  7. each node has it own hash-map that contains it neighbors.
 *  	the key is the destination and the value is the edge.
 *
 * 
**/
public class NodeData implements node_data {
	private node_data parent;
	private int key;
	private String info;
	private int tag;
	private double weight;
	private geo_location location;
	HashMap<Integer,edge_data> neighbors;//key is destination, value is edge
	
	/**
	 * 
	 * Node Data constructor, based on key weight and location.
	 *
	 */
	public NodeData(int key,geo_location location)
	{
		this.key=key;
		this.weight=Double.MAX_VALUE;
		this.location=location;
		this.info="white";
		this.tag=Integer.MAX_VALUE;
		this.neighbors=new HashMap<>();
		this.parent=null;
	}
	/**
	 * 
	 * copy constructor, initiates the vertex on the base of another vertex.
	 * @param n
	 */
	public NodeData (node_data n) {
		this.key=n.getKey();
		this.location=n.getLocation();
		this.info="white";
		this.tag=Integer.MAX_VALUE;
		this.weight=getWeight();
		this.neighbors=new HashMap<>();
		this.parent=null;
	}
		
	/**
	 * get key method.
	 * @return the key of the node.
	 */
	@Override
	public int getKey() {
		
		return this.key;
	}
	/**
	 * 
	 *get location method.
	 *@return the location of the node. 
	 *
	 */
	@Override
	public geo_location getLocation() {
		
		return this.location;
	}
	/**
	 * set location method
	 * @param p
	 * 
	 */
	@Override
	public void setLocation(geo_location p) {
		this.location=p;
	}
	/**
	 * get weight method.
	 * @return the weight of the node.
	 *
	 */
	@Override
	public double getWeight() {
		
		return this.weight;
	}
	/**
	 *sets the weight of the node method.
	 *@param w
	 *
	 */
	@Override
	public void setWeight(double w) {
		this.weight=w;
		
	}
	/**
	 * get info method.
	 *@return the info of the node.
	 */
	@Override
	public String getInfo() {
		
		return this.info;
	}
	/**
	 * set info method.
	 * @param s
	 *
	 */
	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}
	/**
	 *
	 * get tag method.
	 * @return the integer type tag belongs to the node.
	 *
	 */
	@Override
	public int getTag() {
		
		return this.tag;
	}
	/**
	 *set tag method
	 *@param t
	 */
	@Override
	public void setTag(int t) {
		this.tag=t;
		
	}
	/**
	 *	add neighbors function, this method checks and add the nodes that are connected to
	 *	this node to the neighbors hash map. 
	 *	@param dest
	 *	@param weight 
	 *	
	 */
	public void addNei(int dest, double weight) {
		if (hasNei(dest)) {
			return;
		}
		edge_data edge = new EdgeData(this.key, dest, weight);
		this.neighbors.put(dest, edge);
	}
	/** 
	 *a method to remove a node from the neighbors map.
	 * @param key
	 */
	public void removeNei(int key) {
		this.neighbors.remove(key);
	}
	/**
	 *a method to get the neighbors of the node, in a form of a collection.
	 *@return null: in case it has no neighbors, a collection: in case it has neighbors.
	 *
	 */
	public Collection<edge_data> getNei(){
		if(neighbors.isEmpty()) 
			return null;
		
		return this.neighbors.values();
	}
	/**
	 * get an edge method.
	 * @param key
	 *
	 */
	public edge_data getEdge(int key) {
		if(!hasNei(key)) {
			return null;
		}
		
		return this.neighbors.get(key);
	}
	/**
	 * a boolean method to check if this node is connected directly to another node.
	 * @param key
	 * @return True: if they are connected, False: if they are not connected.
	 */
	public boolean hasNei(int key) {
		return neighbors.containsKey(key);
	}
	/**
	 * get the parent vertex of a node,
	 * @return node_data type represents the parent.
	 */
	public node_data getParent() {
		return this.parent;
	}
	/**
	 * set a parent to this node.
	 * @param parent
	 */
	public void setParent(node_data parent) {
		this.parent=parent;
	}
	@Override
	/**
	 * this boolean method checks whether are two nodes are equal to each other.
	 * based on key and location.
	 * 
	 */
	public boolean equals(Object o) {
		if(!(o instanceof node_data) ) return false;
		node_data clone= ((NodeData)o);
		if(clone.getKey()!=this.getKey()||!(clone.getLocation().equals(this.getLocation())))
			return false;
			
		return true;
		
	}
	
}
