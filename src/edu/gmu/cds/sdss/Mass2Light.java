/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sdss;

/**
 * Mass-to-light ratios for SDSS galaxies from Bell et all.
 * 
 * 
 * THE OPTICAL AND NEAR-INFRARED PROPERTIES OF GALAXIES: I. LUMINOSITY AND STELLAR
MASS FUNCTIONS
ERIC F. BELL
Max-Planck-Institut f�r Astronomie, K�nigstuhl 17, D-69117 Heidelberg, Germany; bell@mpia.de
DANIEL H. MCINTOSH, NEAL KATZ, AND MARTIN D. WEINBERG
Department of Astronomy, University of Massachusetts, 710 North Pleasant Street, Amherst, MA 01003-9305;
dmac@hamerkop.astro.umass.edu, nsk@kaka.astro.umass.edu, weinberg@astro.umass.edu
ASTROPHYSICAL JOURNAL SUPPLEMENT SERIES, IN PRESS: August 4, 2003
 * @author aholinch
 *
 */
public class Mass2Light 
{
	public static final double H0 = 73;
	public static final double c = 299792.458;
	
	/** Mass to light ratio for Sb */
	public static final double M2L_Sb = 4.5;
	
	public static final double Msun_u = 6.41; 
	public static final double Msun_g = 5.15;
	public static final double Msun_r = 4.67;
	public static final double Msun_i = 4.56;
	public static final double Msun_z = 4.53;
	
	public static final double Msun_B = 5.48;
	public static final double Msun_V = 4.83;
	
	public static final double Msun[] = {Msun_u,Msun_g,Msun_r,Msun_i,Msun_z};
	
	public static final String COLORS[] = {"u-g","u-r","u-i","u-z","g-r","g-i","g-z","r-i","r-z"};
	
    public static final double ag[] = {-0.221,-0.39,-0.375,-0.4,-0.499,-0.379,-0.367,-0.106,-0.124};
    public static final double bg[] = {0.485,0.417,0.359,0.332,1.519,0.914,0.698,1.982,1.067};
    public static final double ar[] = {-0.099,-0.223,-0.212,-0.232,-0.306,-0.22,-0.215,-0.022,-0.041};
    public static final double br[] = {0.345,0.299,0.257,0.239,1.097,0.661,0.508,1.431,0.78};
    public static final double ai[] = {-0.053,-0.151,-0.144,-0.161,-0.222,-0.152,-0.153,0.006,-0.018};
    public static final double bi[] = {0.268,0.233,0.201,0.187,0.864,0.518,0.402,1.114,0.623};
    public static final double az[] = {-0.105,-0.178,-0.171,-0.179,-0.223,-0.175,-0.171,-0.052,-0.041};
    public static final double bz[] = {0.226,0.192,0.165,0.151,0.689,0.421,0.322,0.923,0.463};
    public static final double aJ[] = {-0.128,-0.172,-0.169,-0.163,-0.172,-0.153,-0.097,-0.079,-0.011};
    public static final double bJ[] = {0.169,-0.138,0.119,0.105,0.444,0.283,0.175,0.65,0.224};
    public static final double aH[] = {-0.209,-0.237,-0.233,-0.205,-0.189,-0.186,-0.117,-0.148,-0.059};
    public static final double bH[] = {0.133,0.104,0.09,0.071,0.266,0.179,0.083,0.437,0.076};
    public static final double aK[] = {-0.26,-0.273,-0.267,-0.232,-0.209,-0.211,-0.138,-0.186,-0.092};
    public static final double bK[] = {0.123,0.091,0.077,0.056,0.197,0.137,0.047,0.349,0.019};

    /**
     * Returns the 9 mass-to-light ratios (by color) for each of the four
     * magnitudes: g,r,i,z.
     * 
     * @param u
     * @param g
     * @param r
     * @param i
     * @param z
     * @return
     */
    public static double[][] getM2L(double u, double g, double r, double i, double z)
    {
    	double m2l[][] = new double[4][9];
    	
    	double color[] = new double[]{u-g,u-r,u-i,u-z,g-r,g-i,g-z,r-i,r-z};
    	int ind = 0;
    	
       	for(ind=0; ind<9; ind++)
    	{
    		m2l[0][ind]=ag[ind]+bg[ind]*color[ind];
    		m2l[1][ind]=ar[ind]+br[ind]*color[ind];
    		m2l[2][ind]=ai[ind]+bi[ind]*color[ind];
    		m2l[3][ind]=az[ind]+bz[ind]*color[ind];
    		//System.out.println(m2l[0][ind] + "\t" + m2l[1][ind]+"\t"+m2l[2][ind] + "\t" + m2l[3][ind]);
    		// do powernow
    		m2l[0][ind]=Math.pow(10.0d,m2l[0][ind]);
    		m2l[1][ind]=Math.pow(10.0d,m2l[1][ind]);
    		m2l[2][ind]=Math.pow(10.0d,m2l[2][ind]);
    		m2l[3][ind]=Math.pow(10.0d,m2l[3][ind]);
    		//System.out.println(m2l[0][ind] + "\t" + m2l[1][ind]+"\t"+m2l[2][ind] + "\t" + m2l[3][ind]);
    	}
    	
    	return m2l;
    }
    
    /**
     * Given apparent magnitude and cosmological redshift, return luminosity
     * @param m
     * @param z
     * @param Msun 
     * @return
     */
    public static double getL(double m, double z, double Msun)
    {
    	double vals[] = getLM(m,z,Msun);
    	return vals[0];
    }
    
    public static double[] getLM(double m, double z, double Msun)
    {
    	// get distance from redshift
    	double d = z*c/H0*1.0e6;
    	
    	// get absolute magnitude
    	double M = m+5.0d-5.0d*Math.log10(d);
    	
    	// convert to luminosity
    	double L = Math.pow(10.0d,(Msun-M)/2.5);
    	
    	double lm[] = new double[]{L,M};
    	
    	return lm;
    }
    
    /**
     * Convert the mass to light ratios to solar masses.
     * 
     * @param m2l
     * @param g
     * @param r
     * @param i
     * @param z
     * @param redshift
     * @return
     */
    public static double[][] getMasses(double m2l[][], double g, double r, double i, double z, double redshift)
    {
    	double m[][] = new double[4][9];
    	
    	double L = 0;
    	
    	double mag[] = {g,r,i,z};
    	
    	for(int ind=0; ind<m.length; ind++)
    	{
    	    L = getL(mag[ind],redshift,Msun[ind+1]);
    	    for(int color=0; color<9; color++)
    	    {
    	    	m[ind][color] = m2l[ind][color]*L;
    	    }
    	}
    	
    	return m;
    }

    /**
     * Calculate min, mean, max of mass values.
     * 
     * @param u
     * @param g
     * @param r
     * @param i
     * @param z
     * @param redshift
     * @return
     */
    public static double[] getMasses(double u, double g, double r, double i, double z, double redshift)
    {
    	double m2l[][] = getM2L(u,g,r,i,z);
    	double mass[][] = getMasses(m2l,g,r,i,z,redshift);
    	
    	double min = Double.MAX_VALUE;
    	double max = -(min-2);
    	double mean = 0;
    	
    	double val = 0;
    	
    	for(int ind=0; ind<4; ind++)
    	{
    		for(int j=0; j<9; j++)
    		{
    			//System.out.println(m2l[ind][j] + "\t" + mass[ind][j]);
    			
    			val = mass[ind][j];
    			mean+=val;
    			
    			if(val < min)
    			{
    				min = val;
    			}
    			
    			if(val > max)
    			{
    				max = val;
    			}
    		}
    	}
    	
    	mean /= 36.0d;
    	
    	double m[] = {min,mean,max};
    	
    	return m;
    }

}
