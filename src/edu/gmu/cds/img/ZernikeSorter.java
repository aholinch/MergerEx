/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

public class ZernikeSorter 
{
	public static class ZC
	{
		String name ;
		double d1;
		double d2;
		
		public ZC()
		{
			
		}
		
		public String toString()
		{
			return name + "\t" + d1 + "\t" + d2;
		}
	}
	
	public static class ZCComp implements Comparator<ZC>
	{
		boolean use2 = false;
		public ZCComp(boolean use2)
		{
			this.use2 = use2;
		}
		@Override
		public int compare(ZC arg0, ZC arg1) 
		{
			double d1 = arg0.d1;
			double d2 = arg1.d1;
			if(use2)
			{
				d1 = arg0.d2;
				d2 = arg1.d2;
			}
			if(d1 < d2) return -1;
			if(d1 > d2) return 1;
			return 0;
		}
		
	}
	
	public static double[] dist(List<ZernikeMoment> mts1, List<ZernikeMoment> mts2)
	{
		double dist = 0;
		double dist2 = 0;
		double tmp = 0;
		
		// dangerous but hey, why not?
		// assume moments in same order
		// just diff magnitudes for now
		int size = Math.min(mts1.size(), mts2.size());
		ZernikeMoment mt1 = null;
		ZernikeMoment mt2 = null;
		for(int i=0; i<size; i++)
		{
			mt1 = mts1.get(i);
			mt2 = mts2.get(i);
			tmp = mt1.mag() - mt2.mag();
			dist += tmp*tmp;
			tmp = mt1.re-mt2.re;
			dist2+=tmp*tmp;
			tmp = mt1.im-mt2.im;
			dist2+=tmp*tmp;
		}
		dist = Math.sqrt(dist);
		dist2 = Math.sqrt(dist2);
		return new double[]{dist,dist2};
	}
	
	public static BufferedImage loadImage(String file)
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
	
	public static int gi(String str)
	{
		int num = 0;
		try{num = Integer.parseInt(str.trim());}catch(Exception ex){};
		return num;
	}
    public static void mainArgs(String args[])
    {
    	if(args.length < 3)
    	{
    		System.out.println("<tgt image> <directory with img files> <# order zernmoments> <ext of image files>");
    		System.exit(0);
    	}
    	int numMoments = gi(args[2]);
    	BufferedImage tgtImg = loadImage(args[0]);
    	System.out.println("Using " + numMoments + " to describe target " + args[0]);
        List<ZernikeMoment> tgtMoms = ZernikeMoment.calculateMoments(tgtImg, numMoments,false);
        String ext = ".png";
        if(args.length > 3)
        {
        	ext = args[3].toLowerCase();
        }
        File dir = new File(args[1]);
        File list[] = dir.listFiles();
        int len = list.length;
        File file = null;
        String name = null;
        BufferedImage bi = null;
        List<ZernikeMoment> imgMoms = null;
        double dist[] = null;
        System.out.println("Looking for images out of " + len + " files in " + args[1]);
        
        List<ZC> zcs = new ArrayList<ZC>(len);
        ZC zc = null;
        
        for(int i=0; i<len; i++)
        {
        	file = list[i];
        	name = file.getName();
        	if(!name.toLowerCase().endsWith(ext))
        	{
        		continue;
        	}
        	bi = loadImage(file.getAbsolutePath());
        	imgMoms = ZernikeMoment.calculateMoments(bi, numMoments,false);	
        	dist = dist(tgtMoms,imgMoms);
        	zc = new ZC();
        	zc.name = name;
        	zc.d1 = dist[0];
        	zc.d2 = dist[1];
        	zcs.add(zc);
        	System.out.println(zc);
        	//System.out.println(name + "\t" + dist[0] + "\t" + dist[1]);
        }
        
        System.out.println("\n\n\n\nsorting\n\n");
        int ts = Math.min(10,zcs.size());
        ZCComp comp = new ZCComp(false);
        java.util.Collections.sort(zcs,comp);
        System.out.println("\n\nTop images by d1");
        for(int i=0; i<ts; i++)
        {
        	System.out.println(zcs.get(i));
        }
        
        comp = new ZCComp(true);
        java.util.Collections.sort(zcs,comp);
        System.out.println("\n\nTop images by d2");
        for(int i=0; i<ts; i++)
        {
        	System.out.println(zcs.get(i));
        }
        System.exit(0);
    }
}
