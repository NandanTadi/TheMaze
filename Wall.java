package application;
import java.awt.*;
public class Wall
{
	Location loc;
	int size;
	public Wall(int r, int c, int size)
	{
		loc=new Location(r,c);
		this.size=size;
	}
	public Rectangle getRect()
	{
		return new Rectangle(loc.getCol()*size,loc.getRow()*size,size,size);
	}
}