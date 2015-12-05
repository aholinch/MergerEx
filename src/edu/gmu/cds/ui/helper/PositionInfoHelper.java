/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import edu.gmu.cds.img.ImageCoordinateModel;
import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectInfoListener;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.CursorInfoPanel;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.util.MouseUtil;

/**
 * Places a listener on the image panel for mouse movements to
 * send coords to cursor info panel
 * 
 * @author aholinch
 *
 */
public class PositionInfoHelper implements MouseInputListener
{
	protected ImagePanel imagePanel;
	protected CursorInfoPanel infoPanel;
	protected ImageCoordinateModel imageModel;
	protected ObjectInfoListener objectInfoListener;
	protected JPopupMenu popupMenu;
	
    public PositionInfoHelper(ImagePanel imagePanel, CursorInfoPanel infoPanel)
    {
    	this.imagePanel = imagePanel;
    	this.infoPanel = infoPanel;
    	
    	imagePanel.addMouseListener(this);
    	imagePanel.addMouseMotionListener(this);
    	
    	buildMenu();
    }
    
    protected void buildMenu()
    {
    	popupMenu = new JPopupMenu("Thumbnail Options");
    	JMenuItem mi = new JMenuItem("Copy Image");
    	ActionListener al = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ClipboardHelper.copyImageToSystemClipboard(imagePanel.getImage());
			}
    		
    	};
    	mi.addActionListener(al);
    	popupMenu.add(mi);    	
    }
    
    public void setImageMode(ImageCoordinateModel imageModel)
    {
    	this.imageModel = imageModel;
    }
    
    public void setObjectInfoListener(ObjectInfoListener listener)
    {
    	objectInfoListener = listener;
    }
    
    public void updateModelInfo(double nx, double ny, double ang1c, double ang2c, double ang1size, double ang2size)
    {
    	if(imageModel == null) imageModel = new ImageCoordinateModel();
    	imageModel.setImageInfo(nx, ny, ang1c, ang2c, ang1size, ang2size);
    }
    
    public void updateModelInfo(TargetData data)
    {
    	if(imageModel == null) imageModel = new ImageCoordinateModel();
    	double width = data.getImageSizeArcMin()/60.0d;
    	imageModel.setImageInfo(data.getImageWidth(), data.getImageHeight(), data.getCenterRA(), data.getCenterDec(), width, width);
    }

	@Override
	public void mouseClicked(MouseEvent e) 
	{
	    if(objectInfoListener == null) return;
	    
		if(imageModel == null) return;
		
		// handle right-click if necessary
		if(MouseUtil.isRightClick(e))
		{
			popupMenu.show(imagePanel, e.getX(), e.getY());
			return;
		}
		
		double xy[] = getImageXY(e);
		
		double ra[] = imageModel.convertPixelsToAngles(xy[0],xy[1]);
		ObjectInfo posOnly = new ObjectInfo();
		posOnly.setPx(xy[0]);
		posOnly.setPy(xy[1]);
		posOnly.setRADeg(ra[0]);
		posOnly.setDecDeg(ra[1]);
		objectInfoListener.foundObject(posOnly);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if(imageModel == null) return;
		
		double xy[] = getImageXY(e);
		
		double ra[] = imageModel.convertPixelsToAngles(xy[0],xy[1]);
		
		if(infoPanel != null) infoPanel.setValues(xy[0], xy[1], ra[0], ra[1]);
	}

	/**
	 * Convert screen coordinates (even though relative to this component)
	 * to the actually image coordinates.
	 * 
	 * @param evt
	 * @return
	 */
	public double[] getImageXY(MouseEvent evt)
	{
		double ix = 0;
		double iy = 0;
		
		int x = evt.getX();
		int y = evt.getY();
		int tx = imagePanel.getTargetWidth();
		int ty = imagePanel.getTargetHeight();	
		double nx = imageModel.getNX();
		double ny = imageModel.getNY();
		
		ix = ((double)x)/((double)tx);
		iy = 1.0d-((double)y)/((double)ty);
		
		ix*=nx;
		iy*=ny;
		
		
		double xy[] = {ix,iy};
		return xy;
	}
}
