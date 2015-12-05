/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StarPlot extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected List<double[]> seriesList = null;
	protected List<Color> colors = null;
	protected double[] maxValues = null;
	
    public StarPlot()
    {
    	super();
    	seriesList = new ArrayList<double[]>();
    	colors = new ArrayList<Color>();
    }
    
    public void setMaxValues(double vals[])
    {
    	maxValues = vals;
    }
    
    public void addSeries(double vals[])
    {
    	addSeries(vals,Color.red);
    }
    
    public void addSeries(double vals[], Color c)
    {
        seriesList.add(vals);
        colors.add(c);
    }
    
    public void paintComponent(Graphics g)
    {
    	int w = getWidth();
    	int h = getHeight();
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(0,0,w,h);
    	
    	int cx = (int)(w*0.5);
    	int cy = (int)(h*0.5);
    	
    	int dx = 0;
    	int dy = 0;
    	
    	double rad = 0.9*Math.min(cx,cy);
    	
    	dx = (int)(cx-rad);
    	dy = (int)(cy-rad);
    	
    	g.setColor(Color.LIGHT_GRAY);
 
    	g.drawArc(dx, dy, (int)(2*rad), (int)(2*rad), 0, 360);
    	
    	if(maxValues == null)
    	{
    		return;
    	}
    	
    	int ns = seriesList.size();
    	
    	if(ns == 0)
    	{
    		return;
    	}
    	
    	int numDim = maxValues.length;
    	
    	double vals[];
    	double xy[] = new double[2];
    	double initXY[] = new double[2];
    	double prevXY[] = null;
    	Graphics2D g2d = (Graphics2D)g;
    	Stroke str = new BasicStroke(3);
    	g2d.setStroke(str);
    	for(int s=0; s<ns; s++)
    	{
    		vals = seriesList.get(s);
    		
			setXY(cx,cy,rad,0,numDim,vals[0],maxValues[0],xy);
			initXY[0] = xy[0];
			initXY[1] = xy[1];
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine((int)xy[0], (int)xy[1], cx,cy);
			
			for(int i=1; i<numDim; i++)
    		{
				prevXY = xy;
				xy = new double[2];
    			setXY(cx,cy,rad,i,numDim,vals[i],maxValues[i],xy);
    			
    			g.setColor(Color.LIGHT_GRAY);
    			g.drawLine((int)xy[0], (int)xy[1], cx,cy);
    			
    			g.setColor(colors.get(s));
        		
    			g.drawLine((int)xy[0], (int)xy[1], (int)prevXY[0], (int)prevXY[1]);
    		}
			
			g.drawLine((int)xy[0], (int)xy[1], (int)initXY[0], (int)initXY[1]);
    		
    	}
    }
    
    protected void setXY(int cx, int cy, double rad, int i, int nv, double val, double maxVal, double xy[] )
    {
    	rad *= val/maxVal;
    	
    	double ang = ((double)i)/((double)nv);
    	ang = Math.toRadians(360.0d*ang);
    	
    	xy[0] = rad*Math.cos(ang)+cx;
    	xy[1] = rad*Math.sin(ang)+cy;
    }
    
    public static void main(String args[])
    {
    	StarPlot sp = new StarPlot();
    	
    	Random rand = new Random();
    	
    	int numDim = 13;
    	
    	double maxValues[] = new double[numDim];
    	double series0[] = new double[numDim];
    	double series1[] = new double[numDim];
    	double series2[] = new double[numDim];
    	double series3[] = new double[numDim];
    	for(int i=0; i<numDim; i++)
    	{
    		maxValues[i] = 1.0d;
    		series0[i] = 1.0d;
    		series1[i] = 0.1*rand.nextDouble()+0.85;
    		series2[i] = rand.nextDouble()*0.4+0.35;
    		series3[i] = rand.nextDouble()*0.3+0.05;
    	}
    	
    	sp.setMaxValues(maxValues);
    	sp.addSeries(series0,Color.LIGHT_GRAY);
    	sp.addSeries(series1);
    	sp.addSeries(series2,Color.green);
    	sp.addSeries(series3,Color.blue);
    	
    	JFrame f =new JFrame("Star Plot");
    	f.setSize(500,500);
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.getContentPane().add(sp);
    	f.setVisible(true);
    }
}
