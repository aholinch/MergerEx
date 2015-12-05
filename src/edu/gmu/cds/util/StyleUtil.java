/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class StyleUtil
{
	public static Color BACKGROUND = new Color(35,34,32);
	public static Color BASIC_TEXT_COLOR = new Color(221,224,193);
	public static Color BASIC_LABEL_COLOR = new Color(239,240,230);
	//private static Color BASIC_BUTTON_COLOR = new Color(241,162,4);
	public static Color BASIC_BUTTON_COLOR = new Color(255,94,5);
	
	private static Font BASIC_FONT = null;
	
	public static JDialog getUndecoratedDialog(java.awt.Window owner, JPanel content, int width, int height)
	{
		JDialog dialog = new JDialog((java.awt.Frame)null,true);
		
		dialog.setUndecorated(true);
		dialog.setSize(width,height);
		
		content.setBorder(BorderFactory.createLineBorder(new Color(212,211,210),1));
		
		dialog.getContentPane().add(content);
		
		return dialog;
	}
	
	public static JPanel getJPanel(LayoutManager man)
	{
		JPanel panel = new JPanel(man);
		
		panel.setBackground(BACKGROUND);
		
		return panel;
	}
	
	public static Font getBasicFont()
	{
    	if(BASIC_FONT == null)
    	{
    		BASIC_FONT = Font.getFont("Verdana");
    	}
    	return BASIC_FONT;
	}
	
    public static void setBackground(Component comp)
    {
    	comp.setBackground(BACKGROUND);
    }
    
    public static void setBasicTextFont(Component comp)
    {
    	getBasicFont();
    	comp.setFont(BASIC_FONT);
    	comp.setForeground(BASIC_TEXT_COLOR);
    }

    public static void setBasicLabelFont(Component comp)
    {
    	getBasicFont();
    	comp.setFont(BASIC_FONT);
    	comp.setForeground(BASIC_LABEL_COLOR);
    }
    
    public static void setButtonStyle(AbstractButton btn)
    {
    	getBasicFont();
    	btn.setFont(BASIC_FONT);
    	btn.setForeground(BASIC_BUTTON_COLOR);
    	btn.setBackground(BACKGROUND);
    }
    
    public static void setTextStyle(JTextComponent txt)
    {
    	getBasicFont();
    	txt.setFont(BASIC_FONT);
    	txt.setForeground(BASIC_TEXT_COLOR);
    	txt.setBackground(BACKGROUND);
    }
    
    public static JLabel getBasicLabel(String txt)
    {
    	JLabel lbl = new JLabel(txt);
    	setBackground(lbl);
    	setBasicLabelFont(lbl);
    	
    	return lbl;
    }
}
