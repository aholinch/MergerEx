/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

/**
 * Using the phi (position angle) and theta (inclination angle) for the
 * primary disk, rotate the vectors in the plane to plane of the sky using
 * diskToSky().  Rotate from sky to the disk using skyToDisk().  Call both
 * with the same phi and theta, DO NOT multiply by -1.
 * 
 * @author aholinch
 *
 */
public class RotationUtil 
{
    public static double[] diskToSky(double phiDeg, double thetaDeg, double rv[])
    {
        double rvout[] = new double[6]; 
        
        double v[] = {rv[3],rv[4],rv[5]};
        double tmp[][] = diskToSky(phiDeg,thetaDeg,rv,v);
        rvout[0]=tmp[0][0];
        rvout[1]=tmp[0][1];
        rvout[2]=tmp[0][2];
        rvout[3]=tmp[1][0];
        rvout[4]=tmp[1][1];
        rvout[5]=tmp[1][2];
        
        return rvout;
    }
    
    public static double[][] diskToSky(double phiDeg, double thetaDeg, double r[], double v[])
    {
    	double rv[][] = new double[2][3];

    	double ctheta = Math.cos(Math.toRadians(thetaDeg));
    	double stheta = Math.sin(Math.toRadians(thetaDeg));
    	double cphi = Math.cos(Math.toRadians(phiDeg));
    	double sphi = Math.sin(Math.toRadians(phiDeg));
    	
    	double x,y,z;
    	double x2,y2,z2;
    	double x3,y3,z3;
    	
    	// position
    	x = r[0]; y = r[1]; z = r[2];
    	
	    x2  =   x * ctheta +  z * stheta;
	    y2  =   y;
	    z2  =  -x * stheta +  z * ctheta;
	    
	    x3  =  x2  * cphi -  y2 * sphi;
	    y3  =  x2  * sphi +  y2 * cphi;
	    z3  =  z2;
	    
	    rv[0][0]=x3;
	    rv[0][1]=y3;
	    rv[0][2]=z3;
	    
	    // velocity
        x = v[0]; y = v[1]; z = v[2];
    	
	    x2  =   x * ctheta +  z * stheta;
	    y2  =   y;
	    z2  =  -x * stheta +  z * ctheta;
	    
	    x3  =  x2  * cphi -  y2 * sphi;
	    y3  =  x2  * sphi +  y2 * cphi;
	    z3  =  z2;
	    
	    rv[1][0]=x3;
	    rv[1][1]=y3;
	    rv[1][2]=z3;
	    
	    return rv;
    }
    public static double[] skyToDisk(double phiDeg, double thetaDeg, double rv[])
    {
        double rvout[] = new double[6]; 
        
        double v[] = {rv[3],rv[4],rv[5]};
        double tmp[][] = skyToDisk(phiDeg,thetaDeg,rv,v);
        rvout[0]=tmp[0][0];
        rvout[1]=tmp[0][1];
        rvout[2]=tmp[0][2];
        rvout[3]=tmp[1][0];
        rvout[4]=tmp[1][1];
        rvout[5]=tmp[1][2];
        
        return rvout;
    }
    
    public static double[][] skyToDisk(double phiDeg, double thetaDeg, double r[], double v[])
    {
    	double rv[][] = new double[2][3];

    	// replace angles with - angle
    	double ctheta = Math.cos(Math.toRadians(-thetaDeg));
    	double stheta = Math.sin(Math.toRadians(-thetaDeg));
    	double cphi = Math.cos(Math.toRadians(-phiDeg));
    	double sphi = Math.sin(Math.toRadians(-phiDeg));
    	
    	double x,y,z;
    	double x2,y2,z2;
    	double x3,y3,z3;
    	
    	// position
    	x = r[0]; y = r[1]; z = r[2];

	    x2  =  x  * cphi -  y * sphi;
	    y2  =  x  * sphi +  y * cphi;
	    z2  =  z;
	    
	    x3  =   x2 * ctheta +  z2 * stheta;
	    y3  =   y2;
	    z3  =  -x2 * stheta +  z2 * ctheta;
	    

	    
	    rv[0][0]=x3;
	    rv[0][1]=y3;
	    rv[0][2]=z3;
	    
	    // velocity
        x = v[0]; y = v[1]; z = v[2];

	    x2  =  x  * cphi -  y * sphi;
	    y2  =  x  * sphi +  y * cphi;
	    z2  =  z;
	    
	    x3  =   x2 * ctheta +  z2 * stheta;
	    y3  =   y2;
	    z3  =  -x2 * stheta +  z2 * ctheta;	    
	    
	    rv[1][0]=x3;
	    rv[1][1]=y3;
	    rv[1][2]=z3;
	    
	    return rv;
    }
    
}
