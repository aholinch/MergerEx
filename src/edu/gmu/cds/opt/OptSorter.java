/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

import java.util.Comparator;

public class OptSorter implements Comparator<OptEntity>
{
    public OptSorter()
    {
    	
    }
    
	@Override
	public int compare(OptEntity o1, OptEntity o2) 
	{
		double f1 = o1.getFitness();
		double f2 = o2.getFitness();
		if(f1 < f2) return 1;
		if(f1 > f2) return -1;
		return 0;
	}

}
