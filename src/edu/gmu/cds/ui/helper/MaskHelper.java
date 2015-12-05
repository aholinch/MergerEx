/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.event.MouseInputListener;

import edu.gmu.cds.img.ImageData;
import edu.gmu.cds.ui.ImagePanel;

public class MaskHelper implements MouseInputListener
{
	protected int nx = 0;
	protected int ny = 0;
	
	protected boolean dragStarted = false;
	protected int startX = 0;
	protected int startY = 0;
	
	protected double scaleX = 0;
	protected double scaleY = 0;
	
	protected double mask[] = null;
    
	protected ImagePanel imagePanel = null;
	protected ImageData imageData = null;
	
	protected BufferedImage paintedImage = null;
	
	protected int psz = 3;
	
	protected Cursor origCursor = null;
	
	public MaskHelper(ImagePanel panel)
	{
		imagePanel = panel;
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);
	}
	
	public void setImageData(ImageData data)
	{
		imageData = data;
		paintedImage = null;
		
		if(imageData!=null)
		{
			nx = imageData.getNx();
			ny = imageData.getNy();
			calcScale();
		}
		
		if(mask==null)
		{
			resetMask(); // allocate the mask
		}
	}
	
	public void calcScale()
	{
		int w = imagePanel.getWidth();
		int h = imagePanel.getHeight();
		
		scaleX = ((double)nx)/((double)w);
		scaleY = ((double)ny)/((double)h);
	}
	
	public void resetMask()
	{
		paintedImage = null;
		
		mask = null; // hint to gc
		if(imageData != null)
		{
			mask = imageData.createEmptyMask();
		}
	}
	
	public void applyMaskAndRepaint()
	{
		if(imageData == null) return;
		
	    paintedImage = imageData.applyMask(mask, Color.black);
	    imagePanel.setImage(paintedImage);
	    imagePanel.repaint();
	}
	
	public BufferedImage getPaintedImage()
	{
		return paintedImage;
	}

	public void maskLine(int x1, int y1, int x2, int y2)
	{
		boolean xsame = x1==x2;
		
		if(xsame)
		{
	        int inc = 1;
	        if(y2<y1) inc = -1;
	        for(int y=y1;y<=y2;y+=inc)
	        {
	        	maskPoint(x1,y);
	        }
		}
		else
		{
			if(x1>x2)
			{
				//swap
				int tmp = 0;
				tmp = x1;
				x1 = x2;
				x2 = tmp;
				tmp = y1;
				y1 = y2;
				y2 = tmp;
			}
			
			double m = ((double)(y2-y1))/((double)(x2-x1));
			double y = 0;
			
			// iterate over x
			for(int x=x1; x<=x2; x++)
			{
				y = m*(x-x1)+y1;
				maskPoint(x,(int)y);
			}
		}
	}
	
	public void maskPoint(int x, int y)
	{
		x = (int)(x*scaleX);
		y = (int)(y*scaleY);
		
		int ind = 0;
		for(int j=Math.max(0, y-psz); j<=Math.min(ny-1,y+psz); j++)
		{
			for(int i=Math.max(0, x-psz); i<=Math.min(nx-1,x+psz); i++)
			{
				ind = j*nx+i;
				mask[ind]=1;
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		if(!dragStarted) return;
		
		int x = e.getX();
		int y = e.getY();
		
		// we mask out pixels between startX,startY and x,y
		maskLine(startX, startY, x, y);
		
		// update startX,startY
		startX = x;
		startY = y;
		applyMaskAndRepaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{

	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(!isLeftButtonDown(e)) return;
		origCursor = imagePanel.getCursor();
		imagePanel.setCursor(UIHelper.getEraserCursor());
		dragStarted = true;
		startX = e.getX();
		startY = e.getY();
		calcScale();
		maskPoint(startX, startY);
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(isLeftButtonDown(e)) return;
		imagePanel.setCursor(origCursor);
		dragStarted = false;
		applyMaskAndRepaint();
	}
	
	public boolean isLeftButtonDown(MouseEvent e)
	{
		boolean flag = false;
		
		int mask1 = MouseEvent.BUTTON1_DOWN_MASK;
		int mods = e.getModifiersEx();
		
		if((mods & mask1)==mask1)
		{
			flag = true;
		}

		return flag;
	}
}
