/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectInfoListener;
import edu.gmu.cds.sim.DiskInfo;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.sim.TargetData.TargetDataHandler;
import edu.gmu.cds.ui.DiskInfoImagePanel;
import edu.gmu.cds.ui.helper.PositionInfoHelper;
import edu.gmu.cds.ui.helper.TabHelper;
import edu.gmu.cds.ui.helper.UIHelper;

/**
 * Specify the object positions and query for information like redshift and magnitudes.
 * 
 * @author aholinch
 *
 */
public class DiskSelectionPanel extends JPanel implements TargetDataHandler, ObjectInfoListener
{
	private static final long serialVersionUID = 1L;

	protected DiskInfoPanel primaryInfoPanel    = null;
	protected DiskInfoPanel secondaryInfoPanel  = null;
	protected JTabbedPane tabs = null;
	
	protected PositionInfoHelper posHelper = null;
	
	protected DiskInfoImagePanel imagePanel = null;
	protected BufferedImage tgtImage = null;

	protected boolean isImageSet = false;
	
	protected String selectionType = null;
	
	protected boolean dataChanged = false;
	
	public DiskSelectionPanel()
	{
		super(new BorderLayout());
		initGUI();
		posHelper = new PositionInfoHelper(imagePanel,null);
		posHelper.setObjectInfoListener(this);
	}
	
	public void initGUI()
	{
		primaryInfoPanel = new DiskInfoPanel("Primary",false,Color.RED,this);
		secondaryInfoPanel = new DiskInfoPanel("Secondary",false,Color.GREEN,this);
		
		//tabs = new JTabbedPane(JTabbedPane.LEFT);
		tabs = TabHelper.createTabbedPane(JTabbedPane.LEFT);
		
		TabHelper.addTab(tabs,"Primary",primaryInfoPanel);
		TabHelper.addTab(tabs,"Secondary",secondaryInfoPanel);
		//tabs.addTab("Primary",primaryInfoPanel);
		//tabs.addTab("Secondary",secondaryInfoPanel);
		
		add(tabs,BorderLayout.NORTH);
		
		imagePanel = new DiskInfoImagePanel();
		imagePanel.setPreferredSize(new Dimension(500,500));
		add(imagePanel,BorderLayout.CENTER);
		
		JPanel sidePanel = new JPanel();
		UIHelper.setMinPrefSize(sidePanel, 150, 400);
		add(sidePanel,BorderLayout.EAST);
		
	}

	public void setSelectionType(String type)
	{
		if(type == null)
		{
			return;
		}
		
		selectionType = type.toLowerCase();
	}
	
	public boolean isImageSet()
	{
		return isImageSet;
	}
	
	public void setImageSet(boolean flag)
	{
		isImageSet = flag;
	}
	
	public void setImage(BufferedImage bi)
	{
		tgtImage = bi;
		imagePanel.setImage(bi);
		primaryInfoPanel.clearInfo();
		secondaryInfoPanel.clearInfo();
		updateImagePanelInfos();
		if(bi != null)
		{
			isImageSet = true;
		}
		else
		{
			isImageSet = false;
		}
	}

	@Override
	public boolean dataHasChanged()
	{
		return dataChanged;
	}
	
	@Override
	public void getInfoFromTargetData(TargetData data) 
	{
		String img = data.getTargetImageFile();
		if(img != null)
		{
			BufferedImage bi = ImageProcessor.readImage(img);
			setImage(bi);
		}
        
		posHelper.updateModelInfo(data);

		primaryInfoPanel.clearInfo();
		secondaryInfoPanel.clearInfo();
		
		primaryInfoPanel.setDiskInfo(data.getPrimaryDiskInfo());
		secondaryInfoPanel.setDiskInfo(data.getSecondaryDiskInfo());
		
		dataChanged = false;
		
		updateImagePanelInfos();
	}

	@Override
	public void setInfoToTargetData(TargetData data) 
	{
		DiskInfo info = null;
		info = data.getPrimaryDiskInfo();
		if(info == null)
		{
			info = new DiskInfo();
			data.setPrimaryDiskInfo(info);
		}
		primaryInfoPanel.getDiskInfo(info);
		
		info = data.getSecondaryDiskInfo();
		if(info == null)
		{
			info = new DiskInfo();
			data.setSecondaryDiskInfo(info);
		}
		secondaryInfoPanel.getDiskInfo(info);
		
		dataChanged = false;
	}

	@Override
	public void foundObject(ObjectInfo info) 
	{
		if(selectionType == null) return;
		
		dataChanged = true;
		DiskInfoPanel ip = primaryInfoPanel;
		if(selectionType.equals("secondary"))
		{
			ip = secondaryInfoPanel;
		}
		
		ip.setObjectInfo(info,true);
		
		updateImagePanelInfos();
	}
	
	protected void updateImagePanelInfos()
	{
		List<ObjectInfo> infos = new ArrayList<ObjectInfo>();
		
		ObjectInfo info = null;
		
		info = primaryInfoPanel.getObjectInfoPosition();
		if(info.getPx() != 0 && info.getPy() != 0)
		{
			infos.add(info);
		}
		
		info = secondaryInfoPanel.getObjectInfoPosition();
		if(info.getPx() != 0 && info.getPy() != 0)
		{
			infos.add(info);
		}
		
		imagePanel.setObjectInfos(infos);
		imagePanel.repaint();
	}
}
