/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.ui.PublicPainter;

public class UIHelper 
{
	private static String synch = "mutex";
	private static Cursor ERASER_CURSOR = null;
	
	/**
	 * Returns a JPanel with the given panel floating in the center
	 * @param panel
	 * @return
	 */
	public static JPanel getCenterFloatPanel(JPanel panel)
	{
		JPanel tmpPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill=GridBagConstraints.NONE;
		gbc.anchor=GridBagConstraints.CENTER;
		tmpPanel.add(panel,gbc);
		return tmpPanel;
	}
	
	public static Cursor getEraserCursor()
	{
		if(ERASER_CURSOR == null)
		{
			synchronized(synch)
			{
				// if is cheap compared to synchronized, so we double-check
				// to avoid extra work
				if(ERASER_CURSOR==null)
				{
			        BufferedImage bi = ImageProcessor.readImageFromClassPath("images/draw_eraser.png");
			        Point pt = new Point(2,22);
			        ERASER_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(bi, pt, "Merger Eraser");
				}
			}
		}
		
		return ERASER_CURSOR;
	}
	
	public static void setMinPrefSize(Component comp, int width, int height)
	{
		Dimension dim = new Dimension(width,height);
		comp.setMinimumSize(dim);
		comp.setPreferredSize(dim);
	}
	
    public static double getNum(JTextField txt)
    {
    	return getNum(txt,0);
    }
    
    public static double getNum(JTextField txt, double defaultNum)
    {
    	double num = defaultNum;
    	
    	try
    	{
    	    String str = txt.getText().trim();
    	    num = Double.parseDouble(str);
    	}
    	catch(Exception ex)
    	{
    		//ex.printStackTrace();
    	}
    	
    	return num;
    }
    
    public static void setLookAndFeel()
    {
		try 
		{
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
		    {
		    	//System.out.println(info.getName());
		        if ("Nimbus".equals(info.getName())) 
		        {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} 
		catch (Exception e) 
		{
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
    }
    
	public static void format(JTextField txt, double num, String fmt)
	{
		DecimalFormat df = new DecimalFormat(fmt);
		txt.setText(df.format(num));
	}
	
	 /**
	  * Try to switch the background and foreground colors.
	  * 
	  * @param btn
	  */
	 public static void emphasizeButton(AbstractButton btn)
	 {
		 
		 Color col = btn.getBackground();
		 btn.setBackground(btn.getForeground());
		 btn.setForeground(col);
	 }
	 
    public static BufferedImage getImageOfComponent(Component c, int width, int height)
    {
    	return getImageOfComponent(c,width,height,BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    public static BufferedImage getImageOfComponent(Component c, int width, int height, int colorType)
    {
    	BufferedImage bi = new BufferedImage(width,height,colorType);
    	if(c != null)
    	{
    		if(c instanceof PublicPainter)
    		{
    			// avoids borders
    			((PublicPainter)c).paintComponent(bi.getGraphics(),width,height);
    		}
    		else
    		{
    			// may include borders
    			c.paint(bi.getGraphics());
    		}
    	}
    	return bi;
    }
    
    public static BufferedImage getTextImage(int width, int height, float fontSize, String text, Color bgColor, Color txtColor)
    {
    	BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
    	Graphics g = bi.getGraphics();
    	g.setColor(bgColor);
    	g.fillRect(0,0, width, height);
    	g.setColor(txtColor);
    	Font f = g.getFont().deriveFont(fontSize);
    	g.setFont(f);
    	int x = 0;
    	int y = 0;
    	FontMetrics fm = g.getFontMetrics();
    	y=(int)(height*0.5+fm.getHeight());
    	x = (int)((width-fm.stringWidth(text))*0.5);
    	g.drawString(text, x, y);
    	return bi;
    }
}
