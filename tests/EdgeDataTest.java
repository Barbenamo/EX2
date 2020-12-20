package api;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank date: 20/12/2020 this
 *           class is a test class for EdgeData class.
 *
 */
class EdgeDataTest {
	@Test
	void srcTest() {
		edge_data[] Edges = new edge_data[5];
		for (int i = 0; i < 5; i++) {
			Edges[i] = new EdgeData(i, i + 1, Double.MAX_VALUE);
			assertTrue(Edges[i].getSrc() == i);
			assertTrue(Edges[i].getDest() == i + 1);
		}
	}

	@Test
	void destTest() {
		edge_data[] Edges = new edge_data[5];
		for (int i = 0; i < 5; i++) {
			Edges[i] = new EdgeData(i, i + 1, Double.MAX_VALUE);
			assertTrue(Edges[i].getDest() == (i + 1));
			assertFalse(Edges[i].getDest() == i);
		}
	}

	@Test
	void weightTest() {
		edge_data[] Edges = new edge_data[5];
		for (int i = 0; i < 5; i++) {
			Edges[i] = new EdgeData(i, i + 1, Double.MAX_VALUE);
			assertTrue(Edges[i].getWeight() == Double.MAX_VALUE);
			Edges[i] = new EdgeData(i, i + 1, i);
			assertTrue(Edges[i].getWeight() == i);
			assertFalse(Edges[i].getWeight() == i + 1);
		}
	}

	@Test
	void infoTest() {
		edge_data[] Edges = new edge_data[5];
		for (int i = 0; i < 5; i++) {
			Edges[i] = new EdgeData(i, i + 1, Double.MAX_VALUE);
			Edges[i].setInfo("white");
			assertTrue(Edges[i].getInfo().equals("white"));
		}
	}

	@Test
	void tagTest() {
		edge_data[] Edges = new edge_data[5];
		for (int i = 0; i < 5; i++) {
			Edges[i] = new EdgeData(i, i + 1, Double.MAX_VALUE);
			int tag = Edges[i].getTag();
			Edges[i].setTag(Integer.MAX_VALUE);
			assertTrue(Edges[i].getTag() == Integer.MAX_VALUE);
			assertFalse(Edges[i].getTag() != tag);
		}
	}
}