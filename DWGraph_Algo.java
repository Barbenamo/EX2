package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import org.json.*;


public class DWGraph_Algo implements dw_graph_algorithms{
	private directed_weighted_graph graph;
	
	public DWGraph_Algo() {
		graph = new DWGraph_DS();
	}
	
	@Override
	public void init(directed_weighted_graph g) {
		this.graph = g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.graph;
	}

	@Override
	public directed_weighted_graph copy() {
		directed_weighted_graph clone = new DWGraph_DS();
		if(this.graph.nodeSize() == 0) return clone;
		
		for(node_data runner : this.graph.getV()) {
			node_data t = new NodeData(runner);
			clone.addNode(t);
		}
		
		for(node_data runner : this.graph.getV()) {
			if(graph.getE(runner.getKey()) != null) {
				for(edge_data nei : graph.getE(runner.getKey())) {
					clone.connect(nei.getSrc(), nei.getDest(), nei.getWeight());
				}
			}
		}
		
		return clone;
	}

	@Override
	public boolean isConnected() {
		if(graph.nodeSize() == 0 || graph.nodeSize() == 1)
			return true;
		for(node_data runner : graph.getV()) {
			if(!DFS(runner))
				return false;
		}
		return true;
	}

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
			if(graph.getE(t.getKey()) != null)
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
	
	@Override
	public boolean save(String file) {
		return ((DWGraph_DS)graph).writeFile(file);
	}

	@Override
	public boolean load(String file) {
		directed_weighted_graph g = readFile(file);
		if(g == null)
			return false;
		this.graph = g;
		return true;
	}

	public void reset() {
		for(node_data runner : graph.getV()) {
			runner.setInfo("white");
			runner.setTag(Integer.MAX_VALUE);
			runner.setWeight(Double.MAX_VALUE);
			((NodeData)runner).setParent(null);
		}
	}
	
	public boolean DFS(node_data src) {
		Queue <node_data> q = new LinkedList<>();
		q.add(src);
		src.setInfo("grey");
		while(!q.isEmpty()) {
			if(graph.getE(q.peek().getKey()) != null) {
				for(edge_data nei : graph.getE(q.peek().getKey())) {
					node_data temp = graph.getNode(nei.getDest());
					if(temp.getInfo() == "white") {
						q.add(temp);
						temp.setInfo("grey");
					}
				}
			}
			q.peek().setInfo("black");
			q.poll();
		}
		
		boolean flag = true;
		for(node_data runner : graph.getV()) {
			if(runner.getInfo() != "black") {
				flag = false;
			}
			runner.setInfo("white");
		}
		return flag;
	}
	
	public class compareWeight implements Comparator<node_data>{

		@Override
		public int compare(node_data o1, node_data o2) {
			if(o1.getWeight() > o2.getWeight()) return 1;
			else if(o1.getWeight() < o2.getWeight()) return -1;
			return 0;
		}
		
	}
	
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
	
	
	public static void main(String[] args) {
		directed_weighted_graph g = new DWGraph_DS();
		node_data n1 = new NodeData(1, new GeoLocation(1,2,3));
		node_data n2 = new NodeData(2, new GeoLocation(2.5,2,3.7));
		node_data n3 = new NodeData(3, new GeoLocation(42.56,2.64,3));
		node_data n4 = new NodeData(4, new GeoLocation(9.854,7.35,1.82));
		node_data n5 = new NodeData(5, new GeoLocation(1.3,6.24,4.75));
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
		
		directed_weighted_graph g2 = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(g);
		ga.save("myGraph");
		dw_graph_algorithms g2a = new DWGraph_Algo();
		g2a.init(g2);
		//System.out.println(g2a.load("myGraph"));
		g2a.save("myFile");
		System.out.println(g2a.load("data/A0"));
		g2a.save("myFile");
		System.out.println(156);
	}
}
