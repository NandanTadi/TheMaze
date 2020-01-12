package application;

public class Polygons{
	
	private int[] x;
	private int[] y;
	private int size;

	public Polygons(int[] x, int[] y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	public int[] getX() {
		return x;
	}
	public int[] getY() {
		return y;
	}
	public int size() {
		return size;
	}
}
