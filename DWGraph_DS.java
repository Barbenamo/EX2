package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;


public class DWGraph_DS implements directed_weighted_graph{
	private HashMap <Integer, node_data> graph; 
	private HashMap <Integer, LinkedList<Integer>> rn;
	private int numOfEdges;
	private int MC;
	
	public DWGraph_DS() {
		graph = new HashMap<>();
		rn = new HashMap<>();
		numOfEdges = 0;
		MC = 0;
	}
	
	@Override
	public node_data getNode(int key) {
		return graph.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(!graph.containsKey(src) || !graph.containsKey(dest))
			return null;
		return ((NodeData)graph.get(src)).getEdge(dest);
	}

	@Override
	public void addNode(node_data n) {
		if(graph.containsKey(n.getKey()) || n.getKey() < 0)
			return;
		rn.put(n.getKey(), new LinkedList<>());
		graph.put(n.getKey(), n);
		MC++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(!graph.containsKey(src) || !graph.containsKey(dest) || src == dest || this.getEdge(src, dest) != null)
			return;
		rn.get(dest).add(src);
		((NodeData)graph.get(src)).addNei(dest, w);
		numOfEdges++;
		MC++;
	}

	@Override
	public Collection<node_data> getV() {
		return graph.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(!graph.containsKey(node_id))
			return null;
		return ((NodeData)graph.get(node_id)).getNei();
	}

	@Override
	public node_data removeNode(int key) {
		if(!graph.containsKey(key))
			return null;
		node_data temp = graph.get(key);
		if(getE(key) != null) {
			for(edge_data runner : getE(key)) {
				removeEdge(key, runner.getDest());
			}
		}
		for(Integer n : rn.get(key)) {
			removeEdge(n, key);
		}
		graph.remove(key);
		MC++;
		return temp;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(!graph.containsKey(src) || !graph.containsKey(dest) || src == dest)
			return null;
		if(getEdge(src, dest) == null)
			return null;
		edge_data edge = getEdge(src, dest);
		((NodeData)graph.get(src)).removeNei(dest);
		numOfEdges--;
		MC++;
		return edge;
	}

	@Override
	public int nodeSize() {
		return graph.size();
	}

	@Override
	public int edgeSize() {
		return numOfEdges;
	}

	@Override
	public int getMC() {
		return MC;
	}
	
public boolean writeFile(String fileName) {
		try {
			JSONObject viewGraph = new JSONObject();
			if (!this.graph.isEmpty()) {
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
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof directed_weighted_graph))
			return false;
		directed_weighted_graph clone = ((DWGraph_DS)o);
		if(clone.edgeSize() != this.edgeSize() || clone.nodeSize() != this.nodeSize())
			return false;
		if(clone.getV() != null) {
			for(node_data node : clone.getV()) {
				if(!this.getNode(node.getKey()).equals(node))
					return false;
				if(clone.getE(node.getKey()) != null) {
					for(edge_data edge : clone.getE(node.getKey())) {
						if(!this.getEdge(edge.getSrc(), edge.getDest()).equals(edge))
							return false;
					}
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		directed_weighted_graph g = new DWGraph_DS();
		node_data n1 = new NodeData(1, new GeoLocation(1,2,3));
		node_data n2 = new NodeData(2, new GeoLocation(1,2,3));
		node_data n3 = new NodeData(3, new GeoLocation(1,2,3));
		node_data n4 = new NodeData(4, new GeoLocation(1,2,3));
		node_data n5 = new NodeData(5, new GeoLocation(1,2,3));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		
		g.connect(1,2,5);
		g.connect(1,3,6.5);
		g.connect(2,2,1.2);
		g.connect(4,2,9);
		g.connect(2, 3,2);
		
	}
}
