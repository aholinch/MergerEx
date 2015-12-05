/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import info.clearthought.layout.TableLayout;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.RotationController;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.ui.slider.CrossHairPanel;
import edu.gmu.cds.ui.slider.DualSliderPanel;
import edu.gmu.cds.ui.slider.Slider2D;
import edu.gmu.cds.ui.slider.Slider2DListener;
import edu.gmu.cds.util.ResourceManager;


public class EnhancePanel extends JPanel implements ActionListener, Slider2DListener, ChangeListener
{
	private static final long serialVersionUID = 1L;
	
	protected String symbols[] = null;
	protected String names[] = null;
	protected String descriptions[][] = null;
	protected String syms[][] = null;
	
	protected String tipPart1 = null;
	protected String tipPart2 = null;

	public static final char theta = '\u0398';
	public static final char phi = '\u03a6';
	
	protected JPanel sliderPanel = null;
	protected Slider2D sld2D = null;
	protected JPanel sldPanelWrapper = null;
	protected CrossHairPanel sldPanel = null;
	
	protected JButton btnSave = new JButton();
	protected JButton btnReset = new JButton();
	protected JButton btnAgain = new JButton();
	protected JButton btnFewer = new JButton();
	
	protected JButton btnFlip1 = new JButton();
	protected JButton btnFlip2 = new JButton();

	protected JToggleButton radM1M2 = new JToggleButton();
	protected JToggleButton radR1R2 = new JToggleButton();
	protected JToggleButton radVXVY = new JToggleButton();
	protected JToggleButton radT1P1 = new JToggleButton();
	protected JToggleButton radT2P2 = new JToggleButton();
	protected JToggleButton radRZVZ = new JToggleButton();
	
	protected AbstractButton buttons[] = {btnSave,btnReset,btnAgain,btnFewer,btnFlip1,btnFlip2,
											radM1M2,radR1R2,radVXVY,radT1P1,radT2P2,radRZVZ};
	protected String toolTips[] = null;
	protected boolean isChanging = false;
	protected int valPairNum = 0;
	
	protected int tmpN1 = 0;
	protected int tmpN2 = 0;
	
	protected double params[] = null;
	
	protected double BIGNMULT = 3.0;
	//protected ScatterPanelOptionMenu spom = null;
	
	protected boolean useTwoSliders = false;
	
	protected DualSliderPanel dualSliderPanel = null;
	
	protected ScatterPanel sp = null;
	protected ImagePanel imagePanel = null;
	protected SimulationHandler simHandler = null;
	protected TargetData targetData = null;
	protected RotationController rc = null;
	
	public EnhancePanel()
    {
    	super();
		initGUI();
	}
	
	/**
	 * Used primarily for min and max info.
	 * 
	 * @param data
	 */
	public void setTargetData(TargetData data)
	{
		targetData = data;
		if(data != null)
		{
			double scale[] = data.calculateScale();
			sp.setDataRange(scale[0], scale[1], scale[2], scale[3]);
		}
	}
	
	public void setSimulationHandler(SimulationHandler handler)
	{
		simHandler = handler;
	}
	
    public void setTargetImage(BufferedImage image)
    {
    	imagePanel.setImage(image);
    	imagePanel.repaint();
    }
	
	public void setNames()
	{
		for(int i=0; i<6; i++)
		{
			buttons[i+6].setText(names[i]);
		}
	}
	
	public void setSymbols()
	{
		for(int i=0; i<6; i++)
		{
			buttons[i+6].setText(symbols[i]);
		}		
	}
	
	public void initGUI()
	{
		ResourceBundle rb = ResourceManager.getInstance().getDefaultResourceBundle();

		names = buildNamesFromResource(rb);
		symbols = buildSymbolsFromResource(rb);
		descriptions = buildDescriptionsFromResource(rb);
		syms = buildSymsFromResource(rb);
		
		tipPart1 = rb.getString("enhance.tippart1");
		tipPart2 = rb.getString("enhance.tippart2");
		
		double size[][] = {{TableLayout.FILL},{0.01,0.53,0.01,0.07,0.01,0.36,0.01}};
		//double size[][] = {{TableLayout.FILL},{2,TableLayout.FILL,5,30,10,0.36,2}};
		setLayout(new TableLayout(size));
		
		sp = new ScatterPanel();
		imagePanel = new ImagePanel();
		
		sp.setDefaultSymbolSize(2);
		
		JPanel topPanel = new JPanel(new GridLayout(1,2,1,1));
		topPanel.add(sp);
		topPanel.add(imagePanel);
		
		rc = new RotationController();
		rc.setController(sp);
		/*
		rc = new RotationController();
		statePanel = new StatePanel();
		
		//statePanel.addComponentListener(sizeManager);
		statePanel.initPanel(this);
		statePanel.setLayout(StatePanel.LAYOUT_1x1);
		statePanel.initGUI();
		statePanel.setSizes(250,250);
		statePanel.addRotationController(rc,0);
		statePanel.setUseFilter(false);
		statePanel.setUseAddedColors(false);
		//statePanel.setDefaultSymbolSize(2);
		*/
		
		sliderPanel = buildSliderPanel(rb);
		
		btnSave.setText(rb.getString("enhance.save"));
		btnReset.setText(rb.getString("enhance.reset"));
		btnAgain.setText(rb.getString("enhance.again"));
		btnFewer.setText(rb.getString("enhance.fewer"));
		
		btnReset.setToolTipText(rb.getString("enhance.resettip"));
		btnAgain.setToolTipText(rb.getString("enhance.againtip"));
		btnFewer.setToolTipText(rb.getString("enhance.fewertip"));
		btnSave.setToolTipText(rb.getString("enhance.savetip"));
		
		btnReset.addActionListener(this);
		btnAgain.addActionListener(this);
		btnFewer.addActionListener(this);
		btnSave.addActionListener(this);
		
		JPanel btnPanel = new JPanel(new FlowLayout());
		btnPanel.add(btnReset);
		btnPanel.add(btnAgain);
		btnPanel.add(btnFewer);
		btnPanel.add(btnSave);
		
		add(topPanel,getRC(1,0));
		add(btnPanel,getRC(3,0));
		add(sliderPanel,getRC(5,0));
		
		int len = buttons.length;
		toolTips = new String[len];
		for(int i=0; i<len; i++)
		{
			toolTips[i] = buttons[i].getToolTipText();
		}
		
		//spom = new ScatterPanelOptionMenu(statePanel.isps[0]);
		UIHelper.emphasizeButton(btnSave);
		setNames();
	}
	
	/**
	 * Build the panel with the 2d slider and pair selectors.
	 * 
	 * @return
	 */
	public JPanel buildSliderPanel(ResourceBundle rb)
	{
		double size[][] = {{0.024,0.18,0.024,0.28,0.024,0.22,0.024,0.2,0.024},{0.07,0.22,0.1,0.22,0.1,0.22,0.07}};

		JPanel panel = new JPanel(new TableLayout(size));
	    
		btnFlip1.setText(rb.getString("enhance.flip1"));
		btnFlip2.setText(rb.getString("enhance.flip2"));
		
	    sldPanel = new CrossHairPanel(150,150);
	    sld2D = new Slider2D(sldPanel);
	    sld2D.setSlider2DListener(this);

	    dualSliderPanel = new DualSliderPanel();
	    dualSliderPanel.setPreferredSize(new Dimension(150,150));
	    //dualSliderPanel.addChangeListener(this);
	    
	    radM1M2.setSelected(true);
	    valPairNum = 0;
	    
	    ButtonGroup bg = new ButtonGroup();
	    bg.add(radM1M2);
	    bg.add(radR1R2);
	    bg.add(radVXVY);
	    bg.add(radT1P1);
	    bg.add(radT2P2);
	    bg.add(radRZVZ);
	    
	    btnFlip1.addActionListener(this);
	    btnFlip2.addActionListener(this);
		radM1M2.addActionListener(this);
		radR1R2.addActionListener(this);
		radVXVY.addActionListener(this);
		radT1P1.addActionListener(this);
		radT2P2.addActionListener(this);
		radRZVZ.addActionListener(this);
		
		btnFlip1.setToolTipText(rb.getString("enhance.flip1tip"));
		btnFlip2.setToolTipText(rb.getString("enhance.flip2tip"));
		radM1M2.setToolTipText(rb.getString("enhance.masstip"));
		radVXVY.setToolTipText(rb.getString("enhance.speedtip"));
		radR1R2.setToolTipText(rb.getString("enhance.sizetip"));
		radT1P1.setToolTipText(rb.getString("enhance.angles1tip"));
		radT2P2.setToolTipText(rb.getString("enhance.angles2tip"));
		radRZVZ.setToolTipText(rb.getString("enhance.planetip"));
		
		int row = 0;
		int col = 1;
		
		row++;
		
		panel.add(radM1M2,getRC(row,col));
		
		row++;
		row++;
		panel.add(radVXVY,getRC(row,col));
		
		row++;
		row++;
		panel.add(radRZVZ,getRC(row,col));
		
		col = 3;
		row = 1;

		sldPanelWrapper = new JPanel(new CardLayout());
		sldPanelWrapper.add(dualSliderPanel,"dp");
		sldPanelWrapper.add(sldPanel,"twod");
	    CardLayout cl = (CardLayout)(sldPanelWrapper.getLayout());
	    cl.show(sldPanelWrapper, "twod");
	    
		//panel.add(sldPanel,getRC(row,col,row+4,col));
		panel.add(sldPanelWrapper,getRC(row,col,row+4,col));
		
		col = 5;
		row = 1;

		panel.add(radR1R2,getRC(row,col));
		
		row++;
		row++;
		panel.add(radT1P1,getRC(row,col));
		
		row++;
		row++;
		panel.add(radT2P2,getRC(row,col));
		
		col = 7;
		row = 3;

		panel.add(btnFlip1,getRC(row,col));

		row++;
		row++;
		panel.add(btnFlip2,getRC(row,col));
		
	    return panel;
	}

	public String getRC(int row, int col)
	{
		return String.valueOf(col)+", " + String.valueOf(row);
	}
	
	public String getRC(int row, int col, int endRow, int endCol)
	{
		return String.valueOf(col)+", " + String.valueOf(row)+ ", "+String.valueOf(endCol)+", " + String.valueOf(endRow);
	}
	
	public void clear()
	{
		sp.clear();
		sp.clearQRot();
		initSliderValues(false);
		sliderChanged(0,0);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if(src instanceof JToggleButton)
		{
			if(src == radM1M2)
			{
				valPairNum = 0;
				initSliderValues(true);
			}
			else if(src == radR1R2)
			{
				valPairNum = 1;
				initSliderValues(true);
			}
			else if(src == radVXVY)
			{
				valPairNum = 2;
				initSliderValues(true);
			}
			else if(src == radT1P1)
			{
				valPairNum = 3;
				initSliderValues(true);
			}
			else if(src == radT2P2)
			{
				valPairNum = 4;
				initSliderValues(true);
			}
			else if(src == radRZVZ)
			{
				valPairNum = 5;
				initSliderValues(true);
			}
		}
		else if(src == btnFlip1)
		{
			if(params!=null)
			{
				params[12]=mod360(params[12]+180);
				runCalculation(true,1000);
			}
		}
		else if(src == btnFlip2)
		{
			if(params!=null)
			{
				params[13]=mod360(params[13]+180);
				runCalculation(true,1000);
			}
		}
		else if(src == btnReset)
		{
			rc.clearRotation();
			sp.repaint();
		}
		else if(src == btnAgain)
		{
			if(params == null)return;
			runCalculation(true,(int)BIGNMULT*1000);
		}
		else if(src == btnFewer)
		{
			if(params == null)return;
			runCalculation(true,1000);
		}
		else if(src == btnSave)
		{
			saveSim();
		}
	}
	
	public void stateChanged(ChangeEvent e)
	{
		double d[] = dualSliderPanel.getValues();
		sliderChanged(d[0],d[1]);
	}

	/**
	 * Responds to changes in the 2D slider.
	 */
	public void sliderChanged(double v1, double v2)
	{
		if(isChanging)
		{
			return;
		}
		
		if(params == null)
		{
			return;
		}
		
		isChanging = true;

		boolean doCalc = false;
		switch(valPairNum)
		{
			case 0:
				//jsp.mass1 = v1;
				params[6]=v1;
				//jsp.mass2 = v2;
				params[7]=v2;
				doCalc = true;
				break;
			case 1:
				//jsp.rout1 = v1;
				params[8] = v1;
				//jsp.rout2 = v2;
				params[9] = v2;
				doCalc = true;
				break;
			case 2:
				//jsp.vx = v1;
				params[3] = v1;
				//jsp.vy = v2;
				params[4] = v2;
				doCalc = true;
				break;
			case 3:
				//jsp.theta1 = v1;
				params[12]=v1;
				//jsp.phi1 = v2;
				params[10]=v2;
				doCalc = true;
				break;
			case 4:
				//jsp.theta2 = v1;
				params[13]=v1;
				//jsp.phi2 = v2;
				params[11]=v2;
				doCalc = true;
				break;
			case 5:
				//jsp.rz = v1;
				params[2] = v1;
				//jsp.vz = v2;
				params[5] = v2;
				doCalc = true;
				break;
		}	
		
		if(doCalc)
		{
			runCalculation();
		}
		
		isChanging = false;
	}
	
	public void runCalculation()
	{
		runCalculation(false,1000);
	}
	
	public void runCalculation(boolean animate, int numPart)
	{
		if(simHandler == null) return;
		
		StateInfo info = new StateInfo();
		info.setParams(params);
		simHandler.runSimulation(info, sp, numPart, animate, true);
	}
		
	public void saveSim()
	{
		if(params == null)
		{
			return;
		}
		
		StateInfo info = new StateInfo();
		info.setParams(params);
		info.enhanced = 1;
		simHandler.selectSimulation(info);
		simHandler.incrementEnhanced();
	}
	
	public void stateInfoSelectedForEnhance(StateInfo info)
	{
		params = info.getParamsCopy();
		params[10]=mod360(params[10]);
		params[11]=mod360(params[11]);
		params[12]=mod360(params[12]);
		params[13]=mod360(params[13]);
		rc.clearRotation();
		runCalculation(false,1000);
		initSliderValues(true);
	}

	public void initSliderValues(boolean showInitPos)
	{
		if(params == null || targetData == null)
		{
			return;
		}
		
		double mins[] = targetData.getMinimums();
		double maxs[] = targetData.getMaximums();
		
		double min1 = 0;
		double max1 = 0;
		double min2 = 0;
		double max2 = 0;

		double val1 = 0;
		double val2 = 0;
	
		String lbl1 = "";
		String lbl2 = "";
		
		lbl1 = descriptions[valPairNum][0];
		lbl2 = descriptions[valPairNum][1];
		String s1 = syms[valPairNum][0];
		String s2 = syms[valPairNum][1];
		
		switch(valPairNum)
		{
			// M1 vs. M2
			case 0:
				min1 = mins[6];
				min2 = mins[7];
				max1 = maxs[6];
				max2 = maxs[7];
				val1 = params[6];
				val2 = params[7];
				break;
				
			// R1 vs. R2
			case 1:
				min1 = mins[8];
				min2 = mins[9];
				max1 = maxs[8];
				max2 = maxs[9];
				val1 = params[8];
				val2 = params[9];
				break;
				
			// VX vs. VY
			case 2:
				min1 = mins[3];
				min2 = mins[4];
				max1 = maxs[3];
				max2 = maxs[4];
				val1 = params[3];
				val2 = params[4];
				break;
				
		    // Theta1 vs. Phi1
			case 3:
				min1 = 0;
				min2 = 0;
				max1 = 360;
				max2 = 360;
				val1 = mod360(params[12]);
				val2 = mod360(params[10]);
				break;
				
		    // Theta2 vs. Phi2
			case 4:
				min1 = 0;
				min2 = 0;
				max1 = 360;
				max2 = 360;
				val1 = mod360(params[13]);
				val2 = mod360(params[11]);
				break;
				
		    // RZ vs. VZ
			case 5:
				min1 = mins[2];
				min2 = mins[5];
				max1 = maxs[2];
				max2 = maxs[5];
				val1 = params[2];
				val2 = params[5];
				break;
				
			default:
				break;
		}
		
		sld2D.setRanges(min1,max1,min2,max2);
		dualSliderPanel.setRanges(min1,max1,min2,max2);
		if(showInitPos)
		{
			sld2D.setUpdateInit(true);
			sld2D.setValues(val1,val2);
			sld2D.setUpdateInit(false);
		}
		else
		{
			sld2D.setUpdateInit(false);			
			sld2D.setValues(val1,val2);
		}
		
		dualSliderPanel.setValues(val1,val2);
		
		dualSliderPanel.setLabels(s1, s2);
		
		String txt = tipPart1 + lbl1 + ".<br>" + tipPart2 + lbl2 + ".";
		txt = "<html>"+txt+"</html>";
		sldPanel.setToolTipText(txt);
		dualSliderPanel.setToolTipText(txt);
		dualSliderPanel.repaint();
	}
	
	public double mod360(double v)
	{
		while(v >= 360.0d)
		{
			v-= 360.0d;
		}
		
		while(v < 0)
		{
			v+=360.0d;
		}
		
		return v;
	}
	
	public void enablePanel()
	{
		for(int i=0; i<buttons.length; i++)
		{
			buttons[i].setEnabled(true);
			buttons[i].setToolTipText(toolTips[i]);
		}
	}
	
	public void disablePanel()
	{
		for(int i=0; i<buttons.length; i++)
		{
			buttons[i].setEnabled(false);
			buttons[i].setToolTipText(null);
		}
	}
	
	protected String[] buildNamesFromResource(ResourceBundle rb)
	{
		String names[] = {rb.getString("enhance.names1"),
				rb.getString("enhance.names2"),
				rb.getString("enhance.names3"),
				rb.getString("enhance.names4"),
				rb.getString("enhance.names5"),
				rb.getString("enhance.names6"),
				};
		return names;
	}
	
	protected String[] buildSymbolsFromResource(ResourceBundle rb)
	{
		String symbols[] = {rb.getString("enhance.symbols1"),
				rb.getString("enhance.symbols2"),
				rb.getString("enhance.symbols3"),
				rb.getString("enhance.symbols4"),
				rb.getString("enhance.symbols5"),
				rb.getString("enhance.symbols6"),
				};
		return symbols;
	}
	
	protected String[][] buildDescriptionsFromResource(ResourceBundle rb)
	{
		String sa[][] = {{rb.getString("enhance.desc11"),rb.getString("enhance.desc12")},
				{rb.getString("enhance.desc21"),rb.getString("enhance.desc22")},
				{rb.getString("enhance.desc31"),rb.getString("enhance.desc32")},
				{rb.getString("enhance.desc41"),rb.getString("enhance.desc42")},
				{rb.getString("enhance.desc51"),rb.getString("enhance.desc52")},
				{rb.getString("enhance.desc61"),rb.getString("enhance.desc62")}};
		return sa;
	}

	protected String[][] buildSymsFromResource(ResourceBundle rb)
	{
		String sa[][] = {{rb.getString("enhance.sym11"),rb.getString("enhance.sym12")},
				{rb.getString("enhance.sym21"),rb.getString("enhance.sym22")},
				{rb.getString("enhance.sym31"),rb.getString("enhance.sym32")},
				{rb.getString("enhance.sym41"),rb.getString("enhance.sym42")},
				{rb.getString("enhance.sym51"),rb.getString("enhance.sym52")},
				{rb.getString("enhance.sym61"),rb.getString("enhance.sym62")}};
		return sa;
	}
	
	public void updateLocalizedResources(ResourceBundle rb)
	{
		
		btnSave.setText(rb.getString("enhance.save"));
		btnReset.setText(rb.getString("enhance.reset"));
		btnAgain.setText(rb.getString("enhance.again"));
		btnFewer.setText(rb.getString("enhance.fewer"));
		
		btnReset.setToolTipText(rb.getString("enhance.resettip"));
		btnAgain.setToolTipText(rb.getString("enhance.againtip"));
		btnFewer.setToolTipText(rb.getString("enhance.fewertip"));
		btnSave.setToolTipText(rb.getString("enhance.savetip"));
		
		btnFlip1.setText(rb.getString("enhance.flip1"));
		btnFlip2.setText(rb.getString("enhance.flip2"));
		btnFlip1.setToolTipText(rb.getString("enhance.flip1tip"));
		btnFlip2.setToolTipText(rb.getString("enhance.flip2tip"));
		radM1M2.setToolTipText(rb.getString("enhance.masstip"));
		radVXVY.setToolTipText(rb.getString("enhance.speedtip"));
		radR1R2.setToolTipText(rb.getString("enhance.sizetip"));
		radT1P1.setToolTipText(rb.getString("enhance.angles1tip"));
		radT2P2.setToolTipText(rb.getString("enhance.angles2tip"));
		radRZVZ.setToolTipText(rb.getString("enhance.planetip"));
			
		names = buildNamesFromResource(rb);
		symbols = buildSymbolsFromResource(rb);
		descriptions = buildDescriptionsFromResource(rb);
		syms = buildSymsFromResource(rb);
		
		tipPart1 = rb.getString("enhance.tippart1");
		tipPart2 = rb.getString("enhance.tippart2");
		
		int len = buttons.length;
		toolTips = new String[len];
		for(int i=0; i<len; i++)
		{
			toolTips[i] = buttons[i].getToolTipText();
		}
	}
	
	/**
	 * Adjust the flag
	 * @param flag
	 */
	public void setUseTwoSliders(boolean flag)
	{
		if(flag && !useTwoSliders)
		{
			setDualSliders();
		}
		else if(!flag && useTwoSliders)
		{
			setCrossHairCursor();
		}
		useTwoSliders = flag;
	}
	
	/**
	 * Put the dual slider cursor into place
	 */
	public void setDualSliders()
	{
	    CardLayout cl = (CardLayout)(sldPanelWrapper.getLayout());
	    cl.show(sldPanelWrapper, "dp");
		sldPanelWrapper.repaint();
	}
	
	/**
	 * Put the cross hair cursor into place
	 */
	public void setCrossHairCursor()
	{
	    CardLayout cl = (CardLayout)(sldPanelWrapper.getLayout());
	    cl.show(sldPanelWrapper, "twod");
		sldPanelWrapper.repaint();
	}

	
	
}
