/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import info.clearthought.layout.TableLayout;

import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CursorInfoPanel extends TableLayoutPanel 
{
	private static final long serialVersionUID = 1L;

	protected JLabel lblX = null;
	protected JLabel lblY = null;
	protected JLabel lblAng1 = null;
	protected JLabel lblAng2 = null;

	protected JTextField txtX = null;
	protected JTextField txtY = null;
	protected JTextField txtAng1 = null;
	protected JTextField txtAng2 = null;
	
	public CursorInfoPanel()
	{
		initGUI();
		setValues(0,0,0,0);
	}
	
	public void initGUI()
	{
		double hgt = 1.0d/4.0d;
		double size[][] = {{2,0.3,2,0.7,2},{2,hgt,2,hgt,2,hgt,2,hgt,2}};
		setLayout(new TableLayout(size));
		
		txtX         = new JTextField(5);
		txtY         = new JTextField(5);
		txtAng1      = new JTextField(5);
		txtAng2      = new JTextField(5);
		
		lblX         = new JLabel("X:");
		lblY         = new JLabel("Y:");
		lblAng1      = new JLabel("RA:");
		lblAng2      = new JLabel("Dec:");
		
		add(lblX,getRC(1, 1));
		add(txtX,getRC(1, 3));
		add(lblY,getRC(3, 1));
		add(txtY,getRC(3, 3));
		add(lblAng1,getRC(5, 1));
		add(txtAng1,getRC(5, 3));
		add(lblAng2,getRC(7, 1));
		add(txtAng2,getRC(7, 3));
		
		setBorder(BorderFactory.createTitledBorder("Cursor Info"));
	}
	
	public void setAngleLabels(String angle1, String angle2)
	{
		lblAng1.setText(angle1);
		lblAng2.setText(angle2);
	}
	
	public void setValues(double x, double y, double ang1, double ang2)
	{
		txtX.setText(format(x,"0.0"));
		txtY.setText(format(y,"0.0"));
		txtAng1.setText(format(ang1,"0.00000"));
		txtAng2.setText(format(ang2,"0.00000"));
		txtAng1.setCaretPosition(0);
		txtAng2.setCaretPosition(0);
	}
	
	public String format(double num, String fmt)
	{
		DecimalFormat df = new DecimalFormat(fmt);
		return df.format(num);
	}
}
