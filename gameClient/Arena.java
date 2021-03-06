package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs
 * Pokemons and avoid the Zombies.
 * 
 * @author boaz.benmoshe
 *@authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
   date: 18/12/2020.
 *	this class represents a 2 dimensional graph that contains multiple agents and pokemons.
 *	the class is used to move the agents on the graph, initiate the pokemons and control the GUI.
 * ///////////////////////////////////
 * characteristics and attributes:
 * 1. the class holds the graph.
 * 2. the class holds three lists, for the agents and pokemons,and an info list.
 * 3. the class has an integer variable called pokemonIndex to mark the last pokemons used.
 * 4. the class uses an hash-map to keep track of each agent.
 * 5.  
 *
 *
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
	private directed_weighted_graph graph;
	private List<CL_Agent> agents;
	private List<CL_Pokemon> pokemons;
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100, 0);
	private static Point3D MAX = new Point3D(0, 100, 0);
	private int numOfAgents;

	public Arena() {
		;
		_info = new ArrayList<String>();
		
	}

	public Arena(directed_weighted_graph graph, String pokemons, int numOfAgents) {
		this.graph = graph;
		this.agents = new LinkedList();
		_info = new ArrayList<String>();
		this.pokemons = json2Pokemons(pokemons);
		this.numOfAgents = numOfAgents;
	

	}
	public void updatePokemons(String pokeString)
	{
		List<CL_Pokemon> newPokemons = json2Pokemons(pokeString);
		List<CL_Pokemon> myPokemons = new LinkedList<>();
		int index=0;
		while(!newPokemons.isEmpty()&&index<newPokemons.size())
		{
			/////////////////////////////////////////////
			CL_Pokemon p = newPokemons.get(index);
			updateEdge(p, graph);
			int src = Math.min(p.get_edge().getSrc(), p.get_edge().getDest());
			int dest = Math.max(p.get_edge().getSrc(), p.get_edge().getDest());
			if (p.getType() == 1) {
				p.set_edge(graph.getEdge(src, dest));
			} else {
				p.set_edge(graph.getEdge(dest, src));
			}
			/////////////////////////////////////////////
			if(newPokemons.size()==1)
			{
				//case 1 -> equals
				if(p.equals(pokemons.get(0)))
				{
					myPokemons.add(pokemons.get(0));
					pokemons = myPokemons;
					return;
				}
				//case 2 -> not equals
				else
				{
					pokemons.get(0).update(newPokemons.get(0));
					myPokemons.add(pokemons.get(0));
					pokemons = myPokemons;
					return;
				}
			}
			for (int i = 0; i < pokemons.size(); i++) {
				if(p.equals(pokemons.get(i)))
				{
					myPokemons.add(pokemons.get(i));
					pokemons.remove(i);
					newPokemons.remove(index);
					break;
				}
				if(i==pokemons.size()-1)
				{
					index++;
				}
			}
		}
	}
	
	public void updateAgents(String s) {
		if (agents.isEmpty()) {
			initAgents(s);
		}

		try {
			
			JSONObject a = new JSONObject(s);
			JSONArray ags = a.getJSONArray("Agents");
			HashMap<Integer, String> agentString = new HashMap<>();
			for (int i = 0; i < ags.length(); i++) {
				int id = ags.getJSONObject(i).getJSONObject("Agent").getInt("id");
				agentString.put(id, ags.getJSONObject(i).toString());
			}
			for (int i = 0; i < agents.size(); i++) {
				agents.get(i).update(agentString.get(agents.get(i).getID()));
				if(agents.get(i).getNextNode()==-1)
				{
					System.out.println();
				}
			}
			
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	/*
	 * public Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p)
	 * { graph = g; this.setAgents(r); this.setPokemons(p); this.pokemonIndex=0;
	 * this.setAgentMap(new HashMap<Integer, CL_Agent>()); }
	 */
	public void setPokemons(List<CL_Pokemon> f) {
		this.pokemons = f;
	}

	public void setAgents(List<CL_Agent> f) {
		this.agents = f;
	}

	public void setGraph(directed_weighted_graph g) {
		this.graph = g;
	}

	private void init() {
		MIN = null;
		MAX = null;
		double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		Iterator<node_data> iter = graph.getV().iterator();
		while (iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if (MIN == null) {
				x0 = c.x();
				y0 = c.y();
				x1 = x0;
				y1 = y0;
				MIN = new Point3D(x0, y0);
			}
			if (c.x() < x0) {
				x0 = c.x();
			}
			if (c.y() < y0) {
				y0 = c.y();
			}
			if (c.x() > x1) {
				x1 = c.x();
			}
			if (c.y() > y1) {
				y1 = c.y();
			}
		}
		double dx = x1 - x0, dy = y1 - y0;
		MIN = new Point3D(x0 - dx / 10, y0 - dy / 10);
		MAX = new Point3D(x1 + dx / 10, y1 + dy / 10);

	}

	public List<CL_Agent> getAgents() {
		return agents;
	}

	public List<CL_Pokemon> getPokemons() {
		return pokemons;
	}

	public directed_weighted_graph getGraph() {
		return graph;
	}

	public List<String> get_info() {
		return _info;
	}

	public void set_info(List<String> _info) {
		this._info = _info;
	}

	////////////////////////////////////////////////////
	public void initPokemons(String s) {
		pokemons = json2Pokemons(s);
		for (int a = 0; a < pokemons.size(); a++) {
			CL_Pokemon p = pokemons.get(a);
			updateEdge(p, graph);
			int src = Math.min(p.get_edge().getSrc(), p.get_edge().getDest());
			int dest = Math.max(p.get_edge().getSrc(), p.get_edge().getDest());
			if (p.getType() == 1) {
				p.set_edge(graph.getEdge(src, dest));
			} else {
				p.set_edge(graph.getEdge(dest, src));
			}
		}

	}

	public List<CL_Agent> initAgents(String agents) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject a = new JSONObject(agents);
			JSONArray ags = a.getJSONArray("Agents");

			for (int i = 0; i < ags.length(); i++) {
				CL_Agent c = new CL_Agent(graph, 0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			this.agents = ans;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for (int i = 0; i < ags.length(); i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		while (itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while (iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
				if (f) {
					fr.set_edge(e);
				}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if (dist > d1 - EPS2) {
			ans = true;
		}
		return ans;
	}

	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p, src, dest);
	}

	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if (type < 0 && dest > src) {
			return false;
		}
		if (type > 0 && src > dest) {
			return false;
		}
		return isOnEdge(p, src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		boolean first = true;
		while (itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if (first) {
				x0 = p.x();
				x1 = x0;
				y0 = p.y();
				y1 = y0;
				first = false;
			} else {
				if (p.x() < x0) {
					x0 = p.x();
				}
				if (p.x() > x1) {
					x1 = p.x();
				}
				if (p.y() < y0) {
					y0 = p.y();
				}
				if (p.y() > y1) {
					y1 = p.y();
				}
			}
		}
		Range xr = new Range(x0, x1);
		Range yr = new Range(y0, y1);
		return new Range2D(xr, yr);
	}

	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

	public int getNumOfAgents() {
		return numOfAgents;
	}

	public void setNumOfAgents(int numOfAgents) {
		this.numOfAgents = numOfAgents;
	}

}