package api;

import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank date: 20/12/2020 this
 *           class is a test class for GeoLocation class.
 *
 */
class GeoLocationTest {
	private Point3D point;

	@Test
	void distanceTest() {
		for (int i = 0; i < 5; i++) {
			point = new Point3D(i, i + 1, i + 2);
			geo_location graph = new GeoLocation(i, i + 1, i + 2);
			double checkPoint = Math.sqrt(Math.pow(point.y() - graph.y(), 2) + Math.pow(point.x() - graph.x(), 2)
					+ Math.pow(point.z() - graph.z(), 2));
			assertTrue(graph.distance(point) == checkPoint);
		}
	}
}