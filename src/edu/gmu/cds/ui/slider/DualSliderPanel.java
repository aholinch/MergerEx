/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.slider;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class DualSliderPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected JSlider sldV1 = null;
	protected JSlider sldV2 = null;
	
	protected String label1 = null;
	protected String label2 = null;
	protected JLabel lbl1 = null;
	protected JLabel lbl2 = null;
	
    protected double minV1 = 0;
    protected double maxV1 = 0;
    protected double minV2 = 0;
    protected double maxV2 = 0;
    
	public DualSliderPanel()
	{
		super(new GridLayout(6,1));
		lbl1 = new JLabel("Value 1");
		lbl2 = new JLabel("Value 2");
		sldV1 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		sldV2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
			
		add(new JLabel(""));
		add(lbl1);
		add(sldV1);
		add(new JLabel(""));
		add(lbl2);
		add(sldV2);
		
	}
	
	public void setLabels(String l1, String l2)
	{
		label1 = l1;
		label2 = l2;
		
		lbl1.setText(l1);
		lbl2.setText(l2);
	}
	
	public void addChangeListener(ChangeListener list)
	{
		sldV1.addChangeListener(list);
		sldV2.addChangeListener(list);
	}
	
	public void setValues(double v1, double v2)
	{
		double rng1 = maxV1-minV1;
		double rng2 = maxV2-minV2;
		
		v1 = (v1-minV1)/rng1;
		v2 = (v2-minV2)/rng2;
		
		v1*=100.0d;
		v2*=100.0d;
		sldV1.setValue((int)v1);
		sldV2.setValue((int)v2);
	}
	
	public double[] getValues()
	{
		double dout[] = new double[2];
		dout[0]=((double)sldV1.getValue())/100.0d*(maxV1-minV1)+minV1;
		dout[1]=((double)sldV2.getValue())/100.0d*(maxV2-minV2)+minV2;
		return dout;
	}
	
    public void setRanges(double min1, double max1, double min2, double max2)
    {
    	minV1 = min1;
    	minV2 = min2;
    	maxV1 = max1;
    	maxV2 = max2;
    }
}
