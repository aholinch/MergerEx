/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectQuery;
import edu.gmu.cds.sim.DiskInfo;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.util.ApplicationProperties;

public class DiskInfoPanel extends TableLayoutPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected String diskName = null;
	protected boolean showDiskName = false;
	protected Color diskColor = null;
	
	protected JLabel lblx   = null;
	protected JLabel lbly   = null;
	protected JLabel lblRA  = null;
	protected JLabel lblDec = null;
	
	protected JTextField txtx   = null;
	protected JTextField txty   = null;
	protected JTextField txtRA  = null;
	protected JTextField txtDec = null;
	
	protected JLabel lblName      = null;
	protected JLabel lblRedshift  = null;
	protected JLabel lblDistance  = null;
	protected JLabel lblMass      = null;
	
	protected JTextField txtName      = null;
	protected JTextField txtRedshift  = null;
	protected JTextField txtDistance  = null;
	protected JTextField txtMass      = null;
	
	protected JButton btnSelect = null;
	protected JButton btnQuery = null;
	
	protected DiskSelectionPanel selectionPanel = null;
	
	public DiskInfoPanel(String name, boolean showName, Color color, DiskSelectionPanel panel)
	{
		super();
		diskName = name;
		showDiskName = showName;
		diskColor = color;
		selectionPanel = panel;
		initGUI();
		UIHelper.setMinPrefSize(this, 300, 210);
	}
	
	public void initGUI()
	{
		JPanel panel1 = buildCoordPanel();
		JPanel panel2 = buildObjectPanel();
		JPanel panel3 = buildButtonPanel();
		
		panel1.setBorder(BorderFactory.createTitledBorder("Coordinates"));
		panel2.setBorder(BorderFactory.createTitledBorder("Object Info"));
	
		// vertical layout
		/**
		double size[][] = {{1,TableLayout.FILL,1d},{2,0.45,1,0.45,1,0.10,2}};
		setLayout(new TableLayout(size));
		
		add(panel1,getRC(1,1));
		add(panel2,getRC(3,1));
		add(panel3,getRC(5,1));
		**/
		
		// horizontal layout
		
		double size[][] = {{1,0.5,1,0.5,1d},{1,0.85,1,0.15,1}};
		setLayout(new TableLayout(size));
		
		add(panel1,getRC(1,1));
		add(panel2,getRC(1,3));
		add(panel3,getRC(3,1,1,3));
		
		if(showDiskName)
		{
			setBorder(BorderFactory.createTitledBorder(diskName));
		}
		else
		{
			setBorder(BorderFactory.createLineBorder(diskColor));
		}
	}
	
	public JPanel buildCoordPanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		
		double size[][] = {{1d,0.3,1d,0.7,1d},{2,0.25,1,0.25,1,0.25,1,0.25,2}};
		panel.setLayout(new TableLayout(size));
		
		lblx = new JLabel("X:");
		lbly = new JLabel("Y:");
		lblRA = new JLabel("RA:");
		lblDec = new JLabel("Dec:");

		int sz = 10;
		txtx = new JTextField(sz);
		txty = new JTextField(sz);
		txtRA = new JTextField(sz);
		txtDec = new JTextField(sz);
		
		panel.add(lblx,panel.getRC(1,1));
		panel.add(lbly,panel.getRC(3,1));
		panel.add(lblRA,panel.getRC(5,1));
		panel.add(lblDec,panel.getRC(7,1));

		panel.add(txtx,panel.getRC(1,3));
		panel.add(txty,panel.getRC(3,3));
		panel.add(txtRA,panel.getRC(5,3));
		panel.add(txtDec,panel.getRC(7,3));
		
		return panel;
	}
	
	public JPanel buildObjectPanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		
		double size[][] = {{1d,0.3,1d,0.7,1d},{2,0.25,1,0.25,1,0.25,1,0.25,2}};
		panel.setLayout(new TableLayout(size));
		
		lblName = new JLabel("Name:");
		lblRedshift = new JLabel("Redshift:");
		lblDistance = new JLabel("Distance:");
		lblMass = new JLabel("Mass:");

		int sz = 10;
		txtName = new JTextField(sz);
		txtRedshift = new JTextField(sz);
		txtDistance = new JTextField(sz);
		txtMass = new JTextField(sz);
		
		panel.add(lblName,panel.getRC(1,1));
		panel.add(lblRedshift,panel.getRC(3,1));
		panel.add(lblDistance,panel.getRC(5,1));
		panel.add(lblMass,panel.getRC(7,1));

		panel.add(txtName,panel.getRC(1,3));
		panel.add(txtRedshift,panel.getRC(3,3));
		panel.add(txtDistance,panel.getRC(5,3));
		panel.add(txtMass,panel.getRC(7,3));
		
		return panel;		
	}
	
	public JPanel buildButtonPanel()
	{
		JPanel panel = new JPanel(new FlowLayout());
		
		btnSelect = new JButton("Select");
		btnQuery = new JButton("Query");
		
		btnSelect.addActionListener(this);
		btnQuery.addActionListener(this);
		
		panel.add(btnSelect);
		panel.add(btnQuery);
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if(src == btnSelect)
		{
			selectionPanel.setSelectionType(diskName);
		}
		else if(src == btnQuery)
		{
			spawnQuery();
		}
	}
	
	public void doQuery()
	{
		double ra = UIHelper.getNum(txtRA);
		double dec = UIHelper.getNum(txtDec);
		
		ObjectInfo info = null;
		
    	
		int type = ApplicationProperties.getInstance().getImageSourceType();
		
		info = ObjectQuery.queryByPos(type, ra, dec);
		selectionPanel.dataChanged = true;
		setObjectInfo(info,false);
	}

	public ObjectInfo getObjectInfoPosition()
	{
		ObjectInfo info = new ObjectInfo();
		info.setName(txtName.getText());
		info.setPx(UIHelper.getNum(txtx));
		info.setPy(UIHelper.getNum(txty));
		return info;
	}
	
	public void setObjectInfo(ObjectInfo info, boolean posOnly)
	{
		if(info == null) return;
		
		if(posOnly)
		{
			UIHelper.format(txtRA, info.getRADeg(), "0.#####");
			UIHelper.format(txtDec, info.getDecDeg(), "0.#####");
			UIHelper.format(txtx, info.getPx(), "0.#####");
			UIHelper.format(txty, info.getPy(), "0.#####");
			
			//txtRA.setText(String.valueOf(info.getRADeg()));
			//txtDec.setText(String.valueOf(info.getDecDeg()));
			//txtx.setText(String.valueOf(info.getPx()));
			//txty.setText(String.valueOf(info.getPy()));
		}
		else
		{
			double c = 299792.458d;
			double H0 = 75.0d;
			
			txtName.setText(info.getName());
			
			double z = info.getRedshift();
			double dist = z*c/H0;
			
			UIHelper.format(txtRedshift, z, "0.######");
			UIHelper.format(txtDistance, dist, "0.##");
			UIHelper.format(txtMass, info.getMass(), "0.###E0");
			//txtRedshift.setText(String.valueOf(z));
			//txtDistance.setText(String.valueOf(dist));
			//txtMass.setText(String.valueOf(info.getMass()));
		}
	}
	
	/**
	 * Set the internal values from this info.
	 * @param info
	 */
	public void setDiskInfo(DiskInfo info)
	{
		if(info == null)
		{
			clearInfo();
			return;
		}
		
		txtName.setText(info.getObjectName());
		UIHelper.format(txtRA, info.getRADeg(), "0.#####");
		UIHelper.format(txtDec, info.getDecDeg(), "0.#####");
		UIHelper.format(txtx, info.getXC(), "0.#####");
		UIHelper.format(txty, info.getYC(), "0.#####");
		UIHelper.format(txtRedshift, info.getRedshift(), "0.######");
		UIHelper.format(txtDistance, info.getDistanceMpc(), "0.##");
		UIHelper.format(txtMass, info.getMassSM(), "0.###E0");
	}
	
	/**
	 * Populate the info values with internal values
	 * @param info
	 */
	public void getDiskInfo(DiskInfo info)
	{
		if(info == null) return;
		
		info.setObjectName(txtName.getText());
		info.setRADeg(UIHelper.getNum(txtRA));
		info.setDecDeg(UIHelper.getNum(txtDec));
		info.setXC(UIHelper.getNum(txtx));
		info.setYC(UIHelper.getNum(txty));
		info.setDistanceMpc(UIHelper.getNum(txtDistance));
		info.setRedshift(UIHelper.getNum(txtRedshift));
		info.setMassSM(UIHelper.getNum(txtMass));
	}
	
	public void clearInfo()
	{
		txtx.setText("");
		txty.setText("");
		txtRA.setText("");
		txtDec.setText("");
		txtName.setText("");
		txtRedshift.setText("");
		txtDistance.setText("");
		txtMass.setText("");
	}
	
	public void spawnQuery()
	{
		Runnable r = new Runnable(){
			public void run()
			{
				Cursor origCursor = DiskInfoPanel.this.getCursor();
				DiskInfoPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try
				{
					doQuery();
				}
				finally
				{
				    DiskInfoPanel.this.setCursor(origCursor);
				}
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
}
