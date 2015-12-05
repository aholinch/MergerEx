/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.slider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class CrossHairPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	// cross-hair position
	protected double xpos = 0;
	protected double ypos = 0;
	
	// initial cross-hair position
	protected double initXPos = 0;
	protected double initYPos = 0;
	
	// cross-hair size
	protected int size = 20;
	protected int thickness = 4;
	
	// grid size
	protected int gs = 2;
	// number of grid cells in each dimension
	protected int ng = 5;
	
	// whether to paint the init cross-hair
	protected boolean paintInitCrossHair = false;
	
	// whether to paint the gird
	protected boolean paintGrid = true;
	
	//protected Color crossHairColor = Color.BLACK;
	protected Color crossHairColor = null;
	//protected Color initCrossHairColor = new Color(53,52,51);
	protected Color initCrossHairColor = null;
	//protected Color initCrossHairColor = Color.BLACK;
	
	//protected Color gridColor = new Color(139,138,137);
	//protected Color gridColor = new Color(102,101,100);
	protected Color gridColor = null;

	public CrossHairPanel()
	{
		this(300,300);
	}
	
	public CrossHairPanel(int w, int h)
	{
		super();
		setSize(w,h);
		setPreferredSize(new Dimension(w,h));
		if(w < 150 || h < 150)
		{
			setMinimumSize(new Dimension(w,h));
		}
		else
		{
			setMinimumSize(new Dimension(150,150));
		}
		//setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	public void resetPos()
	{
		int width = getWidth();
		int height = getHeight();
		
		double x = width*0.5;
	    double y = height*0.5;
		setInitPos(x,y,false);
		setPos(x,y,false);
	}
	
	public Color getGridColor()
	{
	    if(gridColor == null)
	    {
	    	gridColor = getBackground().darker();
	    }
	    
	    return gridColor;
	}
	
	public Color getCrossHairColor()
	{
	    if(crossHairColor == null)
	    {
	    	crossHairColor = getForeground();
	    }
	    
	    return crossHairColor;
	}
	
	public Color getInitCrossHairColor()
	{
	    if(initCrossHairColor == null)
	    {
	    	initCrossHairColor = getBackground().brighter();
	    }
	    
	    return initCrossHairColor;
	}
	
	public void setPos(double x, double y)
	{
		setPos(x,y,true);
	}
	
	public void setPos(double x, double y, boolean doPaint)
	{
		this.xpos = x;
		this.ypos = y;

		if(doPaint)
		{
			repaint();
		}
	}
	
	public void setInitPos(double x, double y)
	{
		setInitPos(x,y,true);
	}
	
	public void setInitPos(double x, double y, boolean doPaint)
	{
		this.initXPos = x;
		this.initYPos = y;

		if(doPaint)
		{
			repaint();
		}
	}
	
	/**
	 * Build the rectangle coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param thickness
	 * @param isVertical
	 * @return
	 */
	protected int[] getRectangle(double x, double y, double size, double thickness, boolean isVertical)
	{
		int i[] = new int[4];
		
		if(isVertical)
		{
			i[0] = (int)(x-thickness*0.5);
			i[1] = (int)(y-size*0.5);
			i[2] = (int)thickness;
			i[3] = (int)(size);
		}
		else
		{
			i[0] = (int)(x-size*0.5);
			i[1] = (int)(y-thickness*0.5);
			i[2] = (int)(size);
			i[3] = (int)thickness;
		}
		
		return i;
	}
	
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		
		int w = getWidth();
		int h = getHeight();
		
		double dx = ((double)w)/((double)ng);
		double dy = ((double)h)/((double)ng);
		
		g.setColor(getGridColor());
		
		double x = -((double)gs)*0.5;
		double y = -((double)gs)*0.5;
		
		for(int i=0; i<ng-1; i++)
		{
			x+=dx;
			y+=dy;
		    g.fillRect((int)x,0,gs,h);	
		    g.fillRect(0,(int)y,w,gs);	
		}
		
		if(paintInitCrossHair)
		{
			paintCursor(g,initXPos,initYPos,size,thickness,getInitCrossHairColor(),getCrossHairColor());
		}

		paintCursor(g,xpos,ypos,size,thickness,getCrossHairColor(),Color.BLACK);
		//paintCursor(g,xpos,ypos,size,thickness,Color.BLACK,getCrossHairColor());
		
	}
	
	protected void paintCursor(Graphics g, double x, double y, int size, int thickness, Color inner, Color outline)
	{
		int ip[] = null;
		
		if(outline != null)
		{
			// fill the outline
			g.setColor(outline);

			ip = getRectangle(x,y,size+2,thickness+2,true);
			g.fillRect(ip[0],ip[1],ip[2],ip[3]);
		
			ip = getRectangle(x,y,size+2,thickness+2,false);
			g.fillRect(ip[0],ip[1],ip[2],ip[3]);
		}
		
		// fill the interior
		g.setColor(inner);
		
		ip = getRectangle(x,y,size,thickness,true);
		g.fillRect(ip[0],ip[1],ip[2],ip[3]);
		
		ip = getRectangle(x,y,size,thickness,false);
		g.fillRect(ip[0],ip[1],ip[2],ip[3]);
	}
}
