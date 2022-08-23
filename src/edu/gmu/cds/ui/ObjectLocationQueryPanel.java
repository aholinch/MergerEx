/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import info.clearthought.layout.TableLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectInfoListener;
import edu.gmu.cds.obj.ObjectQuery;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.util.ApplicationProperties;

public class ObjectLocationQueryPanel extends TableLayoutPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public static final String SDSS_TIP = "Enter SDSS ID like 587722984435351614";
	public static final String NAME_TIP = "Enter name for NED query like Arp 82";
	public static final String PREF_TIP = "Select source for position query";
	public static final String POS_TIP  = "Query preferred source by RA/Dec";
	
	protected JTextField txtName = null;
	protected JTextField txtSDSS = null;
	protected JTextField txtRA = null;
	protected JTextField txtDec = null;
	
	protected JLabel lblName = null;
	protected JLabel lblSDSS = null;
	protected JLabel lblPos = null;
	protected JLabel lblRA = null;
	protected JLabel lblDec = null;
	
	protected JButton btnQueryName = null;
	protected JButton btnQuerySDSS = null;
	protected JButton btnQueryPos = null;
	protected JButton btnReset = null;
	
	protected JRadioButton radNED = null;
	protected JRadioButton radDR7 = null;
	protected JRadioButton radDR8 = null;
	protected JRadioButton radDR9 = null;
	protected JRadioButton radDR17 = null;
	
	protected ButtonGroup bg = null;
	
	protected String defaultImageSrc = null;
	
	protected ApplicationProperties props = null;
	
	protected ObjectInfoListener objectListener = null;
	
	public ObjectLocationQueryPanel()
	{
		initGUI();
		props = ApplicationProperties.getInstance();
		updateUIFromProps();
	}
	
	public void setObjectInfoListener(ObjectInfoListener listener)
	{
		objectListener = listener;
	}
	
	public void initGUI()
	{
		double size[][] = {{2,0.33,1.0/12.0,1.0d/6.0d,1.0/12.0,1.0/6.0,1.0/6.0,2},{2,0.25,0.25,0.25,0.25,2}};
		setLayout(new TableLayout(size));
		
		txtName = new JTextField(30);
		txtSDSS = new JTextField(30);
		txtRA = new JTextField(15);
		txtDec = new JTextField(15);
		
		lblName = new JLabel("Search by Name:");
		lblSDSS = new JLabel("Search by SDSS ID:");
		lblPos = new JLabel("Search by Position:");
		lblRA = new JLabel("  RA");
		lblDec = new JLabel("  Dec");
		lblRA.setToolTipText("DDD.DDDD");
		lblDec.setToolTipText("DD.DDDD");
		
		btnQueryName = new JButton("Query");
		btnQuerySDSS = new JButton("Query");
		btnQueryPos = new JButton("Query");
		btnReset = new JButton("Reset");
		
		btnQueryName.addActionListener(this);
		btnQuerySDSS.addActionListener(this);
		btnQueryPos.addActionListener(this);
		btnReset.addActionListener(this);

		add(txtName,getRC(1,2,1,5));
		add(txtSDSS,getRC(2,2,2,5));
		add(txtRA,getRC(3,3));
		add(txtDec,getRC(3,5));

		add(lblName,getRC(1,1));
		add(lblSDSS,getRC(2,1));
		add(lblPos,getRC(3,1));
		add(lblRA,getRC(3,2));
		add(lblDec,getRC(3,4));
		
		add(btnQueryName,getRC(1,6));
		add(btnQuerySDSS,getRC(2,6));
		add(btnQueryPos,getRC(3,6));
		add(btnReset,getRC(4,6));
		
		radNED = new JRadioButton("NED/DSS");
		radDR7 = new JRadioButton("SDSS DR7");
		radDR8 = new JRadioButton("SDSS DR8");
		radDR9 = new JRadioButton("SDSS DR9");
		radDR17 = new JRadioButton("SDSS DR17");
		bg = new ButtonGroup();
		bg.add(radNED);
		//bg.add(radDR7);
		//bg.add(radDR8);
		//bg.add(radDR9);
		bg.add(radDR17);
		
		radNED.addActionListener(this);
		radDR7.addActionListener(this);
		radDR8.addActionListener(this);
		radDR9.addActionListener(this);
		radDR17.addActionListener(this);
		
		radDR17.setSelected(true);
		
		JPanel radPanel = new JPanel(new FlowLayout());
		JLabel tmpLabel = new JLabel("Preferred Source:");
		tmpLabel.setToolTipText(PREF_TIP);
		radPanel.add(tmpLabel);
		radPanel.add(radNED);
		//radPanel.add(radDR7);
		//radPanel.add(radDR8);
		
		// As of 20121026 I am seeing similar results from DR9 as DR8.
		//radPanel.add(radDR9);
		radPanel.add(radDR17);
		
		add(radPanel,getRC(4,0,1,5));
		
		setBorder(BorderFactory.createTitledBorder("Target Query"));
		
		lblName.setToolTipText(NAME_TIP);
	    txtName.setToolTipText(NAME_TIP);
	    btnQueryName.setToolTipText("Query NED");
	    
		lblSDSS.setToolTipText(SDSS_TIP);
	    txtSDSS.setToolTipText(SDSS_TIP);
	    btnQuerySDSS.setToolTipText("Query SDSS");
	    
	    lblPos.setToolTipText(POS_TIP);
	    txtRA.setToolTipText(POS_TIP);
	    txtDec.setToolTipText(POS_TIP);
	    btnQueryPos.setToolTipText("Query preferred source by position");
	    
	    radNED.setToolTipText("Info from NED, image from DSS");
		//radNED.setToolTipText(PREF_TIP);
	    radDR7.setToolTipText(PREF_TIP);
	    radDR8.setToolTipText(PREF_TIP);
	    radDR9.setToolTipText(PREF_TIP);
	    radDR17.setToolTipText(PREF_TIP);
	}

	public void actionPerformed(ActionEvent evt) 
	{
		Object src = evt.getSource();
		
		if(src == btnQueryName)
		{
			queryByName();
		}
		else if(src == btnQuerySDSS)
		{
			queryBySDSS();
		}
		else if(src == btnQueryPos)
		{
			queryByPos();
		}
		else if(src == btnReset)
		{
			reset();
		}
		else if(src instanceof JRadioButton)
		{
			updatePropsFromUI();
		}
	}
	
	public void queryByName()
	{
	    String name = txtName.getText().trim();	
	    ObjectInfo info = ObjectQuery.queryNEDByName(name);
	    if(info != null)
	    {
	    	txtRA.setText(String.valueOf(info.raDeg));
	    	txtDec.setText(String.valueOf(info.decDeg));
	    	updateObjectInfo(info);
	    }
	}
	
	public void queryBySDSS()
	{
		String sdssId = txtSDSS.getText().trim();
		int type = ObjectQuery.SDSSDR7;
		if(radDR8.isSelected())
		{
			type = ObjectQuery.SDSSDR8;
		}
		else if(radDR9.isSelected())
		{
			type = ObjectQuery.SDSSDR9;
		}
		else if(radDR17.isSelected())
		{
			type = ObjectQuery.SDSSDR17;
		}
	    ObjectInfo info = ObjectQuery.querySDSSByName(type,sdssId);
	    if(info != null)
	    {
	    	txtRA.setText(String.valueOf(info.raDeg));
	    	txtDec.setText(String.valueOf(info.decDeg));
	    	updateObjectInfo(info);
	    }
	}
	
	public void queryByPos()
	{
		double ra = UIHelper.getNum(txtRA);
		double dec = UIHelper.getNum(txtDec);
		ObjectInfo info = null;
		
    	
		int type = ObjectQuery.SDSSDR7;
		if(radDR8.isSelected())
		{
			type = ObjectQuery.SDSSDR8;
		}
		else if(radDR9.isSelected())
		{
			type = ObjectQuery.SDSSDR9;
		}
		else if(radDR17.isSelected())
		{
			type = ObjectQuery.SDSSDR17;
		}
		else if(radNED.isSelected())
		{
			type = ObjectQuery.NED;
		}
		
		info = ObjectQuery.queryByPos(type, ra, dec);
		
	   	updateObjectInfo(info); 		
	}
	
	public void reset()
	{
		txtRA.setText("");
		txtDec.setText("");
		txtName.setText("");
		txtSDSS.setText("");
	}
	
	public void clear()
	{
		txtName.setText("");
		txtSDSS.setText("");
		txtRA.setText("");
		txtDec.setText("");
	}
	
	public void updateObjectInfo(ObjectInfo info)
	{
		if(info == null) return;
		if(objectListener == null) return;
		
		objectListener.foundObject(info);
	}
	
	public void updateUIFromProps()
	{
		defaultImageSrc = props.getImageSource();
		if(defaultImageSrc == null) defaultImageSrc = "DR7";
		
		if("DR7".equals(defaultImageSrc))
		{
			radDR7.setSelected(true);
		}
		else if("DR8".equals(defaultImageSrc))
		{
			radDR8.setSelected(true);
		}
		else if("DR9".equals(defaultImageSrc))
		{
			radDR9.setSelected(true);
		}
		else if("DR17".equals(defaultImageSrc))
		{
			radDR17.setSelected(true);
		}
		else if("NED".equals(defaultImageSrc))
		{
			radNED.setSelected(true);
		}
	}
	
	public void updatePropsFromUI()
	{
		if(radNED.isSelected())
		{
			defaultImageSrc = "NED";
		}
		else if(radDR7.isSelected())
		{
			defaultImageSrc = "DR7";
		}
		else if(radDR8.isSelected())
		{
			defaultImageSrc = "DR8";
		}
		else if(radDR9.isSelected())
		{
			defaultImageSrc = "DR9";
		}
		else if(radDR17.isSelected())
		{
			defaultImageSrc = "DR17";
		}

		props.setImageSource(defaultImageSrc);
		props.save();
	}
}
