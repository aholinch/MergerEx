/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import edu.gmu.cds.util.MathUtil;

public class SimUtil 
{
	/**
	 * If observational values have been set, can estimate range of simulation values.
	 * 
	 * @param data
	 * @return
	 */
	public static double[][] initializeMinMax(TargetData data)
	{
		DiskInfo info = null;
		
		info = data.getPrimaryDiskInfo();
		double ra1Deg = info.getRADeg();
		double dec1Deg = info.getDecDeg();
		double rad1Deg = info.getRadArcMin()/60.0d;
		double mass1SM = info.getMassSM();
		double distMpc = info.getDistanceMpc();

		info = data.getSecondaryDiskInfo();
		double ra2Deg = info.getRADeg();
		double dec2Deg = info.getDecDeg();
		double rad2Deg = info.getRadArcMin()/60.0d;
		double mass2SM = info.getMassSM();
        
		if(distMpc == 0.0)
		{
			distMpc = info.getDistanceMpc();
		}
		
        return SimUtil.initializeMinMax(ra1Deg, dec1Deg, ra2Deg, dec2Deg, distMpc, rad1Deg, rad2Deg, mass1SM, mass2SM);
	}
	
	/**
	 * Estimates range of simulation values based upon observational values.
	 * 
	 * @param ra1Deg
	 * @param dec1Deg
	 * @param ra2Deg
	 * @param dec2Deg
	 * @param distMpc
	 * @param rad1Deg
	 * @param rad2Deg
	 * @param mass1SM
	 * @param mass2SM
	 * @return
	 */
	public static double[][] initializeMinMax(double ra1Deg, double dec1Deg, double ra2Deg, double dec2Deg,
			                                  double distMpc, double rad1Deg, double rad2Deg, double mass1SM, double mass2SM)
	{
		double mins[] = new double[12];
		double maxs[] = new double[12];
		
		double r[] = calculateRxRy(ra1Deg,dec1Deg,ra2Deg,dec2Deg,distMpc);
		double dist = Math.sqrt(r[0]*r[0]+r[1]*r[1]);
		
		mins[0]=r[0];
		maxs[0]=r[0];
		mins[1]=r[1];
		maxs[1]=r[1];
		
		// rz is + or - 5 radii
		double rad = Math.toRadians(rad1Deg + rad2Deg);
		rad *= 5d*distMpc/Constants.DU_mpc;
		mins[2] = -rad;
		maxs[2] = rad;
		
		// vx, vy, vz is + or - 2*sqrt(3) * escape velocity
		double mass = (mass1SM+mass2SM)/Constants.MU_sm;
		double vEsc = circularVelocity(mass,dist,rad,0.01);
		
		vEsc *= 2.0d*Math.sqrt(3.0d);
		mins[3] = -vEsc;
		maxs[3] = vEsc;
		mins[4] = -vEsc;
		maxs[4] = vEsc;
		mins[5] = -vEsc;
		maxs[5] = vEsc;
		
		// m1, m2 is 0.1 to 10
		mass = mass1SM/Constants.MU_sm;
		mins[6] = 0.1*mass;
		maxs[6] = 10*mass;
		mass = mass2SM/Constants.MU_sm;
		mins[7] = 0.1*mass;
		maxs[7] = 10*mass;
		
		// r1, r2 is 0.5 to 1.5
		rad = Math.toRadians(rad1Deg);
		rad *= distMpc/Constants.DU_mpc;
		mins[8] = 0.5*rad;
		maxs[8] = 1.5*rad;
		rad = Math.toRadians(rad2Deg);
		rad *= distMpc/Constants.DU_mpc;
		mins[9] = 0.5*rad;
		maxs[9] = 1.5*rad;
		
		// theta range is 60 deg
		mins[10] = 60;
		mins[11] = 60;
		
		// phi range is 60 deg
		maxs[10] = 60;
		maxs[11] = 60;
		
		double ret[][] = {mins,maxs};
		return ret;
	}
	
	/**
	 * Determine angular separation between the two points.  Assumes ra1, dec1 refers
	 * to primary which is at the origin.
	 * 
	 * @param ra1Deg
	 * @param dec1Deg
	 * @param ra2Deg
	 * @param dec2Deg
	 * @param distMpc
	 * @return
	 */
	public static double[] calculateRxRy(double ra1Deg, double dec1Deg, double ra2Deg, double dec2Deg, double distMpc)
	{
		double r[] = new double[2];
		
		double deltaDec = MathUtil.getAngularSeparation(ra1Deg, dec1Deg, ra1Deg, dec2Deg);
		double deltaRa = MathUtil.getAngularSeparation(ra1Deg, dec2Deg, ra2Deg, dec2Deg);
		
		deltaDec = Math.toRadians(deltaDec);
		deltaRa = Math.toRadians(deltaRa);
		
		double rx = deltaRa*distMpc/Constants.DU_mpc;
		double ry = deltaDec*distMpc/Constants.DU_mpc;
		
		if(dec2Deg < dec1Deg) ry *= -1.0d;
		
		double raDiff = Math.abs(ra2Deg - ra1Deg);
		if(raDiff < 180.0d  && (ra2Deg > ra1Deg))
		{
			rx *= -1.0d;
		}
		else if(raDiff > 180.0d  && ra2Deg < ra1Deg)
		{
			// wraps 24h - 0h
			rx *= -1.0d;
		}
		
		r[0]=rx;
		r[1]=ry;
		
		return r;
	}
	

    /**
     * Compute the circular velocity for a particle at a distance r from the specified mass.
     * The rout scale of the disk and softening length, eps, are provided.
     */
    public static double circularVelocity(double mass, double r, double rout, double eps)
    {
        double ftotal = mass / ( r*r + eps );
        double v = Math.sqrt(ftotal*r);

        return v;
    }

}
