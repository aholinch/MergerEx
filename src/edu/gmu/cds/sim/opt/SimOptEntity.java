/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim.opt;

import java.awt.image.BufferedImage;

import edu.gmu.cds.opt.OptEntity;
import edu.gmu.cds.sim.StateInfo;

public class SimOptEntity implements OptEntity 
{
    public StateInfo info = null;
    public BufferedImage bi = null;
    public double fitness = 0;
    public boolean hasFitness = false;
    
    public SimOptEntity()
    {
    	info = new StateInfo();
    }
    
    public SimOptEntity(StateInfo info)
    {
    	this.info = info;
    }
    
	@Override
	public OptEntity getNewEntity(double[] params) 
	{
		SimOptEntity ent = new SimOptEntity();
		ent.setParams(params);
		return ent;
	}

	@Override
	public double[] getParams() 
	{
		return info.getParamsCopy();
	}

	@Override
	public void setParams(double[] params) 
	{
		info.setParams(params);
	}

	@Override
	public double getFitness() 
	{
		return fitness;
	}

	@Override
	public void setFitness(double f) 
	{
		hasFitness = true;
		fitness = f;
	}

	@Override
	public boolean hasFitness() 
	{
		return hasFitness;
	}

	@Override
	public void setHasFitness(boolean flag) 
	{
		hasFitness = flag;
	}

	public String toString()
	{
		return fitness + "\t"+info.toString();
	}
}
