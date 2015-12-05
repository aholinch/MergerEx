/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class SquareKeeper implements ComponentListener 
{
	public int MIN_DIM = 20;
	
    public SquareKeeper()
    {
    	
    }

	@Override
	public void componentResized(ComponentEvent e) 
	{
		Component c = e.getComponent();
		int ow = c.getWidth();
		int oh = c.getHeight();
		int sz = Math.min(ow,oh);
		if(sz < MIN_DIM)
		{
			sz = MIN_DIM;
		}
		
		if(sz != ow || sz != oh)
		{
			c.setSize(new Dimension(sz,sz));
			c.setPreferredSize(new Dimension(sz,sz));
			c.setMinimumSize(new Dimension(sz,sz));
		}
	}
	
	@Override
	public void componentHidden(ComponentEvent e) 
	{
	}

	@Override
	public void componentMoved(ComponentEvent e) 
	{
	}

	@Override
	public void componentShown(ComponentEvent e) 
	{
	}

}
