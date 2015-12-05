/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import edu.gmu.cds.sim.Parameters;
import edu.gmu.cds.sim.Run;
import edu.gmu.cds.sim.StateInfo;

public class SimRunUtil 
{
	public int numParticles = 2000;
	public double preferredH = 0.005;
	
	public SimRunUtil()
	{
		
	}
	
	
	protected void setN(StateInfo info, Parameters params, int totalN)
	{
		double m1 = info.m1;
		double m2 = info.m2;
		double totm = m1+m2;
		m1 /= totm;
		m2 /= totm;
		
		int n1 = (int)(totalN*m1);
		int n2 = (int)(totalN*m2);
		
		int n = n1+n2;
		while(n < totalN)
		{
			n1++;
			n = n1+n2;
		}
		
		if(n > totalN)
		{
			n = totalN;
		}
		while(n1+n2>n)
		{
			n1--;
		}
		params.n  = n;
		params.n1 = n1;
		params.n2 = n2;
	}

    public double[][] getParticlePositions(StateInfo info)
    {
		double tstart = -10;
		double tend = 0;
	
		return getParticlePositions(info,numParticles,1,tstart,tend);
    }
    
    public double[][] getParticlePositions(StateInfo info, int numParticles, int potType, double tstart, double tend)
    {
    	Run run = new Run();
		
    	setN(info,run.params,numParticles);
		
		info.setValuesToParameters(run.params);
		
		run.params.time=tstart;
		run.params.potential_type=potType;
		run.params.tstart=tstart;
		run.params.tend=tend;
		run.params.h=preferredH;
		run.initRun(run.params);
				
        double t0 = 0;

        int nstep_local = 7500;
        run.params.tstart = tstart;
        run.params.tend = tend;
        t0 = run.params.tstart;
        run.params.nstep = (int)((run.params.tend-t0)/run.params.h)+2;
        nstep_local = run.params.nstep;
        
        for(int i=0; i< nstep_local; i++)
        {
            run.takeAStep();
        }

        return run.x0;
    }
    
    public BufferedImage generateImage(double parts[][], int width, int height, double minx, double maxx, double miny, double maxy)
    {
    	BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
    	
    	double rngx = maxx-minx;
    	double rngy = maxy-miny;
    	
    	rngx = ((double)width)/rngx;
    	rngy = ((double)height)/rngy;
    	
    	int vals[][] = new int[height][width];
    	int i = 0;
    	int j = 0;
    	double x = 0;
    	double y = 0;
    	
    	int np = parts.length;
    	for(int p=0; p<np; p++)
    	{
    		x = parts[p][0];
    		y = parts[p][1];
    		x = (x-minx)*rngx;
    		y = (y-minx)*rngx;
    		i = (int)x;
    		j = (int)y;
    		if(i >= 0 && i<width && j>=0 && j<height)
    		{
    			putParticle(vals,i,height-j-1,width,height);
    		}
    	}
    	
    	int max = 0;
    	int line[] = null;
    	for(j=0;j<height;j++)
    	{
    		line = vals[j];
    		for(i=0;i<width;i++)
    		{
    			if(line[i]>max)
    			{
    				max = line[i];
    			}
    		}
    	}

    	double scale = 255.0d/(double)max;
    	WritableRaster raster = bi.getRaster();
    	for(j=0;j<height;j++)
    	{
    		line = vals[j];
    		for(i=0;i<width;i++)
    		{
    			line[i]= (int)(line[i]*(scale));
    		}
    		raster.setPixels(0, j, width, 1, line);
    	}
    	return bi;
    }
    
    public void putParticle(int vals[][], int x, int y, int w, int h)
    {
    	vals[y][x]+=5;
    	int minx = Math.max(0, x-2);
    	int miny = Math.max(0, y-2);
    	int maxx = Math.min(w-1,x+2);
    	int maxy = Math.min(h-1,y+2);
    	for(int j=miny; j<=maxy; j++)
    	{
    		for(int i=minx; i<=maxx; i++)
    		{
    			vals[j][i]+=1;
    		}
    	}
    	
    	minx = Math.max(1, x-1);
    	miny = Math.max(1, y-1);
    	maxx = Math.min(w-2,x+1);
    	maxy = Math.min(h-2,y+1);
    	for(int j=miny; j<=maxy; j++)
    	{
    		for(int i=minx; i<=maxx; i++)
    		{
    			vals[j][i]+=2;
    		}
    	}
    }
}
