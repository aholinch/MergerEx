/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.util.OrbitUtil;
import edu.gmu.cds.util.RotationUtil;


public class SimSummary 
{
	public String targetName;
	public int numBetaRejected;
    public int numViewed;
    public int numSelected;
    public int numEnhanced;
    public int numEvaluated;
    public int numMergerWarsCompetitions;
    
    public List<StateInfo> selectedStates;
    public List<StateInfo> notSelectedStates;
    
    private int tmpWars = 0;
    private int tmpEval = 0;
    private int tmpEnhc = 0;
    private int tmpView = 0;
    
    public long startTime = 0;
    
    public SimSummary()
    {
    	startTime = System.currentTimeMillis();
    	selectedStates = new ArrayList<StateInfo>(300);
    	notSelectedStates = new ArrayList<StateInfo>(3000);
    }
    
    public void addStateInfo(StateInfo info, boolean selected)
    {
    	// calculate orbit parameters
    	calculateOrbitParameters(info);
    	
    	numViewed++;
    	if(selected)
    	{
    		if(!selectedStates.contains(info))
			{
    			info.isSelected = true;
    			selectedStates.add(info);
    			numSelected = selectedStates.size();
			}
    	}
    	else
    	{
    		if(!notSelectedStates.contains(info))
    		{
    			info.isSelected = false;
    			notSelectedStates.add(info);
    		}
    	}
    }
    
    public void selectState(StateInfo info)
    {
    	if(notSelectedStates.contains(info))
		{
    		notSelectedStates.remove(info);
		}
    	if(!selectedStates.contains(info))
    	{
    		selectedStates.add(info);
			numSelected = selectedStates.size();
    	}
    }
    
    public void unselectState(StateInfo info)
    {
    	if(selectedStates.contains(info))
		{
    		selectedStates.remove(info);
			numSelected = selectedStates.size();
		}
    	if(!notSelectedStates.contains(info))
    	{
    		notSelectedStates.add(info);
    	}
    	
    }
    
    public void update()
    {
    	numSelected = selectedStates.size();
    	
    	tmpWars = 0;
    	tmpEval = 0;
    	tmpEnhc = 0;
    	tmpView = 0;
    	
    	tmpView = numSelected + notSelectedStates.size();
    	
    	updateFromList(selectedStates);
    	updateFromList(notSelectedStates);
    	
    	numMergerWarsCompetitions = tmpWars/2;
    	
    	if(tmpEnhc > numEnhanced)
    	{
    		numEnhanced = tmpEnhc;
    	}
    	
    	tmpView -= tmpEnhc;
    	if(tmpView > numViewed)
    	{
    		numViewed = tmpView;
    	}
    	numEvaluated = tmpEval;
    }
    
    private void updateFromList(List<StateInfo> list)
    {
    	int size = list.size();
    	StateInfo si = null;
    	for(int i=0; i<size; i++)
    	{
    		si = list.get(i);
    		tmpWars += si.numComps;
    		if(si.enhanced > 0)
    		{
    			tmpEnhc++;
    		}
    		if(si.evaluated > 0)
    		{
    			tmpEval++;
    		}
    	}
    }
    
    public void reset(String targetName)
    {
    	this.targetName = targetName;
    	numBetaRejected = 0;
    	numViewed = 0;
    	numSelected = 0;
    	numEnhanced = 0;
    	numEvaluated = 0;
    	numMergerWarsCompetitions = 0;
    	selectedStates.clear();
    	notSelectedStates.clear();
    	startTime = System.currentTimeMillis();
    }
    
    /**
     * Rotate the orbit vector to the plane of the primary disk and calculate the
     * Classical Orbit Elements.
     * 
     * @param info
     */
    public void calculateOrbitParameters(StateInfo info)
    {
    	double r[] = {info.rx,info.ry,info.rz};
    	double v[] = {info.vx,info.vy,info.vz};
    	double rv[][] = RotationUtil.skyToDisk(info.phi1, info.theta1, r, v);
    	info.orbParams = OrbitUtil.rvToCoe(rv[0], rv[1], info.m1 + info.m2);
    }
}
