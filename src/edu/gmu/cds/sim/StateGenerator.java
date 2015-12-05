/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.util.Random;

/**
 * Uses TargetData to generate random states.
 * 
 * @author aholinch
 *
 */
public class StateGenerator 
{
	protected TargetData targetData = null;
	protected Random rand = null;
	protected double mins[] = null;
	protected double maxs[] = null;
	
    public StateGenerator()
    {
        init();	
    }
    
    public StateGenerator(TargetData data)
    {
    	setTargetData(data);
    	init();
    }
    
    protected void init()
    {
    	rand = new Random();
    }
    
    public void setTargetData(TargetData data)
    {
    	targetData = data;
    	mins = targetData.getMinimums();
    	maxs = targetData.getMaximums();
    }
    
    public StateInfo randStateInfo()
    {
    	StateInfo info = new StateInfo();
    	
    	// set any other defaults from the targetData
    	
    	// set parameters
    	info.setParams(randParams());
    	
    	return info;
    }
    
    /**
     * 0 - rx
     * 1 - ry
     * 2 - rz
     * 3 - vx
     * 4 - vy
     * 5 - vz
     * 6 - m1
     * 7 - m2
     * 8 - rad1
     * 9 - rad2
     * 10 - phi1
     * 11 - phi2
     * 12 - theta1
     * 13 - theta2
     * @return
     */
    public double[] randParams()
    {
    	int len = mins.length;
    	
    	double vals[] = new double[14];
    	
    	// last four are for angles
    	len-=2;
    	for(int i=0; i<len; i++)
    	{
    		vals[i] = rand(mins[i],maxs[i]);
    	}
    	
    	double ang = 0;
    	double tmp = 0;
    	
    	// phi1
    	ang = targetData.getPrimaryDiskInfo().getPhiDeg();
    	ang += maxs[10]*(rand.nextDouble()-0.5);
    	vals[10] = ang;
    	
    	
    	// phi2
    	ang = targetData.getSecondaryDiskInfo().getPhiDeg();
    	ang += maxs[11]*(rand.nextDouble()-0.5);
    	vals[11] = ang;
    	
    	
    	// theta1
    	ang = targetData.getPrimaryDiskInfo().getThetaDeg();
    	ang += mins[10]*(rand.nextDouble()-0.5);
    	
    	// 4-fold degeneracy
    	tmp = rand.nextDouble();
    	if(tmp < 0.25)
    	{
    		ang = 360.0d-ang;
    	}
    	else if(tmp < 0.5)
    	{
    		ang = 180.0d-ang;
    	}
    	else if(tmp < 0.75)
    	{
    		ang += 180.0d;
    	}
    	else
    	{
    		// do nothing
    	}
    	vals[12] = ang;
    	
    	
    	// theta2
    	ang = targetData.getSecondaryDiskInfo().getThetaDeg();
    	ang += mins[11]*(rand.nextDouble()-0.5);
    	
    	// 4-fold degeneracy
    	tmp = rand.nextDouble();
    	if(tmp < 0.25)
    	{
    		ang = 360.0d-ang;
    	}
    	else if(tmp < 0.5)
    	{
    		ang = 180.0d-ang;
    	}
    	else if(tmp < 0.75)
    	{
    		ang += 180.0d;
    	}
    	else
    	{
    		// do nothing
    	}
    	vals[13] = ang;
    	
    	return vals;
    }
    
    protected double rand(double min, double max)
    {
        double val = rand.nextDouble();
        double rng = max-min;
        val *= rng;
        val += min;
        return val;
    }
}
