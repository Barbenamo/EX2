package api;

public class GeoLocation implements geo_location{
	private double x;
	private double y;
	private double z;
	
	public GeoLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public GeoLocation(String location) {
		 String [] numbers = location.split(",");
		 this.x = Double.parseDouble(numbers[0]);
		 this.y = Double.parseDouble(numbers[1]);
		 this.z = Double.parseDouble(numbers[2]);
	}
	
	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public double z() {
		return z;
	}

	@Override
	public double distance(geo_location g) {
		double d = Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) +Math.pow(this.z - g.z(), 2));
		return d;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof geo_location))
			return false;
		geo_location l = ((GeoLocation)o);
		if(l.x() != this.x() || l.y() != this.y() || l.z() != this.z())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "" + x + "," + y + "," + z;
	}


}
