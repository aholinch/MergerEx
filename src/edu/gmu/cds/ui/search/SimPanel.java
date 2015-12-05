/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.gmu.cds.img.ContourFinder;
import edu.gmu.cds.img.ContourPoint;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.RotationController;
import edu.gmu.cds.ui.ScatterPanel;

public class SimPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	protected Color selectionColor = Color.red;
	
	protected ScatterPanel sp1 = new ScatterPanel();
	protected ScatterPanel sp2 = new ScatterPanel();
	protected ScatterPanel sp3 = new ScatterPanel();
	protected ScatterPanel sp4 = new ScatterPanel();
	protected ScatterPanel sp5 = new ScatterPanel();
	protected ScatterPanel sp6 = new ScatterPanel();
	protected ScatterPanel sp7 = new ScatterPanel();
	protected ScatterPanel sp8 = new ScatterPanel();
	
	protected int numPanels = 8;
	protected ScatterPanel sps[] = {sp1,sp2,sp3,sp4,sp5,sp6,sp7,sp8};
	protected boolean isSelected[] = new boolean[8];
	protected HashMap<ScatterPanel,Integer> hmPanels = null;
	
	protected ImagePanel imagePanel = new ImagePanel();
	
	protected JButton btnMore = null;
	
	protected JPanel centerPanel = null;
	
	protected SimulationHandler simHandler = null;
	
	protected TargetData targetData = null;
	
	protected boolean isClickable = true;
	protected boolean isRunning = false;
	protected boolean animate = true;
	protected boolean showStereo = false;
	protected ExecutorService executor = null;
	
	public SimPanel()
	{
		super(new BorderLayout());
		initGUI();
	}
	
	public void setClickable(boolean flag)
	{
		isClickable = flag;
	}
	
	public void setRunning(boolean flag)
	{
		isRunning = flag;
	}
	
	public void setSimulationHander(SimulationHandler handler)
	{
		simHandler = handler;
	}
	
	protected void initGUI()
	{
		initScatterPanels();
		
	 	centerPanel = new JPanel(new GridLayout(3,3,5,5));
	 	for(int i=0; i<4; i++)
	 	{
	 		centerPanel.add(sps[i]);
	 	}
	 	centerPanel.add(imagePanel);
	 	for(int i=0; i<4; i++)
	 	{
	 		centerPanel.add(sps[i+4]);
	 	}
	 	
	 	add(centerPanel,BorderLayout.CENTER);
	 	
	 	btnMore = new JButton("More");
	 	btnMore.setToolTipText("Run more simulations from randomly selected parameters");
	 	
	 	btnMore.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				spawnMore();
			}
	 		
	 	});
	 	
	 	JPanel btnPanel = new JPanel(new FlowLayout());
	 	btnPanel.add(btnMore);
	 	add(btnPanel,BorderLayout.SOUTH);
	}
	
	protected void initScatterPanels()
	{
		hmPanels = new HashMap<ScatterPanel,Integer>();
		
	    MouseListener ml = new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		Object src = e.getSource();
	    		if(src instanceof ScatterPanel)
	    		{
	    			togglePanel((ScatterPanel)src);
	    		}
	    	}
	    };
	    
	    
	    ScatterPanel sp = null;
	    for(int i=0; i<numPanels; i++)
	    {
	    	sp = sps[i];
	    	RotationController rc = new RotationController();
	        rc.setController(sp);
	    	sp.setDrawTime(false);
	    	hmPanels.put(sp, new Integer(i));
	    	sp.addMouseListener(ml);
	    }
	}
	
    public void setTargetImage(BufferedImage image)
    {
    	clearSims();

    	imagePanel.setImage(image);
    	imagePanel.repaint();
    }
	
	public void setTargetData(TargetData data)
	{
		targetData = data;
		updateScale();
	}
	
	protected void updateScale()
	{
	    if(targetData == null) return;	

	    double scale[] = targetData.calculateScale();
	    
	    for(int i=0; i<sps.length; i++)
	    {
	    	sps[i].setDataRange(scale[0],scale[1],scale[2],scale[3]);
	    }
	}
	
    protected void clearSims()
    {
		// clear panels
		ScatterPanel sp = null;
		for(int i=0; i<numPanels; i++)
		{
			sp = sps[i];
			sp.setBorder(null);
			sp.clear();
			sp.clearQRot();
			sp.repaint();
			isSelected[i]=false;
		}    	
    }
    
    public void spawnMore()
    {
    	Runnable r = new Runnable(){
    		public void run()
    		{
    			Cursor c = SimPanel.this.getCursor();
    			SimPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    		
    			try
    			{
    				setClickable(false);
    				doMore();
    				setClickable(true);
    			}
    			finally
    			{
    				SimPanel.this.setCursor(c);
    			}
    		}
    	};
    	
    	if(executor != null)
    	{
    		executor.submit(r);
    	}
    	else
    	{
    		Thread t = new Thread(r);
    		t.start();
    	}
    }
    
	public void doMore()
	{
		if(isRunning) return;
		
		clearSims();
		
		// run simulations
        if(simHandler != null)
        {
        	simHandler.runRandomSimulations(sps,animate);
        }
	}
	
	public void togglePanel(ScatterPanel panel)
	{
		int ind = hmPanels.get(panel);
		boolean flag = isSelected[ind];
		flag = !flag;
		isSelected[ind] = flag;
		
		if(flag)
		{
			panel.setBorder(BorderFactory.createLineBorder(selectionColor,4));
			if(simHandler != null)
			{
				simHandler.selectSimulation(panel);
			}
		}
		else
		{
			panel.setBorder(null);
			if(simHandler != null)
			{
				simHandler.unselectSimulation(panel);
			}
		}
	}

	public void setAnimate(boolean flag)
	{
		animate = flag;
	}
	
	public void setShowStereo(boolean flag)
	{
		showStereo = flag;
		for(int i=0; i<sps.length; i++)
		{
			sps[i].setShowStereo(flag);
		}
	}
}
