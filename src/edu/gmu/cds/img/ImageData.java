/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Represents image as an array of values.  Mostly just a single band.
 * 
 * @author aholinch
 *
 */
public class ImageData 
{
	protected BufferedImage origImage = null;
	protected int nx = 0;
	protected int ny = 0;
	protected double data[] = null;
	
	public ImageData()
	{
		
	}
	
	public ImageData(BufferedImage img)
	{
		setImage(img);
	}
	
	public int getNx()
	{
		return nx;
	}
	
	public int getNy()
	{
		return ny;
	}
	
	public double[] getData()
	{
		return data;
	}
	
 	public void setImage(BufferedImage img)
	{
		origImage = img;
		nx = 0;
		ny = 0;
		data = null;
		if(img == null) return;
		nx = origImage.getWidth();
		ny = origImage.getHeight();
		data = ImageProcessor.imageToArray(origImage, 0);
	}
	
    public BufferedImage getThresholdImage(double minValue, Color bgColor)
    {
    	// copy bi
    	BufferedImage biCopy = new BufferedImage(nx,ny,BufferedImage.TYPE_BYTE_GRAY);;
    	origImage.copyData(biCopy.getRaster());

    	int arr[] = new int[3];
    	arr[0]=bgColor.getRed();
    	arr[1]=bgColor.getGreen();
    	arr[2]=bgColor.getBlue();
    	
    	WritableRaster raster = biCopy.getRaster();
    	int ind = 0;

    	for(int j=0; j<ny; j++)
    	{
	    	for(int i=0; i<nx; i++)
	    	{
	    		if(data[ind]<minValue)
	    		{
	    			raster.setPixel(i, j, arr);
	    		}
	    		ind++;
	    	}
    	}
		
		return biCopy;
    }
    
    /**
     * Return an image that keeps the pixels in the mask and sets others to the specified color
     * @param mask
     * @return
     */
    public BufferedImage sampleMask(double mask[],Color color)
    {
    	// copy bi
    	BufferedImage biCopy = new BufferedImage(nx,ny,BufferedImage.TYPE_BYTE_GRAY);;
    	origImage.copyData(biCopy.getRaster());

    	int arr[] = new int[3];
    	arr[0]=color.getRed();
    	arr[1]=color.getGreen();
    	arr[2]=color.getBlue();
    	
    	WritableRaster raster = biCopy.getRaster();
    	int ind = 0;

    	for(int j=0; j<ny; j++)
    	{
	    	for(int i=0; i<nx; i++)
	    	{
	    		if(mask[ind]==0)
	    		{
	    			raster.setPixel(i, j, arr);
	    		}
	    		ind++;
	    	}
    	}
		
		return biCopy;

    }
    
    /**
     * Set all pixels indicated in mask to specified color.
     * 
     * @param mask
     * @param color
     * @return
     */
    public BufferedImage applyMask(double mask[], Color color)
    {
    	// copy bi
    	BufferedImage biCopy = new BufferedImage(nx,ny,BufferedImage.TYPE_BYTE_GRAY);;
    	origImage.copyData(biCopy.getRaster());

    	int arr[] = new int[3];
    	arr[0]=color.getRed();
    	arr[1]=color.getGreen();
    	arr[2]=color.getBlue();
    	
    	WritableRaster raster = biCopy.getRaster();
    	int ind = 0;

    	for(int j=0; j<ny; j++)
    	{
	    	for(int i=0; i<nx; i++)
	    	{
	    		if(mask[ind]>0)
	    		{
	    			raster.setPixel(i, j, arr);
	    		}
	    		ind++;
	    	}
    	}
		
		return biCopy;
    }
    
    /**
     * Creates an empty array useful for masking.
     * @return
     */
    public double[] createEmptyMask()
    {
    	int size = nx*ny;
    	double mask[] = new double[size];
    	return mask;
    }
}
