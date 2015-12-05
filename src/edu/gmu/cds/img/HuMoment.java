/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * The HuMoment was implemeted with the help of 
 * the discussion at http://en.wikipedia.org/wiki/Image_moment
 *
 * @author aholinch
 *
 */
public class HuMoment 
{
    public static double rawMoment(int i, int j, int arr[][])
    {
    	double mom = 0;
    	double norm = 0;
    	int row[] = null;
    	int xt = 0;
    	int yt = 0;
    	
    	int h = arr.length;
    	int w = arr[0].length;
    	
    	for(int y=0; y<h; y++)
    	{
    		yt = (int)Math.pow(y+1.0,j);
    		row = arr[y];
    		for(int x = 0; x<w; x++)
    		{
    			xt = (int)Math.pow(x+1.0d, i);
    			mom += xt*yt*row[x];
    			norm += row[x];
    		}
    	}
    	//mom = mom/norm;
    	return mom;
    }
    
    public static double rawMoment(int i, int j, double arr[][])
    {
    	double mom = 0;
    	double norm = 0;
    	
    	double row[] = null;
    	double xt = 0;
    	double yt = 0;
    	
    	int h = arr.length;
    	int w = arr[0].length;
    	
    	for(int y=0; y<h; y++)
    	{
    		yt = Math.pow(y+1.0,j);
    		row = arr[y];
    		for(int x = 0; x<w; x++)
    		{
    			xt = Math.pow(x+1.0d, i);
    			mom += xt*yt*row[x];
    			norm += row[x];
    		}
    	}
    	//mom = mom/norm;
    	return mom;
    }

    
    public static double rawMoment(int i, int j, List<List<ContourPoint>> cntrs)
    {
    	double mom = 0;
    	double norm = 0;
    	
    	double xt = 0;
    	double yt = 0;
    	double x = 0;
    	double y = 0;
    	
    	int nc = cntrs.size();
    	int np = 0;
    	List<ContourPoint> pts = null;
    	ContourPoint pnt = null;

    	for(int c=0; c<nc; c++)
    	{
    		pts = cntrs.get(c);
    		np = pts.size();
    		for(int p=0; p<np; p++)
    		{
    			pnt = pts.get(p);
    			x = pnt.x;
    			y = pnt.y;
    		    yt = Math.pow(y+1.0,j);
    			xt = Math.pow(x+1.0d, i);
    			mom += xt*yt;
    			norm++;
    		}
    	}
    	mom = mom/norm;
    	return mom;
    }

    public static double centralMoment(int i, int j, double arr[][])
    {
    	double mom = 0;
    	
    	if(i==0 && j==0)
    	{
    		return rawMoment(0,0,arr);
    	}
    	
    	if((i==0 && j==1) || (i==1 && j==0))
    	{
    		return 0;
    	}
    	
    	double row[] = null;
    	double xt = 0;
    	double yt = 0;
    	
    	double m00 = rawMoment(0,0,arr);
    	double m10 = rawMoment(1,0,arr);
    	double m01 = rawMoment(0,1,arr);

    	double xb = m10/m00;
    	double yb = m01/m00;
    	//System.out.println(m00 + "\t" + m10 + "\t" + m01 + "\t" + xb + "\t" + yb);

    	int h = arr.length;
    	int w = arr[0].length;
    	
    	for(int y=0; y<h; y++)
    	{
    		yt = Math.pow(y+1.0-yb,j);
    		row = arr[y];
    		for(int x = 0; x<w; x++)
    		{
    			xt = Math.pow(x+1.0d-xb, i);
    			mom += xt*yt*row[x];
    		}
    	}
    	return mom;
    }
    
    public static double centralMoment(int i, int j, List<List<ContourPoint>> cntrs)
    {
    	double mom = 0;
    	
    	if(i==0 && j==0)
    	{
    		return rawMoment(0,0,cntrs);
    	}
    	
    	if((i==0 && j==1) || (i==1 && j==0))
    	{
    		return 0;
    	}
    	
    	double xt = 0;
    	double yt = 0;
    	
    	double m00 = rawMoment(0,0,cntrs);
    	double m10 = rawMoment(1,0,cntrs);
    	double m01 = rawMoment(0,1,cntrs);

    	double xb = m10/m00;
    	double yb = m01/m00;

    	double x = 0;
    	double y = 0;
    	
    	int nc = cntrs.size();
    	int np = 0;
    	List<ContourPoint> pts = null;
    	ContourPoint pnt = null;

    	for(int c=0; c<nc; c++)
    	{
    		pts = cntrs.get(c);
    		np = pts.size();
    		for(int p=0; p<np; p++)
    		{
    			pnt = pts.get(p);
    			x = pnt.x;
    			y = pnt.y;
    		    yt = Math.pow(y+1.0d-yb,j);
    			xt = Math.pow(x+1.0d-xb, i);
    			mom += xt*yt;
    		}
    	}

    	return mom;
    }
    
    public static double centralMoment(int i, int j, int arr[][])
    {
    	double mom = 0;
    	
    	if(i==0 && j==0)
    	{
    		return rawMoment(0,0,arr);
    	}
    	
    	if((i==0 && j==1) || (i==1 && j==0))
    	{
    		return 0;
    	}
    	
    	int row[] = null;
    	double xt = 0;
    	double yt = 0;
    	
    	double m00 = rawMoment(0,0,arr);
    	double m10 = rawMoment(1,0,arr);
    	double m01 = rawMoment(0,1,arr);
    	
    	double xb = m10/m00;
    	double yb = m01/m00;
    	
    	int h = arr.length;
    	int w = arr[0].length;
    	
    	for(int y=0; y<h; y++)
    	{
    		yt = Math.pow(y+1.0-yb,j);
    		row = arr[y];
    		for(int x = 0; x<w; x++)
    		{
    			xt = Math.pow(x+1.0d-xb, i);
    			mom += xt*yt*row[x];
    		}
    	}
    	return mom;
    }
    
    public static double getScaleInvariantMoment(int i, int j, List<List<ContourPoint>> cntrs)
    {
    	double u00 = 0;
    	double uij = 0;
    	
    	// should be 1 because we normalized
		u00 = centralMoment(0,0,cntrs);

    	
    	uij = centralMoment(i,j,cntrs);
    	
    	double exp = 1.0d + 0.5*(i+j);
    	
    	double out = uij/Math.pow(u00,exp);
    	
    	return out;
    }

    public static double getScaleInvariantMoment(int i, int j, double arr[][])
    {
    	return getScaleInvariantMoment(i,j,arr,null,true);
    }
    
    public static double getScaleInvariantMoment(int i, int j, double arr[][], double mu00[], boolean compute00)
    {
    	double u00 = 0;
    	double uij = 0;
    	
    	if(mu00 == null) 
		{
    		compute00 = true;
    		mu00 = new double[1];
		}
    	
    	if(compute00 = true)
    	{
    		u00 = centralMoment(0,0,arr);
    		mu00[0] = u00;
    	}
    	else
    	{
    		u00 = mu00[0];
    	}
    	
    	uij = centralMoment(i,j,arr);
    	
    	double exp = 1.0d + 0.5*(i+j);
    	
    	double out = uij/Math.pow(u00,exp);
    	
    	return out;
    }
    
    public static double getScaleInvariantMoment(int i, int j, int arr[][])
    {
    	return getScaleInvariantMoment(i,j,arr,null,true);
    }
    
    public static double getScaleInvariantMoment(int i, int j, int arr[][], double mu00[], boolean compute00)
    {
    	double u00 = 0;
    	double uij = 0;
    	
    	if(mu00 == null) 
		{
    		compute00 = true;
    		mu00 = new double[1];
		}
    	
    	if(compute00 = true)
    	{
    		u00 = centralMoment(0,0,arr);
    		mu00[0] = u00;
    	}
    	else
    	{
    		u00 = mu00[0];
    	}
    	
    	uij = centralMoment(i,j,arr);
    	
    	double exp = 1.0d + 0.5*(i+j);
    	
    	double out = uij/Math.pow(u00,exp);
    	
    	return out;
    }

    public static double[] getHuMoments(List<List<ContourPoint>> cntrs)
    {
    	double out[] = new double[8];
    	
    	
    	double n20 = 0;
    	double n02 = 0;
    	double n30 = 0;
    	double n03 = 0;
    	double n21 = 0;
    	double n12 = 0;
    	double n11 = 0;
    	
    	n11 = getScaleInvariantMoment(1,1,cntrs);
    	n20 = getScaleInvariantMoment(2,0,cntrs);
    	n02 = getScaleInvariantMoment(0,2,cntrs);
    	n30 = getScaleInvariantMoment(3,0,cntrs);
    	n03 = getScaleInvariantMoment(0,3,cntrs);
    	n21 = getScaleInvariantMoment(2,1,cntrs);
    	n12 = getScaleInvariantMoment(1,2,cntrs);

    	//System.out.println(n11);
    	//System.out.println(n20);
    	//System.out.println(n02);
    	//System.out.println(n30);
    	//System.out.println(n03);
    	//System.out.println(n21);
    	//System.out.println(n12);
        double t0 = n30 + n12;
	    double t1 = n21 + n03;

	        double q0 = t0 * t0, q1 = t1 * t1;
	
	        double n4 = 4 * n11;
	        double s = n20 + n02;
            double d = n20 - n02;
	
	        out[0] = s;
	        out[1] = d * d + n4 * n11;
            out[3] = q0 + q1;
	        out[5] = d * (q0 - q1) + n4 * t0 * t1;
	
	        t0 *= q0 - 3 * q1;
	        t1 *= 3 * q0 - q1;
	
	        q0 = n30 - 3 * n12;
	        q1 = 3 * n21 - n03;
	
	        out[2] = q0 * q0 + q1 * q1;
	        out[4] = q0 * t0 + q1 * t1;
	        out[6] = q1 * t0 - q0 * t1;
    	
	    	double n30pn12 = n30+n12;
	    	double n21pn03 = n21+n03; 
    	// I8
    	out[7] = n11*(n30pn12*n30pn12-n21pn03*n21pn03)-(n20-n02)*n30pn12*n21pn03;
    	return out;
    }
    public static double[] getHuMoments(double arr[][])
    {
    	double out[] = new double[8];
    	
    	double mu00[] = new double[1];
    	
    	double n20 = 0;
    	double n02 = 0;
    	double n30 = 0;
    	double n03 = 0;
    	double n21 = 0;
    	double n12 = 0;
    	double n11 = 0;
    	
    	n11 = getScaleInvariantMoment(1,1,arr,mu00,true);
    	n20 = getScaleInvariantMoment(2,0,arr,mu00,false);
    	n02 = getScaleInvariantMoment(0,2,arr,mu00,false);
    	n30 = getScaleInvariantMoment(3,0,arr,mu00,false);
    	n03 = getScaleInvariantMoment(0,3,arr,mu00,false);
    	n21 = getScaleInvariantMoment(2,1,arr,mu00,false);
    	n12 = getScaleInvariantMoment(1,2,arr,mu00,false);

    	//System.out.println(n11);
    	//System.out.println(n20);
    	//System.out.println(n02);
    	//System.out.println(n30);
    	//System.out.println(n03);
    	//System.out.println(n21);
    	//System.out.println(n12);
        double t0 = n30 + n12;
	    double t1 = n21 + n03;

	        double q0 = t0 * t0, q1 = t1 * t1;
	
	        double n4 = 4 * n11;
	        double s = n20 + n02;
            double d = n20 - n02;
	
	        out[0] = s;
	        out[1] = d * d + n4 * n11;
            out[3] = q0 + q1;
	        out[5] = d * (q0 - q1) + n4 * t0 * t1;
	
	        t0 *= q0 - 3 * q1;
	        t1 *= 3 * q0 - q1;
	
	        q0 = n30 - 3 * n12;
	        q1 = 3 * n21 - n03;
	
	        out[2] = q0 * q0 + q1 * q1;
	        out[4] = q0 * t0 + q1 * t1;
	        out[6] = q1 * t0 - q0 * t1;
    	
	    	
	    	double n30pn12 = n30+n12;
	    	double n21pn03 = n21+n03; 
    	// I8
    	out[7] = n11*(n30pn12*n30pn12-n21pn03*n21pn03)-(n20-n02)*n30pn12*n21pn03;
    	return out;
    }
    
    public static double[] getHuMoments(int arr[][])
    {
    	double out[] = new double[8];
    	
    	double mu00[] = new double[1];
    	
    	double n20 = 0;
    	double n02 = 0;
    	double n30 = 0;
    	double n03 = 0;
    	double n21 = 0;
    	double n12 = 0;
    	double n11 = 0;
    	
    	n11 = getScaleInvariantMoment(1,1,arr,mu00,true);
    	n20 = getScaleInvariantMoment(2,0,arr,mu00,false);
    	n02 = getScaleInvariantMoment(0,2,arr,mu00,false);
    	n30 = getScaleInvariantMoment(3,0,arr,mu00,false);
    	n03 = getScaleInvariantMoment(0,3,arr,mu00,false);
    	n21 = getScaleInvariantMoment(2,1,arr,mu00,false);
    	n12 = getScaleInvariantMoment(1,2,arr,mu00,false);

    	//System.out.println(n11);
    	//System.out.println(n20);
    	//System.out.println(n02);
    	//System.out.println(n30);
    	//System.out.println(n03);
    	//System.out.println(n21);
    	//System.out.println(n12);
    	
    	double n20mn02 = n20-n02;
    	double n30pn12 = n30+n12;
    	double n21pn03 = n21+n03; 

    	// I1
    	out[0] = n20+n02;

    	// I2 (n20-n02)^2 + 4n11^2
    	out[1] = n20mn02*n20mn02 + 4.0d*n11*n11;
    	
    	// I3
    	out[2] = Math.pow(n30-3.0d*n12,2.0)+Math.pow(3.0d*n21-n03,2.0d);
    	
    	// I4
    	out[3] = n30pn12*n30pn12 + n21pn03*n21pn03;
    	
    	// I5
    	out[4] =  (n30-3.0d*n12)*n30pn12*(n30pn12*n30pn12-3.0d*n21pn03*n21pn03);
    	out[4] += (3.0d*n21-n03)*n21pn03*(3.0d*n30pn12*n30pn12-n21pn03*n21pn03);
    	
    	// I6
    	out[5] = (n20-n02)*(n30pn12*n30pn12-n21pn03*n21pn03)+4*n11*n30pn12*n21pn03;
    	
    	// I7
    	out[6] = (3.0d*n21-n03)*(n30pn12)*(n30pn12*n30pn12-3.0d*n21pn03*n21pn03);
    	out[6] -=(3.0d*n30-n12)*(n21pn03)*(3.0d*n30pn12*n30pn12-n21pn03*n21pn03);
    	
    	// I8
    	out[7] = n11*(n30pn12*n30pn12-n21pn03*n21pn03)-(n20-n02)*n30pn12*n21pn03;
    	return out;
    }

    /**
     * for grayscale images
     * @param bi
     * @return
     */
    public static double[] getHuMoments(BufferedImage bi, boolean invert, boolean threshold, String f)
    {
    	double arr[][] = ImageProcessor.imageTo2dArray(bi, 0, invert);
    	if(threshold)
    	{
    		ImageProcessor.threshold(arr, 20, true);
    	}
    	
    	if(f != null)
    	{
    		bi = ImageProcessor.arrayToImage(bi, arr);
    		ImageProcessor.writeImage(bi, f);
    	}
    	return getHuMoments(arr);
    }
    
}
