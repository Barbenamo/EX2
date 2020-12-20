package api;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;

import api.DWGraph_DS;
import api.GeoLocation;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank date: 20/12/2020 this
 *           class is a test class for DWGraph_DS class.
 *
 */
class DWGraph_DSTest {
	directed_weighted_graph graphCreator(int vertices, int edges) {
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
	void addRemoveTest() {
		directed_weighted_graph g = new DWGraph_DS();
		// add negative node
		g.addNode(new NodeData(-8, new GeoLocation(1, 2, 3)));
		assertEquals(0, g.nodeSize());
		assertEquals(0, g.getMC());
		// crate graph with nodes 0-4
		g = graphCreator(5, 0);
		assertEquals(5, g.nodeSize());
		// check remove node when node does not exist
		g.removeNode(9);
		assertEquals(5, g.nodeSize());
		// remove a node from the graph
		g.removeNode(3);
		assertEquals(4, g.nodeSize());
		// remove all nodes
		g.removeNode(0);
		g.removeNode(1);
		g.removeNode(2);
		g.removeNode(4);
		assertEquals(0, g.nodeSize());
	}

	@Test
	void connectNodes() {
		// connect node to itself
		directed_weighted_graph g = graphCreator(5, 0);
		g.connect(1, 1, 5.72);
		assertNull(g.getEdge(1, 1));
		assertEquals(0, g.edgeSize());
		// connect to non existing node
		g.connect(1, 9, -5.43);
		assertNull(g.getEdge(1, 9));
		assertEquals(0, g.edgeSize());
		// check connect
		g.connect(1, 2, 5);
		g.connect(1, 3, 6.5);
		g.connect(4, 2, 9);
		g.connect(2, 3, 2);
		assertEquals(4, g.edgeSize());
		// get edge
		assertEquals(6.5, g.getEdge(1, 3).getWeight());
		assertNull(g.getEdge(2, 1));
		g.removeEdge(1, 4);
		assertEquals(4, g.edgeSize());
		g.removeEdge(4, 2);
		assertEquals(3, g.edgeSize());
		assertNull(g.getEdge(4, 2));
	}

	@Test
	void runTime() {
		long start = new Date().getTime();
		directed_weighted_graph graph = graphCreator(1000000, 10000000);
		long end = new Date().getTime();
		assertTrue((end - start) / 1000.0 < 35.0, "Takes to long");
		assertEquals(1000000, graph.nodeSize());
		assertEquals(10000000, graph.edgeSize());

	}
}
