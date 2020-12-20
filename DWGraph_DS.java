package api;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
 * date:23/11/2020
 * This class represents a directional weighted graph.
 *  * ///////////////////////////////////
 *  characteristics and attributes:
 *  1. the data base used to implement the graph is a hash-map, the key is the node's key and the value
 *  is the node itself.
 *  2. the class has a MC integer which usesd to calculate the number of changes made on the graph.
 *  3. the graph holds the number of edges that exists on the graph.
 *  4. the class holds another hash-map called rn (reverse neighbors), that used in different 
 *  algorithms.  
 * 	
 */


	public class DWGraph_DS implements directed_weighted_graph {
	private HashMap<Integer,node_data> g;
	private int MC;
	private int numOfEdges;
	private HashMap<Integer,LinkedList<Integer>> rn;//reverse neighbors.
	/**
	 * a default constructor to initiate the class object-the graph.
	 * 
	 */
	public DWGraph_DS() {
		this.g=new HashMap<>();
		this.rn=new HashMap<>();
		this.MC=0;
		this.numOfEdges=0;
	}
	
	
	/**
	 * a method to get the vertex using the key.
	 * @param key
	 * @return node_data-the vertex
	 */
	@Override
	public node_data getNode(int key) {
		return g.get(key);
	}
	/**
	 * a method to get the edge between two vertices, based on the keys.
	 * @param src
	 * @param dest
	 * @return the edge itself.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if(!g.containsKey(dest)||!g.containsKey(src)) {
			return null;
		}
		return ((NodeData)g.get(src)).getEdge(dest) ;
	}
	/**
	 * a void method to add a node to the graph.
	 * @param n
	 * 
	 */
	@Override
	public void addNode(node_data n) {
		if(g.containsKey(n.getKey())||n.getKey()<0) {
			return;
		}
		rn.put(n.getKey(),new LinkedList<>());
		g.put(n.getKey(), n);
		this.MC++;
	}
	/**
	 * a void method to connect two vertices with an edge.
	 * conditions: the graph contains these two nodes, the two nodes are not the same one or 
	 * are already connected.
	 * @param src
	 * @param dest
	 * @param w 
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if(!g.containsKey(src)||!g.containsKey(dest)||src==dest||getEdge(src, dest)!=null) {
			return;
		}
		((NodeData)g.get(src)).addNei(dest, w);
		rn.get(dest).add(src);
		this.MC++;
		this.numOfEdges++;
	}
	/**
	 * a method to get a collection of all the vertices in the graph.
	 * @return a Collection of node_data type objects.
	 */
	@Override
	public Collection<node_data> getV() {
		
		return g.values();
	}
	/**
	 *  a method to get a collection of all the edges that are connected to a specific node.
	 *  the node is selected via its key
	 *  @param node_id;
	 * @return a Collection of edge_data type objects. 
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if(!g.containsKey(node_id)) {
			return null;
		}
		return ((NodeData)g.get(node_id)).getNei();
	}
	/**
	 * a method to remove a node form the graph.
	 * conditions: the graph contains the specific node.
	 * in this method we disconnected every edge that came out of this node,
	 * and via the rn hash map, we disconnected every edge coming into the node.
	 * @param key-identify the node.
	 * @return the node_data type object that was removed.
	 */
	@Override
	public node_data removeNode(int key) {
		if(!g.containsKey(key)) {
			return null;
			}
		node_data temp=g.get(key);
		if(getE(key)!=null) {
		for(edge_data runner:getE(key)) {
			removeEdge(key,runner.getDest());	
			}
		}
		for(Integer n:rn.get(key)) {
			removeEdge(n, key);
		}
		g.remove(key);
		this.MC++;
		return temp;
	}
	/**
	 * a method to remove an edge between two vertices.
	 * conditions: the graph has to have the two vertices.
	 * the source and destination are not equal.
	 * the edge between the two vertices exists.
	 * @param src
	 * @param dest
	 * @return the edge_data edge that was removed.
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(!g.containsKey(src)||!g.containsKey(dest)||src==dest||!((NodeData)g.get(src)).hasNei(dest)) {
			return null;
		}
		if(getEdge(src,dest)==null)return null;
		
		edge_data t=getEdge(src,dest);
		((NodeData)g.get(src)).removeNei(dest);
		this.MC++;
		this.numOfEdges--;
		return t;
	}
	/**
	 * a method to receive the number of vertices in the graph.
	 * @return integer- the size of the g hashmap
	 */
	@Override
	public int nodeSize() {
		return g.size();
	}
	/**
	 * a method to receive the number of edges in the graph.
	 * @return integer- the number of edges.
	 */
	@Override
	public int edgeSize() {
		
		return numOfEdges;
	}
	/**
	 * a method to receive the number of changes made in the graph.
	 * changes are defined as:
	 * adding a node.
	 * removing a node.
	 * connecting two nodes.
	 * disconnecting two nods.
	 */
	@Override
	public int getMC() {
		
		return MC;
	}
	/**
	 * this method is used to serialize the graph to a txt, JSON formated file.
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean writeFile(String fileName) {
		try {
			JSONObject viewGraph = new JSONObject();
			if (!this.g.isEmpty()) {
				JSONArray nodes = new JSONArray();
				JSONArray edges=new JSONArray();
				for (node_data runner : getV()) {
					JSONObject nodeInfo = new JSONObject();
					nodeInfo.put("id", runner.getKey());
					nodeInfo.put("pos", runner.getLocation().toString());
					nodes.put(nodeInfo);
					if (getE(runner.getKey()) != null) {
						for (edge_data nei : getE(runner.getKey())) {
							JSONObject edgeInfo = new JSONObject();
							edgeInfo.put("src", runner.getKey());
							edgeInfo.put("dest", nei.getDest());
							edgeInfo.put("w", nei.getWeight());
							edges.put(edgeInfo);
						}
					}
				
				}
				viewGraph.put("Nodes",nodes);
				viewGraph.put("Edges", edges);
			}
			FileWriter file = new FileWriter(fileName);
			file.write(viewGraph.toString());
			file.flush();
			file.close();
			return true;
		} catch (JSONException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	/**
	 * this method checks whether are two graphs are equal, based on the instance
	 * and the number of edges and nodes.
	 * the method also checks each vertex and compare it to the other using the equals method
	 * in NodeData class.
	 * same goes for the edges.
	 * 
	 * @param o.
	 * @return True: if the graphs are the same, False: if they are different.
	 */
	@Override
	public boolean equals(Object o) {
		if(! (o instanceof directed_weighted_graph) )
			return false;
		directed_weighted_graph clone =((DWGraph_DS)o);
		if(clone.edgeSize()!=this.edgeSize()||clone.nodeSize()!=this.nodeSize())
			return false;
		if(clone.getV()!=null) {
			for(node_data node:clone.getV()) {
				if(!(this.getNode(node.getKey()).equals(node))) {
					return false;
				}
				if(clone.getE(node.getKey())!=null) {
					for(edge_data edge:clone.getE(node.getKey())) {
						if(!this.getEdge(edge.getSrc(),edge.getDest()).equals(edge))
							return false;
					}
				}
			}
		}
	return true;	
	}
	

}
