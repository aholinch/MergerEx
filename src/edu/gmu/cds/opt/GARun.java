/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GARun implements FitnessEvaluator
{
	protected GA ga = null;
	protected FitnessEvaluator fitnessEvaluator = null;
	protected int popSize;
	protected double avgFitness;
	protected List<OptEntity> pop;
	protected Random rand = new Random();
	protected double pmut = 0.005;
	protected boolean bDoFitness = true;
	protected int numThreads = 0;
	protected int numExtraParams = 3;
	
	public GARun(int popSize)
	{
		this.popSize = popSize;
		pop = new ArrayList<OptEntity>(popSize);
	}
	
	public GARun(int popSize, int numThreads)
	{
		this(popSize);
		setNumThreads(numThreads);
	}
	
	public void setDoFitness(boolean flag)
	{
		bDoFitness = flag;
	}
	
	public List<OptEntity> getPop()
	{
		return pop;
	}
		
	public int getPopSize()
	{
		return popSize;
	}
	
	public void updatePopSize()
	{
		popSize = 0;
		if(pop != null)
		{
			popSize = pop.size();
		}
	}
	
	
	/**
	 * Returns the population subset starting at i1 and 
	 * goes upto, but not including, i2;
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public List<OptEntity> getSubPop(int i1, int i2)
	{
		List<OptEntity> subPop = new ArrayList<OptEntity>();
		for(int i=i1;i<i2;i++)
		{
			subPop.add(pop.get(i));
		}
		
		return subPop;
	}

	
	public void setNumThreads(int num)
	{
		if(num < 2)
		{
			num = 0;
		}
		
		numThreads = num;
	}
	
	public void init(GA ga, FitnessEvaluator eval)
	{
		this.ga = ga;
		this.fitnessEvaluator = eval;
		pop.clear();
		
		OptEntity ent = null;
		for(int i=0; i<popSize; i++)
		{
	        ent = generateRandom();
		    pop.add(ent);	
		}
	}
	
	public void init(GA ga, FitnessEvaluator eval, List<OptEntity> population)
	{
		this.ga = ga;
		this.fitnessEvaluator = eval;
		popSize = population.size();
		
		pop.clear();
		pop.addAll(population);
	}
	
	/**
	 * Preserve population members
	 */
	public void resetPopulationSize(int newSize)
	{
		List<OptEntity> newPop = new ArrayList<OptEntity>(newSize);
		newPop.addAll(pop.subList(0, newSize));
		pop = newPop;
		popSize = newSize;
	}
	
	public void updateFitness()
	{
        updateFitness(false);
    }

	public void updateFitness(boolean rebuildZero)
	{
		OptEntity ent = null;
		
		double sum = 0;
        double tmpf = 0.0d;
		
		if(bDoFitness)
		{
			double numPer = ((double)popSize)/((double)numThreads);
			if(numThreads > 1 && numPer >= 1.0d)
			{
				threadedUpdateFitness(rebuildZero);
			}
			else
			{
				// do serial version
				for(int i=0; i<popSize; i++)
				{
					ent = pop.get(i);
					tmpf = f(ent);
	                while(tmpf == 0 && rebuildZero)
	                {
	                    ent = generateRandom();
		                pop.set(i,ent);
		                tmpf = f(ent);
	                }
	                ent.setFitness(tmpf);
				}
			}
		}
		
		// sort by fitness
		OptSorter sorter = new OptSorter();
		java.util.Collections.sort(pop, sorter);

		// get average fitness
		sum = 0.0d;
		for(int i=0; i<popSize; i++)
		{
			sum += pop.get(i).getFitness();
		}
		avgFitness = sum/popSize;
		System.out.println("avg fitness = " + avgFitness + " for " + popSize);
	}
	
	/**
	 * Split the population over a number of threads to speed
	 * things up.
	 * 
	 * @param rebuildZero
	 */
	protected void threadedUpdateFitness(boolean rebuildZero)
	{
		int numPer = (int)(((double)popSize)/((double)numThreads));
System.out.println("threading\t" + numThreads + "\t" + numPer);
        int min = -numPer;
        int max = 0;
        
        FitnessRunner runners[] = new FitnessRunner[numThreads];
        FitnessRunner fr = null;
        Thread thread = null;
        
        // loop over num threads, create runners and spawn them
        for(int t = 0; t < numThreads; t++)
        {
        	min+=numPer;
        	max+=numPer;
        	
        	// ensure we have proper coverage of all members
        	if(t == (numThreads - 1))
        	{
        		max = popSize;
        	}
        	
        	fr = new FitnessRunner(this,min,max);
        	runners[t] = fr;
        	
        	thread = new Thread(fr);
        	fr.owningThread = thread;
        	thread.start();
        }
        
        // wait for them all to finish
        int numRunning = numThreads;
        while(numRunning > 0)
        {
        	numRunning = 0;
        	for(int t=0; t<numThreads; t++)
        	{
        		if(!runners[t].isDone)
        		{
        			numRunning++;
        		}
        	}
        } // end while
	}
	
	public GARun getGARun(int popSize)
	{
		GARun gar = new GARun(popSize,this.numThreads);
		return gar;
	}
	
	/**
	 * Sub-classes can construct a new GA instance if they want.
	 * @return
	 */
	public GA getGA()
	{
		return ga;
	}
	
	public FitnessEvaluator getFitnessEvaluator()
	{
		return fitnessEvaluator;
	}
		
	public void genNewPop()
	{
		List<OptEntity> newPop = getNewPopByCrossover();
		// only add new values if they have better fitness, replace worst
		//GARun gaTmp = new GARun(popSize);
		GARun gaTmp = getGARun(popSize);
		gaTmp.pop = newPop;
		gaTmp.ga = ga;
		gaTmp.fitnessEvaluator = fitnessEvaluator;
		
		//System.out.println("\n\ngaTmp values");
		gaTmp.updateFitness();
		
		combinePopulations(gaTmp);
	}
	
	/**
	 * Take the current population and apply crossover and
	 * mutation to get new offspring.
	 * 
	 * @return
	 */
	public List<OptEntity> getNewPopByCrossover()
	{
		List<OptEntity> newPop = new ArrayList<OptEntity>(popSize);

		byte n1[], n2[];
		int gsize = ga.getNbytes(ga.numGenes);
		
		OptEntity proto = fitnessEvaluator.getPrototype();
		while(newPop.size() < popSize)
		{
			n1 = new byte[gsize];
			n2 = new byte[gsize];
            
			crossover(n1,n2,gsize);
            
			// put new values in population
			newPop.add(ga.decodeEntity(n1, proto));
			newPop.add(ga.decodeEntity(n2, proto));
		}
		
		return newPop;
	}	
	
	
	/**
	 * Make two new population members.
	 * 
	 * @param n1
	 * @param n2
	 * @param gsize
	 */
	public void crossover(byte n1[], byte n2[], int gsize)
	{
		double tot = 0.5*(popSize+1)*(popSize);
		int i = 0;
        int i1 = 0;
		byte ba1[], ba2[]; 
		
		// randomly select two parents based upon rank
		i = randomRank(tot);
		ba1 = ga.encodeEntity(pop.get(i));

        i1 = i;
		i = randomRank(tot);
        while(i == i1)
        { 
		    i = randomRank(tot);
        }
		ba2 = ga.encodeEntity(pop.get(i));
		
		// do crossover
		System.arraycopy(ba1,0,n1,0,gsize);
		System.arraycopy(ba2,0,n2,0,gsize);
		
		// pick crossover point
		if(rand.nextDouble() < 0.85)
		{
			// orignal crossover mechanism
			/*
			 i = rand.nextInt(gsize);
			 System.arraycopy(ba1,i,n2,i,gsize-i);
			 System.arraycopy(ba2,i,n1,i,gsize-i);
			 */
			
			// jw's suggested multiple crossovers
			int ng = ga.numGenes-1;
			int nb1 = 0;
			int nb2 = 0;
			for(int g=0; g<ng; g++)
			{
				if(rand.nextDouble() < 0.3)
				{
					nb1 = ga.getNbytes(g);
					nb2 = ga.getNbytes(g);
					System.arraycopy(ba1,nb1,n2,nb1,nb2-nb1);
					System.arraycopy(ba2,nb1,n1,nb1,nb2-nb1);
				}
			}
		}
		
		// do mutation
		for(int j=0; j<n1.length; j++)
		{
			if(rand.nextDouble() < pmut)
			{
				n1[j] = (byte)rand.nextInt(10);
			}
		}
		for(int j=0; j<n2.length; j++)
		{
			if(rand.nextDouble() < pmut)
			{
				n2[j] = (byte)rand.nextInt(10);
			}
		}
	}
		
	/**
	 * Sort the population in this GARun as well as within
	 * gaTmp.  Only keep the popSize best individuals.
	 * 
	 * @param gaTmp
	 */
	public void combinePopulations(GARun gaTmp)
	{
		int i=0;
		double fn, fo;
		
		for(i=0; i<popSize; i++)
		{
		    fn = gaTmp.pop.get(i).getFitness();
		    fo = pop.get(popSize-i-1).getFitness();
		    if(fo>fn)
		    {
		    	break;
		    }
		    else
		    {
		    	pop.set(popSize-i-1,gaTmp.pop.get(i));
		    }
		}		
	}

	/**
	 * Adjust the mutation rate based upon distribution of 
	 * fitness values in the population.
	 */
	public void adjustMutation()
	{
		double fmax = pop.get(0).getFitness();
		double fmed = pop.get(popSize/2).getFitness();
		double rdif = (fmax-fmed)/(fmax+fmed);
		
		if(rdif <= 0.05)
		{
			pmut = Math.min(1.5*pmut,0.25);
		}
		else if(rdif >= 0.25)
		{
			pmut = Math.max(0.005,pmut/1.5);
		}
	}
	
	public int randomRank(double tot)
	{
/*
	    int i = 0;
	    int itot = 0;
	    
	    double num = tot*rand.nextDouble();
	    
	    itot = popSize;
	    
	    while(itot < num)
	    {
	    	i++;
	    	itot+= (popSize - i);
	    }
*/
		int i = rand.nextInt(popSize);
		
	    return i;
	}
	
	public OptEntity generateRandom()
	{
		return fitnessEvaluator.generateRandom();
	}
	
	public OptEntity getPrototype()
	{
		return fitnessEvaluator.getPrototype();
	}
	
	/**
	 * The fitness function.
	 * 
	 * @param params
	 * @return
	 */
	public double f(OptEntity ent)
	{
		return fitnessEvaluator.f(ent);
	}
	
	/**
	 * Helper class that implements Runnable to enable threaded
	 * fitness calculations.
	 * 
	 * @author aholinch
	 *
	 */
	public static class FitnessRunner implements Runnable
	{
		protected Thread owningThread = null;
		protected GARun gar = null;
		protected int min;
		protected int max;
		protected boolean rebuildZero = false;
		protected boolean isDone = false;
		
		/**
		 * Runs from popMember = min to popMember < max
		 * 
		 * @param ga
		 * @param min
		 * @param max
		 */
		public FitnessRunner(GARun gar, int min, int max)
		{
		    this.gar = gar;
		    this.min = min;
		    this.max = max;
		}
		
		/**
		 * Runs from popMember = min to popMember < max.
		 * Assumes GARun.f() is thread safe.
		 */
		public void run()
		{
			OptEntity ent = null;
	        double tmpf = 0.0d;
	        
	        try
	        {
				for(int i=min; i<max; i++)
				{
					ent = gar.pop.get(i);
				    tmpf = gar.f(ent);
		            
				    while(tmpf == 0 && rebuildZero)
		            {
		                ent = gar.generateRandom();
		                gar.pop.set(i,ent);	
				        tmpf = gar.f(ent);
		            }
				    ent.setFitness(tmpf);
				}
	        }
	        finally
	        {
	        	isDone = true;
	        }
		}
	} // end FitnessRunner class
	
}
