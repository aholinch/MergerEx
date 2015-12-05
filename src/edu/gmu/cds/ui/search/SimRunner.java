/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.Color;

import edu.gmu.cds.sim.Parameters;
import edu.gmu.cds.sim.Run;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.helper.UIHelper;

public class SimRunner implements Runnable 
{
	public static Color DEFAULT_COLOR = new Color(180,120,20,75);
	protected StateInfo info = null;
	protected ScatterPanel panel = null;
	protected boolean animate = false;
	protected boolean generateImage = false;
	protected Color color = Color.red;
	protected int numParticles = 2000;
	protected double preferredH = 0.005;
	protected static long tot = 0;
	protected static int count = 0;
	protected int particleSize = 0;
	
	public SimRunner(StateInfo si, ScatterPanel sp, boolean anim, boolean genImage)
	{
		info = si;
		panel = sp;
		animate = anim;
		generateImage = genImage;
		color = DEFAULT_COLOR;
	}
	
	public void run()
	{		
		Run run = new Run();
		
		setDefaults(run.params);
		
		info.setValuesToParameters(run.params);
		
		double tstart = -10;
		double tend = 0;

		if(animate)
		{
			panel.clear();
			panel.repaint();
		}
		
		run.params.time=tstart;
		run.params.potential_type=1;
		run.params.tstart=tstart;
		run.params.tIsSet = true;
		run.params.tend=tend;
		run.params.h=preferredH;
		run.initRun(run.params);
		System.out.println(run.params.tmin);
		System.out.println(info.rx + "\t" + info.ry);
		if(animate)
		{
			panel.addSeries(run.x0, color);
			panel.setSymbolSize(panel.getNumSeries()-1, particleSize);
			panel.repaint();
		}
		
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
            if(animate && i%20==0)panel.repaint();
        }

        if(!animate)
        {
        	// remove old data and add new series now that it is done running
        	panel.clear();
        	panel.addSeries(run.x0, color);
			panel.setSymbolSize(panel.getNumSeries()-1, particleSize);
        }
        
        // for animate and not animate
		panel.repaint();
		
		if(generateImage)
		{
			info.image = UIHelper.getImageOfComponent(panel, 512, 512);
		}
		
	}
	
	public void setNumberOfParticles(int n)
	{
		numParticles = n;
	}
	
	public void setPreferredH(double h)
	{
		preferredH = h;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
		if(color == null)
		{
			this.color = DEFAULT_COLOR;
		}
	}
	
	public void setParticleSize(int size)
	{
		particleSize = size;
	}
	
	protected void setDefaults(Parameters params)
	{
		setN(params,numParticles);
	}
	
	protected void setN(Parameters params, int totalN)
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
		params.n  = n;
		params.n1 = n1;
		params.n2 = n2;
	}
	
	public static double[] getInteractionInfo(StateInfo si)
	{
		long t1 = System.currentTimeMillis();
		Run run = new Run();
		double tstart = -10;
		double tend = 0;
		run.params.n1=2;
		run.params.n2=2;
		run.params.n = 4;
		si.setValuesToParameters(run.params);	
		run.params.time=tstart;
		run.params.potential_type=1;
		run.params.tstart=tstart;
		run.params.tend=tend;
		run.params.h=0.002;
		double mins[] = run.getMinValues(run.params);
		long t2 = System.currentTimeMillis();
		tot += (t2-t1);
		count++;
		return mins;
	}

	public static void dump()
	{
		System.out.println(tot + "\t" + count + "\t" + ((double)tot)/((double)count));
	}
}
