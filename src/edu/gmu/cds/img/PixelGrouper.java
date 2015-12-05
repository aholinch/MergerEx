/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Generates group information for pixels in a 2d array.
 * 
 *
 */
public class PixelGrouper 
{
	/**
	 * Determine if the loc is in group.
	 * 
	 * @param x
	 * @param y
	 * @param group
	 * @return
	 */
	public static boolean isLocInGroup(int x, int y, List<Chord> group)
	{
		boolean flag = false;
		int size = group.size();
		
		Chord chord = null;
		for(int i=0; i<size; i++)
		{
			chord = group.get(i);
			if(chord.contains(x, y))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * Determines the group that contains the coordinates if any
	 * 
	 * @param x
	 * @param y
	 * @param groupIds
	 * @param hmGroups
	 * @return
	 */
	public static Integer getGroupIdForCoordinates(int x, int y, List<Integer> groupIds, HashMap<Integer,List<Chord>> hmGroups)
	{
		Integer id = null;
		
		// ids are typically sorted by size and we are usually looking for biggest
		int size = groupIds.size();
		Integer testId = null;
		List<Chord> group = null;
		for(int i=0; i<size; i++)
		{
			testId = groupIds.get(i);
			group = hmGroups.get(testId);
			if(isLocInGroup(x,y,group))
			{
				id = testId;
				break;
			}
		}
		return id;
	}
	
	/**
	 * Fills the two int arrays with the x and y coordinates of the pixels in the specified
	 * group.  The arrays must be allocated with sufficient space.
	 * 
	 * @param x
	 * @param y
	 * @param groupId
	 * @param hmGroups
	 */
	public static void populateGroupCoordinates(int x[], int y[], int groupId, HashMap<Integer,List<Chord>> hmGroups)
	{
		Chord chord = null;
		List<Chord> group = (List<Chord>)hmGroups.get(new Integer(groupId));
		
		int size = group.size();
		
		int ind = 0;
		int yv = 0;
		int xv = 0;
		
		for(int i=0; i<size; i++)
		{
			chord = (Chord)group.get(i);
			yv = chord.y;
			for(xv=chord.xstart; xv<=chord.xend; xv++)
			{
				x[ind] = xv;
				y[ind] = yv;
				ind++;
			}
		}
	}
	
	public static int[][] groupPixels(ImageData data, List<Integer> groupIds, List<Integer> groupSizes, HashMap<Integer,List<Chord>> hmGroups)
	{
		double pdata[] = null;
		int nx, ny;
		
		nx = data.getNx();
		ny = data.getNy();
		pdata = data.getData();
		
		return groupPixels(pdata,nx,ny,groupIds,groupSizes,hmGroups);
	}
	/**
	 * Group the pixels into connected groups.  Populate group ids and sizes into provided lists.
	 * Sort lists to be descending by group id.  Populate HashMap with lists of pixel chords.
	 * 
	 * @param data
	 * @param nx
	 * @param ny
	 * @param groupIds
	 * @param groupSizes
	 * @param hmGroups
	 * @return a 2d array with pixels assigned to their respective groups
	 */
	public static int[][] groupPixels(double data[], int nx, int ny, List<Integer> groupIds, List<Integer> groupSizes, HashMap<Integer,List<Chord>> hmGroups)
	{
		int i=0;
		int j=0;
		int k=0;
		
		int newGroup[] = new int[1];
		int minGroup = 0;
		List<Chord> prevLine = null;
		List<Chord> curLine = null;
		Chord curChord = null;
		Chord prevChord = null;
		
		if(hmGroups == null)
		{
			hmGroups = new HashMap<Integer,List<Chord>>();
		}
		else
		{
			hmGroups.clear();
		}
		
		List<Chord> curGroup = null;
		
		// process first line
		curLine = processLine(data,0,nx,minGroup,newGroup);
		
		// ensure chords are added to groups
		for(j=0;j<curLine.size();j++)
	    {
	    	curChord = (Chord)curLine.get(j);
	    	curGroup = getGroup(curChord, hmGroups);
	    	curGroup.add(curChord);
	    }
		
		int origGroupId = 0;
		int curGroupId = 0;
		
		// loop over remaining lines
		for(i=1; i<ny; i++)
		{
			minGroup = newGroup[0];
		    prevLine = curLine;
		    curLine = processLine(data,i,nx,minGroup,newGroup);
		    
		    // loop over all chords in line and check for overlap with previous line
		    for(j=0;j<curLine.size();j++)
		    {
		    	curChord = (Chord)curLine.get(j);
		    	curGroup = getGroup(curChord, hmGroups);
		    	curGroup.add(curChord);
		    	origGroupId = curChord.groupId;
		    	curGroupId = curChord.groupId;
		    	
		    	for(k=0; k<prevLine.size(); k++)
		    	{
		    		prevChord = (Chord)prevLine.get(k);
		    		
		    		if(curChord.overlaps(prevChord) && curGroupId != prevChord.groupId)
		    		{
		    			if(curGroupId == origGroupId)
		    			{
		    				// just add this chord to the group from the previous line
		    				curGroup = moveChordToGroup(curChord, prevChord.groupId, hmGroups);
		    				curGroupId = prevChord.groupId;
		    			}
		    			else
		    			{
		    				// take everything in prevChords group and add it to curChords group
		    				moveToNewGroup(curGroup, curChord.groupId, prevChord, hmGroups);
		    			}
		    		}
		    	} // end loop over prev chords
		    	
		    } // end loop over cur chords
		    
		} // end loop over lines
		
		// now, let's setup a pixel mask
		int mask[][] = new int[ny][nx];
		
		List<Integer> ids = new ArrayList<Integer>(hmGroups.keySet());
		
		int numGroups = ids.size();
		
		int num = 0;
		
		for(i=0; i<numGroups; i++)
		{
			groupIds.add(ids.get(i));
			num = getGroupSize((List<Chord>)hmGroups.get(ids.get(i)),mask);
			groupSizes.add(new Integer(num));
		}
		
		// sort groupIds descending by size
		Integer tmpId;
		Integer size1, size2;
		
		for(i=0; i<numGroups-1; i++)
		{
			for(j=i+1; j<numGroups; j++)
			{
				size1 = (Integer)groupSizes.get(i);
				size2 = (Integer)groupSizes.get(j);
				
				if(size1.compareTo(size2)<0)
				{
					// switch sizes
					groupSizes.set(i, size2);
					groupSizes.set(j, size1);
					
					// switch ids
					tmpId = (Integer)groupIds.get(i);
					groupIds.set(i, groupIds.get(j));
					groupIds.set(j, tmpId);
				}
			}
		}
		
		return mask;
	}
	
	/**
	 * Sets the group id to the mask array.  Returns the number of pixels in the group.
	 * 
	 * @param group
	 * @param mask
	 * @return
	 */
	public static int getGroupSize(List<Chord> group, int mask[][])
	{
		int num = 0;
		int size = group.size();
		
		Chord chord = null;
		int id = 0;
		
		for(int i=0; i<size; i++)
		{
		    chord = (Chord)group.get(i);
		    id = chord.groupId;
		    
		    for(int j=chord.xstart; j<=chord.xend; j++)
		    {
		    	mask[chord.y][j] = id;
		    	num++;
		    }
		}
		
		return num;
	}
	
	/**
	 * Moves the individual chord to the new group.
	 * 
	 * @param chord
	 * @param groupId
	 * @param hmGroups
	 * @return
	 */
	public static List<Chord> moveChordToGroup(Chord chord, int groupId, HashMap<Integer,List<Chord>> hmGroups)
	{
	    List<Chord> group = null;
	    
	    // remove previous group for chord, it is the only member
	    hmGroups.remove(new Integer(chord.groupId));
	    
	    // get new group
	    group = (List<Chord>)hmGroups.get(new Integer(groupId));
	    
	    chord.groupId = groupId;
	    group.add(chord);
	    
	    return group;
	}
	/**
	 * Moves the chords associated with the previous group to the new group.
	 * 
	 * @param newGroup
	 * @param newId
	 * @param chord
	 * @param hmGroups
	 */
	public static void moveToNewGroup(List<Chord> newGroup, int newId, Chord chord, HashMap<Integer,List<Chord>> hmGroups)
	{
	    List<Chord> list = null;
	    
	    Integer id = new Integer(chord.groupId);
	    
	    list = hmGroups.get(id);

	    int size = list.size();
	    Chord tmpChord = null;
	    
	    for(int i=0; i<size; i++)
	    {
	    	tmpChord = (Chord)list.get(i);
	    	tmpChord.groupId = newId;
	    	newGroup.add(tmpChord);
	    }
	    
	    // remove the old group from the map
	    hmGroups.remove(id);
	}
	
	/**
	 * Returns the list for the groupid in the chord.  If none exists, it is created.
	 * @param chord
	 * @param hmGroups
	 * @return
	 */
	public static List<Chord> getGroup(Chord chord, HashMap<Integer,List<Chord>> hmGroups)
	{
	    List<Chord> list = null;
	    
	    Integer id = new Integer(chord.groupId);
	    
	    list = hmGroups.get(id);
	    
	    if(list == null)
	    {
	    	list = new ArrayList<Chord>();
	    	hmGroups.put(id, list);
	    }
	    
	    return list;
	}
	
	/**
	 * Process the scan line to identify the chords.
	 * 
	 * @param data
	 * @param lineno
	 * @param nx
	 * @return
	 */
	public static List<Chord> processLine(double data[], int lineno, int nx, int minGroup, int newGroup[])
	{
		List<Chord> chords = new ArrayList<Chord>();
		
		Chord curChord = null;
		
		int ind = lineno*nx;
		for(int i=0; i<nx; i++)
		{
			if(data[ind] > 0)
			{
	            if(curChord == null)
	            {
	            	minGroup++;
	            	curChord = new Chord();
	            	curChord.groupId = minGroup;
	            	curChord.xstart = i;
	            	curChord.y = lineno;
	            }
	            
	            curChord.xend = i;
			}
			else
			{
				// terminate the current chord
				if(curChord != null)
				{
					chords.add(curChord);
					curChord = null;
				}
			}
			ind++;
		}
		
		if(curChord != null)
		{
			chords.add(curChord);
		}
		
		newGroup[0] = minGroup;
		
		return chords;
	}
	

	/**
	 * From the ImageModule (match_software) estimate_axes method.
	 * 
	 * @param igroup
	 * @param pixel_list_i
	 * @param pixel_list_j
	 * @param group_list
	 * @param group_start
	 * @param group_end
	 * @param image
	 * @param background
	 * @return
	 */
	public static double[] estimate_axes(int xx[], int yy[])
	{
		double outValues[] = new double[7];
	    double xcenter, ycenter;
	    double semimajor, semiminor;
	    double image_axis_pa;
	    double filling_factor_ellipse;
	    double filling_factor_rectangle;
	    
	    int npixels;
	    int i;

	    int iangle;
	    double angle;

	    double xmax, xmin, ymax, ymin;
	    double xtmp, ytmp;

	    double min_area, area;
	    double ca, sa;
	    double xc1, yc1, xct, yct;
	    double dx, dy;

	    npixels = xx.length;
	    
	    image_axis_pa = 0;
	    semimajor = 0;
	    semiminor = 0;

	    /*
		!
		!     -----initialize counters and zero center of box position
		!
		*/
	    xcenter = 0.0;
	    ycenter = 0.0;
	    
	    /*
		!
		!     -----go through loops to determine unweight center of pixels
		!
		*/
	    for(i=0;i<npixels;i++)
	    {
	      xcenter = xcenter + xx[i];
	      ycenter = ycenter + yy[i];
	    }
	    /*
		!
		!     -----normalize by dividing by the number of pixels
		!
		*/
	    xcenter = xcenter / (double)(npixels);
	    ycenter = ycenter / (double)(npixels);

	    /*
		!
		!     -----initialize areas of bounding box
		!
		*/
	    min_area = 1e20;
	    area = 1e10;
	    
	    xc1 = 0;
	    yc1 = 0;

	    /*
		!
		!     -----go through possible angles to find the minimum bounding box
		!
		*/
	    for(iangle=0;iangle<=180;iangle+=1)
	    {
	    /*
		!
		!     -----change the loop variable into an angle, and find sin/cos
		!
		*/
	      angle = iangle * Math.PI / 180.0;
	      ca = Math.cos(angle);
	      sa = Math.sin(angle);

	      /*
			!
			!     -----reset the pixel limits 
			!      
			*/
	      xmax = -1e5;
	      ymax = -1e5;
	      xmin = 1e5;
	      ymin = 1e5;
	      /*
			!
			!     -----loop
			!
			*/
	      for(i=0;i<npixels;i++)
	      {
	        xtmp = ca * (xx[i] - xcenter) - sa * (yy[i] - ycenter);
	        ytmp = sa * (xx[i] - xcenter) + ca * (yy[i] - ycenter);

	        xmin = Math.min(xmin, xtmp);
	        xmax = Math.max(xmax, xtmp);

	        ymin = Math.min(ymin, ytmp);
	        ymax = Math.max(ymax, ytmp);
	      }

	      dx = (xmax - xmin);
	      dy = (ymax - ymin);
	      area = dx * dy;

	      if ( (area < min_area) )
	      {
	        min_area = area;
	        dx = (xmax - xmin) / 2.0;
	        dy = (ymax - ymin) / 2.0;
	        semimajor = Math.max( dx, dy);
	        semiminor = Math.min( dx, dy);

	        xct = (xmax + xmin) / 2.0;
	        yct = (ymax + ymin) / 2.0;

	        // rotate them back
	        ca = Math.cos(-angle);
	        sa = Math.sin(-angle);
	        xc1 = ca * (xct) - sa * (yct)+xcenter;
	        yc1 = sa * (xct) + ca * (yct)+ycenter;

	        if(dy > dx)
	        {
	        	angle+=Math.PI/2.0d;
	        }

	        image_axis_pa = angle;
	      }
	    }

	    filling_factor_ellipse = (double)(npixels) / semimajor*semiminor*Math.PI;
	    filling_factor_rectangle = (double)(npixels)/ semimajor*semiminor*4.0;

	    outValues[0] = xc1;
	    outValues[1] = yc1;
	    outValues[2] = semimajor;
	    outValues[3] = semiminor;
	    outValues[4] = image_axis_pa;
	    outValues[5] = filling_factor_ellipse;
	    outValues[6] = filling_factor_rectangle;

	    return outValues;
    }
	//  end subroutine estimate_axes

	public static double mod(double x, double y)
	{
	    while(x>=y)
	    {
	    	x-=y;
	    }
	    
	    while(x<0)
	    {
	    	x+=y;
	    }
	    
	    return x;
	}
	
	/**
	 * Represents continuous row of pixels
	 * @author aholinch
	 *
	 */
    public static class Chord
    {
    	int y;
    	int xstart;
    	int xend;
    	int groupId;
    	
    	public Chord()
    	{	
    	}
    	
    	public boolean contains(int px, int py)
    	{
    		boolean flag = false;
    		if(y == py)
    		{
    			if(xstart<=px && px<=xend)
    			{
    				flag = true;
    			}
    		}
    		return flag;
    	}
    	public boolean overlaps(Chord chord)
    	{
    	    boolean overlap = false;
    	    
    	    if(xstart>=chord.xstart && xstart <= chord.xend)
    	    {
    	    	overlap = true;
    	    }
    	    else if(xend >= chord.xstart && xend <= chord.xend)
    	    {
    	    	overlap = true;
    	    }
    	    else if(xstart <= chord.xstart && xend >= chord.xend)
    	    {
    	    	overlap = true;
    	    }
    	    
    	    return overlap;
    	}
    }
}
