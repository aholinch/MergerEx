/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.gmu.cds.sim.DiskInfo;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.UIHelper;

/**
 * Specify the angles and sizes of the disks.
 * 
 * @author aholinch
 *
 */
public class DiskOrientationPanel extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	
	protected String diskName = null;
	protected boolean showDiskName = false;
	protected Color diskColor = null;
	
	protected DiskSetupPanel setupPanel = null;
	
	protected JTextField txtRadius = null;
	protected JTextField txtTheta = null;
	protected JTextField txtPhi = null;
	
	protected JLabel lblRadius = null;
	protected JLabel lblTheta = null;
	protected JLabel lblPhi = null;

	protected JSlider sldRadius = null;
	protected JSlider sldTheta = null;
	protected JSlider sldPhi = null;
	
	protected double angScale = 0.5;
	protected double radScale = 0.05;
	
	public DiskOrientationPanel(String name, boolean showName, Color color, DiskSetupPanel panel)
	{
		super();
		diskName = name;
		showDiskName = showName;
		diskColor = color;
		setupPanel = panel;
		
		initGUI();
		reset();
		UIHelper.setMinPrefSize(this, 300, 210);
	}
	
	public void initGUI()
	{	
		setLayout(new GridLayout(3,1));
		sldTheta = new JSlider(JSlider.HORIZONTAL, 0, 360,0); // 0 to 180
		sldPhi = new JSlider(JSlider.HORIZONTAL, 0, 720,0); // 0 to 360
		sldRadius = new JSlider(JSlider.HORIZONTAL, 0, 200,0); // 0 to 10
		
		sldTheta.addChangeListener(this);
		sldPhi.addChangeListener(this);
		sldRadius.addChangeListener(this);
		
		txtTheta = new JTextField(10);
		txtPhi = new JTextField(10);
		txtRadius = new JTextField(10);
		
		lblRadius = new JLabel("Disk Radius (arcmin):");
		lblTheta = new JLabel("Disk inclination (deg):");
		lblPhi = new JLabel("Disk position angle (deg):");
		
		JPanel panel = null;
		
		panel = buildPanel(sldTheta,lblTheta,txtTheta);
		add(panel);

		panel = buildPanel(sldPhi,lblPhi,txtPhi);
		add(panel);

		panel = buildPanel(sldRadius,lblRadius,txtRadius);
		add(panel);
		
		if(showDiskName)
		{
			setBorder(BorderFactory.createTitledBorder(diskName));
		}
		else
		{
			setBorder(BorderFactory.createLineBorder(diskColor));
		}
	}
	
	public JPanel buildPanel(JSlider sld, JLabel lbl, JTextField txt)
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		double size[][] = {{2,150,2,0.15,TableLayout.FILL,2},{2,0.5,2,0.5,2}};
		panel.setLayout(new TableLayout(size));
		
		panel.add(lbl,panel.getRC(1,1));
		panel.add(txt,panel.getRC(1,3));
		panel.add(sld,panel.getRC(3,1,1,4));
		
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		return panel;
	}

	public double getAngle(JSlider sld)
	{
		double ang = sld.getValue();
		return ang*angScale;
	}
	
	public double getRadius(JSlider sld)
	{
		double rad = sld.getValue();
		return rad*radScale;
	}

	public void setAngle(JSlider sld, double ang)
	{
		int val = (int)(ang/angScale);
		sld.setValue(val);
	}
	
	public void setRadius(JSlider sld, double rad)
	{
		int val = (int)(rad/radScale);
		sld.setValue(val);
	}
	
	public void reset()
	{
		sldTheta.setValue(0);
		sldPhi.setValue(0);
		sldRadius.setValue(0);
	    txtTheta.setText("0");
	    txtPhi.setText("0");
	    txtRadius.setText("0");
	}
	
	public void setInfo(double thetaDeg, double phiDeg, double radArcMin)
	{
		DecimalFormat df = new DecimalFormat("0.##");
		setAngle(sldTheta,thetaDeg);
		setAngle(sldPhi,phiDeg);
		setRadius(sldRadius,radArcMin);
		
		txtRadius.setText(df.format(radArcMin));
		txtTheta.setText(df.format(thetaDeg));
		txtPhi.setText(df.format(phiDeg));
	}
	
	/**
	 * Set the internal values from this info.
	 * @param info
	 */
	public void setDiskInfo(DiskInfo info)
	{
		if(info == null)
		{
			reset();
			return;
		}
		
		double rad = info.getRadArcMin();
		double theta = info.getThetaDeg();
		double phi = info.getPhiDeg();	
		
		setInfo(theta,phi,rad);
	}
	
	/**
	 * Populate the info values with internal values
	 * @param info
	 */
	public void getDiskInfo(DiskInfo info)
	{
		if(info == null) return;
		
		info.setRadArcMin(UIHelper.getNum(txtRadius));
		info.setThetaDeg(UIHelper.getNum(txtTheta));
		info.setPhiDeg(UIHelper.getNum(txtPhi));
	}

	@Override
	public void stateChanged(ChangeEvent e) 
	{
		double val = 0;
		Object src = e.getSource();
		DecimalFormat df = new DecimalFormat("0.##");
		
		if(src == sldTheta)
		{
			val = getAngle(sldTheta);
			txtTheta.setText(df.format(val));
		}
		else if(src == sldPhi)
		{
			val = getAngle(sldPhi);
			txtPhi.setText(df.format(val));			
		}
		else if(src == sldRadius)
		{
			val = getRadius(sldRadius);
			txtRadius.setText(df.format(val));	
		}
		
		if(setupPanel != null)
		{
			setupPanel.updateDiskInfoFromSliders();
		}
	}

}
