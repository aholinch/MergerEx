/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

/**
 * A basic interface for optimizing a set of parameters that can be
 * expressed as an array of doubles.
 * 
 * @author aholinch
 *
 */
public interface OptEntity 
{
	public OptEntity getNewEntity(double params[]);
    public double[] getParams();
    public void setParams(double params[]);
    
    public double getFitness();
    public void setFitness(double f);
    
    public boolean hasFitness();
    public void setHasFitness(boolean flag);
}
