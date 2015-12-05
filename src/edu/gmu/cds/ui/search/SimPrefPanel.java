/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import edu.gmu.cds.util.ApplicationProperties;

public class SimPrefPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected JCheckBox chkAnimate = new JCheckBox("Animate");
	protected JCheckBox chkStereo = new JCheckBox("Stereo");
	protected JTextField txtPartCnt = new JTextField("2000");
	protected JTextField txtSize = new JTextField("2");
	protected JColorChooser jcc = null;
	
	protected JTabbedPane tabs = null;
	
	public SimPrefPanel()
	{
		super(new BorderLayout());
		initGUI();
	}
	
	public void initGUI()
	{
		JPanel topPanel = new JPanel(new GridLayout(3,2));
		topPanel.add(new JLabel("Num Particles:"));
		topPanel.add(txtPartCnt);
		topPanel.add(new JLabel("Particle Size:"));
		topPanel.add(txtSize);
		topPanel.add(chkAnimate);
		topPanel.add(chkStereo);
		JPanel tmpPanel = new JPanel(new FlowLayout());
		tmpPanel.add(topPanel);
	
		jcc = new JColorChooser();
		
		tabs = new JTabbedPane();
		tabs.add("General",tmpPanel);
		tabs.add("Color",jcc);
		
		add(tabs,BorderLayout.CENTER);
	}
	
	public void setPrefsToPanel(ApplicationProperties props)
	{
		chkAnimate.setSelected(props.getAnimateRandom());
		chkStereo.setSelected(props.getShowStereo());
		txtPartCnt.setText(String.valueOf(props.getParticleCount()));
		txtSize.setText(String.valueOf(props.getParticleSize()));
		jcc.setColor(props.getParticleColor());
	}
	
	public void setPanelToPrefs(ApplicationProperties props)
	{
		props.setAnimateRandom(chkAnimate.isSelected());
		props.setShowStereo(chkStereo.isSelected());
		props.setParticleCount(getInt(txtPartCnt,2000));
		props.setParticleSize(getInt(txtSize,2));
		props.setParticleColor(getSelectedColor());
	}
	
	public int getInt(JTextField txt, int def)
	{
		String str = txt.getText();
		int num = def;
		try{num = Integer.parseInt(str.trim());}catch(Exception ex){}
		return num;
	}
	
	/**
	 * Returns the user's selected r,g,b but with alpha set to 20%
	 * @return
	 */
	public Color getSelectedColor()
	{
		Color c = jcc.getColor();
		if(c == null)
		{
			return new Color(1.0f,0.0f,0.0f,0.2f);
		}
		
		Color out = new Color(c.getRed(),c.getGreen(),c.getBlue(),51);
		return out;
	}
}
