package api;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank date: 20/12/2020 this
 *           class is a test class for NodeData class.
 *
 */
class NodeDataTest {
	node_data[] Nodes;

	@Test
	void keyTest() {
		Nodes = new NodeData[5];
		for (int i = 0; i < 5; i++) {
			geo_location location = new GeoLocation(i, i + 1, i + 2);
			Nodes[i] = new NodeData(i, location);
			assertTrue(Nodes[i].getKey() == i);
		}
	}

	@Test
	void locationTest() {
		Nodes = new NodeData[5];
		for (int i = 0; i < 5; i++) {
			geo_location location = new GeoLocation(i, i + 1, i + 2);
			Nodes[i] = new NodeData(i, location);
			assertTrue(Nodes[i].getLocation() == location);
		}
	}

	@Test
	void weightTest() {
		Nodes = new NodeData[5];
		for (int i = 0; i < 5; i++) {
			geo_location location = new GeoLocation(i, i + 1, i + 2);
			Nodes[i] = new NodeData(i, location);
			double weight = 8.7;
			Nodes[i].setWeight(weight);
			assertTrue(Nodes[i].getWeight() == 8.7);
		}
	}

	@Test
	void infoTest() {
		Nodes = new NodeData[5];
		for (int i = 0; i < 5; i++) {
			geo_location location = new GeoLocation(i, i + 1, i + 2);
			Nodes[i] = new NodeData(i, location);
			String str = "white";
			Nodes[i].setInfo(str);
			assertTrue(Nodes[i].getInfo().equals("white"));
		}
	}

	@Test
	void tagTest() {
		Nodes = new NodeData[5];
		for (int i = 0; i < 5; i++) {
			geo_location location = new GeoLocation(i, i + 1, i + 2);
			Nodes[i] = new NodeData(i, location);
			int tag = 0;
			Nodes[i].setTag(tag);
			assertFalse(Nodes[i].getTag() == Integer.MAX_VALUE);
			assertTrue(Nodes[i].getTag() == 0);
		}
	}
}