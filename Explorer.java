package application;
import java.awt.*;
public class Explorer
{
	private Location loc;
	private int size;
	private int r, c;

	public Explorer(int r, int c, int size)
	{
		loc = new Location(r,c);
		this.size=size;
		this.r = r;
		this.c = c;
	}
	public Rectangle getRect()
	{
		return new Rectangle(loc.getCol()*size,loc.getRow()*size,size,size);
	}
	public Location getLoc() {
		return loc;
	}
}