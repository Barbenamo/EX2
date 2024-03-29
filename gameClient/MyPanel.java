package gameClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;


public class MyPanel extends JPanel{
	private int _ind;
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	private static long time;
	private BufferedImage greenPokemon;
	private BufferedImage orangePokemon;
	private BufferedImage vertex;
	private Image screen;
	private Image agent;
	
	public MyPanel(int _ind, Arena _ar, Range2Range _w2f, long time) {
		//this.setBackground(Color.pink);
		this._ar = _ar;
		this._ind = _ind;
		this._w2f = _w2f;
		initImages();
	}
	
	public void initImages() {
		try {
			greenPokemon = ImageIO.read(new File("data/green.jpg"));
			orangePokemon = ImageIO.read(new File("data/orange.jpg"));
			vertex = ImageIO.read(new File("data/��vertex.jpg"));
			screen = ImageIO.read(new File("data/��screen.jpg"));
			agent = ImageIO.read(new File("data/agent.jpg"));
		}
		catch (IOException e) {
		}
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
        int w = this.getWidth();
        int h = this.getHeight();
        g.drawImage(screen, 0,0, w, h, null);
	  }
	
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
	}
	
	
	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
	}
	
	public void paint(Graphics g) {
		
	    Graphics2D myG = (Graphics2D)g;
		int w = this.getWidth()+10000;
		int h = this.getHeight();
//		g.clearRect(0, 0, w, h);
		
		paintComponent(myG);
		updateFrame();
//		g.drawImage(screen, 0, 0,w,h, null);
		drawPokemons(myG);
		drawGraph(myG);
		drawAgants(myG);
		drawInfo(myG);
		drawTime(myG);
	}
	
	void updateTime(long time) {
		this.time = time;
	}
	
	private void drawTime(Graphics g) {
		g.drawString("Time to end: " + time/1000 + " sec", 10,10);
	}

	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}
		
	}
	private void drawGraph(Graphics g) {
		
		directed_weighted_graph gg = _ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n,5,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		int index = 0;
		while(itr.hasNext()) {
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=10;
			BufferedImage image = greenPokemon;
			g.setColor(Color.green);
			if(f.getType()<0) {image = orangePokemon;}
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				g.drawImage(image, (int)fp.x()-3*r, (int)fp.y()-3*r, 6*r, 6*r, this);
				String edge = f.get_edge()==null?" none":f.get_edge().toString();
				String dist = ""+f.getMin_dist();
				//String agent =""+ (f.getAgent() == null ? "none" : f.getAgent().getID());
				String print = "Edge: " + edge + ", shortest dist = " + dist;// + ", agent: " +agent;
//				g.drawString(print,  700, index +40);
				
			}
			index += 40;
			}
		}
	}
	private void drawAgants(Graphics g) {
		List<CL_Agent> rs = _ar.getAgents();
	//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.red);
		int i=0;
		while(rs!=null && i<rs.size()) {
			geo_location c = rs.get(i).getLocation();
			CL_Agent a = rs.get(i);
			int r=8;
			i++;
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				g.drawImage(this.vertex, (int)fp.x()-2*r, (int)fp.y()-2*r, 6*r, 6*r, this);
			//	g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
				g.drawString(""+a.getValue(), (int)fp.x()-r, (int)fp.y()-4*r);
				String print = ""+a.getID()+" : value = "+ a.getValue() + ", Pokemon = " + a.getCurrPokemon().get_edge().getSrc() + " -> " + a.getCurrPokemon().get_edge().getDest();
				print += ", SDT: " + a.get_sg_dt();
			//	g.drawString(print, a.getID()*2 + 10, 10+ print.length());

			}
		}
	}
	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		g.drawImage(vertex, (int)fp.x()-2*r, (int)fp.y()-2*r, 4*r, 4*r, this);
	//	g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
	//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}
}