/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.helper.UIHelper;

public class ReviewPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected StateInfoImageListPanel imageListPanel = null;
	protected JScrollPane jsp = null;
	
	protected BufferedImage targetImage = null;
	protected ImagePanel imagePanel = null;
	
	public ReviewPanel()
	{
		super(new GridLayout(1,2));
		initGUI();
	}
	
	protected void initGUI()
	{
		imagePanel = new ImagePanel();
		add(UIHelper.getCenterFloatPanel(imagePanel));
		
		imageListPanel = new StateInfoImageListPanel();
		jsp = new JScrollPane(imageListPanel);
		add(jsp);
		
		int width = imageListPanel.getImageWidth();
		int height = imageListPanel.getImageHeight();
		imagePanel.setSize(width, height);
		imagePanel.setPreferredSize(new Dimension(width,height));
		imagePanel.setMinimumSize(new Dimension((int)(0.9*width),(int)(0.9*height)));
	}
	
    public void setTargetImage(BufferedImage image)
    {
    	targetImage = image;
    	imagePanel.setImage(targetImage);
    }
}
