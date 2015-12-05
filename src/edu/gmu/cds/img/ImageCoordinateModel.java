/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

/**
 * Handles conversions between angle coordinates and pixels coordinates.  Such as
 * converting x,y to ra,dec or lat,lon.
 * 
 * @author aholinch
 *
 */
public class ImageCoordinateModel 
{
	protected double nx;
	protected double ny;
	protected double ang1c;
	protected double ang2c;
	protected double ang1size;
	protected double ang2size;
	protected double yres;
	protected double xres;
	
	public ImageCoordinateModel()
	{
		
	}
	
	public double getNX()
	{
		return nx;
	}
	
	public double getNY()
	{
		return ny;
	}
	
	/**
	 * Sets scale from image dimensions, angular coordinates for center, and angular dimensions units in degrees
	 * @param nx
	 * @param ny
	 * @param ang1c
	 * @param ang2c
	 * @param ang1size
	 * @param ang2size
	 */
	public void setImageInfo(double nx, double ny, double ang1c, double ang2c, double ang1size, double ang2size)
	{
		this.nx = nx;
		this.ny = ny;
		this.ang1c = ang1c;
		this.ang2c = ang2c;
		this.ang1size = ang1size;
		this.ang2size = ang2size;
		
		yres = ang2size/ny;
		xres = ang1size/nx;
		
		xres /= Math.cos(Math.toRadians(ang2c));
	}
	
	/**
	 * Convert pixel coordinates to angle coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    public double[] convertPixelsToAngles(double x, double y)
    {
    	double ang1 = 0;
    	double ang2 = 0;
    	
    	double dy = y-0.5d*ny;
    	double dx = 0.5*nx-x;  // opposite for lat/lon
    	
    	dy*=yres;
    	dx*=xres;
    	
    	ang1 = ang1c+dx;
    	ang2 = ang2c+dy;
    	
    	double angs[] = {ang1,ang2};
    	return angs;    	
    }
    
    /**
     * Convert angle coordinates to pixel coordinates.
     * 
     * @param ang1
     * @param ang2
     * @return
     */
    public double[] convertAnglesToPixels(double ang1, double ang2)
    {
    	double x = 0;
    	double y = 0;
    	
    	double xy[] = {x,y};
    	return xy;
    }
}
