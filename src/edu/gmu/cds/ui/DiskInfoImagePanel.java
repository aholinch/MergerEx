/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.sim.DiskInfo;

public class DiskInfoImagePanel extends ImagePanel 
{
	private static final long serialVersionUID = 1L;
	
	protected List<ObjectInfo> objectInfos = null;
	protected List<DiskInfo> diskInfos = null;
	protected double arcMinPerPixel = 0;
	
	public DiskInfoImagePanel()
	{
		super();
		init();
	}

	public DiskInfoImagePanel(BufferedImage bi)
	{
		super(bi);
		init();
	}
	
	public void setArcMinPerPixel(double scale)
	{
		arcMinPerPixel = scale;
	}
	
	public void init()
	{
		objectInfos = new ArrayList<ObjectInfo>();
		diskInfos = new ArrayList<DiskInfo>();
	}
	
	public void setObjectInfos(List<ObjectInfo> infos)
	{
		objectInfos = infos;
	}
	
	public void setDiskInfos(List<DiskInfo> infos)
	{
		diskInfos = infos;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paintObjectInfos(g);
		paintDiskInfos(g);
	}
	
	public void paintObjectInfos(Graphics g)
	{
		if(objectInfos == null || objectInfos.size() == 0) return;
		
		int w = getWidth();
		int h = getHeight();
		
		int nx = origImage.getWidth();
		int ny = origImage.getHeight();
		
		int targetX = w;
		int targetY = h;
		
    	// we need to scale image
    	double scaleX = ((double)targetX)/((double)nx);
    	double scaleY = ((double)targetY)/((double)ny);
    	
    	g.setColor(Color.red);
    	
    	int size = objectInfos.size();
    	ObjectInfo info = null;
    	double px = 0;
    	double py = 0;
    	for(int i=0; i<size; i++)
    	{
    		info = objectInfos.get(i);
    		px = info.getPx()*scaleX;
    		py = h-info.getPy()*scaleY;
    		
    		g.fillRect((int)(px-2), (int)(py-2), 5, 5);
    	}
	}
	
	
	public void paintDiskInfos(Graphics g)
	{
		if(diskInfos == null || diskInfos.size() == 0) return;
		
		int w = getWidth();
		int h = getHeight();
		
		int nx = origImage.getWidth();
		int ny = origImage.getHeight();
		
		int targetX = w;
		int targetY = h;
		
    	// we need to scale image
    	double scaleX = ((double)targetX)/((double)nx);
    	double scaleY = ((double)targetY)/((double)ny);
    	
    	g.setColor(Color.red);
    	
    	int size = diskInfos.size();
    	DiskInfo info = null;
    	double px = 0;
    	double py = 0;
    	double a = 0;
    	double b = 0;
    	double theta = 0;
    	double phi = 0;
    	
    	Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.MAGENTA);
		//double sinp = 0;
		//double cosp = 0;
		
    	for(int i=0; i<size; i++)
    	{
    		info = diskInfos.get(i);
    		
    		theta = Math.toRadians(info.getThetaDeg());
    		
    		// to draw ellipse we need to add 90 degs to rotation
    		phi   = Math.toRadians(info.getPhiDeg()+90);
    		
    		// flip phi due to inverted y
    		//sinp = -Math.sin(phi);
    		//cosp = Math.cos(phi);
    		//phi = Math.atan2(sinp, cosp);
    		
    		a = info.getRadArcMin()/arcMinPerPixel;
    		b = a*Math.cos(theta);
    		
    		px = info.getXC()*scaleX;
    		py = h-info.getYC()*scaleY;
    		
    		//System.out.println(px + "\t" + py + "\t" + info.getXC() + "\t" + info.getYC());

			a = scaleDistance(a,scaleX,scaleY,phi);
			b = scaleDistance(b,scaleX,scaleY,phi+Math.PI/2.0d);
			
			g2d.rotate(-phi, px, py);
			
			Ellipse2D.Double ellipse = new Ellipse2D.Double(px-a,py-b,2*a,2*b);
			g2d.draw(ellipse);
			Rectangle2D.Double rect = new Rectangle2D.Double(px-a,py-b,2*a,2*b);
			g2d.draw(rect);
			
			g2d.rotate(phi,px,py);

    	}
	}
	
    /**
     * For distances that are not parallel to x or y, we have to get the
     * components and scale by x and y values.
     * 
     * @param d
     * @param sx
     * @param sy
     * @param angle
     * @return
     */
    private double scaleDistance(double d, double sx, double sy, double angle)
    {
    	double ca = Math.cos(angle);
    	double sa = Math.sin(angle);
    	
    	double x = ca*d;
    	double y = sa*d;
    	//double x = sa*d;
    	//double y = ca*d;
    	
    	x *= sx;
    	y *= sy;
    	
    	d = Math.sqrt(x*x + y*y);
    	
        return d;	
    }
}
