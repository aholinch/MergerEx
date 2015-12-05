/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * When provided a list of states, this class can select random sets to
 * be competed in MergerWars tournaments and keep the list up-to-date.
 * 
 * @author aholinch
 *
 */
public class MergerWars 
{
    public List<StateInfo> states;
    
	protected StateInfo tourn[] = new StateInfo[8];
	protected int indLeft = 0;
	protected int indRight = 0;
	protected int compNum = 0;
	protected int wins[] = new int[8];
	
	protected int minComps = 0;
	
    public MergerWars()
    {
    	
    }
    
    protected void updateMinComps()
    {
    	minComps = Integer.MAX_VALUE;
        int size = states.size();
        for(int i=0; i<size; i++)
        {
        	if(states.get(i).numComps < minComps)
        	{
        		minComps = states.get(i).numComps;
        	}
        }
    }
    
    public void setStates(List<StateInfo> infos)
    {
    	states = infos;
    }
    
    public boolean isReady()
    {
    	if(states == null) return false;
    	if(states.size() < 8) return false;
    	
    	return true;
    }

    public StateInfo[] getTournamentArray()
    {
    	return tourn;
    }
    
    public StateInfo[] getNextTournament()
    {
    	/*
    	if(tourn[0]!=null)
    	{
    	    for(int i=0; i<8; i++)
    	    {
    	    	System.out.println(i+ "\t" + tourn[i].numComps+"\t"+tourn[i].numWins+"\t"+tourn[i].fitness);
    	    }
    	}
    	*/
    	
    	for(int i=0; i<8; i++) tourn[i]=null;
    	
    	List<StateInfo> tmpList = new ArrayList<StateInfo>();
    	updateMinComps();
    	
    	int tgtComp = minComps - 1;
    	int size = states.size();
    	
    	// consider tuning this
    	int tgtSize = Math.min(size, 16);
    	
    	StateInfo si = null;
    	
    	while(tmpList.size() < tgtSize)
    	{
    		tgtComp++;
    		for(int i=0; i<size; i++)
    		{
    			si = states.get(i);
    			if(si.numComps==tgtComp)
    			{
    				tmpList.add(si);
    			}
    		}
    	}

    	size = tmpList.size();
    	
    	List<Integer> inds = new ArrayList<Integer>();
    	
    	int ind = 0;
    	Integer oi = null;
    	
    	Random rand = new Random();
    	
    	while(inds.size() < 8)
    	{
    		ind = rand.nextInt(size);
    		oi = new Integer(ind);
    		if(!inds.contains(oi))
    		{
    			tourn[inds.size()]=tmpList.get(ind);
    			inds.add(oi);
    		}
    	}
    	
        return tourn;
    }
    
    public int getLeftInd()
    {
    	return indLeft;
    }
    
    public int getRightInd()
    {
    	return indRight;
    }
    
    public void setWinner(int ind)
    {
    	tourn[indLeft].numComps++;
    	tourn[indRight].numComps++;
    	tourn[indLeft].evaluated = 1;
    	tourn[indRight].evaluated = 1;
    	
        if(ind == 1)
        {
        	wins[indLeft]++;
        	tourn[indLeft].numWins++;
        }
        else if(ind == 2)
        {
        	wins[indRight]++;
        	tourn[indRight].numWins++;
        }
        // else no winner
        
    	tourn[indLeft].fitness = ((double)tourn[indLeft].numWins)/((double)tourn[indLeft].numComps);
    	tourn[indRight].fitness = ((double)tourn[indRight].numWins)/((double)tourn[indRight].numComps);
    	
        nextCompetition();
    }
    
    public void nextRound()
    {
    	if(!isReady()) return;
    	
    	// sort the list by fitness
    	if(tourn != null && tourn[0] != null)
    	{
    		StateInfoSorter.sortStateInfos(states);
       	}
    	
    	tourn = getNextTournament();
    	compNum = 0;
    	for(int i=0; i<8; i++) wins[i]=0;
    	nextCompetition();
    }

    public void nextCompetition()
    {
    	// this tournament is over
    	if(compNum > 6)
    	{
    		nextRound();
    		return;
    	}
    	
    	int tw = 0;
    	
    	switch(compNum)
    	{
    		case 0:
    			indLeft = 0;
    			indRight = 1;
    			break;
    		case 1:
    			indLeft = 2;
    			indRight = 3;
    			break;
    		case 2:
    			indLeft = 4;
    			indRight = 5;
    			break;
    		case 3:
    			indLeft = 6;
    			indRight = 7;
    			break;
    		case 4:
    			indLeft = 0;
    			indRight= 2;
    			if(wins[1]>wins[0])
    			{
    				indLeft = 1;
    			}
    			if(wins[3]>wins[2])
    			{
    				indRight = 3;
    			}
    			break;
    		case 5:
    			indLeft = 4;
    			indRight= 6;
    			if(wins[5]>wins[4])
    			{
    				indLeft = 5;
    			}
    			if(wins[7]>wins[6])
    			{
    				indRight = 7;
    			}
    			break;
    		case 6:
    			indLeft = 0;
    			tw = wins[0];
    			for(int i=1;i<4; i++)
    			{
    				if(wins[i]>tw)
    				{
    					indLeft = i;
    					tw = wins[i];
    				}
    			}
    			indRight = 4;
    			tw = wins[4];
    			for(int i=5;i<8; i++)
    			{
    				if(wins[i]>tw)
    				{
    					indRight = i;
    					tw = wins[i];
    				}
    			}
    			break;
    	}
        
        compNum++;
    }
}
