/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A monitoring utility to assist in tracking down performance issues.
 * 
 * @author aholinch
 *
 */
public class MonitorUtil 
{
	static HashMap<String,CallList> hmBlocks = new HashMap<String,CallList>();
	
	public static void startCall(String callName)
	{
		long t1 = System.currentTimeMillis();
	    BlockCall bc = new BlockCall();
	    bc.t1 = t1;
	    CallList cl = (CallList)hmBlocks.get(callName);
	    if(cl == null)
	    {
	    	cl = new CallList();
	    	cl.name = callName;
	    	hmBlocks.put(callName, cl);
	    }
	    cl.addCall(bc);
	}
	
	public static void endCall(String callName)
	{
		long t2 = System.currentTimeMillis();
	    CallList cl = (CallList)hmBlocks.get(callName);

	    if(cl != null)
	    {
	    	BlockCall bc = cl.getLastCall();
	    	if(bc != null)
	    	{
	    		if(bc.t2 == 0)
	    		{
	    			bc.t2 = t2;
	    			long dur = t2-bc.t1;
	    			cl.dur+=dur;
	    			bc.dur = dur;
	    		}
	    	}
	    }
	}
	
	public static void report()
	{
		List<CallList> calls = new ArrayList<CallList>(hmBlocks.values());
		int size = calls.size();
		for(int i=0; i<size; i++)
		{
			System.out.println(calls.get(i));
		}
	}
	
	/**
	 * Represents a named block of code with a millisecond start and end time.
	 * 
	 * @author aholinch
	 *
	 */
    public static class BlockCall
    {
    	long t1;
    	long t2;
    	long dur;
    }
    
    /**
     * Represents a list of named blocks of code, all with same name.
     * 
     * @author aholinch
     *
     */
    public static class CallList
    {
    	long dur;
    	List<BlockCall> calls;
    	String name = null;
    	
    	public CallList()
    	{
    		calls = new ArrayList<BlockCall>();
    	}
    	
    	public void addCall(BlockCall call)
    	{
    		calls.add(call);
    	}
    	
    	public BlockCall getLastCall()
    	{
    		BlockCall bc = null;
    		int size = calls.size();
    		if(size > 0)
    		{
    			bc = (BlockCall)calls.get(size-1);
    		}
    		
    		return bc;
    	}
    	
    	public String toString()
    	{
    		double avg = ((double)dur)/((double)calls.size());
    		
    		return name + "\t" + dur + "ms\t"  + calls.size() + "\t" +avg;
    	}
    }
}
