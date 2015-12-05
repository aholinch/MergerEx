/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ContourFinder 
{
	private static int[][] OFFSETS_1 = null;
	private static int[][] OFFSETS_2 = null;
	private static int[][] OFFSETS_3 = null;
	private static int[][] OFFSETS_4 = null;
	private static int[][] OFFSETS_5 = null;
	private static int[][] OFFSETS_6 = null;
	private static int[][] OFFSETS_7 = null;
	private static int[][] OFFSETS_8 = null;
	
	static
	{
	    OFFSETS_1 = calcOffsets(1);	
	    OFFSETS_2 = calcOffsets(2);	
	    OFFSETS_3 = calcOffsets(3);	
	    OFFSETS_4 = calcOffsets(4);	
	    OFFSETS_5 = calcOffsets(5);	
	    OFFSETS_6 = calcOffsets(6);	
	    OFFSETS_7 = calcOffsets(7);	
	    OFFSETS_8 = calcOffsets(8);	
	}
	
	public static int[][] calcOffsets(int startInd)
	{
		int ind;
		int offs[][] = new int[8][2];
		for(int i=0; i<8; i++)
		{
			ind = startInd + i;
			if(ind > 8) ind -= 8;
			setXYOffset(ind,offs[i]);
		}
		return offs;
	}
	
	/**
	 *  1 2 3
	 *  8 X 4
	 *  7 6 5
	 * @param mooreNum
	 * @param xy
	 */
	public static void setXYOffset(int mooreNum, int xy[])
	{
		switch(mooreNum)
		{
		    case 1:
		    	xy[0]=-1;
		    	xy[1]=1;
		    	break;
		    case 2:
		    	xy[0]=0;
		    	xy[1]=1;
		    	break;
		    case 3:
		    	xy[0]=1;
		    	xy[1]=1;
		    	break;
		    case 4:
		    	xy[0]=1;
		    	xy[1]=0;
		    	break;
		    case 5:
		    	xy[0]=1;
		    	xy[1]=-1;
		    	break;
		    case 6:
		    	xy[0]=0;
		    	xy[1]=-1;
		    	break;
		    case 7:
		    	xy[0]=-1;
		    	xy[1]=-1;
		    	break;
		    case 8:
		    	xy[0]=-1;
		    	xy[1]=0;
		    	break;
	    }
	}
	
	/**
	 * For a given x,y and entry, we determine set of offsets to use
	 * @param pt
	 * @return
	 */
	public static int[][] getOffsets(ContourPoint pt)
	{
	    int offs[][] = null;
	    
	    int dx = pt.ex - pt.x;
	    int dy = pt.ey - pt.y;
	    
	    if(dx==0 && dy == -1)
	    {
	    	offs = OFFSETS_6;
	    }
	    else if(dx==-1 && dy==-1)
	    {
	    	offs = OFFSETS_7;
	    }
	    else if(dx==-1 && dy ==0)
	    {
	    	offs = OFFSETS_8;
	    }
	    else if(dx==-1 && dy ==1)
	    {
	    	offs = OFFSETS_1;
	    }
	    else if(dx==0 && dy ==1)
	    {
	    	offs = OFFSETS_2;
	    }
	    else if(dx==1 && dy ==1)
	    {
	    	offs = OFFSETS_3;
	    }
	    else if(dx==1 && dy ==0)
	    {
	    	offs = OFFSETS_4;
	    }
	    else if(dx==1 && dy ==-1)
	    {
	    	offs = OFFSETS_5;
	    }
	    
	    return offs;
	}
	
	public static List<List<ContourPoint>> findContoursBinary(BufferedImage bi)
	{
		// wrap image in an RGBSource to "pad" with background pixels
		RGBSource src = new RGBSource(bi, 0);
		
		int h = bi.getHeight();
		int w = bi.getWidth();
		
	    
		List<ContourPoint> pts = null;
		List<ContourPoint> allPts = new ArrayList<ContourPoint>(1000);
		List<List<ContourPoint>> contours = new ArrayList<List<ContourPoint>>(10);
		
		// find start pixel
		for(int i=0; i<w; i++)
		{
			for(int j=0; j<h; j++)
			{
				//System.out.println(bi.getRGB(i, j));
			    if(src.getSample(i, j) != 0 && src.getSample(i,j-1)==0)
			    {
			    	ContourPoint pt = new ContourPoint(i,j,i,j-1);
				    if(allPts.contains(pt)) continue;
				    //System.out.println("Starting contour at\t"+i + "\t"+j);
				    pts = findContourBinary(src, pt);
				    contours.add(pts);
				    allPts.addAll(pts);
			    }
			}
		}
		
		return contours;
	}
	
	public static List<ContourPoint> findContourBinary(RGBSource bi, ContourPoint pt)
	{
		List<ContourPoint> pts = new ArrayList<ContourPoint>(1000);
		ContourPoint start = new ContourPoint(pt);
		
		// maintain copies of the start point for each entry point
		List<ContourPoint> startPts = new ArrayList<ContourPoint>(8);
		startPts.add(start);
		
		ContourPoint newPt = null;
		int x,y =0;
		
		pts.add(start);
		int [][]offsets = null;
		int i = 0;
		
		i = 0;
		try
		{
		while(i < 8)
		{
			offsets = getOffsets(pt);
			
			for(i=1; i<8; i++)
			{
		    	x = pt.x + offsets[i][0];	
		    	y = pt.y + offsets[i][1];
		    	if(bi.getSample(x,y) != 0)
		    	{
		    		// we've found another point
		    	    newPt = new ContourPoint(x,y,pt.x+offsets[i-1][0],pt.y+offsets[i-1][1]);
		    		   		
		    		if(!pts.contains(newPt))
		    		{
		    			pts.add(newPt);
		    		}
		    		pt = newPt;
		    		break;
		    	}
			}
			
			// if we've hit the same start point we can end
			if(pt.equals(start))
		    {
				// as long as the entry point is the same
				if(pt.sameEntry(start))
				{
					i = 8;
				}
				else
				{
					int j = 0;
					for(j=0; j<startPts.size(); j++)
					{
						if(pt.sameEntry(startPts.get(j)))
						{
							i = 8;
							break;
						}
					}
					if(j == startPts.size())
					{
						// new entry direction
						startPts.add(pt);
					}
				}
		    }
		}
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }   	
		return pts;
	}
	public static List<List<ContourPoint>> findContours(BufferedImage bi, int rgbBackground)
	{
		// wrap image in an RGBSource to "pad" with background pixels
		RGBSource src = new RGBSource(bi, rgbBackground);
		
		int h = bi.getHeight();
		int w = bi.getWidth();
		
	    
		List<ContourPoint> pts = null;
		List<ContourPoint> allPts = new ArrayList<ContourPoint>(1000);
		List<List<ContourPoint>> contours = new ArrayList<List<ContourPoint>>(10);
		
		// find start pixel
		for(int i=0; i<w; i++)
		{
			for(int j=0; j<h; j++)
			{
				//System.out.println(bi.getRGB(i, j));
			    if(src.getRGB(i, j) != rgbBackground && src.getRGB(i,j-1)==rgbBackground)
			    {
			    	ContourPoint pt = new ContourPoint(i,j,i,j-1);
				    if(allPts.contains(pt)) continue;
				    //System.out.println("Starting contour at\t"+i + "\t"+j);
				    pts = findContour(src, pt, rgbBackground);
				    contours.add(pts);
				    allPts.addAll(pts);
			    }
			}
		}
		
		return contours;
	}
	
	public static List<ContourPoint> findContour(RGBSource bi, ContourPoint pt, int rgbBackground)
	{
		List<ContourPoint> pts = new ArrayList<ContourPoint>(1000);
		ContourPoint start = new ContourPoint(pt);
		
		// maintain copies of the start point for each entry point
		List<ContourPoint> startPts = new ArrayList<ContourPoint>(8);
		startPts.add(start);
		
		ContourPoint newPt = null;
		int x,y =0;
		
		pts.add(start);
		int [][]offsets = null;
		int i = 0;
		
		i = 0;
		try
		{
		while(i < 8)
		{
			offsets = getOffsets(pt);
			
			for(i=1; i<8; i++)
			{
		    	x = pt.x + offsets[i][0];	
		    	y = pt.y + offsets[i][1];
		    	if(bi.getRGB(x,y) != rgbBackground)
		    	{
		    		// we've found another point
		    	    newPt = new ContourPoint(x,y,pt.x+offsets[i-1][0],pt.y+offsets[i-1][1]);
		    		   		
		    		if(!pts.contains(newPt))
		    		{
		    			pts.add(newPt);
		    		}
		    		pt = newPt;
		    		break;
		    	}
			}
			
			// if we've hit the same start point we can end
			if(pt.equals(start))
		    {
				// as long as the entry point is the same
				if(pt.sameEntry(start))
				{
					i = 8;
				}
				else
				{
					int j = 0;
					for(j=0; j<startPts.size(); j++)
					{
						if(pt.sameEntry(startPts.get(j)))
						{
							i = 8;
							break;
						}
					}
					if(j == startPts.size())
					{
						// new entry direction
						startPts.add(pt);
					}
				}
		    }
		}
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }   	
		return pts;
	}
	
	public static void dumpContour(List<ContourPoint> pts)
	{
		int size = pts.size();
		ContourPoint pt = null;
		for(int i=0; i<size; i++)
		{
			pt = pts.get(i);
			System.out.println(pt.x + "\t" + pt.y);
		}
	}

	/**
	 * Get minx,maxx,miny,maxy
	 * 
	 * @param pts
	 * @return
	 */
	public static int[] getExtents(List<ContourPoint> pts)
	{
		int minx = Integer.MAX_VALUE;
		int maxx = -(minx-2);
		int miny = minx;
		int maxy = maxx;
		
		int size = pts.size();
		int x,y;
		for(int i=0; i<size; i++)
		{
			x = pts.get(i).x;
			y = pts.get(i).y;
			if(x > maxx) maxx=x;
			if(x < minx) minx=x;
			if(y > maxy) maxy=y;
			if(y < miny) miny=y;
		}
		return new int[]{minx,maxx,miny,maxy};
	}
	
	public static List<ContourPoint> getPointsInside(List<ContourPoint> contour)
	{
		return getPointsInside(contour,getExtents(contour));
	}
	
	public static List<ContourPoint> getPointsInside(List<ContourPoint> contour, int vals[])
	{
	    int size = contour.size();
	    List<ContourPoint> pts = new ArrayList<ContourPoint>(size*4);
	    pts.addAll(contour);
	    
	    int minx = vals[0];
	    int maxx = vals[1];
	    int miny = vals[2];
	    int maxy = vals[3];
	    
	    ContourPoint pt = null;
	    
	    for(int x=minx; x<=maxx; x++)
	    {
	    	for(int y=miny; y<=maxy; y++)
	    	{
	    		if(isInside(x,y,contour))
	    		{
	    			pt = new ContourPoint(x,y,0,0);
	    			if(!pts.contains(pt))
	    			{
	    				pts.add(pt);
	    			}
	    		}
	    	}
	    }
	    return pts;
	}
	
	/**
	 * Determine if the point is inside the specified contour.
	 * 
	 * @param x
	 * @param y
	 * @param pts
	 * @return
	 */
	public static boolean isInside(int xt, int yt, List<ContourPoint> pts)
	{
        boolean flag = false;
        int xnew,ynew;
        int xold,yold;
        int x1,y1;
        int x2,y2;
        int i;

        int npoints = pts.size();
        
        if (npoints < 3) {
             return(flag);
        }
        xold=pts.get(npoints-1).x;
        yold=pts.get(npoints-1).y;
        for (i=0 ; i < npoints ; i++) {
             xnew=pts.get(i).x;
             ynew=pts.get(i).y;
             if (xnew > xold) {
                  x1=xold;
                  x2=xnew;
                  y1=yold;
                  y2=ynew;
             }
             else {
                  x1=xnew;
                  x2=xold;
                  y1=ynew;
                  y2=yold;
             }
             if ((xnew < xt) == (xt <= xold)          /* edge "open" at one end */
              && ((long)yt-(long)y1)*(long)(x2-x1)
               < ((long)y2-(long)y1)*(long)(xt-x1)) {
                  flag=!flag;
             }
             xold=xnew;
             yold=ynew;
        }

        return flag;
	}
	
	public static BufferedImage paintContours(BufferedImage bi, List<List<ContourPoint>> cntrs, Color color)
	{
		int nc = cntrs.size();
		int np = 0;
		List<ContourPoint> pts = null;
		ContourPoint pnt = null;
		ContourPoint prevPnt = null;
		int h = bi.getHeight();
		int w = bi.getWidth();
		BufferedImage biOut = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		
		Graphics g = biOut.getGraphics();
		g.drawImage(bi, 0, 0, null);
		g.setColor(color);
		((Graphics2D)g).setStroke(new BasicStroke(8));
		
		for(int c =0; c<nc; c++)
		{
			pts = cntrs.get(c);
			np = pts.size();
			pnt = pts.get(0);
			for(int p=1; p<np; p++)
			{
				prevPnt = pnt;
				pnt = pts.get(p);
				g.drawLine(prevPnt.x, prevPnt.y, pnt.x, pnt.y);
			}
			prevPnt = pts.get(0);
			g.drawLine(prevPnt.x, prevPnt.y, pnt.x, pnt.y);

		}
		return biOut;
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
	 * @param pnts
	 * @param eps
	 * @return
	 */
	public static List<ContourPoint> approximateContour(List<ContourPoint> pnts, double eps)
	{
		double dmax = 0;
		int index = -1;
		int size = pnts.size();
		int end = size-1;
		if(size == 2)
		{
			return pnts;
		}
		
		ContourPoint pt = null;

		pt = pnts.get(0);
		double x1 = pt.x;
		double y1 = pt.y;
		
		pt = pnts.get(end);
		double x2 = pt.x;
		double y2 = pt.y;

		double x0 = 0;
		double y0 = 0;
		
		double dx = x2-x1;
		double dy = y2-y1;
		double den = dx*dx+dy*dy;
		double tmp = x2*y1-x1*y2;
		double num = 0;
		
		for(int i=1; i<end; i++)
		{
			pt = pnts.get(i);
			x0 = pt.x;
			y0 = pt.y;
			num = dy*x0-dx*y0+tmp;
			num *= num;
			if(num > dmax)
			{
				dmax = num;
				index = i;
			}
		}
		
		//System.out.println(dmax +"\t" + eps*eps*den + "\t" + dmax/den + "\t" + eps*eps + "\t"+Math.sqrt(dmax/den)+"\t"+eps);
		List<ContourPoint> out = null;
		if(dmax>eps*eps*den)
		{
			List<ContourPoint> l1 = subList(pnts,0,index);
			List<ContourPoint> l2 = subList(pnts,index,end);
			l1 = approximateContour(l1,eps);
			l2 = approximateContour(l2,eps);
			out = new ArrayList<ContourPoint>(size);
			
			// remove duplicate copy of l1
			l1.remove(l1.size()-1);
			out.addAll(l1);
			out.addAll(l2);
		}
		else
		{
			out = new ArrayList<ContourPoint>();
			out.add(pnts.get(0));
			out.add(pnts.get(end));
		}
		
		return out;
	}
	
	public static List<ContourPoint> subList(List<ContourPoint> pnts, int start, int end)
	{
		int size = end-start+1;
		List<ContourPoint> out = new ArrayList<ContourPoint>(size);
		for(int i=start; i<=end; i++)
		{
			out.add(pnts.get(i));
		}
		return out;
	}
	
	public static class RGBSource
	{
	    int w = 0;
	    int h = 0;
	    int bgColor = 0;
	    BufferedImage bi = null;
	    WritableRaster raster = null;
	    
	    public RGBSource(BufferedImage bi, int bgColor)
	    {
	    	this.bi = bi;
	    	this.bgColor = bgColor;
	    	w = bi.getWidth();
	    	h = bi.getHeight();
	    }
	    
	    public int getRGB(int x, int y)
	    {
	    	if(x < 0 || y < 0) return bgColor;
	    	if(x >= w || y >= h) return bgColor;
	    	
	    	//System.out.println(x + "\t" + y + "\t" + bi.getRGB(x, y));
	    	return bi.getRGB(x, y);
	    }   
	    
	    public int getSample(int x, int y)
	    {
	    	if(x < 0 || y < 0) return bgColor;
	    	if(x >= w || y >= h) return bgColor;
	    	
	    	if(raster == null) raster = bi.getRaster();
	    	return raster.getSample(x, y,0);
	    }   
	}

    
}
