/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import edu.gmu.cds.obj.ObjectInfo;

/**
 * Helps to plot ObjectInfos on top of images
 * @author aholinch
 *
 */
public class ObjectInfoHelper 
{
    public static void plotInfos(Graphics g, int w, int h, BufferedImage bi, List<ObjectInfo> infos)
    {
    	/*
    	int iw = bi.getWidth();
    	int ih = bi.getHeight();
    	
    	double px = 0;
    	double py = 0;
    	
    	int size = infos.size();
    	ObjectInfo info = null;
    	
    	for(int i=0; i<size; i++)
    	{
    		info = infos.get(i);
    		px = info.getPx();
    	}
    	*/
    }
}
