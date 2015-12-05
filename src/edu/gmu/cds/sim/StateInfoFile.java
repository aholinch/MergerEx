/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.util.FileUtil;

public class StateInfoFile 
{
    protected SimSummary simSummary = null;
    protected String sessionid = null;
    protected String userid = null;
    
    public StateInfoFile()
    {
    	
    }
    
    public StateInfoFile(SimSummary sum)
    {
    	setSimSummary(sum);
    }
    
    public void setSessionID(String id)
    {
    	sessionid = id;
    }
    
    public String getSessionID()
    {
    	return sessionid;
    }
    
    public void setUserID(String id)
    {
    	userid = id;
    }
    
    public String getUserID()
    {
    	return userid;
    }
    
    public SimSummary getSimSummary()
    {
    	return simSummary;
    }
    
    public void setSimSummary(SimSummary sum)
    {
    	simSummary = sum;
    }
    
    public void save(String file)
    {
    	save(file,true);
    }
    
    public void save(String file, boolean includeRejected)
    {
       	long t2 = System.currentTimeMillis();
    	double rt = (t2 - simSummary.startTime)/1000.0d;
    	StringBuilder sb = new StringBuilder(10000);
    	sb.append("#targetname ").append(simSummary.targetName).append("\n");
    	sb.append("#viewed ").append(simSummary.numViewed).append(" selected ").append(simSummary.numSelected);
    	sb.append(" enhanced ").append(simSummary.numEnhanced).append(" evaluated ").append(simSummary.numEvaluated);
    	sb.append(" merger_wars ").append(simSummary.numMergerWarsCompetitions);
    	sb.append(" beta_rej ").append(simSummary.numBetaRejected).append("\n");
    	sb.append("#start_time ").append(new java.util.Date(simSummary.startTime)).append("\n");
    	sb.append("#runtime ").append(rt).append(" seconds\n");
    	sb.append("#userid ").append(userid).append("\n");
    	sb.append("#sessionid ").append(sessionid).append("\n");
    	
    	int size = 0;
    	sb.append("\n#selected\n");
    	size = simSummary.selectedStates.size();
    	for(int i=0; i<size; i++)
    	{
    		sb.append(simSummary.selectedStates.get(i).toLongString()).append("\n");
    	}
    	
    	if(includeRejected)
    	{
	    	sb.append("\n#rejected\n");
	    	size = simSummary.notSelectedStates.size();
	    	for(int i=0; i<size; i++)
	    	{
	    		sb.append(simSummary.notSelectedStates.get(i).toString()).append("\n");
	    	}
    	}
    	
    	FileUtil.writeStringToFile(file, sb.toString());
    }
    
    public List<StateInfo> read(String file)
    {
		List<StateInfo> infos = null;
    	Reader r = FileUtil.getReader(file);
    	try
    	{
    		LineNumberReader lnr = new LineNumberReader(r);
    		String line = null;
    		
    		line = lnr.readLine();
    		StateInfo info = null;
    		
    		if(simSummary.selectedStates != null)
    		{
    			infos = simSummary.selectedStates;
    			infos.clear();
    		}
    		else
    		{
        		infos = new ArrayList<StateInfo>(1000);    			
        		simSummary.selectedStates = infos;
        	}
    		
    		if(simSummary.notSelectedStates != null)
    		{
    			simSummary.notSelectedStates.clear();
    		}
    		else
    		{
    			simSummary.notSelectedStates = new ArrayList<StateInfo>(1000);
    		}
			
    		while(line != null)
    		{
    			if(!line.startsWith("#") && !(line.trim().length() == 0))
    			{
    				info = new StateInfo(line);
    				infos.add(info);
    			}
    			else if(line.startsWith("#rejected"))
    			{
    				// switch to filling the rejected list
    				infos = simSummary.notSelectedStates;
    			}
    			line = lnr.readLine();
    		}
    		simSummary.update();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		FileUtil.close(r);
    	}
    	
    	return infos;
    }
    
    public static List<StateInfo> readSimple(String file)
    {
		List<StateInfo> infos = null;
    	Reader r = FileUtil.getReader(file);
    	try
    	{
    		LineNumberReader lnr = new LineNumberReader(r);
    		String line = null;
    		
    		line = lnr.readLine();
    		StateInfo info = null;
    		
    		infos = new ArrayList<StateInfo>(1000);    			
			
    		while(line != null)
    		{
    			if(!line.startsWith("#") && !(line.trim().length() == 0))
    			{
    				info = new StateInfo(line);
    				infos.add(info);
    			}
    			line = lnr.readLine();
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		FileUtil.close(r);
    	}
    	
    	return infos;
    }
}
