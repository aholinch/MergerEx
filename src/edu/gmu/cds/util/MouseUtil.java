/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.event.MouseEvent;

public class MouseUtil 
{
	public static boolean hasMask(int allMasks, int mask)
	{
		return ((allMasks&mask)==mask);
	}
	
	public static boolean isShiftClick(MouseEvent e)
	{
		int mask = e.getModifiersEx();
		
		return hasMask(mask,MouseEvent.SHIFT_DOWN_MASK);             
	}
	
	public static boolean isRightClick(MouseEvent e)
	{
		int button = e.getButton();
		if(button == MouseEvent.BUTTON3)
		{
			return true;
		}
		
		int mask = e.getModifiersEx();
		
		boolean hasModifier = hasMask(mask,MouseEvent.SHIFT_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.CTRL_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.ALT_DOWN_MASK);
		
		if(hasModifier)
		{
			return true;
		}
		
		return false;
	}

}
