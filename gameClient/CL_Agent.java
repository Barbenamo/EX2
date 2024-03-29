package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.CL_Agent.comparePokemons;
import gameClient.util.Point3D;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.json.JSONObject;




/**
 * NOTE: this class is made by Boaz-Ben-Moshe and has been edited by the authors.
 * @authors: Bar Ben Amo & Dror Tapiero &Chaya Blank.
 * date: 18/12/2020.
 *  this class is used to represent an agent on the graph.
 *  he is a one-dimensional dot that moves on the graph from one vertex to another.
 *  he's goal is to collect as much as pokemons that he can, by moving on the pokemons edge.
 *  ///////////////////////////////////
 *  characteristics and attributes:
 *  1. each agent has an unique id that helps identify him.
 *  2. each agent has an integer value that track he's own score based on the pokemons he caught.
 *  	the value of each agent is displayed above him, in the game window.
 *  3. different agents has different speeds, that affected by the agent's score and current edge length.
 *  4. each agent has an geo location position, which change each time he moves.
 *  5. each agent 'knows' whether he stands on an vertex or an edge, and which one exactly.
 *  6. each agent has he's own pokemon he's after, one in every time.
 *  7. the agent also know how to get to he's current pokemon, and what is the fastest way
 *     to get to him, using the class variable-mission. (based on *shortestPath algorithm).
 *     the mission is a list that contains each node that the agent is supposed to go to.  	 
 *  8. the communication between the agent and the graph is based in the game-server Server.
 *  9. each agent is being 'updated' while the game is running, using the **update function.  
 *    
 *   *shortestPath algorithm: an algorithm that calculates the fastest and cheapest way to get from
 *   one vertex to another on an directed weighted graph.
 *   the algorithm is performed in the DWGraph_Algo class.
 *   ** update: a method that deserialize the agent using the exiting graph info,which transfered in
 *   	 JSON format.
 *   ///////////////////////////////////	
 */
public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;
	private int id;//the unique key for the agent
	// private long _key;
	private geo_location pos;//the position of the aget
	private double speed;//the speed of the agent
	private edge_data currEdge;// the current edge of an agent
	private node_data currNode;//the current node of an agent
	private directed_weighted_graph graph;
	private long _sg_dt;// speed needed to pass current edge-measured in units/ms
	private double value; //the current score of the agent
	private List<node_data> mission;// a list that contains every node the agent need to 
	//pass to get to the pokemon. based on shortest path algorithm.
	private CL_Pokemon currPokemon;//current pokemon 

	private PriorityQueue<CL_Pokemon> pokemons;

	
	
	/**
	 * this constructor initiates the agent on the graph.
	 * @param g
	 * @param start_node
	 * 
	 */
	public CL_Agent(directed_weighted_graph g, int start_node) {
		graph = g;
		setValue(0);
		this.currNode = graph.getNode(start_node);
		pos = currNode.getLocation();
		id = -1;
		setSpeed(0);
		this.mission=new LinkedList<>();
		this.pokemons = new PriorityQueue<CL_Pokemon>(new comparePokemons(this.id));
		_sg_dt = 95;
		}

	
	/**
	 * this method updates the agent and all of its characteristics.
	 * the method deserialize the agent using a String formated according to JSON format.
	 * @param json
	 */
	public void update(String json) {
		JSONObject line;
		try {
			//{"Agent":{"src":9,"pos":"35.19597880064568,32.10154696638656,0.0","id":0,"dest":-1,"value":0,"speed":1}}
			//an example of an agent description.
			line = new JSONObject(json);
			JSONObject agentInfo = line.getJSONObject("Agent");
			int id = agentInfo.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					this.id = id;
				}
				double speed = agentInfo.getDouble("speed");
				String p = agentInfo.getString("pos");
				Point3D pp = new Point3D(p);
				int src = agentInfo.getInt("src");
				int dest = agentInfo.getInt("dest");
				double value = agentInfo.getDouble("value");
				this.pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setValue(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * this method get the source node of the agent.
	 * @return the integer key of the source node.
	 */
	// @Override
	public int getSrcNode() {
		return this.currNode.getKey();
	}
	/**
	 * this method serializes the agent to an output for the server (or other) uses.
	 * @return a JSON formated String describing the agent and his characteristics.
	 */ 
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{" + "\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":"
				+ this.currNode.getKey() + "," + "\"dest\":" + d + "," + "\"speed\":" + this.getSpeed() + ","
				+ "\"pos\":\"" + pos.toString() + "\"" + "}" + "}";
		return ans;
	}
	/**
	 * this method sets the value (score) of the agent
	 * @param v which taken from the captured pokemon value 
	 */
	private void setValue(double v) {
		value = v;
	}
	
	/**
	 * this boolean method checks if the agent is on an edge or an vertex, in order to set the next node
	 * @param dest
	 * @return True if the agent is on an edge, False if the agent is on an vertex. 
	 */
	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this.currNode.getKey();
		this.currEdge = graph.getEdge(src, dest);
		if (currEdge != null) {
			ans = true;
		} else {
			currEdge = null;
		}
		return ans;
	}
	/**
	 * this method sets the current node for the agent
	 * @param src
	 */
	public void setCurrNode(int src) {
		this.currNode = graph.getNode(src);
	}
	/**
	 * this method checks whether the agent is moving or not, based on the edge.
	 * @return True if he is moving, False if he is not moving.
	 */
	public boolean isMoving() {
		
		return this.currEdge != null;
	}
	/**
	 * a toString method, for self checking.
	 * @return an indication String, JSON formated
	 */
	public String toString() {
		return toJSON();
	}
	/**
	 * another to String method, for self checking
	 * @return a non JSON formated indication string
	 */
	public String toString1() {
		String ans = "" + this.getID() + "," + pos + ", " + isMoving() + "," + this.getValue();
		return ans;
	}
	/**
	 * gets the unique id of the agent
	 * @return an integer represent the id
	 */
	public int getID() {
		return this.id;
	}
	/**
	 * gets the geo location of the agent
	 * @return a geo_location type belongs to the agent.
	 */
	public geo_location getLocation() {
		return pos;
	}
	/**
	 * get the value of the agent
	 * @return a double type variable that represents the score this specific agent scored so far.
	 */
	public double getValue() {
		return this.value;
	}	
	/**
	 * a method to get the next node that the agent is meant to go to.
	 * @return the integer key of the destination.
	 */
	public int getNextNode() {
		int ans = -2;
		if(this.currEdge==null) {
			ans = -1;
		}
		else {
			ans = this.currEdge.getDest();
		}
		return ans;
	}
	
	/**
	 * a method to get the speed of an agent
	 * @return a Double type represents the speed
	 */
	public double getSpeed() {
		return this.speed;
	}
	/**
	 * sets the speed of an agent
	 * @param v
	 */
	public void setSpeed(double v) {
		this.speed = v;
	}
	/**
	 * Speed Distance Time: this method determines the speed that the agent will move on an edge
	 * based on the time and the length of an edge.
	 * @param time
	 * 
	 */
	public void set_SDT(long time) {
		long ddt = (long) (time + this.getSpeed());
		if (this.currEdge != null) {
			double weight = getcurrEdge().getWeight();
			geo_location dest = graph.getNode(getcurrEdge().getDest()).getLocation();
			geo_location src = graph.getNode(getcurrEdge().getSrc()).getLocation();
			double edgeLength = src.distance(dest);
			double agentPosOnEdge = pos.distance(dest);
			if (this.getCurrPokemon().get_edge() == this.getcurrEdge()) {
				agentPosOnEdge = currPokemon.getLocation().distance(this.pos);
			}
			double norm =agentPosOnEdge / edgeLength;
			double dt = weight * norm / this.getSpeed();
			ddt = (long) (1000.0 * dt);
		}
	
		this.set_sg_dt(ddt);
	}
	/**
	 * gets the current edge the agent is on.
	 * 
	 * @return ede_data type representing the edge
	 */
	public edge_data getcurrEdge() {
		return this.currEdge;
	}
	/**
	 * get the speed that the agent needs to move across an edge
	 * @return long type parameter.
	 */
	public long get_sg_dt() {
		return _sg_dt;
	}
	/**
	 * sets the speed the agent needs to be to get across an edge.
	 * @param _sg_dt
	 */
	public void set_sg_dt(long _sg_dt) {
		this._sg_dt = _sg_dt;
	}
	/**
	 * gets the current pokemon the agent is after.
	 * @return a CL_Pokemon type pokemon that exists on the graph on a specific edge.
	 */
	public CL_Pokemon getCurrPokemon() {
		return currPokemon;
	}
	/**
	 * sets the agent a pokemon to go after.
	 * @param currPokemon
	 */
	public void setCurrPokemon(CL_Pokemon currPokemon) {
		this.currPokemon = currPokemon;
	}
	/**
	 * this method gets the mission of the agent.
	 * @return a list that contains all the nodes the agent is meant to follow in order to catch
	 * the pokemon.
	 */
	public List<node_data> getMission() {
		
		return mission;
	}
	/**
	 * sets the mission to an agent
	 * @param l: the agent receives a list with nodes to go to.
	 */
	public void setMission(List<node_data> l) {
		l.remove(0);
		this.mission = l;
	}
	public CL_Pokemon getPokemon() {
		return pokemons.poll();
	}

	public void addPokemon(CL_Pokemon pokemon) {
		this.pokemons.add(pokemon);
	}

	public class comparePokemons implements Comparator<CL_Pokemon>{
		int ID;
		public comparePokemons(int ID) {
			this.ID = ID;
		}
		
		@Override
		public int compare(CL_Pokemon p1, CL_Pokemon p2) {
			if(p1.getAgentDist(ID) > p2.getAgentDist(ID))
				return 1;
			else if(p1.getAgentDist(ID) < p2.getAgentDist(ID))
				return -1;
			return 0;
		}
		
	}

	public void restartPokemons() {
		this.pokemons = new PriorityQueue<CL_Pokemon>(new comparePokemons(this.id));
	}

	public PriorityQueue<CL_Pokemon> getPokemonQueue() {
		return this.pokemons;
	}

}