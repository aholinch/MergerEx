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
import java.util.List;

public class HuAnalysis 
{

	public static double dist2(double a1[], double a2[])
	{
		int len = Math.min(a1.length,a2.length);
		double tmp = 0;
		double sum = 0;
		double sum2 = 0;
		double mult = a1[0]/a2[0];
		
		for(int i=0; i<len; i++)
		{
			tmp = a1[i]-mult*a2[i];
			sum+= tmp*tmp;
			tmp = a1[i]-a2[i];
			sum2+=tmp*tmp;
		}
		sum = Math.sqrt(sum);
		sum2 = Math.sqrt(sum2);
		return sum+sum2;
	}
	/**
	 * http://docs.opencv.org/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html?highlight=cvmatchshapes#double%20matchShapes%28InputArray%20contour1,%20InputArray%20contour2,%20int%20method,%20double%20parameter%29
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static double match1(double a1[], double a2[])
	{
		double tmp = 0;
		double sum = 0;

		double ma = 0;
		double mb = 0;
		for(int i=0; i<7; i++)
		{
			ma = Math.signum(a1[i])*Math.log(Math.abs(a1[i]));
			mb = Math.signum(a2[i])*Math.log(Math.abs(a2[i]));
			if(a2[i]==0){
				tmp = Math.abs(1.0d/ma);
			}
			else
			{
				tmp = Math.abs(1.0d/ma-1.0d/mb);
			}
			sum += tmp;
		}
		return sum;
	}
	
	public static double match2(double a1[], double a2[])
	{
		double tmp = 0;
		double sum = 0;

		double ma = 0;
		double mb = 0;
		for(int i=0; i<7; i++)
		{
			ma = Math.signum(a1[i])*Math.log(Math.abs(a1[i]));
			mb = Math.signum(a2[i])*Math.log(Math.abs(a2[i]));
			if(a2[i]==0) mb = 0;

			tmp = Math.abs(ma-mb);
			sum += tmp;
		}
		return sum;
	}
	
	public static double match3(double a1[], double a2[])
	{
		double tmp = 0;
		double sum = 0;

		double ma = 0;
		double mb = 0;
	
		for(int i=0; i<7; i++)
		{
			ma = Math.signum(a1[i])*Math.log(Math.abs(a1[i]));
			mb = Math.signum(a2[i])*Math.log(Math.abs(a2[i]));
			if(a2[i]==0) mb = 0;
			tmp = Math.abs(ma-mb)/Math.abs(ma);
			if(tmp > sum) sum = tmp;
		}
		return sum;
	}
	
	
	public static List<List<ContourPoint>> findUsefulContours(List<List<ContourPoint>> cntrs, int w, int h, int x1, int y1, int x2, int y2)
	{
		List<List<ContourPoint>> out = new ArrayList<List<ContourPoint>>();
		
		// find two largest contours
		int nc=cntrs.size();
		if(nc <=2 ) return cntrs;
		
		int sizes[] = new int[nc];
		List<ContourPoint> pts = null;
		int exts[] = null;
		for(int i=0; i<nc; i++)
		{
			pts = cntrs.get(i);
			exts = ContourFinder.getExtents(pts);
			sizes[i]=(exts[1]-exts[0])*(exts[3]-exts[2]);
		}
		
		int tmp = 0;
		for(int i=0; i<nc-1; i++)
		{
			for(int j=i+1; j<nc; j++)
			{
				if(sizes[i]<sizes[j])
				{
					tmp = sizes[i];
					sizes[i]=sizes[j];
					sizes[j]=tmp;
					pts = cntrs.get(i);
					cntrs.set(i, cntrs.get(j));
				}
			}
		}
		
		int ind = 0;
		if(cntrs.size() > 1 && sizes[ind] >= 0.9*(w*h))
		{
			
			cntrs.remove(0);
			ind++;
		}
		
		out.add(cntrs.get(0));
		out.add(cntrs.get(1));
		return out;
	}
	
}
