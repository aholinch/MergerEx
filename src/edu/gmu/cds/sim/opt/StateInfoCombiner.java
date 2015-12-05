/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim.opt;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.sim.StateInfoFile;
import edu.gmu.cds.sim.StateInfoSorter;

public class StateInfoCombiner 
{
    public static List<StateInfo> combineStates(String dirStr)
    {
        List<StateInfo> allInfos = new ArrayList<StateInfo>(100000);
        
        List<StateInfo> infos = null;
        File dir = new File(dirStr);
        File files[] = dir.listFiles();
        File f = null;
        String name = null;

        for(int i=0; i<files.length; i++)
        {
        	f = files[i];
        	name = f.getName();
        	if(name.endsWith(".txt") && name.contains("states"))
        	{
        		infos = StateInfoFile.readSimple(f.getAbsolutePath());
        		if(infos != null)
        		{
        			allInfos.addAll(infos);
        		}
        	}
        }
        StateInfoSorter.sortStateInfos(allInfos);
        return allInfos;
    }
    
}
