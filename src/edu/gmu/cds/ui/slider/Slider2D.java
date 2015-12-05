/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.slider;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Slider2D implements MouseMotionListener
{
    protected Component comp = null;
    protected double minV1 = 0;
    protected double maxV1 = 0;
    protected double minV2 = 0;
    protected double maxV2 = 0;
    
    protected double origMinV1 = 0;
    protected double origMaxV1 = 0;
    protected double origMinV2 = 0;
    protected double origMaxV2 = 0;
    
    
    protected boolean isCrossHair;
    protected boolean updateInit = false;
    
    protected Slider2DListener listener = null;
    
    protected int zoomLevel = 5;
    
    public Slider2D()
    {
    	
    }

    public Slider2D(Component comp)
    {
    	setComp(comp);
    }
    
    public void setSlider2DListener(Slider2DListener listener)
    {
    	this.listener = listener;
    }
    
    public void setComp(Component comp)
    {
    	isCrossHair = false;
    	this.comp = comp;
    	if(comp != null)
		{
    		comp.addMouseMotionListener(this);
    		if(comp instanceof CrossHairPanel)
    		{
    			isCrossHair = true;
    		}
		}
    }
    
	public void mouseDragged(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		if(isCrossHair)
		{
			((CrossHairPanel)comp).setPos(x,y);
		}
		
		if(listener != null)
		{
			int height = comp.getHeight();
			int width = comp.getWidth();
			double v1 = getValue(height-y,height,minV1,maxV1);
			double v2 = getValue(x,width,minV2,maxV2);
			
			listener.sliderChanged(v1,v2);
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
    
    public void setRanges(double min1, double max1, double min2, double max2)
    {
    	zoomLevel = 5;
    	
    	minV1 = min1;
    	minV2 = min2;
    	maxV1 = max1;
    	maxV2 = max2;
    	origMinV1 = min1;
    	origMinV2 = min2;
    	origMaxV1 = max1;
    	origMaxV2 = max2;
    }
    
    // this doesn't work!!!!!!!!!!!
    public void setZoomLevel(int level)
    {
    	zoomLevel = level;
    	if(level == 5)
    	{
    		minV1 = origMinV1;
    		maxV1 = origMaxV1;
    		minV2 = origMinV2;
    		maxV2 = origMaxV2;
    	}
    	else 
    	{
    		double factor = Math.pow(2.0,level-5);
    		minV1 = origMinV1*factor;
    		maxV1 = origMaxV1*factor;
    		minV2 = origMinV2*factor;
    		maxV2 = origMaxV2*factor;
    	}
    }
    
    public void setValues(double v1, double v2)
    {
    	double rng1 = maxV1 - minV1;
    	double rng2 = maxV2 - minV2;
    	
    	if(v1 < minV1)
    	{
    		v1 = minV1;
    	}
    	if(v1 > maxV1)
    	{
    		v1 = maxV1;
    	}
    	if(v2 < minV2)
    	{
    		v2 = minV2;
    	}
    	if(v2 > maxV2)
    	{
    		v2 = maxV2;
    	}
    	
    	v1-=minV1;
    	v2-=minV2;
    	
    	v1 /= rng1;
    	v2 /= rng2;
    	
    	int y = comp.getHeight() - (int)(v1*comp.getHeight());
    	int x = (int)(v2*comp.getWidth());
    	
		if(isCrossHair)
		{
			if(updateInit)
			{
				((CrossHairPanel)comp).setInitPos(x,y,false);
			}
			
			((CrossHairPanel)comp).setPos(x,y);
		}
    }
    
	public double getValue(int p, int lim, double min, double max)
	{
		if(p <= 0)
		{
			return min;
		}
		
		if(p >= lim)
		{
			return max;
		}
		
		double val = ((double)p)/((double)lim);
	
		double rng = max-min;
		
		val = val*rng+min;
		
		return val;
	}
	
	public boolean isUpdateInit()
	{
		return updateInit;
	}
	
	public void setUpdateInit(boolean flag)
	{
		this.updateInit = flag;
		if(flag && (comp instanceof CrossHairPanel))
		{
			((CrossHairPanel)comp).paintInitCrossHair = true;
		}
	}
}
