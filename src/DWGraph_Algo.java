package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import EX1.Wgraph_DS;
import EX1.node_info;
import EX1.weighted_graph;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank
 * date:26/11/2020 
 * this class implements a directed graph various algorithms.
 * the algorithms are: initiation, copy, isConnected, shortest path distance, shortest path,
 * save and load.
 * save and load methods are based on JSON format.
 *  ///////////////////////////////////
 *  characteristics and attributes:
 *  the class only has an directed weighted graph as an class variable.
 *  
 */


public class DWGraph_Algo implements dw_graph_algorithms {
	private directed_weighted_graph graph;
	
	/**
	 * this method is a default constructor that builds a graph based on DWGraph_DS class.
	 */
	public DWGraph_Algo() {
	this.graph=new DWGraph_DS();
	}
	
	/**
	 *
	 * this method initiate a graph which this class works on.
	 * @param g
	 */
	@Override
	public void init(directed_weighted_graph g) {
		this.graph=g;
	}
	
	/**
	 * retrieves a graph.
	 * @return directed_weigthed_graph type. 
	*/
	@Override
	public directed_weighted_graph getGraph() {
		
		return this.graph;
	}
	
	/**
	 * a deep copy method.
	 * the deep copy considers the  number of nodes and the edges.
	 * he also considers the direction of the edge and its weight.
	 * @return a clone graph similar to this graph.
	*/
	@Override
	public directed_weighted_graph copy() {
		directed_weighted_graph clone = new DWGraph_DS();
		if (this.graph.nodeSize() == 0)
			return clone;
		for (node_data runner : this.graph.getV()) {
			node_data temp = new NodeData(runner);
			clone.addNode(temp);
		}
		for (node_data runner : this.graph.getV()) 
		{
			if (graph.getE(runner.getKey()) != null) {
				for (edge_data nei : graph.getE(runner.getKey())) {
					clone.connect(nei.getSrc(), nei.getDest(), nei.getWeight());
				}
			}
		}

		return clone;
	}
	/**
	 * this method checks if the graph is strongly connected.
	 *	the connectivity is a feature that determine if there is a valid path
	 *	between each node on the graph
	 *	corner cases: if the number of nodes are 1 or 0 the graph is defined as connected.
	 *  the algorithm uses DFS algorithm.
	 *  @return True: if the graph is connected, False: if the graph is not connected.
	*/	
	@Override
	public boolean isConnected() {
		if(graph.nodeSize()==0||graph.nodeSize()==1) {
			return true;
		}
		
		for(node_data runner:graph.getV()) {
			if(!DFS(runner)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * this method retrieves the shortest path between two nodes.
	 * the nodes are identified with their keys- src to dest.
	 * 	shortest path distance is based on the weight of the edge.
	 * 	 the weight sums up and saved on each node weight.
	 * @param src
	 * @param dest
	 * @return a Double type that indicates the distance.
	 */ 
	@Override
	public double shortestPathDist(int src, int dest) {
		if(graph.nodeSize()==0||(graph.getNode(src)==null)||(graph.getNode(dest)==null)) {
			
			return -1;
		}
		if(src==dest) {
			return 0;
		}
		PriorityQueue<node_data> q = new PriorityQueue<>(new compareWeight());
		
		graph.getNode(src).setWeight(0);
		q.add(graph.getNode(src));
		q.peek().setInfo("grey");
		while (!q.isEmpty()) {
			node_data t = q.poll();
			if(graph.getE(t.getKey())!=null) {
			for (edge_data runner: graph.getE(t.getKey())) {
				double k = t.getWeight() + runner.getWeight();
				node_data nei=graph.getNode(runner.getDest());
				if (nei.getInfo().equals("white") || nei.getWeight() > k) {
					q.removeIf(n -> n.getKey()==nei.getKey());
					nei.setInfo("grey");
					nei.setWeight(k);
					q.add(nei);
				
				}
			}
			}
			t.setInfo("black");
		}
		
		if (graph.getNode(dest).getWeight()!=Double.MAX_VALUE) {
			double ans=graph.getNode(dest).getWeight();
			reset();
			return ans;
		}
		reset();
		return -1;// in case iterator didnt make it to dest
	}
	/**
	 * a method that retrieves a list contains all nodes that goes through the 
	 * shortest path between two nodes.
	 * 	path distance is based on the weight of the edge.
	 * @param src
	 * @param dest
	 * the nodes are identified with the parameters src and dest.(for keys)
	 * corner cases: if the number of noedes in the graph is zero,
	 * or the graph does not contains the two parameter nodes, the method return null.
	 * if the source is equal to the destination, the list will contain only the that one node.
	 * 
	 * @return a list that contains all nodes from these two nodes.
	 */ 
		
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(graph.nodeSize()==0||(graph.getNode(src)==null)||(graph.getNode(dest)==null)) {
			return null;
		}
		LinkedList<node_data> list =new LinkedList<>();
		
		
		if(src==dest) {
			list.add(graph.getNode(dest));
			return list;
		}
		PriorityQueue<node_data> q = new PriorityQueue<>(new compareWeight());
		
		graph.getNode(src).setWeight(0);
		q.add(graph.getNode(src));
		q.peek().setInfo("grey");
		while (!q.isEmpty()) {
			node_data t = q.poll();
			for (edge_data runner: graph.getE(t.getKey())) {
				double k = t.getWeight() + runner.getWeight();
				node_data nei=graph.getNode(runner.getDest());
				if (nei.getInfo().equals("white") || nei.getWeight() > k) {
					q.removeIf(n -> n.getKey()==nei.getKey());
					nei.setInfo("grey");
					nei.setWeight(k);
					((NodeData)nei).setParent(t);
					q.add(nei);
				}
			}
			t.setInfo("black");
			if (t.getKey() == dest) {
				//list.addFirst(t);
				node_data parent = ((NodeData)t);
				list.addFirst(parent);
				while(((NodeData)parent).getParent() != null) {
					parent = ((NodeData)parent).getParent();
					list.addFirst(parent);
				}
				reset();
				return list;
			}

		}
		return null;// in case iterator didnt make it to dest
	}

		
	
	/**
	 * SERIALIZATION MEHTOD
	 *	A method to save a graph on a txt file.
	 *	uses the JSON format.
	 *	@param file
	 * @return True: if the file was successfully saved, False: if there was am exception during the
	 * save process.
	 * 
	 */
	@Override
	public boolean save(String file) {
	return 	((DWGraph_DS)graph).writeFile(file);	
	}
	/**
	 * DESERIALIZATION METHOD
	 *	A method to load and create a graph from a txt file.
	 *	uses the JSON format.
	 *	@param file
	 * @return True: if the graph was successfully created, False: if there were exception during
	 * the process. 
	 */
	@Override
	public boolean load(String file) {
		directed_weighted_graph g=readFile(file);
		if(g==null){
		return false;
		}
		this.graph=g;
		return true;
	}
	/**
	 * a reset method to reset all of the main characteristics of the graph after being used
	 * in DFS algorithms.
	 * it resets every node info to white, the tag to infinity, the weight to infinity and the node 
	 * parent to null.
	 */
	public void reset() {
		for (node_data runner : graph.getV()) {
			runner.setInfo("white");
			runner.setTag(Integer.MAX_VALUE);
			runner.setWeight(Double.MAX_VALUE);
			((NodeData)runner).setParent(null);
		}
	}
	
	/**
	
	 * a method to search across the graph using DFS algorithm.
	 * this method is used in various algorithms.
	 * @param src
	 */
	public boolean DFS(node_data src) {
		Queue<node_data>  q=new LinkedList<>();
		q.add(src);
		src.setInfo("grey");
		while(!q.isEmpty()) 
		{
			if(graph.getE(q.peek().getKey())!=null) {
				for(edge_data nei:graph.getE(q.peek().getKey())) {
					node_data temp=graph.getNode(nei.getDest());
					if(temp.getInfo()=="white") {
						q.add(temp);
						temp.setInfo("grey");
					}
				}
				
			}
			q.peek().setInfo("black");
			q.poll();
		}
		boolean flag=true;
		for(node_data runner:graph.getV()) {
			if(runner.getInfo()!="black") {
				flag=false;
			}
			runner.setInfo("white");
		}
		return flag;
	}
	/**

	 *a sub class to use weight-based comparator
	 *this class compares two edges based on their weights.
	 */
	public class compareWeight implements Comparator<node_data> {

		public int compare(node_data o1, node_data o2) {
			if (o1.getWeight() > o2.getWeight()) {
				return 1;
			} else if (o1.getWeight() < o2.getWeight()) {
				return -1;
			} else {
				return 0;
			}

		}

	}
	/**
	 * this method read from a file using the scanner library.
	 * the file has to be JSON formated.
	 * this function used in the load method.
	 * @param fileName
	 * @return a graph that was created from the file.
	 */
	public directed_weighted_graph readFile(String fileName) {
		try {
			directed_weighted_graph g = new DWGraph_DS();
			Scanner scanner = new Scanner(new File(fileName));
			String jsonString = scanner.useDelimiter("\\A").next();
			scanner.close();
			JSONObject viewGraph = new JSONObject(jsonString);
			JSONArray nodes = viewGraph.getJSONArray("Nodes");
			JSONArray edges = viewGraph.getJSONArray("Edges");
				for (int i = 0; i < nodes.length(); i++) {
					JSONObject nodeInfo = nodes.getJSONObject(i);
					node_data node = new NodeData(nodeInfo.getInt("id"),
					new GeoLocation(nodeInfo.getString("pos")));
					g.addNode(node);
				}
				
				for (int j = 0; j < edges.length(); j++) {
					JSONObject edgeInfo=edges.getJSONObject(j);
					int source = edgeInfo.getInt("src");
					int dest = edgeInfo.getInt("dest");
					double weight = edgeInfo.getDouble("w");
					g.connect(source, dest, weight);
				}	
			return g;
			
		} catch (FileNotFoundException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}
	}
	

}
