/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import info.clearthought.layout.TableLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.gmu.cds.sim.SimUtil;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.sim.TargetData.TargetDataHandler;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.UIHelper;

public class ParameterRangePanel extends JPanel implements TargetDataHandler, ActionListener
{
	private static final long serialVersionUID = 1L;

	protected JTextField txtMinRx = new JTextField(10);
	protected JTextField txtMinRy = new JTextField(10);
	protected JTextField txtMinRz = new JTextField(10);
	protected JTextField txtMaxRx = new JTextField(10);
	protected JTextField txtMaxRy = new JTextField(10);
	protected JTextField txtMaxRz = new JTextField(10);
	
	protected JTextField txtMinVx = new JTextField(10);
	protected JTextField txtMinVy = new JTextField(10);
	protected JTextField txtMinVz = new JTextField(10);
	protected JTextField txtMaxVx = new JTextField(10);
	protected JTextField txtMaxVy = new JTextField(10);
	protected JTextField txtMaxVz = new JTextField(10);
	
	protected JTextField txtMinM1 = new JTextField(10);
	protected JTextField txtMinM2 = new JTextField(10);
	protected JTextField txtMaxM1 = new JTextField(10);
	protected JTextField txtMaxM2 = new JTextField(10);
	
	protected JTextField txtMinR1 = new JTextField(10);
	protected JTextField txtMinR2 = new JTextField(10);
	protected JTextField txtMaxR1 = new JTextField(10);
	protected JTextField txtMaxR2 = new JTextField(10);
	
	protected JTextField txtTheta1Rng = new JTextField(10);
	protected JTextField txtTheta2Rng = new JTextField(10);
	protected JTextField txtPhi1Rng = new JTextField(10);
	protected JTextField txtPhi2Rng = new JTextField(10);
	
	protected JTextField txtMins[] = {txtMinRx,txtMinRy,txtMinRz,
			                          txtMinVx,txtMinVy,txtMinVz,
			                          txtMinM1,txtMinM2,txtMinR1,txtMinR2,
			                          txtTheta1Rng,txtTheta2Rng};
	
	protected JTextField txtMaxs[] = {txtMaxRx,txtMaxRy,txtMaxRz,
			                          txtMaxVx,txtMaxVy,txtMaxVz,
			                          txtMaxM1,txtMaxM2,txtMaxR1,txtMaxR2,
			                          txtPhi1Rng,txtPhi2Rng};
	
	protected TargetData targetData = null;
	
	protected double[] initMins = null;
	protected double[] initMaxs = null;
	
	protected JButton btnCalc = null;
	
	public ParameterRangePanel()
	{
		initGUI();
	}
	
	public void initGUI()
	{
		setLayout(new BorderLayout());
		
		JPanel pnl = buildParamPanel();
		
		JLabel topLabel = new JLabel("The quantities below are in simulation units.");
		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(topLabel);
		
		JPanel bottomPanel = new JPanel(new FlowLayout());
		btnCalc = new JButton("Calculate Simulation Ranges");
		btnCalc.addActionListener(this);
		bottomPanel.add(btnCalc);
		
		add(pnl,BorderLayout.CENTER);
		add(topPanel,BorderLayout.NORTH);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public JPanel buildParamPanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		
		double hgt = 1/12.0d;
		
		double size[][] = {{5,0.2,2,0.3,10,0.2,3,0.3,5},{2,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,hgt,1,2}};
		panel.setLayout(new TableLayout(size));
	
		int row = 1;
		addRow(panel,txtMinRx,txtMaxRx,"Rx",row);
		row+=2;
		addRow(panel,txtMinRy,txtMaxRy,"Ry",row);
		row+=2;
		addRow(panel,txtMinRz,txtMaxRz,"Rz",row);
		
		row+=2;
		addRow(panel,txtMinVx,txtMaxVx,"Vx",row);
		row+=2;
		addRow(panel,txtMinVy,txtMaxVy,"Vy",row);
		row+=2;
		addRow(panel,txtMinVz,txtMaxVz,"Vz",row);
		
		row+=2;
		addRow(panel,txtMinM1,txtMaxM1,"M1",row);
		row+=2;
		addRow(panel,txtMinM2,txtMaxM2,"M2",row);
		
		row+=2;
		addRow(panel,txtMinR1,txtMaxR1,"Radius 1",row);
		row+=2;
		addRow(panel,txtMinR2,txtMaxR2,"Radius 2",row);
		
		JLabel lbl = null;
		
		row+=2;
		lbl = new JLabel("Inc 1 Range:");
		panel.add(lbl,panel.getRC(row,1));
		panel.add(txtTheta1Rng,panel.getRC(row, 3));
		lbl = new JLabel("Angle 1 Range:");
		panel.add(lbl,panel.getRC(row,5));
		panel.add(txtPhi1Rng,panel.getRC(row, 7));
		
		row+=2;
		lbl = new JLabel("Inc 2 Range:");
		panel.add(lbl,panel.getRC(row,1));
		panel.add(txtTheta2Rng,panel.getRC(row, 3));
		lbl = new JLabel("Angle 2 Range:");
		panel.add(lbl,panel.getRC(row,5));
		panel.add(txtPhi2Rng,panel.getRC(row, 7));
		
		txtMinRx.setEditable(false);
		txtMaxRx.setEditable(false);
		txtMinRy.setEditable(false);
		txtMaxRy.setEditable(false);
		
		return panel;
	}
	
	public void addRow(TableLayoutPanel panel, JTextField txt1, JTextField txt2, String lbl, int row)
	{
		JLabel lbl1 = new JLabel("Min " + lbl + ":");
		JLabel lbl2 = new JLabel("Max " + lbl + ":");
		
		panel.add(lbl1,panel.getRC(row,1));
		panel.add(txt1,panel.getRC(row,3));
		panel.add(lbl2,panel.getRC(row,5));
		panel.add(txt2,panel.getRC(row,7));
	}
	
	public void calculateMinMax()
	{
		if(targetData == null) return;
		
		double vals[][] = SimUtil.initializeMinMax(targetData);
		
		double mins[] = vals[0];
		double maxs[] = vals[1];
		
		int len = mins.length;
		
		for(int i=0; i<len; i++)
		{
			UIHelper.format(txtMins[i], mins[i], "0.######");
			UIHelper.format(txtMaxs[i], maxs[i], "0.######");
		}
	}
	
	@Override
	public boolean dataHasChanged()
	{
		boolean flag = false;
		
		if(initMins == null || initMaxs == null)
		{
			return flag;
		}
		
		int len = 12;
		
		for(int i=0; i<len; i++)
		{
		    if(initMins[i]!=UIHelper.getNum(txtMins[i]))
		    {
		    	flag = true;
		    	break;
		    }
		    if(initMaxs[i]!=UIHelper.getNum(txtMaxs[i]))
		    {
		    	flag = true;
		    	break;
		    }
		}
		
		return flag;
	}

	@Override
	public void getInfoFromTargetData(TargetData data) 
	{
		// hold local reference
		targetData = data;
		
		double mins[] = data.getMinimums();
		double maxs[] = data.getMaximums();
		
		int len = mins.length;
		
		initMins = new double[len];
		initMaxs = new double[len];
		
		for(int i=0; i<len; i++)
		{
			UIHelper.format(txtMins[i], mins[i], "0.######");
			UIHelper.format(txtMaxs[i], maxs[i], "0.######");
			
			// to ensure we only care about equality to limits of precision
		    initMins[i] = UIHelper.getNum(txtMins[i]);	
		    initMaxs[i] = UIHelper.getNum(txtMaxs[i]);	
		}
	}

	@Override
	public void setInfoToTargetData(TargetData data) 
	{
		// local reference is of no importance
		
		int len = 12;
		
		double mins[] = new double[len];
		double maxs[] = new double[len];
		
		for(int i=0; i<len; i++)
		{
		    mins[i] = UIHelper.getNum(txtMins[i]);	
		    maxs[i] = UIHelper.getNum(txtMaxs[i]);	
		}
		
		data.setMinimums(mins);
		data.setMaximums(maxs);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if(src == btnCalc)
		{
			calculateMinMax();
		}
	}
}
