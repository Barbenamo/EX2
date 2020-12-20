package gameClient;

import api.*;


import org.json.*;
import Server.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ex2 implements Runnable {
	private static MyFrame frame;
	private static Arena arena;
	public static long time;
	private directed_weighted_graph graph;
	private game_service game;
	private static int level;
	private static long id;

	public static void main(String[] a) {
		Thread client = new Thread(new ex2());
		client.start();
//		id = Long.parseLong(a[0]);
//		level = Integer.parseInt(a[1]);
	}

	@Override
	public void run() {
		level= 23;
		id=315212837;
		this.game = Game_Server_Ex2.getServer(level); // you have [0,23] games
		game.login(id);
		
		graph = loadGraph(game.getGraph());
		initGuiArena();
		initGame();
		timeListener l=new timeListener(frame.getPanel());
		game.startGame();
		l.update(game.timeToEnd());
		frame.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
		int ind = 0;
		long dt = 115;
		while (game.isRunning()) {
			moveAgents();
			try {
				if (ind % 1 == 0) {
					l.update(game.timeToEnd());
					frame.repaint();
				}
				dt = setSpeed();
				Thread.sleep(dt);
				ind++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}

	// file write to load a graph.
	public directed_weighted_graph loadGraph(String s) {
		FileWriter file;
		try {
			file = new FileWriter("myGraph");
			file.write(s);
			file.flush();
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.load("myGraph");
		directed_weighted_graph graph = ga.getGraph();
		return graph;
	}

	public void initGuiArena() {
		int numOfAgents = 0;
		try {
			String info = game.toString();
			JSONObject line;
			line = new JSONObject(info);
			JSONObject gameInfo = line.getJSONObject("GameServer");
			numOfAgents = gameInfo.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		arena = new Arena(graph, game.getPokemons(), numOfAgents);
		arena.initPokemons(game.getPokemons());
		ex2.time = game.timeToEnd();
		frame = new MyFrame("test Ex2", time);
		frame.setSize(1000, 700);
		frame.update(arena);
		frame.setVisible(true);
	}

	public void initGame() {
		List<CL_Pokemon> pokemons = arena.getPokemons();
		int index=0;
		for (int a = 0; a < arena.getNumOfAgents(); a++) {
			game.addAgent(pokemons.get(index).get_edge().getSrc());// set the agent as close as possible
			index++;
			if(index>=pokemons.size())
				index=0;
		}
		System.out.println(game.getAgents());
		String a = game.getAgents();
		arena.updateAgents(a);
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(graph);
		for(CL_Agent agent : arena.getAgents()) {
			for(CL_Pokemon pokemon : arena.getPokemons()) {
				pokemon.addAgentDist(agent.getID());
			}
		}
	}

	// this method calculates the shortest distance between an agent to each pokemon
	// on the arena
	public void closestPokemon(CL_Agent agent, dw_graph_algorithms ga) {
		String s=game.getPokemons();
		arena.updatePokemons(s);
		arena.updateAgents(game.getAgents());
		int src = agent.getSrcNode();
		//double dist = -1;

//		CL_Pokemon myP = arena.getPokemons().get(0);
		agent.restartPokemons();
		for (CL_Pokemon p : arena.getPokemons()) {
			int srcPokemon = p.get_edge().getSrc();
			int destPokemon = p.get_edge().getDest();
			double edgeLength = graph.getNode(srcPokemon).getLocation().distance(graph.getNode(destPokemon).getLocation());
			double agentPosOnEdge = graph.getNode(srcPokemon).getLocation().distance(p.getLocation());
			double edgeDist = ga.shortestPathDist(src, p.get_edge().getDest()) + agentPosOnEdge/edgeLength;
			p.setAgentDist(agent.getID(), edgeDist);
			agent.addPokemon(p);
		}
		
		
	}
	
	public void setMission(CL_Agent agent) {
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(graph);
		closestPokemon(agent, ga);
		agent.setCurrPokemon(agent.getPokemon());
		CL_Pokemon p = agent.getCurrPokemon();
		List<node_data>l=ga.shortestPath(agent.getSrcNode(), p.get_edge().getDest());
		if(agent.getSrcNode()==p.get_edge().getDest()){
			l.add(graph.getNode(p.get_edge().getSrc()));
			l.add(graph.getNode(p.get_edge().getDest()));
		}
		agent.setMission(l);
		/*if(level == 1 && agent.getSrcNode()==10 && p.get_edge().getSrc()==10) {
			if(agent.getPokemonQueue().peek().get_edge().equals(p.get_edge())) {
				agent.setWarning(true);
				agent.set_sg_dt(0);
			}
		}*/
		System.out.println("pokemon: " + p.get_edge().toString());
		System.out.print("Mission: ");
		for (node_data runner : agent.getMission()) {
			System.out.print(runner.getKey() + " ");
		}
		System.out.println();
		if(agent.getMission().isEmpty())
			System.out.println();
		while (!agent.getMission().isEmpty()) {
			game.chooseNextEdge(agent.getID(), agent.getMission().get(0).getKey());
			agent.getMission().remove(0);
		}
	}

	public void moveAgents() {
		String s=game.getPokemons();
		arena.updatePokemons(s);
		arena.updateAgents(game.getAgents());
		
		for (CL_Agent a : arena.getAgents()) {
			int nxtNode = a.getNextNode();
			if (!a.isMoving() && (nxtNode == -1 || a.getCurrPokemon() == null)) {
				setMission(a);
				nxtNode = a.getNextNode();
			}
		}
		game.move();
	}
	public long setSpeed()
	{
		long time=95;
		arena.updateAgents(game.getAgents());
		for(CL_Agent agent:arena.getAgents())
		{
			agent.set_SDT(95);
			if(agent.get_sg_dt()<time)
				time=agent.get_sg_dt();
		}
		return time;
	}
}