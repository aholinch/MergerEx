/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.util.Comparator;
import java.util.List;

public class StateInfoSorter implements Comparator<StateInfo>
{
	public StateInfoSorter()
	{
		
	}

	@Override
	public int compare(StateInfo o1, StateInfo o2) 
	{
		double v1 = o1.fitness;
		double v2 = o2.fitness;
		
		if(Double.isNaN(v1)) return 1;
		if(Double.isNaN(v2)) return -1;
		
		// higher fitness goes first
		if(v1 > v2) return -1;
		if(v2 > v1) return 1;
		
		v1 = o1.numComps;
		v2 = o2.numComps;
		
		if(v1 < v2) return -1;
		if(v2 < v1) return 1;
		
		v1 = o1.m1/o1.m2;
		v2 = o2.m1/o2.m2;
		
		if(v1 < v2) return -1;
		if(v2 < v1) return 1;
		
		return 0;
	}

	public static void sortStateInfos(List<StateInfo> infos)
	{
		java.util.Collections.sort(infos,new StateInfoSorter());
	}
}
