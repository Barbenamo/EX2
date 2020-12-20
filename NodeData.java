package api;

import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {
	private int key;
	private geo_location location;
	private double weight;
	private String info;
	private double tag;
	private HashMap<Integer, edge_data> neighbors; // {key-dest , value-edge_data}
	private node_data parent;
	

	public NodeData(int key, geo_location location) {
		this.key = key;
		this.location = location;
		this.weight = Double.MAX_VALUE;
		this.info = "white";
		this.tag = Integer.MAX_VALUE;
		this.neighbors = new HashMap<>();
		this.parent = null;
	}
	
	public NodeData(node_data n) {
		this.key = n.getKey();
		this.location = n.getLocation();
		this.weight = n.getWeight();
		this.info = "white";
		this.tag = Integer.MAX_VALUE;
		this.neighbors = new HashMap<>();
		this.parent = null;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public geo_location getLocation() {
		return location;
	}

	@Override
	public void setLocation(geo_location p) {
		this.location = p;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double w) {
		weight = w;
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

	public void addNei(int dest, double w) {
		if(hasNei(dest))
			return;
		edge_data edge = new EdgeData(this.key, dest, w);
		neighbors.put(dest, edge);
	}

	public void removeNei(int key) {
		neighbors.remove(key);
	}

	public boolean hasNei(int key) {
		return neighbors.containsKey(key);
	}
	
	public Collection<edge_data> getNei() {
		if(neighbors.isEmpty()) return null;
		return neighbors.values();
	}

	public edge_data getEdge(int key) {
		if(!hasNei(key))
			return null;
		return neighbors.get(key);
	}
	
	public node_data getParent () {
		return this.parent;
	}
	
	public void setParent(node_data parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof node_data))
			return false;
		node_data clone = ((NodeData)o);
		if(clone.getKey() != this.getKey() || !clone.getLocation().equals(this.getLocation()))
			return false;
		return true;
	}
}
