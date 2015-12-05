/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

public class GA
{
    public int numGenes;
    public int ndigits[];
    public double min[];
    public double max[];
    
    // the chromosome, should be 4 * numGenes in length
    public byte vals[];
    
    public GA()
    {
    	
    }
    
    public GA(int numGenes)
    {
    	this.numGenes = numGenes;
    	min = new double[numGenes];
    	max = new double[numGenes];
    	vals = new byte[7*numGenes];
    	setNdigits(7,numGenes);
    }
    
    public void setNdigits(int nd,int numGenes)
    {
    	ndigits = new int[numGenes];
    	for(int i=0; i<numGenes; i++)
    	{
    		ndigits[i] = nd;
    	}
    }
    
    /**
     * Returns the number of bytes need to encode
     * the data before this gene.
     * 
     * @param ind
     * @return
     */
    public int getNbytes(int ind)
    {
    	int nb = 0;
    	
    	for(int i=0; i<ind; i++)
    	{
    		nb+= ndigits[i];
    	}
    	
    	return nb;
    }
    
    /**
     * Gets the divisor for that number of digits.
     * 
     * @param nd
     * @return
     */
    public int getDivisor(int nd)
    {
    	int div = 10;
    	
    	for(int i=1; i<nd; i++)
    	{
    		div*=10;
    	}
    	
    	return div;
    }
    
    public byte[] encodeEntity(OptEntity ent)
    {
    	double p[] = ent.getParams();
    	return encodeParams(ent.getParams());
    }
    
    public OptEntity decodeEntity(byte vals[], OptEntity proto)
    {
    	double fvals[] = decodeParams(vals);
    	return proto.getNewEntity(fvals);
    }
    
    public byte[] encodeParams(double fvals[])
    {
    	byte params[] = new byte[getNbytes(numGenes)];
    	byte ba[] = null;
    	
    	for(int i=0; i<numGenes; i++)
    	{
    	    ba = encodeNum(fvals[i],min[i],max[i],ndigits[i]);
    	    System.arraycopy(ba,0,params,getNbytes(i),ndigits[i]);
    	}
    	return params;
    }
    
    public byte[] encodeNum(double val, double min, double max, int nd)
    {
    	int i = 0;
    	byte ba[] = new byte[nd];
    	
        double rng = max-min;
        val = (val-min)/rng;
        
        if(val >= 1.0)
        {
        	for(i=0; i<nd; i++)
        	{
        		ba[i] = 9;
        	}
        	
        	return ba;
        }
        else if (val <= 0)
        {
        	for(i=0; i<nd; i++)
        	{
        		ba[i] = 0;
        	}
        	
        	return ba;
        }
        
        int div = getDivisor(nd);
        
        val *= div;
        int ival = 0;
        
        for(i=0; i<nd; i++)
        {
        	div/= 10;
        	ival = (int)(val/(double)div);
        	ba[i] = (byte)ival;
        	val -= ival*div;
        }
        /**
        int ival = (int)(10000*(val));
        
        int i1 = (int)(ival/10000);
        
        if(i1 == 1)
        {
        	ba[0] = 9;
        	ba[1] = 9;
        	ba[2] = 9;
        	ba[3] = 9;
        	return ba;
        }
        
        int i2 = (int)(ival/1000);
        ival -= i2*1000;
        int i3 = (int)(ival/100);
        ival -= i3*100;
        int i4 = (int)(ival/10);
        ival -= i4*10;
        ba[0] = (byte)i2;
        ba[1] = (byte)i3;
        ba[2] = (byte)i4;
        ba[3] = (byte)ival;
        **/
        return ba;
    }
    
    public double[] decodeParams(byte params[])
    {
    	double fvals[] = new double[numGenes];
    	byte ba[] = null;
    	
    	for(int i=0; i<numGenes; i++)
    	{
    		ba = new byte[ndigits[i]];
    	    System.arraycopy(params,getNbytes(i),ba,0,ndigits[i]);
    	    fvals[i] = decodeNum(ba,min[i],max[i],ndigits[i]);
    	}
    	
    	return fvals;
    }

    public double decodeNum(byte val[], double min, double max, int nd)
    {
    	double num = 0;
    	
        double rng = max-min;
        
        int div = 1;
        
        for(int i=nd; i>0; i--)
        {
        	num += div*val[i-1];
        	div *= 10;
        }
        num /= (double)div;
        /*
        num = 1000*val[0] + 100*val[1] + 10*val[2] + 1*val[3];
        num /= 10000.0;
        */
        num *= rng;
        num += min;
        
        return num;    
    }
    
    public String dumpVals(byte params[])
    {
        double vals[] = decodeParams(params);
        String str = "";
        for(int i=0; i<vals.length; i++)
        {
        	str += vals[i];
        	str += "\t";
        }
        
        return str;
    }
    
    public String dumpParams(double params[])
    {
        String str = "";
        
        if(params != null)
        {
	        for(int i=0; i<params.length; i++)
	        {
	        	str += params[i];
	        	str += "\t";
	        }
        }
        return str;    	
    }
}
