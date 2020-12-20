package api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.GeoLocation;
import api.NodeData;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank date: 20/12/2020 this
 *           class is a test class for DWGraph_Algo class.
 *
 */
class DWGraph_AlgoTest {

	directed_weighted_graph graph_creator(int vertices, int edges) {
		directed_weighted_graph graph = new DWGraph_DS();
		for (int i = 0; i < vertices; i++) {
			double x = Math.random() * 99;
			double y = Math.random() * 99;
			double z = Math.random() * 99;
			node_data node = new NodeData(i, new GeoLocation(x, y, z));
			graph.addNode(node);
		}
		int firstNum = 0;
		int secondNum = 1;
		int index = 0;
		while (index < edges) {
			double w = Math.random() * 99 + 1;
			graph.connect(firstNum, secondNum++, w);
			index++;
			if (secondNum == vertices - 1) {
				firstNum++;
				secondNum = firstNum + 1;
			}
		}
		return graph;
	}

	@Test
	void copyTest() {
		directed_weighted_graph g = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(g);
		directed_weighted_graph clone = new DWGraph_DS();
		clone = ga.copy();
		assertEquals(g, clone);

		g = graph_creator(5, 4);
		assertNotEquals(g, clone);
		ga.init(g);
		clone = ga.copy();
		assertEquals(g, clone);
		clone.addNode(new NodeData(5, new GeoLocation(1, 1, 1)));
		assertNotEquals(clone, g);
	}

	@Test
	void isConnectedTest() {
		directed_weighted_graph g = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(g);
		assertTrue(ga.isConnected());// an empty graph is connected.
		g.addNode(new NodeData(5, new GeoLocation(0, 0, 0)));
		ga.init(g);
		assertTrue(ga.isConnected());// A graph with one node is connected.
		g.addNode(new NodeData(6, new GeoLocation(1, 0, 0)));// adding a new node
		ga.init(g);// initialize
		assertFalse(ga.isConnected());// 2 nodes and no connection between them
		g.connect(5, 6, 2);// connect one to another
		ga.init(g);// initialize
		assertFalse(ga.isConnected());// only one direction, still not connected
		g.connect(6, 5, 3);// make a bi directional path, now the graph is connected
		ga.init(g);// initialize
		assertTrue(ga.isConnected());
		g.removeNode(6);
		g.removeNode(5);
		ga.init(g);

		node_data n1 = new NodeData(1, new GeoLocation(1, 2, 3));
		node_data n2 = new NodeData(2, new GeoLocation(1, 2, 4));
		node_data n3 = new NodeData(3, new GeoLocation(1, 2, 5));
		node_data n4 = new NodeData(4, new GeoLocation(1, 2, 6));
		node_data n5 = new NodeData(5, new GeoLocation(1, 3, 6));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(1, 3, 2);
		g.connect(3, 2, 2);
		g.connect(2, 5, 2);
		g.connect(5, 4, 2);
		g.connect(4, 1, 2);
		ga.init(g);
		assertTrue(ga.isConnected());
		g.removeEdge(4, 1);
		g.connect(4, 2, 3);
		ga.init(g);
		assertFalse(ga.isConnected());
	}

	@Test
	void shortestPathDistTest() {
		directed_weighted_graph g = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(g);
		node_data n1 = new NodeData(1, new GeoLocation(1, 2, 3));
		node_data n2 = new NodeData(2, new GeoLocation(1, 2, 4));
		node_data n3 = new NodeData(3, new GeoLocation(1, 2, 5));
		node_data n4 = new NodeData(4, new GeoLocation(1, 2, 6));
		node_data n5 = new NodeData(5, new GeoLocation(1, 3, 6));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(1, 3, 2);
		g.connect(3, 2, 2);
		g.connect(2, 5, 2);
		g.connect(5, 4, 2);
		g.connect(4, 1, 2);
		ga.init(g);
		assertEquals(6, ga.shortestPathDist(1, 5));
		g.connect(1, 5, 7);
		ga.init(g);
		assertEquals(6, ga.shortestPathDist(1, 5));
		g.removeEdge(4, 1);
		ga.init(g);
		assertEquals(-1, ga.shortestPathDist(4, 1));
		assertEquals(0, ga.shortestPathDist(1, 1));
		assertEquals(-1, ga.shortestPathDist(7, 7));
		assertNull(ga.shortestPath(5, 6));// A non existing nodes

	}

	@Test
	void shortestPathTest() {
		directed_weighted_graph g = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		node_data n1 = new NodeData(1, new GeoLocation(1, 2, 3));
		node_data n2 = new NodeData(2, new GeoLocation(1, 2, 4));
		node_data n3 = new NodeData(3, new GeoLocation(1, 2, 5));
		node_data n4 = new NodeData(4, new GeoLocation(1, 2, 6));
		node_data n5 = new NodeData(5, new GeoLocation(1, 3, 6));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(1, 3, 2);
		g.connect(3, 2, 2);
		g.connect(2, 5, 2);
		g.connect(5, 4, 2);
		g.connect(4, 1, 2);
		ga.init(g);
		LinkedList<node_data> l = new LinkedList<>();
		l.add(n1);
		l.add(n3);
		l.add(n2);
		l.add(n5);
		assertArrayEquals(l.toArray(), ga.shortestPath(1, 5).toArray());
		g.connect(1, 5, 7);
		ga.init(g);
		assertArrayEquals(l.toArray(), ga.shortestPath(1, 5).toArray());
		assertNull(ga.shortestPath(1, 7));
		assertNull(ga.shortestPath(7, 7));
		l = new LinkedList<>();
		l.add(n1);
		assertArrayEquals(l.toArray(), ga.shortestPath(1, 1).toArray());
	}

	@Test
	void saveLoadTest() {
		directed_weighted_graph g = new DWGraph_DS();
		directed_weighted_graph g2 = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		node_data n1 = new NodeData(1, new GeoLocation(1, 2, 3));
		node_data n2 = new NodeData(2, new GeoLocation(1, 2, 4));
		node_data n3 = new NodeData(3, new GeoLocation(1, 2, 5));
		node_data n4 = new NodeData(4, new GeoLocation(1, 2, 6));
		node_data n5 = new NodeData(5, new GeoLocation(1, 3, 6));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(1, 3, 2);
		g.connect(3, 2, 2);
		g.connect(2, 5, 2);
		g.connect(5, 4, 2);
		g.connect(4, 1, 2);
		ga.init(g);
		ga.save("myGraph");
		dw_graph_algorithms g2a = new DWGraph_Algo();
		g2a.init(g2);
		g2a.load("myGraph");
		assertEquals(g2a.getGraph(), ga.getGraph());
	}

}