/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import edu.gmu.cds.util.FileUtil;

/**
 * 
 * @author aholinch
 *
 */
public class ImageProcessor 
{
	public static void convertToGray(String origImage, String tgtImage)
	{
		BufferedImage bi = readImage(origImage);
		bi = convertToGray(bi);
		writeImage(bi,tgtImage);
	}
	
	public static BufferedImage convertToGray(BufferedImage bi)
	{
		return convertToGray(bi,0.299,0.587,0.114);
	}
	
	public static BufferedImage convertToGray(BufferedImage bi, double redW, double greenW, double blueW)
	{
		//redW = 0.299;
		//greenW = 0.587;
		//blueW = 0.114;
		
		BufferedImage biOut = null;
	    //if(bi.getRaster().getNumBands() < 3)
		//{
		//	biOut = copyImage(bi);
		//}
		//else
		{
		    int nx = bi.getWidth();
		    int ny = bi.getHeight();
		    
		    double datar[] = null;
		    double datag[] = null;
		    double datab[] = null;
		    
		    if(bi.getRaster().getNumBands() >= 3)
		    {
		    	datar = imageToArray(bi, 0);
		    	datag = imageToArray(bi, 1);
		    	datab = imageToArray(bi, 2);
		    }
		    else
		    {
		    	datar = imageToArray(bi, 0);
		    	int len = datar.length;
		    	datag = new double[len];
		    	datab = new double[len];
		    	System.arraycopy(datar, 0, datag, 0, len);
		    	System.arraycopy(datar, 0, datab, 0, len);
		    }
		    
	    	int len = nx*ny;
	    	for(int i=0; i<len; i++)
	    	{
	    		datar[i] = redW*datar[i]+greenW*datag[i]+blueW*datab[i];
	    	}
	    	
	    	biOut = new BufferedImage(nx,ny,BufferedImage.TYPE_BYTE_GRAY);
	    	arrayToImage(biOut, datar, nx, ny, 0);
		}
		
		return biOut;
	}
	
	/**
	 * Read the image from file.
	 * 
	 * @param file
	 * @return
	 */
    public static BufferedImage readImage(String file)
    {
    	BufferedImage bi = null;
    	try
    	{
    	    bi = ImageIO.read(new File(file));	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return bi;
    }
    
	/**
	 * Read the image from file.
	 * 
	 * @param file
	 * @return
	 */
    public static BufferedImage readImageFromClassPath(String file)
    {
    	BufferedImage bi = null;
    	try
    	{
    		InputStream is = FileUtil.getInputStream(file);
    	    bi = ImageIO.read(is);	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return bi;
    }
 
    /**
     * Write the image to file.
     * 
     * @param img
     * @param file
     */
    public static void writeImage(BufferedImage img, String file)
    {
    	String ext = FileUtil.getExtension(file);
    	try
    	{
    		ImageIO.write(img, ext, new File(file));
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public static BufferedImage resize(BufferedImage bi, int width, int height)
    {
    	int type = bi.getType();
    	BufferedImage out = new BufferedImage(width,height,type);
		int nx = bi.getWidth();
		int ny = bi.getHeight();
		int targetX = width;
		int targetY = height;
		
    	// we need to scale image
    	double scaleX = ((double)targetX)/((double)nx);
    	double scaleY = ((double)targetY)/((double)ny);
    	
    	Graphics2D g2d = (Graphics2D)out.getGraphics();
    	
    	if(scaleX < 1.0 || scaleY < 1.0)
    	{
    		// we are scaling down
    		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    	}
    	else
    	{
    		// we are scaling up
    		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    	}
    	
    	g2d.drawImage(bi,0,0,targetX,targetY,null);

    	return out;
    }
    
	/**
	 * Convert the image into a 1d array.
	 * 
	 * @param image
	 * @return
	 */
	public static double[] imageToArray(BufferedImage image, int band)
	{
		double d[] = null;
		int nx = image.getWidth();
		int ny = image.getHeight();
		
		Raster raster = image.getData();
		d = new double[ny*nx];
		int ind = 0;
		for(int i=0; i<ny; i++)
		{
			for(int j=0; j<nx; j++)
			{
				d[ind++] = raster.getSampleDouble(j,i,band);
			}
		}
		
		return d;
	}
	
	/**
	 * Convert the image into a 2d array.
	 * 
	 * @param image
	 * @return
	 */
	public static double[][] imageTo2dArray(BufferedImage image, int band)
	{
		return imageTo2dArray(image, band, false);
	}
	
	public static double[][] imageTo2dArray(BufferedImage image, int band, boolean doInverse)
	{

		double d[][] = null;
		int nx = image.getWidth();
		int ny = image.getHeight();
		
		Raster raster = image.getData();
		d = new double[ny][nx];
		double row[]= null;

		if(!doInverse)
		{
		for(int i=0; i<ny; i++)
		{
			row = d[i];
			for(int j=0; j<nx; j++)
			{
				row[j] = raster.getSampleDouble(j,i,band);
			}
		}
		}
		else
		{
			for(int i=0; i<ny; i++)
			{
				row = d[i];
				for(int j=0; j<nx; j++)
				{
					row[j] = 255-raster.getSampleDouble(j,i,band);
				}
			}
	
		}
		
		return d;
	}
	
	/**
	 * Create a copy of the image.
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage copyImage(BufferedImage bi)
	{
		int nx = bi.getWidth();
		int ny = bi.getHeight();
		
		BufferedImage biCopy = new BufferedImage(nx,ny,bi.getType());
		bi.copyData(biCopy.getRaster());
		
		return biCopy;
	}
	
	/**
	 * Take the array of values and set it to the image for the specified band.
	 * 
	 * @param bi
	 * @param d
	 * @param band
	 * @return
	 */
	public static BufferedImage arrayToImage(BufferedImage bi, double[] d, int nx, int ny, int ...band)
	{
		
	    if(bi == null)
    	{
	    	bi = new BufferedImage(nx,ny,BufferedImage.TYPE_INT_RGB);
    	}
	    WritableRaster raster = bi.getRaster();
		
	    int nb = band.length;
	    int ind = 0;
		for(int i=0; i<ny; i++)
		{
			for(int j=0; j<nx; j++)
			{
				for(int b=0;b<nb;b++)
				{
					raster.setSample(j,i,band[b],d[ind++]);
				}
			}
		}
	    return bi;
	}
	
	public static BufferedImage arrayToImage(BufferedImage bi, double[][] d )
	{
		
		int ny = d.length;
		int nx = d[0].length;
		
	    if(bi == null)
    	{
	    	bi = new BufferedImage(nx,ny,BufferedImage.TYPE_BYTE_GRAY);
    	}
	    WritableRaster raster = bi.getRaster();
		
	    double row[] = null;
		for(int i=0; i<ny; i++)
		{
			row = d[i];
			for(int j=0; j<nx; j++)
			{
					raster.setSample(j,i,0,row[j]);
			}
		}
	    return bi;
	}
	
	public static void threshold(double arr[][], double thresh, boolean makeBinary)
	{
		int h = arr.length;
		int w = arr[0].length;
		double row[] = null;
		for(int y=0; y<h; y++)
		{
			row = arr[y];
			for(int x=0; x<w; x++)
			{
				if(row[x]<thresh) row[x]=0;
				else if(makeBinary) row[x]=255;
			}
		}
	}
	
	public static BufferedImage erode(BufferedImage bi)
	{
		int type = bi.getType();
		int w = bi.getWidth();
		int h = bi.getHeight();
		
		if(type == 0)
		{
			type = BufferedImage.TYPE_BYTE_GRAY;
		}
		BufferedImage dst = new BufferedImage(w,h,type);
		
		int nb = dst.getRaster().getNumBands();
		double src[] = null;
		double out[] = null;
		WritableRaster raster = dst.getRaster();
		for(int b=0; b<nb; b++)
		{
			src = imageToArray(bi,b);
			out = erode(src,w,h);
			int ind = 0;
			for(int y=0; y<h; y++)
			{
				for(int x=0; x<w; x++)
				{
					raster.setSample(x,y,b,out[ind]);
					ind++;
				}
			}
		}
		return dst;
	}
	
	public static double[] erode(double src[], int w, int h)
	{
		int len = src.length;
		double dst[] = new double[len];
		int offs[] = {-w-1,-w,-w+1,-1,1,w-1,w,w+1};
		int nl = offs.length;
		double res = 0;
		int ind = 0;
		int row = 0;
		int cnt = 0;
		for(int y=1; y<h-1; y++)
		{
		    row = y*w;
			for(int x=1; x<w-1; x++)
			{
				ind = row+x;
				res = src[ind];
				for(int i=0; i<nl; i++)
				{
					res = Math.min(res, src[ind+offs[i]]);
				}
				//if(res != src[ind])
				//cnt++;
				//System.out.println(x+"\t"+y+"\t"+res+"\t"+src[ind]);
				dst[ind]=res;
			}
		}
		//System.out.println("\n\neroded " + cnt + "\n");
		return dst;
	}
	
	public static BufferedImage dilate(BufferedImage bi)
	{
		int type = bi.getType();
		int w = bi.getWidth();
		int h = bi.getHeight();
		
		if(type == 0)
		{
			type = BufferedImage.TYPE_BYTE_GRAY;
		}
		BufferedImage dst = new BufferedImage(w,h,type);
		
		int nb = dst.getRaster().getNumBands();
		double src[] = null;
		double out[] = null;
		WritableRaster raster = dst.getRaster();
		for(int b=0; b<nb; b++)
		{
			src = imageToArray(bi,b);
			out = dilate(src,w,h);
			int ind = 0;
			for(int y=0; y<h; y++)
			{
				for(int x=0; x<w; x++)
				{
					raster.setSample(x,y,b,out[ind]);
					ind++;
				}
			}
		}
		return dst;
	}
	
	public static double[] dilate(double src[], int w, int h)
	{
		int len = src.length;
		double dst[] = new double[len];
		int offs[] = {-w-1,-w,-w+1,-1,1,w-1,w,w+1};
		int nl = offs.length;
		double res = 0;
		int ind = 0;
		int row = 0;
		int cnt = 0;
		for(int y=1; y<h-1; y++)
		{
		    row = y*w;
			for(int x=1; x<w-1; x++)
			{
				ind = row+x;
				res = src[ind];
				for(int i=0; i<nl; i++)
				{
					res = Math.max(res, src[ind+offs[i]]);
				}
				//if(res != src[ind])
				//cnt++;
				//System.out.println(x+"\t"+y+"\t"+res+"\t"+src[ind]);
				dst[ind]=res;
			}
		}
		//System.out.println("\n\neroded " + cnt + "\n");
		return dst;
	}
	
	public static void threshold(BufferedImage bi, double thresh, boolean makeBinary)
	{
		int h = bi.getHeight();
		int w = bi.getWidth();
		WritableRaster wr = bi.getRaster();
		int nb = wr.getNumBands();
		double samp = 0;
		int black = Color.black.getRGB();
		int white = Color.white.getRGB();
		
		if(nb == 1)
		{
			for(int y=0; y<h; y++)
			{
				for(int x=0; x<w; x++)
				{
					samp = wr.getSampleDouble(x, y, 0);
					//if(samp > thresh) wr.setSample(x, y, 0, 255);
					//else wr.setSample(x, y, 0, 0);
					if(samp>thresh)bi.setRGB(x, y, white);
					else bi.setRGB(x, y, 0);
				}
			}
		}
		else if(nb > 2)
		{
			for(int y=0; y<h; y++)
			{
				for(int x=0; x<w; x++)
				{
					samp = wr.getSampleDouble(x, y, 0);
					samp += wr.getSampleDouble(x, y, 1);
					samp += wr.getSampleDouble(x, y, 2);
					samp *= 0.3333d;
					if(samp>thresh)bi.setRGB(x, y, white);
					else bi.setRGB(x, y, 0);
					/*
					if(samp > thresh)
					{
						wr.setSample(x, y, 0, 255);
						wr.setSample(x, y, 1, 255);
						wr.setSample(x, y, 2, 255);
					}
					else 
					{
						wr.setSample(x, y, 0, 0);
						wr.setSample(x, y, 1, 0);
						wr.setSample(x, y, 2, 0);
						//wr.setSample(x, y, 3, 255);
					}
					*/
				}
			}
			
		}
	}

}
