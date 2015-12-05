/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.ui.helper.UIHelper;

/**
 * Displays a list of images corresponding to StateInfo objects.
 * 
 * @author aholinch
 *
 */
public class StateInfoImageListPanel extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;

	public List<StateInfo> infos = null;
    
	protected int imageWidth = 300;
	protected int imageHeight = 300;
	protected int imageOffsetX = 17;
	protected int imageOffsetY = 5;
	
	protected int selectedImage = -1;
	protected StateClickListener listener = null;
	
	protected JPopupMenu popup = null;
	protected int popupIndex = 0;
	protected boolean popupEnabled = true;
	
	protected Color outlineColor = new Color(241,162,4);
	
	protected BufferedImage tmpImage = null;
	
	public StateInfoImageListPanel()
	{
		super();
		infos = new ArrayList<StateInfo>();
		addMouseListener(this);
		getTempImage();
	}
	
	public int getImageWidth()
	{
		return imageWidth;
	}
	
	public int getImageHeight()
	{
		return imageHeight;
	}
	
	public void setStateSelectionListener(StateClickListener lstnr)
	{
		listener = lstnr;
	}
	
	public void setStateInfos(List<StateInfo> infoList)
	{
		infos = infoList;
		refreshPreferredSize();
	}
	
	public List<StateInfo> getInfos()
	{
		return infos;
	}
	
	public void addStateInfo(StateInfo info)
	{
		infos.add(info);
		refreshPreferredSize();
	}
	
	public void removeStateInfo(StateInfo info)
	{
		if(infos.remove(info))
		{
			refreshPreferredSize();
		}
	}
	
	public StateInfo getSelectedStateInfo()
	{
		if(selectedImage == -1 || infos == null || infos.size() == 0)
		{
			return null;
		}
		
		if(selectedImage >= infos.size())
		{
			return null;
		}
		
		return infos.get(selectedImage);
	}
	
	protected BufferedImage getTempImage()
	{
		if(tmpImage == null)
		{
			tmpImage = UIHelper.getTextImage(imageWidth, imageHeight, 12f, "Image Not Yet Ready", Color.black, Color.red);
		}
		return tmpImage;
	}
	
	public void paintComponent(Graphics g)
	{
		int width = getWidth()*3;
		int height = getHeight()*4;
		g.setColor(this.getBackground());
		g.fillRect(0,0,width,height);
		
		if(infos!=null && infos.size() > 0)
		{
			StateInfo si = null;
			int size = infos.size();
			int y = 0;
			BufferedImage image = null;
			
			for(int i=0; i<size; i++)
			{
			    si = infos.get(i);
			    image = si.image;
			    if(image == null)
			    {
			    	//System.out.println("Null image for si " + i);
			    	image = getTempImage();
			    }
			    
			    y=(imageHeight+imageOffsetY)*i+imageOffsetY;
			    g.drawImage(image,imageOffsetX,y,imageWidth,imageHeight,null);
			    
			    if(i==selectedImage)
			    {
			    	g.setColor(outlineColor);
			    	int off = 1;
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off,imageHeight+off);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+1,imageHeight+off+1);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+2,imageHeight+off+2);
					off++;
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off,imageHeight+off);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+1,imageHeight+off+1);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+2,imageHeight+off+2);
					off++;
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off,imageHeight+off);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+1,imageHeight+off+1);
					g.drawRect(imageOffsetX-off,y-off,imageWidth+off+2,imageHeight+off+2);
			    }
			}
		}

	}
	
	/**
	 * Ensure preferred size is large enough to show all images.  Usually
	 * this will trigger an enclosing JScrollPane to resize as well.  Uses
	 * the same size for the minimum size.
	 */
	public void refreshPreferredSize()
	{
		int size = infos.size();
		
		int w = imageWidth+2*imageOffsetX;
		int h = size*(imageHeight+imageOffsetY);
		
		Dimension dim = new Dimension(w,h);
		
		setPreferredSize(dim);
		setMinimumSize(dim);
		revalidate();
	}
	
	/**
	 * Select the given image.  Does nothing if selected index
	 * doesn't exist.
	 * 
	 * @param ind
	 */
	public void selectImage(int ind)
	{
		if(ind > infos.size()-1)
		{
			ind = -1;
		}
		
		if(ind != selectedImage)
		{
			selectedImage = ind;
		}
		else
		{
			selectedImage = -1;
		}
		
		repaint();
		
		if(listener != null)
		{
			listener.stateClicked(getSelectedStateInfo());
		}

		// notify listeners of selection?
	}

	public int getImageIndexForCoords(int x, int y)
	{
		int mx = x;
		int my = y-imageOffsetY;
		
		// check x first, it's quicker
		if(mx >= imageOffsetX && mx <= (imageOffsetX+imageWidth))
		{
			// ok, let's see if y is on an image
			int ny = (int)(((double)my)/((double)(imageOffsetY+imageHeight)));
			int ylim = ny*(imageOffsetY+imageHeight);
			if(my >= ylim && my <= (ylim+imageHeight))
			{
				// we're on an image
				
				return ny;
			}
		}
	
		return -1;		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() != MouseEvent.BUTTON1)
		{
			return;
		}
		int mask = e.getModifiersEx();
		
		boolean hasModifier = hasMask(mask,MouseEvent.SHIFT_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.CTRL_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.ALT_DOWN_MASK);
		if(hasModifier)
		{
			return;
		}
		
		int ind = getImageIndexForCoords(e.getX(),e.getY());
		selectImage(ind);
		
		return;
	}

	public boolean hasMask(int allMasks, int mask)
	{
		return ((allMasks&mask)==mask);
	}
	
	public void mousePressed(MouseEvent e)
	{
		int button = e.getButton();
		int mask = e.getModifiersEx();
		
		boolean hasModifier = hasMask(mask,MouseEvent.SHIFT_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.CTRL_DOWN_MASK) ||
		                      hasMask(mask,MouseEvent.ALT_DOWN_MASK);
		
		
		if(button == MouseEvent.BUTTON1 && !hasModifier)
		{
			return;
		}
		
		int ind = getImageIndexForCoords(e.getX(),e.getY());
		
		if(ind > -1 && ind < infos.size())
		{
			showPopup(e,ind);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}
	
	public void showPopup(MouseEvent e, int index)
	{
		if(popupEnabled && popup != null)
		{
			popupIndex = index;
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/**
	 * Return the state info for the isp under the popup.
	 * 
	 * @return
	 */
	public StateInfo getPopupInfo()
	{
		StateInfo si = null;
		
		try{si = infos.get(popupIndex);}catch(Exception ex){};
		
		return si;
	}
}
