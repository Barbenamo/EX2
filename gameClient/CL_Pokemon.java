package gameClient;

import api.edge_data;
import gameClient.util.Point3D;

import java.util.HashMap;

import org.json.JSONObject;



/**
 * original author: Boaz Ben Moshe.
 * NOTE: this class was edited and modified by the author.
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
 * date: 18/12/2020.
 * this class is used to represent the Pokemon in the directed weighted graph.
 * the pokemon is an one-dimensional dot that exist on the graph's edges.
 * agents are set to catch the pokemons in order to score points.
 * ///////////////////////////////////
 *  characteristics and attributes:
 *  1. each pokemon has an edge_data class variable, which indicates the specific edge that he's located
 *     on.
 *  2. each pokemon has an value, which determines the score that the agent will get when catching it.
 *  3. each pokemon has a *type which equals to +-1.
 *	4. initiation: the initiation of an pokemon is used with a deserialize method using JSON format.
 *  5. a to-String method allows to inform the user about the value and type of the pokemon.
 *  6. a boolean equals method is used to indicate whether two pokemons are equals to each other.
 *  	the difference is based on the location and the type of the pokemons.
 *   7. a hash-map which contains information about the distance between our pokemon and each one 
 *     of the agents. 
 *
 *	*type: the type is an indicator that helps to define the specific location of the pokemon.
 *	example: if the type is 1, the pokemon is on the edge that starts from the lesser vertex key
 *	to the higher vertex key, and vice versa.
 *	type 1: 6--->7
 *	type -1: 7--->6.
 *	the agent have to move on the right edge (considering the type) in order to catch the pokemon.
 *	/////////////////////////////////// 
 */
public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private HashMap <Integer, Double> agentDist;
	
	/**
	 * a constructor to the pokemon, which initiates all his characteristics
	 * @param p: position
	 * @param t: type
	 * @param v: value
	 * @param s: 
	 * @param e: edge
	 */
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
		this.agentDist = new HashMap<>();
	}
	/**
	 * this method deserializes a pokemon for a JSON formated string.
	 * @param json
	 * @return a CL_Pokemon typed pokemon.
	 */
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * a to String method, for self checking.
	 */
	public String toString() {
		return "F:{v=" + _value + ", t=" + _type + "}";
	}
	/**
	 * a get edge method.
	 * @return an edge_data type edge that represents the pokemon's edge.
	 */
	public edge_data get_edge() {
		return _edge;
	}
	/**
	 * set edge method, sets the edge that the pokemons stands on.
	 * @param _edge
	 */
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}
	/**
	 * get the Location of the pokemon
	 * @return a Point3D type which represents the geographic location of the pokemon
	 */
	public Point3D getLocation() {
		return _pos;
	}
	/**
	 * get the type of the pokemon
	 * @return an integer (+-1) that represent the exact edge the pokemon is on.
	 */
	public int getType() {
		return _type;
	}

	/**
	 * get the value of the pokemon, how much does it worth.
	 * @return Double type represents his value.
	 */
	public double getValue() {
		return _value;
	}
	/**
	 * a method to get the minimum distance from a pokemon to an agent.
	 * @return double type represents the distance
	 */
	public double getMin_dist() {
		return min_dist;
	}
	/**
	 * sets the minimum distance.
	 * @param mid_dist
	 */
	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}
	
	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
	/**
	 * a void update method, to re-calibrate the pokemon when its being initiated after his capture.
	 * @param p
	 */
	public void update(CL_Pokemon p) {
		_type = p.getType();
		_value = p.getValue();
		set_edge(p.get_edge());
		_pos = p.getLocation();
	}
	
	@Override
	/**
	 * a boolean method to check whether two pokemons are equals based on their instance, position and value.
	 */
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CL_Pokemon))
			return false;
		CL_Pokemon other=((CL_Pokemon)obj);
		if(!other.getLocation().close2equals(this._pos)||!other.get_edge().equals(this._edge))
			return false;
		if(other.getType()!=this._type||other.getValue()!=this._value)
			return false;
		return true;
	}
	public double getAgentDist(int ID) {
		return agentDist.get(ID);
	}

	public void setAgentDist(int ID, double value) {
		this.agentDist.replace(ID, value);
	}
	
	public void addAgentDist(int ID) {
		this.agentDist.put(ID, -1.0);
	}
}