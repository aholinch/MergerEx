/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

import java.util.Random;

public class SimpleEvaluator implements FitnessEvaluator
{
	public Random rand = new Random();
	
	public SimpleEvaluator()
	{
		
	}
	
	
	public double f(OptEntity ent)
	{
		return f(ent.getParams());
	}
	
	public double f(double params[])
	{
		return f2(params);
	}
	
	// min is a 0.5, 0.5
	public double f1(double[] params) 
	{
		int n = 9;
		double sig = 0.15;
		
	    double val = 0.0;
	    double x = params[0];
	    double y = params[1];
	    
	    double r = Math.sqrt((x-0.5)*(x-0.5) + (y-0.5)*(y-0.5));
	    val=Math.cos(n*Math.PI*r);
	    val = val *val;
	    val = val*Math.exp(-r*r/sig);
	    
	    return val;
	}
	
	// min is at 0.395759627601425, 0.331573987886851
	public double f2(double[] params)
	{
		double x = params[0];
		double y = params[1];
		double y2 = y*y;
		double t1 = x-2.0d*x*x-y2;
		t1 = Math.exp(t1);
		double t2 = x+y+x*y2;
		t2 = Math.sin(6*t2);
		return 1-t1*t2;
	}

	@Override
	public OptEntity generateRandom() 
	{
		double vals[] = generateRandomParams();
		SimpleEntity ent = new SimpleEntity();
		ent.setParams(vals);
		return ent;
	}
	
	public double[] generateRandomParams()
	{
		double vals[]  = new double[2];
		vals[0]=rand.nextDouble();
		vals[1]=rand.nextDouble();
		return vals;
	}
	
	public OptEntity getPrototype()
	{
		return new SimpleEntity();
	}

	public static class SimpleEntity implements OptEntity
	{
		public double fitness = 0;
		public double params[] = null;
		public boolean hasFitness = false;
		
		public SimpleEntity()
		{
			params=new double[2];
		}

		@Override
		public OptEntity getNewEntity(double[] params) 
		{
			SimpleEntity ent = new SimpleEntity();
			ent.params = params;
			return ent;
		}

		@Override
		public double[] getParams() 
		{
			return params;
		}

		@Override
		public void setParams(double[] params) 
		{
			this.params = params;
		}

		@Override
		public double getFitness() 
		{
			return fitness;
		}

		@Override
		public void setFitness(double f) 
		{
			fitness = f;
			hasFitness = true;
		}

		@Override
		public boolean hasFitness() 
		{
			return hasFitness;
		}

		@Override
		public void setHasFitness(boolean flag)
		{
			hasFitness = flag;
		}
		
		public String toString()
		{
			return fitness + "\t"+params[0]+"\t"+params[1];
		}
	}
}
