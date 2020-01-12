package application;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
public class MazeProgram extends JPanel implements KeyListener,MouseListener
{
	JFrame frame;
	int x=20,y=20;
	ArrayList<ArrayList<Character>> mazeSet = new ArrayList<ArrayList<Character>>();
	ArrayList<Explorer> explorers = new ArrayList<Explorer>();
	ArrayList<Polygons> walls = new ArrayList<Polygons>();
	ArrayList<GradientPaint> gradients = new ArrayList<GradientPaint>();
	GraphicsContext gc;
	String direction = "S";
	String[] dOps = {"N", "E", "S", "W"};
	int teleport = 2;
	int locSaver = 2;
	int locSaveX, locSaveY;
	int count = 0;
	int showIt = 1;
	int startR = 240, startG = 240, startB = 240, endR = 0, endG = 0, endB = 0;
	BufferedImage image;
   	
	public MazeProgram()
	{
		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,800);
		frame.setVisible(true);
		frame.addKeyListener(this);
		this.addMouseListener(this);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,1000,800); 
							
		
		for(int i = 0; i < mazeSet.size(); i++) {
			for(int j = 0; j < mazeSet.get(0).size(); j++) {
				if(mazeSet.get(i).get(j) == ' ') {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect((j)*10, i*10, 10, 10);
				}
				else {
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect((j)*10, i*10, 10, 10);
				}	
			}
		}
		
		
		
		g.setColor(Color.green);
		g.fillRect(locSaveX, locSaveY, 10, 10);
		
		g.setColor(Color.BLUE);
		g.fillRect(20, 20, 10, 10);
		g.fillRect(710, 270, 10, 10);

		g.setColor(Color.red);
		g.fillOval(x, y, 10, 10);
		
		
		if(showIt % 2 == 0) {
			for(int d = 0;d < walls.size(); d++)
			{				
				g2.setPaint(gradients.get(d));
				g.fillPolygon(walls.get(d).getX(), walls.get(d).getY(), 4);
				g.setColor(Color.black);
				g.drawPolygon(walls.get(d).getX(), walls.get(d).getY(), 4);			
			}
		}
		g.setColor(Color.red);
		g.drawString("Teleports Remaining: " + teleport, 760, 25);
		
		if(locSaver == 1) {
			g.drawString("LOCATION HAS BEEN SAVED" , 760, 50);
		}

		
		g.setColor(Color.red);
		g.drawString("CONTROLS", 760, 600);
		g.drawString("SPACE = DISABLE/ENABLE", 760, 625);
		g.drawString("T = Teleport To Random Location", 760, 650);
		g.drawString("S = Save Location / Go Back", 760, 675);
		g.drawString("DEVELOPED BY NANDAN TADI", 760, 725);

		if(x == 200 && y == 3) {
			g.drawString("GOOD JOB YOU HAVE SURVIVED", 200, 300);
		}
		
		if(count == 0) {
			try {
				image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/east.jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		g.drawImage(image, 800, 100, 150, 150, null);
		
	}
	public void setBoard()
	{

		File name = new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/maze1.txt");
		try
		{			
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			String remove = input.readLine();
			while( (text=input.readLine())!= null)
			{								
				mazeSet.add(new ArrayList<Character>());
							
				char[] temp = text.toCharArray();
				for(int i = 0; i < temp.length; i++) {
					mazeSet.get(count).add(temp[i]);
				}
				count++;
			}
		}
		catch (IOException io)
		{
			System.err.println("File error");
		}


        URL resource = getClass().getResource("audio.wav");
        AudioClip clip = new AudioClip(resource.toString());
        clip.play();
	    
		setWalls();
	}
	
	public void setWalls()
	{
		walls.clear();
		gradients.clear();
		explorers.add(new Explorer(x, y ,10));
		
		floor();
		ceiling();

		int currentCol = ((explorers.get(explorers.size()-1).getLoc().getRow()))/10;
		int currentRow = (explorers.get(explorers.size()-1).getLoc().getCol())/10;
				
		switch(direction) 
		{
			case "N":
				for(int i = 4; i >= 0; i--) {
					if (currentCol - 1 >= 0 && currentRow - i -1>= 0 && mazeSet.get(currentRow - i).get(currentCol - 1) == '#'){
						left(i);
					}
					if (currentCol + 1 >= 0 && currentRow - i -1>= 0 && mazeSet.get(currentRow - i).get(currentCol + 1) == '#'){
						right(i);
					}
					if(currentRow - i >= 0 &&mazeSet.get(currentRow - i).get(currentCol) == '#') {
						front(i);
					}

				}
			break;
			
			case "E":
				for(int i = 4; i >= 0; i--) {
					if (currentRow - 1 >= 0 && currentCol - i - 1 >= 0 && mazeSet.get(currentRow - 1).get(currentCol + i) == '#'){
						left(i);
					}
					if (currentRow + 1 >= 0 && currentCol - i - 1 >= 0 && mazeSet.get(currentRow + 1).get(currentCol + i) == '#'){
						right(i);
					}
					if(currentCol + i >= 0 &&mazeSet.get(currentRow).get(currentCol + i) == '#') {
						front(i);
					}
					
				}
			break;
			
			case "S":
				for(int i = 4; i >= 0; i--) {
					if (currentCol - 1 >= 0 && currentRow + i -1 >= 0 && mazeSet.get(currentRow + i).get(currentCol + 1) == '#'){
						left(i);
					}
					if (currentCol - 1 >= 0 && currentRow + i -1 >= 0 && mazeSet.get(currentRow + i).get(currentCol - 1) == '#'){
						right(i);
					}
					if(currentRow + i >= 0 &&mazeSet.get(currentRow + i).get(currentCol) == '#') {
						front(i);
					}
				}
			break;
			
			case "W":
				for(int i = 4; i >= 0; i--) {
					if(currentCol + 1 >= 0 && currentRow - i >= 0 && mazeSet.get(currentRow + 1).get(currentCol - i) == '#') {
						left(i);
					}
					if(currentCol - 1 >= 0 && currentRow - i >= 0 && mazeSet.get(currentRow - 1).get(currentCol - i) == '#') {
						right(i);
					}
					if(currentCol - i >= 0 &&mazeSet.get(currentRow).get(currentCol - i) == '#') {
						front(i);
					}
				}
			break;
		}
		
	}

	public void keyPressed(KeyEvent e)
	{
		
		int tempX = x;
		int tempY = y;
		
		if(e.getKeyCode()==32) {
			showIt++;
		}
		if(e.getKeyCode()==37) {
			count++;
			if(direction == "E") {
				direction = "N";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/north.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "N") {
				direction  = "W";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/west.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "W") {
				direction = "S";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/south.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "S") {
				direction = "E";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/east.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			setWalls();
			repaint();

		}
		if(e.getKeyCode()==39) {
			if(direction == "E") {
				direction = "S";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/south.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "S") {
				direction  = "W";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/west.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "W") {
				direction = "N";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/north.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(direction == "N") {
				direction = "E";
				try {
					image = ImageIO.read(new File("/Users/suribabu_tadi/eclipse-workspace/TheMaze/src/application/east.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
		}
		if(e.getKeyCode()==38) {
			if(direction == "N") {
				y-=10;
			}
			if(direction == "E") {
				x+=10;
			}
			if(direction == "S") {
				y+=10;
			}
			if(direction == "W") {
				x-=10;
			}
			
	        URL resource2 = getClass().getResource("step.wav");
	        AudioClip clip2 = new AudioClip(resource2.toString());
			clip2.play();
			
			setWalls();
			repaint();

		}
		if(teleport != 0 && e.getKeyCode() == 84) {
		String checker = "no";
			while(checker == "no") {
				x = (int)(Math.random()*710)+1;
				y = (int)(Math.random()*290)+1;
				if(mazeSet.get(y/10).get(x/10) == ' ') {
					checker = "yes";
				}
			}
			teleport--;
		}
		if(teleport != 0 && e.getKeyCode() == 83) {
			locSaver--;
			if(locSaver == 1) {
				locSaveX = x;
				locSaveY = y;
			}
			if(locSaver == 0) {
				x = locSaveX;
				y = locSaveY;
				locSaver = 2;
				locSaveX = 800;
				locSaveY = 800;
			}
		}
		if(mazeSet.get(y/10).get(x/10) == '#') {
			x = tempX;
			y = tempY;
		}
		
		explorers.clear();
		explorers.add(new Explorer(x, y, 10));
		System.out.println(direction);
		
		setWalls();
		repaint();
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("X PLOT: " + e.getX());
		System.out.println("Y PLOT: " + e.getY());
		System.out.println();
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public void ceiling() {
		for(int i = 0; i < 5; i++) {
			int[] x={50+50*i,750-50*i,700-50*i,100+50*i};
			int[] y={50+50*i,50+50*i,100+50*i,100+50*i};
			walls.add(new Polygons(x, y , 4));
			endR = startR - 50 - 50 * i;
			endG = startG - 50 - 50 * i;
			endB = startB - 50 - 50 * i;
			if(endR < 0) {
				endR = 0;
			}
			if(endG < 0) {
				endG = 0;
			}
			if(endB < 0) {
				endB = 0;
			}
			gradients.add(new GradientPaint(x[0], y[0], new Color(startR - 50 * i, startG - 50 * i, startB - 50 * i), x[0], y[3], new Color(endR, endG, endB)));
			repaint();
		}
	}
	public void floor() {
		for(int i = 0; i < 5; i++) {
			int[] x={100+50*i,700-50*i,750-50*i,50+50*i};
			int[] y={700-50*i,700-50*i,750-50*i,750-50*i};
			walls.add(new Polygons(x, y , 4));
			endR = startR - 50 - 50 * i;
			endG = startG - 50 - 50 * i;
			endB = startB - 50 - 50 * i;
			if(endR < 0) {
				endR = 0;
			}
			if(endG < 0) {
				endG = 0;
			}
			if(endB < 0) {
				endB = 0;
			}
			gradients.add(new GradientPaint(x[2], y[2], new Color(startR - 50 * i, startG - 50 * i, startB - 50 * i), x[2], y[0], new Color(endR, endG, endB)));
			repaint();
		}
	}
	public void left(int i) {
		int[] y={50+50*i,750-50*i,700-50*i,100+50*i};
		int[] x={50+50*i,50+50*i,100+50*i,100+50*i};
		walls.add(new Polygons(x, y , 4));
		endR = startR - 50 - 50 * i;
		endG = startG - 50 - 50 * i;
		endB = startB - 50 - 50 * i;
		if(endR < 0) {
			endR = 0;
		}
		if(endG < 0) {
			endG = 0;
		}
		if(endB < 0) {
			endB = 0;
		}
		gradients.add(new GradientPaint(x[0], y[0], new Color(startR - 50 * i, startG - 50 * i, startB - 50 * i), x[2], y[0], new Color(endR, endG, endB)));
		repaint();
	}
	public void right(int i) {
		int[] y={100+50*i,700-50*i,750-50*i,50+50*i};
		int[] x={700-50*i,700-50*i,750-50*i,750-50*i};
		endR = startR - 50 - 50 * i;
		endG = startG - 50 - 50 * i;
		endB = startB - 50 - 50 * i;
		if(endR < 0) {
			endR = 0;
		}
		if(endG < 0) {
			endG = 0;
		}
		if(endB < 0) {
			endB = 0;
		}
		gradients.add(new GradientPaint(x[3], y[3], new Color(startR - 50 * i, startG - 50 * i, startB - 50 * i), x[0], y[3], new Color(endR, endG, endB)));
		walls.add(new Polygons(x, y , 4));
		repaint();
	}
	public void front(int i) {
		int[] x={50+50*i,750-50*i,750-50*i,50+50*i};
		int[] y={50+50*i,50+50*i,750-50*i,750-50*i};
		endR = startR - 50 - 50 * i;
		endG = startG - 50 - 50 * i;
		endB = startB - 50 - 50 * i;
		if(endR < 0) {
			endR = 0;
		}
		if(endG < 0) {
			endG = 0;
		}
		if(endB < 0) {
			endB = 0;
		}
		gradients.add(new GradientPaint(x[0], y[1], new Color(startR - 50 * i, startG - 50 * i, startB - 50 * i), x[2], y[3], new Color(endR, endG, endB)));
		walls.add(new Polygons(x, y , 4));
		repaint();
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}