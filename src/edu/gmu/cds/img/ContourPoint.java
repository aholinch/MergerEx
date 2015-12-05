/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

public class ContourPoint {
	public int x;
	public int y;
	public int ex;
	public int ey;
	
	public ContourPoint(ContourPoint pt)
	{
		x = pt.x;
		y = pt.y;
		ex = pt.ex;
		ey = pt.ey;
	}
	public ContourPoint(int x, int y, int ex, int ey)
	{
		this.x = x;
		this.y = y;
		this.ex = ex;
		this.ey = ey;
	}
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		if(o instanceof ContourPoint) return equals((ContourPoint)o);
		return false;
	}
	
	public boolean equals(ContourPoint pt)
	{
		if(x == pt.x && y == pt.y)
		{
			return true;
		}
		
		return false;
	}
	
    public boolean sameEntry(ContourPoint pt)
    {
    	if(pt.ex == ex && pt.ey == ey)
    	{
    		return true;
    	}
    	
    	return false;
    }

}
