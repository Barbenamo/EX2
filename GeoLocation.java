package api;
/**
 * @authors: Bar Ben Amo & Dror Tapiero & Chaya Blank.
 *this class impelemnts geo-location 
 *it has 3 base coordinates to mark 3D location. 
 * ///////////////////////////////////
 *  characteristics and attributes:
 *  this class has only 3 Double typed variable to represent that represents geographic coordinates
 *  of a node or an edge.
 * /////////////////////////////////// 
 *
 */
public class GeoLocation implements geo_location {
	private double x,y,z;
	/**
	 * a copy-constructor that creates a location using the 3 given coordinates.
	 * @param x
	 * @param y
	 * @param z
	 */
	public GeoLocation(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * get the x coordinate.
	 * @return Double x which represents the coordinate.
	 */
	@Override
	public double x() {
		
		return x;
	}
	/**
	 * get the y coordinate.
	 * @return Double y which represents the coordinate.
	 */
	@Override
	public double y() {
		
		return y;
	}
	/**
	 * get the z coordinate.
	 * @return Double z which represents the coordinate.
	 */
	@Override
	public double z() {
		
		return z;
	}
	/**
	 * a method that calculate the root mean square distance form this location to the given location.
	 * @param g
	 * @return a Double type represents the distance.
	 * 
	 */
	@Override
	public double distance(geo_location g) {
		
		return Math.sqrt(Math.pow(g.x()-this.x, 2)+Math.pow(g.y()-this.y, 2)+Math.pow(g.z()-this.z, 2));
	}
	/**
	 * a to- String method to describe the location.
	 * for self uses.
	 * @return String type.
	 */
	public String toString() {
		return "" + x + "," + y + "," + z ;
	}
	/**
	 * a constructor that builds a geo location using a String.
	 * @param location
	 */
	public GeoLocation(String location) {
		String[] numbers=location.split(",");
		this.x=Double.parseDouble(numbers[0]);
		this.y=Double.parseDouble(numbers[1]);
		this.z=Double.parseDouble(numbers[2]);
	}
	@Override
	/**
	 *  a boolean method to check whether two locations are equals to each other based on their 
	 *  coordinates.
	 *  @return True: if they are equal, False: if they are different. 
	 */
	public boolean equals(Object o) {
		if (!(o instanceof geo_location))
			return false;
		geo_location l = ((GeoLocation) o);
		if (l.x() != this.x() || l.y() != this.y() || l.z() != this.z())
			return false;
		return true;
	}

}
