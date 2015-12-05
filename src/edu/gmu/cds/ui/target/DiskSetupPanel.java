/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.gmu.cds.img.ImageData;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.img.PixelGrouper;
import edu.gmu.cds.sim.DiskInfo;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.sim.TargetData.TargetDataHandler;
import edu.gmu.cds.ui.DiskInfoImagePanel;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.MaskHelper;
import edu.gmu.cds.ui.helper.TabHelper;
import edu.gmu.cds.ui.helper.UIHelper;

/**
 * Specify the object positions and query for information like redshift and magnitudes.
 * 
 * @author aholinch
 *
 */
public class DiskSetupPanel extends JPanel implements TargetDataHandler, ChangeListener
{
	private static final long serialVersionUID = 1L;

	protected DiskOrientationPanel primaryOrientationPanel    = null;
	protected DiskOrientationPanel secondaryOrientationPanel  = null;
	
	protected int px = 0;
	protected int py = 0;
	protected int sx = 0;
	protected int sy = 0;
	
	protected JTabbedPane tabs = null;
	protected JSlider sldBG = null;
	protected JButton btnEstimate = null;
	protected JButton btnSaveTarget = null;
	protected JButton btnClearMask = null;
	
	protected DiskInfoImagePanel imagePanel = null;
	protected BufferedImage tgtImage = null;
	protected ImageData imageData = null;
	protected ImageData bgImageData = null;
	protected MaskHelper maskHelper = null;
	
	protected boolean isImageSet = false;
	protected boolean isBGSet = false;
	protected int bgValue = 0;
	protected double arcMinPerPixel = 0;
	
	protected boolean dataChanged = false;
	protected String targetImageName = null;
	protected boolean onlyShowDiskGroups = true;
	
	public DiskSetupPanel()
	{
		super(new BorderLayout());
		initGUI();
	}
	
	public void initGUI()
	{
		primaryOrientationPanel = new DiskOrientationPanel("Primary",false,Color.RED,this);
		secondaryOrientationPanel = new DiskOrientationPanel("Secondary",false,Color.GREEN,this);
		
		tabs = TabHelper.createTabbedPane(JTabbedPane.LEFT);
		
		TabHelper.addTab(tabs,"Primary",primaryOrientationPanel);
		TabHelper.addTab(tabs,"Secondary",secondaryOrientationPanel);
		
		add(tabs,BorderLayout.NORTH);
		
		JPanel sidePanel = buildSidePanel();
		UIHelper.setMinPrefSize(sidePanel, 150, 400);
		
		imagePanel = new DiskInfoImagePanel();
		imagePanel.setPreferredSize(new Dimension(500,500));
		maskHelper = new MaskHelper(imagePanel);
		
		add(imagePanel,BorderLayout.CENTER);
		
		add(sidePanel,BorderLayout.EAST);
	}

	protected JPanel buildSidePanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		
		double size[][] = {{2,TableLayout.FILL,2},{2,25,2,25,2,25,2,TableLayout.FILL,2}};
		panel.setLayout(new TableLayout(size));
		
		btnEstimate = new JButton("Estimate Disks");
		btnEstimate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt)
			{
				spawnEstimateDisks();
			}
		});
		btnEstimate.setEnabled(false);
		
		btnSaveTarget = new JButton("Save Target Image");
		btnSaveTarget.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt)
			{
				spawnSaveTargetImage();
			}
		});
		btnSaveTarget.setEnabled(false);
		
		btnClearMask = new JButton("Clear Mask");
		btnClearMask.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt)
			{
				clearMask();
			}
		});
		
		sldBG = new JSlider(JSlider.VERTICAL,0,255,0);
		sldBG.addChangeListener(this);

		JPanel pnl = new JPanel(new GridLayout(1,1));
		pnl.add(sldBG);
		pnl.setBorder(BorderFactory.createTitledBorder("Background"));
		
		panel.add(btnEstimate,panel.getRC(5,1));
		panel.add(btnSaveTarget,panel.getRC(3,1));
		panel.add(btnClearMask,panel.getRC(1,1));
		panel.add(pnl,panel.getRC(7, 1));
		
		return panel;
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
		primaryOrientationPanel.reset();
		secondaryOrientationPanel.reset();
		if(bi != null)
		{
			isImageSet = true;
			imageData = new ImageData(tgtImage);
			sldBG.setValue(0);
			sldBG.repaint();
			setBGFlag(false);
			maskHelper.resetMask();
		}
		else
		{
			isImageSet = false;
		}
	}
		
	protected void setBGFlag(boolean flag)
	{
		isBGSet = flag;
		btnEstimate.setEnabled(flag);
		btnSaveTarget.setEnabled(flag);
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

		targetImageName = data.getTargetImageFile();
		
		primaryOrientationPanel.reset();
		secondaryOrientationPanel.reset();
		
		double angW = data.getImageSizeArcMin();
		double pixW = data.getImageWidth();
		
		arcMinPerPixel = angW/pixW;
		imagePanel.setArcMinPerPixel(arcMinPerPixel);
		DiskInfo info = null;
		
		info = data.getPrimaryDiskInfo();
		primaryOrientationPanel.setDiskInfo(info);
		setPrimaryPosition(info);
		
		info = data.getSecondaryDiskInfo();
		secondaryOrientationPanel.setDiskInfo(info);
		setSecondaryPosition(info);
		dataChanged = false;
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
		primaryOrientationPanel.getDiskInfo(info);
		
		info = data.getSecondaryDiskInfo();
		if(info == null)
		{
			info = new DiskInfo();
			data.setSecondaryDiskInfo(info);
		}
		secondaryOrientationPanel.getDiskInfo(info);
		dataChanged = false;
	}
	
	public void setPrimaryPosition(DiskInfo info)
	{
		px = (int)info.getXC();
		py = (int)info.getYC();
	}
	
	public void setSecondaryPosition(DiskInfo info)
	{
		sx = (int)info.getXC();
		sy = (int)info.getYC();
	}
	
	protected void updateDiskInfoFromSliders()
	{
		dataChanged = true;
		updateImagePanelInfos();
	}
	
	protected void setBG(int value)
	{
		if(!isImageSet) return;
		
		bgValue = value;

		boolean flag = true;
		if(value <= 0)
		{
			flag = false;
			bgImageData = null;
		}
		
		setBGFlag(flag);
		
		BufferedImage bi = imageData.getThresholdImage(value, Color.black);
		if(flag)
		{
			bgImageData = new ImageData(bi);
			maskHelper.setImageData(bgImageData);
			maskHelper.applyMaskAndRepaint();
		}		
	}

	@Override
	public void stateChanged(ChangeEvent e) 
	{
		Object src = e.getSource();
		if(src == sldBG)
		{
			dataChanged = true;
			setBG(sldBG.getValue());
		}
	}
	
	public void spawnSaveTargetImage()
	{
	    Runnable r = new Runnable()
	    {
	    	public void run()
	    	{
	    		Cursor c = DiskSetupPanel.this.getCursor();
	    		DiskSetupPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    		try
	    		{
	    			saveUpdatedTargetImage();
	    		}
	    		finally
	    		{
	    			DiskSetupPanel.this.setCursor(c);
	    		}
	    	}
	    };
	    
	    Thread t = new Thread(r);
	    t.start();
	}
	
	public void saveUpdatedTargetImage()
	{
		if(!isBGSet) return;
		
		if(targetImageName == null)
		{
			showError("Target image name not set");
			return;
		}
		
		if(bgImageData == null)
		{
			showError("Background image data is null");
			return;
		}
		
		BufferedImage bi = maskHelper.getPaintedImage();
		if(bi == null)
		{
			bi = imagePanel.getImage();
		}
		
		if(onlyShowDiskGroups)
		{
			List<int[]> pixs = fetchDiskPixelCoords(bi);
			// build mask
			int w = bi.getWidth();
			int h = bi.getHeight();
			double mask[] = new double[w*h];
			
			int size = 0;
			int ind = 0;
			int x[] = null;
			int y[] = null;
			
			for(int i=0; i<pixs.size()-1; i+=2)
			{
				x = pixs.get(i);
				y = pixs.get(i+1);
				
				size = x.length;
				for(int j=0; j<size; j++)
				{
					ind = x[j]+y[j]*w;
					mask[ind]=1;
				}
			}
			
			ImageData tmpData = new ImageData(bi);
			bi = tmpData.sampleMask(mask, Color.black);
		}
		
		ImageProcessor.writeImage(bi, targetImageName);
	}
	
	public void clearMask()
	{
		maskHelper.resetMask();
		maskHelper.applyMaskAndRepaint();
	}
	
	public void spawnEstimateDisks()
	{
	    Runnable r = new Runnable()
	    {
	    	public void run()
	    	{
	    		Cursor c = DiskSetupPanel.this.getCursor();
	    		DiskSetupPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    		try
	    		{
	    			estimateDisks();
	    		}
	    		finally
	    		{
	    			DiskSetupPanel.this.setCursor(c);
	    		}
	    	}
	    };
	    
	    Thread t = new Thread(r);
	    t.start();
	}
	
	public void estimateDisks()
	{
		if(!isImageSet) return;
		if(!isBGSet) return;
		
		ImageData bgImageData = new ImageData(imagePanel.getImage());
		
		List<Integer> groupIds = new ArrayList<Integer>();
		List<Integer> groupSizes = new ArrayList<Integer>();
		HashMap<Integer,List<PixelGrouper.Chord>> hmGroups = new HashMap<Integer,List<PixelGrouper.Chord>>();

		PixelGrouper.groupPixels(bgImageData, groupIds, groupSizes, hmGroups);

		int ny = bgImageData.getNy();
		
		Integer pId = PixelGrouper.getGroupIdForCoordinates(px, (ny-1-py), groupIds, hmGroups);
		Integer sId = PixelGrouper.getGroupIdForCoordinates(sx, (ny-1-sy), groupIds, hmGroups);

		if(pId == null || sId == null) showError("Unable to Estimate Disks");
		
		int value = bgValue;
		boolean found = true;
		while(pId == sId)
		{
			value++;
			bgImageData = new ImageData(imageData.getThresholdImage(value, Color.black));
			
			groupIds = new ArrayList<Integer>();
			groupSizes = new ArrayList<Integer>();
			hmGroups = new HashMap<Integer,List<PixelGrouper.Chord>>();

			PixelGrouper.groupPixels(bgImageData, groupIds, groupSizes, hmGroups);
			
			pId = PixelGrouper.getGroupIdForCoordinates(px, (ny-1-py), groupIds, hmGroups);
			sId = PixelGrouper.getGroupIdForCoordinates(sx, (ny-1-sy), groupIds, hmGroups);

			if(pId == null || sId == null)
			{
				found = false;
				showError("Unable to Estimate Disks");
				break;
			}
		}
		
		if(found)
		{
			dataChanged = true;
			
			// estimate ellipses from groups
			int pInd = -1;
			int sInd = -1;
			
			int size = groupIds.size();
			for(int i=0; i<size; i++)
			{
			    if(groupIds.get(i)==pId)
			    {
			    	pInd = i;
			    }
			    else if(groupIds.get(i)==sId)
			    {
			    	sInd = i;
			    }
			    if(pInd >=0 && sInd >=0)break;
			}
			int size1 = ((Integer)groupSizes.get(pInd)).intValue();
			int size2 = ((Integer)groupSizes.get(sInd)).intValue();
			
			int x1[] = new int[size1];
			int y1[] = new int[size1];
			
			int x2[] = new int[size2];
			int y2[] = new int[size2];
			
			PixelGrouper.populateGroupCoordinates(x1,y1,pId,hmGroups);
			PixelGrouper.populateGroupCoordinates(x2,y2,sId,hmGroups);
			
			double theta = 0;
			double phi = 0;
			double rad = 0;
			
            double ellipseFit[] = null;
			
			ellipseFit = PixelGrouper.estimate_axes(x1, y1);
			
			phi = Math.toDegrees(ellipseFit[4])-90.0d;
			theta = Math.toDegrees(Math.acos(ellipseFit[3]/ellipseFit[2]));
			rad = ellipseFit[2]*arcMinPerPixel;
			
			primaryOrientationPanel.setInfo(theta,phi,rad);

			ellipseFit = PixelGrouper.estimate_axes(x2, y2);
						
			phi = Math.toDegrees(ellipseFit[4])-90.0d;
			theta = Math.toDegrees(Math.acos(ellipseFit[3]/ellipseFit[2]));
			rad = ellipseFit[2]*arcMinPerPixel;
			
			secondaryOrientationPanel.setInfo(theta,phi,rad);
			
			updateImagePanelInfos();
		}
	}

	public void showError(String msg)
	{
		System.out.println(msg);
	}
	
	protected void updateImagePanelInfos()
	{
		List<DiskInfo> infos = new ArrayList<DiskInfo>();
		
		DiskInfo info = null;
		
		info = new DiskInfo();
		info.setXC(px);
		info.setYC(py);
		primaryOrientationPanel.getDiskInfo(info);
		if(info.getRadArcMin() != 0)
		{
			infos.add(info);
		}
		
		info = new DiskInfo();
		info.setXC(sx);
		info.setYC(sy);
		secondaryOrientationPanel.getDiskInfo(info);
		if(info.getRadArcMin() != 0)
		{
			infos.add(info);
		}
		
		imagePanel.setDiskInfos(infos);
		imagePanel.repaint();
	}
	
	protected List<int[]> fetchDiskPixelCoords(BufferedImage image)
	{
		List<int[]> pixelArrays = new ArrayList<int[]>();
		
		ImageData bgImageData = new ImageData(image);
		
		List<Integer> groupIds = new ArrayList<Integer>();
		List<Integer> groupSizes = new ArrayList<Integer>();
		HashMap<Integer,List<PixelGrouper.Chord>> hmGroups = new HashMap<Integer,List<PixelGrouper.Chord>>();

		PixelGrouper.groupPixels(bgImageData, groupIds, groupSizes, hmGroups);

		int ny = bgImageData.getNy();
		
		Integer pId = PixelGrouper.getGroupIdForCoordinates(px, (ny-1-py), groupIds, hmGroups);
		Integer sId = PixelGrouper.getGroupIdForCoordinates(sx, (ny-1-sy), groupIds, hmGroups);

		int pInd = -1;
		int sInd = -1;
		
		int size = groupIds.size();
		for(int i=0; i<size; i++)
		{
		    if(groupIds.get(i)==pId)
		    {
		    	pInd = i;
		    }
		    if(groupIds.get(i)==sId)
		    {
		    	sInd = i;
		    }
		    if(pInd >=0 && sInd >=0)break;
		}
		int size1 = ((Integer)groupSizes.get(pInd)).intValue();
		int size2 = ((Integer)groupSizes.get(sInd)).intValue();
		
		int x1[] = new int[size1];
		int y1[] = new int[size1];
		
		int x2[] = new int[size2];
		int y2[] = new int[size2];
	
		pixelArrays.add(x1);
		pixelArrays.add(y1);
		pixelArrays.add(x2);
		pixelArrays.add(y2);
		
		PixelGrouper.populateGroupCoordinates(x1,y1,pId,hmGroups);
		PixelGrouper.populateGroupCoordinates(x2,y2,sId,hmGroups);
		
		return pixelArrays;
	}
}
