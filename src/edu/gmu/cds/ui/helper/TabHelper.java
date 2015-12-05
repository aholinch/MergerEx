/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This TabHelper is useful for rendering the tabs to the side so we can avoid
 * nesting too many levels of standard tabs.
 * 
 * Santhosh Kumar's Weblog
 * 
 * http://www.jroller.com/santhosh/date/20050617#adobe_like_tabbedpane_in_swing
 * @author aholinch
 *
 */
public class TabHelper 
{
	public static void addTab(JTabbedPane tabPane, String text, Component comp)
	{ 
	    int tabPlacement = tabPane.getTabPlacement(); 
	    switch(tabPlacement){ 
	        case JTabbedPane.LEFT: 
	        case JTabbedPane.RIGHT: 
	            tabPane.addTab("", new VerticalTextIcon(text, tabPlacement==JTabbedPane.RIGHT), comp); 
	            return; 
	        default: 
	            tabPane.addTab(text, null, comp); 
	    } 
	} 
	
	public static JTabbedPane createTabbedPane(int tabPlacement)
	{ 
	    switch(tabPlacement){ 
	        case JTabbedPane.LEFT: 
	        case JTabbedPane.RIGHT: 
	            Object textIconGap = UIManager.get("TabbedPane.textIconGap"); 
	            Insets origInsets = UIManager.getInsets("TabbedPane.tabInsets");
	            Insets tabInsets = origInsets; 
	            if(tabInsets==null)
	            {
	            	tabInsets = new Insets(2,2,2,2);
	            }
	            
	            UIManager.put("TabbedPane.textIconGap", new Integer(1)); 
	  	        UIManager.put("TabbedPane.tabInsets", new Insets(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom)); 
	  	        
	  	        JTabbedPane tabPane = new JTabbedPane(tabPlacement); 

	  	        UIManager.put("TabbedPane.textIconGap", textIconGap);
	  	        UIManager.put("TabbedPane.tabInsets", tabInsets); 
                
	  	        return tabPane; 
	        default: 
	            return new JTabbedPane(tabPlacement); 
	    } 
	} 
	
	public static BufferedImage getImage(String text, boolean clockwise)
	{
		JLabel lbl = new JLabel();
		Font f = lbl.getFont();
		f = f.deriveFont(Font.BOLD);
		FontMetrics fm = lbl.getFontMetrics(f);
		Color bg = new Color(1f,1f,1f,0.0f);
		Color fontColor = lbl.getForeground();
		
		return getImage(text,clockwise,f,fm,bg,fontColor);
	}
	
	public static BufferedImage getImage(String text, boolean clockwise, Font font, FontMetrics fm, Color bg, Color fontColor)
	{
		BufferedImage bi = null;
		
		int width = SwingUtilities.computeStringWidth(fm, text); 
        int height = fm.getHeight(); 

        bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setColor(bg);
        g2.fillRect(0, 0, width, height);
        
        g2.setFont(font); 
        g2.setColor(fontColor); 
        
        g2.drawString(text, 0, fm.getLeading()+fm.getAscent());
        
		return bi;
	}
	
	public static class VerticalTextIcon extends ImageIcon
	{
		private static final long serialVersionUID = 1L;

		public String text = null;
		public boolean clockwise = false;
		
		public VerticalTextIcon(String text, boolean clockwise)
		{
		    super();
		    setImage(TabHelper.getImage("   "+ text+ "   ",clockwise));
		    this.text = text;
		    this.clockwise = clockwise;
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			Graphics2D g2 = (Graphics2D)g;
			AffineTransform oldTransform = g2.getTransform();
			int w = super.getIconWidth();
			int h = super.getIconHeight();
			
			if(clockwise)
			{
				g2.translate(x+h, y);
				g2.rotate(-Math.PI*0.5);
			}
			else
			{
				g2.translate(x, y+w);
				g2.rotate(-Math.PI*0.5);
			}
			g2.drawImage((BufferedImage)getImage(), 0,0, w, h,null);
			g2.setTransform(oldTransform);
		}
		public int getIconHeight()
		{
			return super.getIconWidth();
		}
		
		public int getIconWidth()
		{
			return super.getIconHeight();
		}
	}

}
