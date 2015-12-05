/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.awt.image.BufferedImage;

public class StateInfo
{
	public static final int TOURNAMENT = 1;
	public static final int SORT       = 2;
	
	public boolean isSaved = false;
	public double params[] = null;
	public double orbParams[] = null;
	public boolean isSelected = false;
	public int generation;
	public double beta;
	public double rmin;
	
	public double impulse;
	public double tmin;
	
	public double rx;
	public double ry;
	public double rz;
	public double vx;
	public double vy;
	public double vz;
	public double m1;
	public double m2;
	public double r1;
	public double r2;
	public double phi1;
	public double phi2;
	public double theta1;
	public double theta2;
	public double sl1;
	public double sl2;
	public double sp1a;
	public double sp2a;
	public double sp3a;
	public double sp1b;
	public double sp2b;
	public double sp3b;
	public double score;
	public int order;
	public int loc;
	
	public int random = 0;
	public int enhanced = 0;
	public int evaluated = 0;
	public int evolved = 0;
	public int level = 0;
	
	// not to be persisted
	public String sessionId = null;
	public double tmpScore = 0.0;
	public int numComps = 0;
	public int numWins = 0;
	public double fitness = 0;
	
	public int exploreStatus = 0;
	
	public BufferedImage image = null;
	public int fileOrder = 0;  // not usually persisted
	public boolean enhancedYet = false; // not persisted
	
	public StateInfo()
	{
		setDefaults();
	}
	
	public StateInfo(String str)
	{
		setDefaults();
		setFromString(str);
	}
	
	public void setDefaults()
	{
		sl1=0.3;
		sl2=0.3;
		sp1a=3;
		sp2a=3;
		sp3a=3;
		sp1b=3;
		sp2b=3;
		sp3b=3;
	}
	
	public void setValuesFromParameters(Parameters params)
	{
		rx = params.sec_vec[0];
		ry = params.sec_vec[1];
		rz = params.sec_vec[2];
		vx = params.sec_vec[3];
		vy = params.sec_vec[4];
		vz = params.sec_vec[5];
		m1 = params.mass1;
		m2 = params.mass2;
		r1 = params.rout1;
		r2 = params.rout2;
		phi1 = params.phi1;
		phi2 = params.phi2;
		theta1 = params.theta1;
		theta2 = params.theta2;
		sl1 = params.epsilon1;
		sl2 = params.epsilon2;
		sp1a = params.rscale1[0];
		sp2a = params.rscale1[1];
		sp3a = params.rscale1[2];
		sp1b = params.rscale2[0];
		sp2b = params.rscale2[1];
		sp3b = params.rscale2[2];
	}
	
	public void setValuesToParameters(Parameters params)
	{
		params.use_sec_vec = true;
		params.sec_vec[0] = rx;
		params.sec_vec[1] = ry;
		params.sec_vec[2] = rz;
		params.sec_vec[3] = vx;
		params.sec_vec[4] = vy;
		params.sec_vec[5] = vz;
		params.mass1 = m1;
		params.mass2 = m2;
		params.rout1 = r1;
		params.rout2 = r2;
		params.phi1 = phi1;
		params.phi2 = phi2;
		params.theta1 = theta1;
		params.theta2 = theta2;
		params.epsilon1 = sl1;
	    params.epsilon2 = sl2;
		params.rscale1[0] = sp1a;
		params.rscale1[1] = sp2a;
		params.rscale1[2] = sp3a;
		params.rscale2[0] = sp1b;
		params.rscale2[1] = sp2b;
		params.rscale2[2] = sp3b;		
	}
	
	public void setFitnessFromString(String str)
	{
		String sa[] = str.split(",");
		if(sa.length > 1)
		{
			this.sessionId = sa[0];
			this.fitness = parseDouble(sa[1]);
			if(sa.length>3)
			{
				this.numWins = (int)parseDouble(sa[2]);
				this.numComps = (int)parseDouble(sa[3]);
			}
		}
	}
	
	public void setFromString(String str)
	{
		if(str.contains("\t"))
		{
			String s[] = str.split("\t");
			setFitnessFromString(s[0]);
			setFromString(s[1]);
			return;
		}
		String sa[] = str.split(",");
		
		rx = parseDouble(sa[0]);
		ry = parseDouble(sa[1]);
		rz = parseDouble(sa[2]);
		vx = parseDouble(sa[3]);
		vy = parseDouble(sa[4]);
		vz = parseDouble(sa[5]);
		m1 = parseDouble(sa[6]);
		m2 = parseDouble(sa[7]);
		r1 = parseDouble(sa[8]);
		r2 = parseDouble(sa[9]);
		phi1 = parseDouble(sa[10]);
		phi2 = parseDouble(sa[11]);
		theta1 = parseDouble(sa[12]);
		theta2 = parseDouble(sa[13]);
		if(sa.length >= 22)
		{
		sl1 = parseDouble(sa[14]);
		sl2 = parseDouble(sa[15]);
		sp1a = parseDouble(sa[16]);
		sp2a = parseDouble(sa[17]);
		sp3a = parseDouble(sa[18]);
		sp1b = parseDouble(sa[19]);
		sp2b = parseDouble(sa[20]);
		sp3b = parseDouble(sa[21]);
		}
		if(sa.length >= 28)
		{
			score = parseDouble(sa[22]);
			order = (int)parseDouble(sa[23]);
			loc = (int)parseDouble(sa[24]);
			impulse = parseDouble(sa[25]);
			tmin = parseDouble(sa[26]);
			rmin = parseDouble(sa[27]);
			
		    if(sa.length >= 34)
		    {
				random  = (int)parseDouble(sa[28]);
				enhanced = (int)parseDouble(sa[29]);
				evaluated = (int)parseDouble(sa[30]);
				level = (int)parseDouble(sa[31]);
				evolved = (int)parseDouble(sa[32]);
				generation = (int)parseDouble(sa[33]);
		    }
		}
		
		params = new double[14];
		params[0] = rx;
		params[1] = ry;
		params[2] = rz;
		params[3] = vx;
		params[4] = vy;
		params[5] = vz;		
		params[6] = m1;
		params[7] = m2;
		params[8] = r1;
		params[9] = r2;
		params[10] = phi1;
		params[11] = phi2;
		params[12] = theta1;
		params[13] = theta2;
	}
	
	public void setImage(BufferedImage img)
	{
		this.image = img;
	}

	public BufferedImage getImage()
	{
		return image;
	}
	
	public double parseDouble(String str)
	{
		double num = 0;
		try{num = Double.parseDouble(str);}catch(Exception ex){};
		return num;
	}
	
	public double[] getParamsCopy()
	{
		double p[] = null;
		
		if(params != null)
		{
			int len = params.length;
			p = new double[len];
			for(int i=0; i<len; i++)
			{
				p[i] = params[i];
			}
		}
		return p;
	}
	
    public void setParams(double p[])
	{
    	if(p == null)
    	{
    		return;
    	}
    	
    	int len = p.length;
		this.params = new double[len];
		for(int i=0;i<len;i++)
		{
			params[i]=p[i];
		}

		rx = params[0];
		ry = params[1];
		rz = params[2];
		vx = params[3];
		vy = params[4];
		vz = params[5];		
		m1 = params[6];
		m2 = params[7];
		r1 = params[8];
		r2 = params[9];
		phi1 = params[10];
		phi2 = params[11];
		theta1 = params[12];
		theta2 = params[13];
	}

    public String getShortString()
    {
    	StringBuffer sb = new StringBuffer(1000);
    	
		sb.append(rx);
		sb.append(",");
		sb.append(ry);
		sb.append(",");
		sb.append(rz);
		sb.append(",");
		sb.append(vx);
		sb.append(",");
		sb.append(vy);
		sb.append(",");
		sb.append(vz);
		sb.append(",");
		sb.append(m1);
		sb.append(",");
		sb.append(m2);

		return sb.toString();
    }
    
    public String formatDouble(double val)
    {
    	// TODO consider improving this ????
    	String str = String.valueOf(val);
    	int ind = str.indexOf('.');
    	if(ind > -1)
    	{
    		int len = str.length();
    		if((len-ind)>6)
    		{
    			str = str.substring(0,ind+6);
    		}
    	}
    	
    	if(Math.abs(val)<1e-6)
    	{
    		str = "0.0";
    	}
    	
    	return str;
    }
    
    public boolean isSelected()
    {
    	return isSelected;
    }
    
    public void setIsSelected(boolean flag)
    {
    	isSelected = flag;
    }
    
    public String toLongString()
    {
    	StringBuilder sb = new StringBuilder(1000);
    	sb.append(sessionId).append(",").append(fitness).append(",").append(numWins).append(",");
    	sb.append(numComps).append("\t").append(toString());
    	return sb.toString();
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer(1000);
    	
		sb.append(formatDouble(rx));
		sb.append(",");
		sb.append(formatDouble(ry));
		sb.append(",");
		sb.append(formatDouble(rz));
		sb.append(",");
		sb.append(formatDouble(vx));
		sb.append(",");
		sb.append(formatDouble(vy));
		sb.append(",");
		sb.append(formatDouble(vz));
		sb.append(",");
		sb.append(formatDouble(m1));
		sb.append(",");
		sb.append(formatDouble(m2));
		sb.append(",");
		sb.append(formatDouble(r1));
		sb.append(",");
		sb.append(formatDouble(r2));
		sb.append(",");
		sb.append(formatDouble(phi1));
		sb.append(",");
		sb.append(formatDouble(phi2));
		sb.append(",");
		sb.append(formatDouble(theta1));
		sb.append(",");
		sb.append(formatDouble(theta2));
		sb.append(",");
		sb.append(formatDouble(sl1));
		sb.append(",");
		sb.append(formatDouble(sl2));
		sb.append(",");
		sb.append(formatDouble(sp1a));
		sb.append(",");
		sb.append(formatDouble(sp2a));
		sb.append(",");
		sb.append(formatDouble(sp3a));
		sb.append(",");
		sb.append(formatDouble(sp1b));
		sb.append(",");
		sb.append(formatDouble(sp2b));
		sb.append(",");
		sb.append(formatDouble(sp3b));
		sb.append(",");
		sb.append(formatDouble(score));
		sb.append(",");
		sb.append(String.valueOf(order));
		sb.append(",");
		sb.append(String.valueOf(loc));
		sb.append(",");
		sb.append(formatDouble(impulse));
		sb.append(",");
		sb.append(formatDouble(tmin));
		sb.append(",");
		sb.append(formatDouble(rmin));
		sb.append(",");
		sb.append(formatDouble(random));
		sb.append(",");
		sb.append(formatDouble(enhanced));
		sb.append(",");
		sb.append(formatDouble(evaluated));
		sb.append(",");
		sb.append(formatDouble(level));
		sb.append(",");
		sb.append(formatDouble(evolved));
		sb.append(",");
		sb.append(formatDouble(generation));
		
    	return sb.toString();
    }
}
