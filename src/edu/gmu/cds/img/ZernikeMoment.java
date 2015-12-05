/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import edu.gmu.cds.util.MonitorUtil;

/**
 * This implementation of ZernikeMoment is inspired by
 * http://murphylab.web.cmu.edu/publications/boland/boland_node85.html
 * 
 * @author aholinch
 *
 */
public class ZernikeMoment 
{
	public int n = 0;
	public int m = 0;
	public double re = 0;
	public double im = 0;
	
	public ZernikeMoment()
	{
		
	}
	
	public ZernikeMoment(int n, int m, double realPart, double imaginaryPart)
	{
		this.n = n;
		this.m = m;
		this.re = realPart;
		this.im = imaginaryPart;
	}
	
	public double mag()
	{
		return Math.sqrt(re*re+im*im);
	}
	
	
	public static List<ZernikeMoment> calculateMoments(BufferedImage image, int order, boolean includeNegatives)
	{
		List<ZernikeMoment> moments = new ArrayList<ZernikeMoment>((int)(0.5*order*order));
		
		ZernikeMoment moment = null;
		
		List<double[][]> lists = getImageArrays(image);
		int w = image.getWidth();
		int h = image.getHeight();
		double vals[][] = lists.get(0);
		double radii[][] = lists.get(1);
		double angles[][] = lists.get(2);
		double tot = lists.get(3)[0][0];
		for(int n=0; n<=order; n++)
		{
			for(int m=0; m<=n; m++)
			{
				if((n-m)%2 != 0)
				{
					continue;
				}
				
				//moment = zernikeMoment(image,n,m);
				moment = zernikeMoment(vals,n,m,w,h,radii,angles,tot);
				moments.add(moment);
				
				if(includeNegatives && m != 0)
				moments.add(new ZernikeMoment(n,-m,moment.re,-moment.im));
				
				//System.out.println(n + "\t" + m + "\t" + moment.re + "\t" + moment.im + "\t" + moment.mag());

			}
		}
		return moments;
	}
	
	public static BufferedImage constructImage(List<ZernikeMoment> moments, int maxOrder, int width, int height)
	{
		BufferedImage bi = null;
		bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

		int numm = moments.size();
		ZernikeMoment moment = null;
		double revals[][] = new double[height][width];
		double imvals[][] = new double[height][width];
		
		List<double[][]> lists = getImageArrays(bi);
		double radii[][] = lists.get(1);
		double angles[][] = lists.get(2);

		double r = 0;
		double val = 0;
		double im = 0;
		double re = 0;
		double rad = 0;
		double arg = 0;
		double mre = 0;
		double mim = 0;
		int n = 0;
		int m = 0;
		
		for(int z = 0; z<numm; z++)
		{
			moment = moments.get(z);
			if(moment.n > maxOrder) continue;
			
			n = moment.n;
			m = moment.m;
			mre = moment.re;
			mim = moment.im;
			System.out.println(n + "\t" + m + "\t" + mre + "\t" + mim);
			// OK, we can reconstruct
			for(int j=0; j<height; j++)
			{
				for(int i=0; i<width; i++)
				{
					r = radii[j][i];
					if(r > 1.0d) continue;
					
					arg = m*angles[j][i];
					rad = zernikeR(n, m, r);
					re = rad*Math.cos(arg);
					im = rad*Math.sin(arg);  // positive because NOT conjugate

					// only keep real part of (A.re + i*A.im)(re + i*im)
					val = mre*re-mim*im;
					revals[j][i]+=val;
					val = mre*im+mim*re;
					imvals[j][i]+=val;
					//System.out.println(i+"\t" +j + "\t" + x + "\t" + y + "\t" + r + "\t" + re + "\t" + im + "\t" + revals[j][i] + "\t" + imvals[j][i]);
				}
			}
		}
		
		WritableRaster wr = bi.getRaster();
		double vals[] = new double[width];
		double norm = 255;
		for(int j=0; j<height; j++)
		{
			for(int i=0; i<width; i++)
			{
				vals[i]=norm*Math.sqrt(revals[j][i]*revals[j][i]+imvals[j][i]+imvals[j][i]);
			}
			wr.setSamples(0, j, width, 1, 0, vals);
		}
		return bi;
	}
	

	public static ZernikeMoment zernikeMoment(BufferedImage image, int n, int m)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		double xc = 0.5*w;
		double yc = 0.5*h;
		double radius = Math.sqrt(xc*xc+yc*yc);
		
		return zernikeMoment(image, n, m, w, h, xc, yc, radius);
	}
	
	public static ZernikeMoment zernikeMoment(BufferedImage image, int n, int m, int w, int h, double xc, double yc, double radius)
	{
		////MonitorUtil.startCall("zm");
		int mabs = Math.abs(m);
		if(n < 0 || mabs > n || (n-mabs)%2 != 0)
		{
			return null;
		}
		
		double re = 0;
		double im = 0;
		double x = 0;
		double y = 0;
		double r = 0;
		double invRad = 1.0d/radius;
		double tot = 0;
		double fxy = 0;
		double rad = 0;
		
		// grayscale, band should be 0
		int band = 0;
		Raster imageSrc = image.getRaster();
		
		double arg = 0;
		
		for(int j=0; j<h; j++)
		{
			y = j-yc;
			for(int i=0; i<w; i++)
			{
				x = i-xc;
				r = Math.sqrt(x*x+y*y)*invRad;
				fxy = imageSrc.getSample(i, j, band);
				tot += fxy;
				
				if(r > 1.0d) continue;
				
				arg = m*Math.atan2(y,x);
				
				// ok get the zernike values (do it inline to avoid function calls)
				rad = zernikeR(n, m, r);
				re += fxy*rad*Math.cos(arg);
				im -= fxy*rad*Math.sin(arg);  // negative due to conjugate
			}
		}
		
		// Normalize
		double norm = (n+1.0d)/Math.PI/tot;
		re *= norm;
		im *= norm;
		////MonitorUtil.endCall("zm");

		return new ZernikeMoment(n,m,re,im);
	}

	public static List<ZernikeMoment> calculateMoments(List<List<ContourPoint>> cntrs, int order, int w, int h, boolean includeNegatives)
	{
		List<ZernikeMoment> moments = new ArrayList<ZernikeMoment>((int)(0.5*order*order));
		
		ZernikeMoment moment = null;
		
		for(int n=0; n<=order; n++)
		{
			for(int m=0; m<=n; m++)
			{
				if((n-m)%2 != 0)
				{
					continue;
				}
				
				moment = zernikeMoment(cntrs,n,m,w,h);
				moments.add(moment);
				
				if(includeNegatives && m != 0)
				moments.add(new ZernikeMoment(n,-m,moment.re,-moment.im));
				
				//System.out.println(n + "\t" + m + "\t" + moment.re + "\t" + moment.im + "\t" + moment.mag());

			}
		}
		return moments;
	}

	public static ZernikeMoment zernikeMoment(List<List<ContourPoint>> cntrs, int n, int m, int w, int h)
	{
		double xc = 0.5*w;
		double yc = 0.5*h;
		double radius = Math.sqrt(xc*xc+yc*yc);
		
		return zernikeMoment(cntrs, n, m, w, h, xc, yc, radius);
	}
	
	public static ZernikeMoment zernikeMoment(List<List<ContourPoint>> cntrs, int n, int m, int w, int h, double xc, double yc, double radius)
	{
		////MonitorUtil.startCall("zm");
		int mabs = Math.abs(m);
		if(n < 0 || mabs > n || (n-mabs)%2 != 0)
		{
			return null;
		}
		
		double re = 0;
		double im = 0;
		double x = 0;
		double y = 0;
		double r = 0;
		double invRad = 1.0d/radius;
		double tot = 0;
		double fxy = 0;
		double rad = 0;
		
		// grayscale, band should be 0
		int band = 0;
		
		double arg = 0;
		
		int nc = cntrs.size();
		List<ContourPoint> cntr = null;
		ContourPoint pnt = null;
		int np = 0;
		for(int c=0; c<nc; c++)
		{
			cntr = cntrs.get(c);
			np = cntr.size();
			for(int p = 0; p<np; p++)
			{
				pnt = cntr.get(p);
				x = pnt.x-xc;
				y = pnt.y-yc;
				r = Math.sqrt(x*x+y*y)*invRad;
				fxy = 255;
				tot += fxy;
				
				if(r > 1.0d) continue;
				
				arg = m*Math.atan2(y,x);
				
				// ok get the zernike values (do it inline to avoid function calls)
				rad = zernikeR(n, m, r);
				re += fxy*rad*Math.cos(arg);
				im -= fxy*rad*Math.sin(arg);  // negative due to conjugate
			}
		}

		
		// Normalize
		double norm = (n+1.0d)/Math.PI/tot;
		re *= norm;
		im *= norm;
		////MonitorUtil.endCall("zm");

		return new ZernikeMoment(n,m,re,im);
	}

	public static ZernikeMoment zernikeMoment(double vals[][], int n, int m, int w, int h, double radii[][], double angles[][], double tot)
	{
		//MonitorUtil.startCall("zmarr");
		int mabs = Math.abs(m);
		if(n < 0 || mabs > n || (n-mabs)%2 != 0)
		{
			return null;
		}
		
		double re = 0;
		double im = 0;
		double fxy = 0;
		double rad = 0;
		double r = 0;
		double arg = 0;
		
		double a1[] = null;
		double a2[] = null;
		double a3[] = null;
		
		for(int j=0; j<h; j++)
		{
			a1 = radii[j];
			a2 = vals[j];
			a3 = angles[j];
			for(int i=0; i<w; i++)
			{
				//r = radii[j][i];
				//fxy = vals[j][i];
				r = a1[i];
				fxy = a2[i];
				
				if(r > 1.0d) continue;
				
				//arg = m*angles[j][i];
				arg = m*a3[i];
				
				// ok get the zernike values (do it inline to avoid function calls)
				rad = zernikeR(n, m, r);
				//MonitorUtil.startCall("ztrig");
				re += fxy*rad*Math.cos(arg);
				im -= fxy*rad*Math.sin(arg);  // negative due to conjugate
				//MonitorUtil.endCall("ztrig");

			}
		}
		
		// Normalize
		double norm = (n+1.0d)/Math.PI/tot;
		re *= norm;
		im *= norm;
		//MonitorUtil.endCall("zmarr");

		return new ZernikeMoment(n,m,re,im);
	}
	/**
	 * Sets the value of the polynomial (real,imaginary) into comp.
	 * 
	 * @param n
	 * @param m
	 * @param x
	 * @param y
	 * @param r
	 * @param comp
	 */
	public static void zernikePolynomial(int n, int m, double x, double y, double r, double comp[])
	{
		r = zernikeR(n,m,r);
		double arg = m*Math.atan2(y,x);
		comp[0] = r*Math.cos(arg);
		comp[1] = r*Math.sin(arg);
	}
	
	/**
	 * zernike radial polynomial
	 * 
	 * @param n
	 * @param l
	 * @param r
	 * @return
	 */
	public static double zernikeR(int n, int l, double r)
	{
		//MonitorUtil.startCall("zr");

	    int s = 0;
	    double sum = 0;
	    if((n-l)%2 != 0)
	    {
			//MonitorUtil.endCall("zr");

	    	return 0;
	    }
	    
	    l=Math.abs(l);
	    
	    double top = 0;
	    double bot1 = 0;
	    double bot2 = 0;
	    double rho = 0;
	    
	    for(s=0; s<=(n-l)/2; s++)
	    {
	    	rho = Math.pow(r,(double)(n-2*s));
	    	top = Math.pow(-1.0d,(double)s)*fact(n-s);
	    	bot1= fact(s)*(fact(0.5*(n+l)-s));
	    	bot2= fact(0.5*(n-l)-s);
	    	sum += rho*top/(bot1*bot2);
	    }
		//MonitorUtil.endCall("zr");

	    return sum;
	}
	
    public static double fact(double n)
    {
    	double fact = 0;
    	////MonitorUtil.startCall("fact");
    	if(n > 0)
    	{
    		//fact = n*fact(n-1);
    		
    		fact = 1;
    		for(int i=2; i<=n; i++)
    		{
    			fact*=i;
    		}
    	}
    	else if(n == 0)
    	{
    		fact = 1;
    	}
    	else
    	{
    		fact = 0;
    	}
    	////MonitorUtil.endCall("fact");
    	
    	return fact;
    }
    
    /**
     * Returns three arrays of size height*width
     * 0 - pixel values
     * 1 - radius
     * 2 - angle
     * 3 - total intensity
     * 
     * @param img
     * @return
     */
    public static List<double[][]> getImageArrays(BufferedImage img)
    {
    	List<double[][]> out = new ArrayList<double[][]>();
    	
        int width = img.getWidth();
        int height = img.getHeight();
        
        double vals[][] = new double[height][width];
        double radii[][] = new double[height][width];
        double angles[][] = new double[height][width];
        double tot[][] = new double[1][1];
        
        out.add(vals);
        out.add(radii);
        out.add(angles);
        out.add(tot);
        
        double x = 0;
        double y = 0;
        double xc = 0.5*width;
        double yc = 0.5*height;
        double radius = Math.sqrt(xc*xc+yc*yc);
        double invRadius = 1.0d/radius;
        double r = 0;
        double ang = 0;
        Raster raster = img.getRaster();
        int band = 0;
        double t = 0;
        
        for(int j=0; j<height; j++)
        {
        	y = j-yc;
        	for(int i=0; i<width; i++)
        	{
        		x = i-xc;
        		r = Math.sqrt(x*x+y*y)*invRadius;
        		ang = Math.atan2(y,x);
        		vals[j][i] = raster.getSample(i, j, band);
        		radii[j][i] = r;
        		angles[j][i] = ang;
        		t+=vals[j][i];
        	}
        }
        tot[0][0]=t;
        return out;
    }
    
    public static List<ZernikeMoment> readMoments(String file)
    {
    	List<ZernikeMoment> moments = new ArrayList<ZernikeMoment>(100);
    	FileReader fr = null;
    	LineNumberReader lnr = null;
    	try
    	{
    		fr = new FileReader(file);
    		lnr = new LineNumberReader(fr);
    		String line = null;
    		ZernikeMoment moment = null;
    		String sa[] = null;
    		
    		line = lnr.readLine();
    		
    		while(line != null)
    		{
    			moment = new ZernikeMoment();
    			sa = line.split("\t");
    			moment.n = (int)gd(sa[0]);
    			moment.m = (int)gd(sa[1]);
    			moment.re = gd(sa[2]);
    			moment.im = gd(sa[3]);
    			moments.add(moment);
    		    line = lnr.readLine();	
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(lnr != null)try{lnr.close();fr.close();}catch(Exception ex){};
    	}
    	return moments;
    }
    
    public static double gd(String str)
    {
    	double num = 0;
    	try{num = Double.parseDouble(str.trim());}catch(Exception ex){}
    	return num;
    }
    
    public static void writeMoments(String file, List<ZernikeMoment> moments)
    {
    	FileWriter fw = null;
    	try
    	{
    		fw = new FileWriter(file);
    		int size = moments.size();
    		ZernikeMoment moment = null;
    		for(int i=0; i<size; i++)
    		{
    			moment = moments.get(i);
    			fw.write(moment.n + "\t" + moment.m + "\t" + moment.re + "\t" + moment.im + "\n");
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(fw != null)try{fw.flush();}catch(Exception ex){};
    	}
    }
    
    public static void processFiles(List<String> files, int order)
    {
    	try
    	{
    		BufferedImage bi = null;
	    	String file = null;
	    	String out = null;
	    	int size = files.size();
	    	List<ZernikeMoment> moments = null;
	    	int ind = 0;
	    	for(int i=0; i<size; i++)
	    	{
	    		file = files.get(i);
	    		ind = file.lastIndexOf('.');
	    		out = file.substring(0,ind)+".zrn";
	    		bi = ImageIO.read(new File(file));
	    		moments = calculateMoments(bi,order,false);
	    		writeMoments(out,moments);
	    	}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public static void processTopDir(File dir, int nThreads, int order)
    {
    	File files[] = dir.listFiles();
    	for(int i=0; i<files.length; i++)
    	{
    		if(files[i].isDirectory())
    		{
    			processDir(files[i],nThreads,order);
    		}
    	}
    }
    
    public static void processDir(File dir, int nThreads, int order)
    {
    	File fileObjs[] = dir.listFiles();
    	int size = fileObjs.length;
    	System.out.println("Processing " + size + " files in " + dir.getName());
    	List<ZernProc> procs = new ArrayList<ZernProc>();
    	ZernProc proc =null;
    	
    	String file = null;
    	int ind = 0;
    	int max = 0;
    	int diff = size/nThreads;
    	
    	try
    	{
	    	for(int i=0; i<nThreads; i++)
	    	{
	    		max = ind+diff;
	    		if(i == (nThreads-1)) max = size;
	    		List<String> files = new ArrayList<String>(diff);
	    		for(int j=ind; j<max; j++)
	    		{
	    			if(fileObjs[j].isFile())
	    			{
	    				file = fileObjs[j].getCanonicalPath();
	    				if(file.endsWith(".jpg"))
	    					files.add(file);
	    			}
	    		}
	    		ind+=diff;

	    		proc = new ZernProc(order,files);
	    		procs.add(proc);
	    	}
	    	
	    	for(int i=0; i<nThreads; i++)
	    	{
	    		proc = procs.get(i);
	    		Thread t = new Thread(proc);
	    		t.start();
	    	}
	    	
	    	int isRunning = 6;
	    	while(isRunning > 0)
	    	{
	    		Thread.sleep(500);
	    		isRunning = 0;
	    		for(int i=0; i<nThreads; i++)
	    		{
	    			if(procs.get(i).isRunning) isRunning++;
	    		}
	    	}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public static class ZernProc implements Runnable
    {
    	int order = 50;
    	List<String> files = null;
    	public boolean isRunning = false;
    	
    	public ZernProc(int o, List<String> f)
    	{
    		order = o;
    		files = f;
    	}
    	
    	public void run()
    	{
    		isRunning = true;
    		processFiles(files,order);
    		isRunning = false;
    	}
    }
    
}
