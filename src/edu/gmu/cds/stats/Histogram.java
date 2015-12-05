/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.stats;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Histogram 
{
    public double bins[];
    public int cnts[];
    public String labels[];
    
    public Histogram()
    {
    	
    }
    
    public Histogram(double bins[])
    {
    	this.bins = bins;
    	if(bins != null)
    	{
    		int nb = bins.length;
    		cnts = new int[nb];
    		labels = new String[nb];
    		for(int i=0; i<nb; i++)
    		{
    			labels[i] = String.valueOf(bins[i]);
    		}
    	}
    }
    
    public Histogram(double bins[], String labels[])
    {
    	this.bins = bins;
    	this.labels = labels;
    	if(bins != null)
    	{
    		this.cnts = new int[bins.length];
    	}
    }
    
    public int getMaxCount()
    {
        int max = -1;
        int nb = bins.length;
        for(int i=0; i<nb; i++)
        {
        	if(cnts[i]>max)
        	{
        		max = cnts[i];
        	}
        }
        
        return max;
    }
    
    public void dump()
    {
        int nb = labels.length;
        for(int i=0; i<nb; i++)
        {
        	System.out.println(labels[i]+"\t"+cnts[i]);
        }
    }
    public void dumpFull()
    {
    	int tot = 0;
    	for(int i=0; i<cnts.length; i++) tot += cnts[i];
        int nb = labels.length;
        System.out.println("# Total counts = " + tot);
        System.out.println("# Bin\tCount\tFraction\tCumulative Fraction");
        double cumul = 0;
        double tmp = 0;
        for(int i=0; i<nb; i++)
        {
        	tmp = ((double)cnts[i])/((double)tot);
        	cumul += tmp;
        	System.out.println(labels[i]+"\t"+cnts[i] + "\t" + tmp + "\t" +cumul);
        }
    }
    
    public int getCounts(double val)
    {
        int ind = -1;
        int nb = bins.length;
        while(ind < (nb-1) && val >= bins[ind+1])
		{
			ind++;
		}
		if(ind <0)ind=0;
        return cnts[ind];
    }
    
    public static Histogram createHistogram(double bins[], double vals[])
    {
    	Histogram hist = new Histogram(bins);
    	
    	double val = 0;
    	int nv = vals.length;
    	int ind = 0;
    	int nb = bins.length;
    	
    	for(int i=0; i<nv; i++)
    	{
    		ind = -1;
    		val = vals[i];
    		while(ind < (nb-1) && val >= bins[ind+1])
    		{
    			ind++;
    		}
    		if(ind <0)ind=0;
    		hist.cnts[ind]++;
    	}
    	return hist;
    }
    
    public static Histogram createHistogram(double bins[], List<Double> vals)
    {
    	Histogram hist = new Histogram(bins);
    	double val = 0;
    	int nv = vals.size();
    	int ind = 0;
    	int nb = bins.length;
    	
    	for(int i=0; i<nv; i++)
    	{
    		ind = -1;
    		val = vals.get(i);
    		while(ind < (nb-1) && val >= bins[ind+1])
    		{
    			ind++;
    		}
    		if(ind <0)ind=0;
    		hist.cnts[ind]++;
    	}    	
    	return hist;    	
    }
    
    public static Histogram createTODHistogram(List<Date> dates)
    {
    	double bins[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
    	
        Histogram hist = new Histogram(bins);
        int nb = 24;
        int size = dates.size();
        double val = 0;
        int ind = 0;
        
        GregorianCalendar gc = new GregorianCalendar();
        Date date = null;
        
        for(int i=0; i<size; i++)
        {
        	date = dates.get(i);
        	gc.setTime(date);
        	val = gc.get(Calendar.HOUR_OF_DAY);
       		ind = -1;
    		while(ind < (nb-1) && val >= bins[ind+1])
    		{
    			ind++;
    		}
    		hist.cnts[ind]++;
        }
        
        return hist;
    }
    
    public static Histogram createDOWHistogram(List<Date> dates)
    {
    	double bins[] = {0,1,2,3,4,5,6};
    	
        Histogram hist = new Histogram(bins);
        int nb = 7;
        int size = dates.size();
        double val = 0;
        int ind = 0;
        
        GregorianCalendar gc = new GregorianCalendar();
        Date date = null;
        
        for(int i=0; i<size; i++)
        {
        	date = dates.get(i);
        	gc.setTime(date);
        	val = gc.get(Calendar.DAY_OF_WEEK);
       		ind = -1;
    		while(ind < (nb-1) && val >= bins[ind+1])
    		{
    			ind++;
    		}
    		hist.cnts[ind]++;
        }
        
        return hist;
    }
	public static double parseDouble(String str)
	{
		double num = 0;
		try{num = Double.parseDouble(str.trim());}catch(Exception ex){}
		return num;
	}
    public static List<Double> readCol(String file, int col, String delim)
    {
    	System.err.println(file);

    	List<Double> fv = new ArrayList<Double>(1000);
    	FileReader fr = null;
    	try
    	{
    		fr = new FileReader(file);
    		LineNumberReader lnr = new LineNumberReader(fr);
    		String line = null;
    		line = lnr.readLine();
    		String sa[] = null;
    		while(line != null)
    		{
    			sa = line.split(delim);
    			if(sa.length > col)
    			{
    				fv.add(parseDouble(sa[col]));
    			}
    			line = lnr.readLine();
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(fr != null) try{fr.close();}catch(Exception ex){};
    	}
    	return fv;
    }

    
    public static double[] estimateBins(List<Double> vals)
    {
    	int size = vals.size();
    	int nbins = (int)Math.sqrt(size);
    	java.util.Collections.sort(vals);
    	double min = vals.get(0);
    	double max = vals.get(size-1);
    	double bins[] = new double[nbins];
    	bins[0]=min;
    	double h = (max-min)/(nbins-1);
    	for(int i=1; i<nbins; i++)
    	{
    		bins[i]=bins[i-1]+h;
    	}
    	return bins;
    }
 
}
