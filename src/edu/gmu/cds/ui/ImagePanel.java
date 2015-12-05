/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Displays a BufferedImage in the panel
 * 
 * @author aholinch
 *
 */
public class ImagePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected BufferedImage origImage = null;
	protected Color backgroundColor = Color.black;
	protected boolean paintingImage = false;
	
	public ImagePanel()
	{
		super();
	}
	
	public ImagePanel(BufferedImage img)
	{
		origImage = img;
	}
	
	public void setImage(BufferedImage img)
	{
		// checking flag not as reliable, but less expensive than synchronized
		int cnt = 0;
		while(paintingImage && cnt < 10)
		{
			cnt++;
			try{Thread.sleep(50);}catch(Exception ex){};
		}
		paintingImage = false;
		origImage = img;
	}
	
	public BufferedImage getImage()
	{
		return origImage;
	}
	
	public void paintComponent(Graphics g)
	{
		int w = getWidth();
		int h = getHeight();
		
		// fill the background
		g.setColor(backgroundColor);
		g.fillRect(0,0,w,h);
		
		// check image
		if(origImage == null) return;

		// draw the image
		paintingImage = true;

		int nx = origImage.getWidth();
		int ny = origImage.getHeight();
		int targetX = w;
		int targetY = h;
		
    	// we need to scale image
    	double scaleX = ((double)targetX)/((double)nx);
    	double scaleY = ((double)targetY)/((double)ny);
    	
    	Graphics2D g2d = (Graphics2D)g;
    	
    	if(scaleX < 1.0 || scaleY < 1.0)
    	{
    		// we are scaling down
    		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    	}
    	else
    	{
    		// we are scaling up
    		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    	}
    	
    	g2d.drawImage(origImage,0,0,targetX,targetY,null);
		
		paintingImage = false;
	}
	
	/**
	 * How wide was it actually drawn?
	 * @return
	 */
	public int getTargetWidth()
	{
		return getWidth();
	}
	
	/**
	 * How tall was it actually drawn?
	 * @return
	 */
	public int getTargetHeight()
	{
		return getHeight();
	}
}
