/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Filter-related utilities for producing kernels for convolution operations.
 * 
 * @author aholinch
 *
 */
public class FilterUtil 
{
	public static final float ONE_NINTH = (float)(1.0/9.0);
	
	/**
	 * Returns a basic blur filter. 
	 * 
	 * @return
	 */
    public static ConvolveOp getBlurFilter()
    {
    	float k[] = {ONE_NINTH,ONE_NINTH,ONE_NINTH,
    			     ONE_NINTH,ONE_NINTH,ONE_NINTH,
    			     ONE_NINTH,ONE_NINTH,ONE_NINTH};
    	
    	Kernel kernel = new Kernel(3,3,k);
    	
    	ConvolveOp co = new ConvolveOp(kernel);
    	
    	return co;
    }
    
    public static ConvolveOp getGaussianBlurFilter()
    {
    	float k[] = {1,4,7,4,1,
    			     4,16,26,16,4,
    			     7,26,41,26,7,
    			     4,16,26,16,4,
    			     1,4,7,4,1};
    	
    	for(int i=0;i<k.length;i++)
    	{
    		k[i] = (float)(k[i]/273.0);
    	}
    	
    	Kernel kernel = new Kernel(5,5,k);
    	
    	ConvolveOp co = new ConvolveOp(kernel);
    	
    	return co;
    }
    
    public static ConvolveOp getBrighterGaussianBlurFilter()
    {
    	float k[] = {1,4,7,4,1,
    			     4,16,26,16,4,
    			     7,26,41,26,7,
    			     4,16,26,16,4,
    			     1,4,7,4,1};
    	
    	for(int i=0;i<k.length;i++)
    	{
    		k[i] = (float)(k[i]/200.0);
    	}
    	
    	Kernel kernel = new Kernel(5,5,k);
    	
    	ConvolveOp co = new ConvolveOp(kernel);
    	
    	return co;
    }
    
    /**
     * Returns default edge detect filter.
     * 
     * @return
     */
    public static ConvolveOp getEdgeDetectFilter()
    {
    	return getEdgeDetectFilter(1.0f);
    }
    
    /**
     * Returns an edge detection filter.
     * 
     * @param mult
     * @return
     */
    public static ConvolveOp getEdgeDetectFilter(float mult)
    {
    	mult = Math.abs(mult);
    	float m1 = (float)(-1.0*mult);
    	float m2 = (float)(8.0*mult);
    	
    	float k[] = {m1,m1,m1,
    			     m1,m2,m1,
    			     m1,m1,m1};
    	
    	Kernel kernel = new Kernel(3,3,k);

    	ConvolveOp co = new ConvolveOp(kernel);
    	
    	return co;
    }
}
