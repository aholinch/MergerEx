/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim.opt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.gmu.cds.img.ContourFinder;
import edu.gmu.cds.img.ContourPoint;
import edu.gmu.cds.img.HuAnalysis;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.img.ZernikeMoment;
import edu.gmu.cds.opt.FitnessEvaluator;
import edu.gmu.cds.opt.GA;
import edu.gmu.cds.opt.GARun;
import edu.gmu.cds.opt.OptEntity;
import edu.gmu.cds.sim.StateGenerator;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.sim.StateInfoFile;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.search.SimRunner;
import edu.gmu.cds.util.SimRunUtil;

public class SimEvaluator implements FitnessEvaluator 
{
    public SimOptEntity proto = null;
    
    public StateGenerator generator = null;
    public TargetData targetData = null;
    public double scale[] = null;
    
    public BufferedImage targetImage = null;
    public List<ZernikeMoment> targetMoments = null;
    
    public SimRunUtil simRunUtil = null;
    public GA ga = null;
    public int numMoments = 5;
    public SimEvaluator()
    {
    	
    }
    
    public SimEvaluator(String tgtFile)
    {
    	this(tgtFile,5);
    }
    
    public SimEvaluator(String tgtFile, int nm)
    {
    	this(TargetData.loadFromFile(tgtFile),nm);
    }
    
    public SimEvaluator(TargetData data, int nm)
    {
    	numMoments = nm;
    	initFromTargetData(data);
    }
    
    public void initFromTargetData(TargetData data)
    {
    	proto = new SimOptEntity();
    	targetData = data;
		generator = new StateGenerator(data);
		targetImage = ImageProcessor.readImage(data.getTargetImageFile());	
		System.out.println("Calculating targetMoments");
		targetMoments = getTargetMoments(targetImage);
		scale = targetData.calculateScale();
		simRunUtil = new SimRunUtil();
		
		double mins[] = new double[14];
		System.arraycopy(targetData.getMinimums(),0,mins,0,10);
		double maxs[] = new double[14];
		System.out.println("len max " + targetData.getMaximums().length);
		System.arraycopy(targetData.getMaximums(),0,maxs,0,10);
		
		for(int i=10;i<14;i++)
		{
			mins[i]=0;
			maxs[i]=360.0d;
		}
		
    	StateInfo info = generator.randStateInfo();
		
		mins[0]=info.rx;
		maxs[0]=info.rx;
		mins[1]=info.ry;
		maxs[1]=info.ry;
		
		ga = new GA(14);
		ga.min = mins;
		ga.max = maxs;
    }
    
	@Override
	public double f(OptEntity ent) 
	{
		SimOptEntity sim = (SimOptEntity)ent;
		BufferedImage simImage = genSimImage(sim.info);

		sim.bi = simImage;
		List<ZernikeMoment> simMoments = getMoments(simImage,false);
		double fitness = 1.0d - dist(targetMoments,simMoments);
		//System.out.println(fitness + "\t" + sim.info.toString());
		return fitness;
	}
	
	public BufferedImage genSimImage(StateInfo info)
	{
		// run the simulation
		double parts[][] = simRunUtil.getParticlePositions(info);

		// buffered image
		BufferedImage bi = null;
		bi = simRunUtil.generateImage(parts, 256, 256, scale[0],scale[1],scale[2],scale[3]);

		return bi;
	}
	
	/*
	public List<ZernikeMoment> getTargetMoments(BufferedImage bi)
	{
		return ZernikeMoment.calculateMoments(bi, numMoments,false);
	}

    public List<ZernikeMoment> getMoments(BufferedImage bi)
	{
		return ZernikeMoment.calculateMoments(bi, numMoments,false);
	}
    */
	
	public List<ZernikeMoment> getTargetMoments(BufferedImage bi)
	{
	    ImageProcessor.threshold(bi, 10, false);
	    int rgb = Color.black.getRGB();
	    rgb = bi.getRGB(2, 2);
	    List<List<ContourPoint>> pnts = ContourFinder.findContours(bi, rgb);
		List<List<ContourPoint>> tgtContours = HuAnalysis.findUsefulContours(pnts,bi.getWidth(),bi.getHeight(),0,0,0,0);
	    return ZernikeMoment.calculateMoments(tgtContours, numMoments, bi.getWidth(), bi.getHeight(), false);
	}

	public List<ZernikeMoment> getMoments(BufferedImage bi, boolean writeContours)
	{
		// Compute Contours
		float n = (float)(3.0d/9.0d);
	    float sharpen[] = new float[]{
	    		n,n,n,
	    		n,n,n,
	    		n,n,n
	    };
	    
	    Kernel kernel = new Kernel(3, 3, sharpen);

	    ConvolveOp op = new ConvolveOp(kernel);
	    bi = ImageProcessor.convertToGray(bi);
	    
	    bi = op.filter(bi, null);
		bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    
	    
	    ImageProcessor.threshold(bi, 100, false);

	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);
	    bi = ImageProcessor.erode(bi);

	    bi = ImageProcessor.dilate(bi);
	    bi = ImageProcessor.dilate(bi);
	    bi = ImageProcessor.dilate(bi);

	    ImageProcessor.threshold(bi, 100, false);


		List<List<ContourPoint>> pnts = ContourFinder.findContoursBinary(bi);

		List<List<ContourPoint>> contours = HuAnalysis.findUsefulContours(pnts,bi.getWidth(),bi.getHeight(),0,0,0,0);
		if(writeContours)
		{
			bi = ContourFinder.paintContours(bi, contours, Color.green);
			ImageProcessor.writeImage(bi, "cnt.png");
		}
		return ZernikeMoment.calculateMoments(contours, numMoments, bi.getWidth(), bi.getHeight(), false);
	}

	public double dist(List<ZernikeMoment> mts1, List<ZernikeMoment> mts2)
	{
		double dist = 0;
		double tmp = 0;
		
		// dangerous but hey, why not?
		// assume moments in same order
		// just diff magnitudes for now
		int size = Math.min(mts1.size(), mts2.size());
		ZernikeMoment mt1 = null;
		ZernikeMoment mt2 = null;
		double dist2 = 0;
		for(int i=0; i<size; i++)
		{
			mt1 = mts1.get(i);
			mt2 = mts2.get(i);
			tmp = mt1.mag() - mt2.mag();
			dist += tmp*tmp;
			tmp = mt1.re - mt2.re;
			dist2 += tmp*tmp;
			tmp = mt1.im - mt2.im;
			dist2 += tmp*tmp;
		}
		dist = Math.sqrt(dist);
		dist2 = Math.sqrt(dist2);
		dist = dist + 0.1*dist2;
		return dist;
	}
	
	
	@Override
	public OptEntity generateRandom() 
	{
		StateInfo info = generator.randStateInfo();
		double mins[] = SimRunner.getInteractionInfo(info);
		while(mins[3]<0.5)
		{
			info = generator.randStateInfo();
			mins = SimRunner.getInteractionInfo(info);	
		}
		
		SimOptEntity ent = new SimOptEntity(info);
		return ent;
	}

	@Override
	public OptEntity getPrototype() 
	{
		return proto;
	}

	public static int gi(String str)
	{
		int n = 0;
		try{n=Integer.parseInt(str.trim());}catch(Exception ex){};
		return n;
	}
	
	public static List<OptEntity> genRandomFromFile(String file, int size)
	{
		List<StateInfo> infos = StateInfoFile.readSimple(file);
		int sz = infos.size();
		if(sz <size)
		{
			size = sz;
		}
		List<OptEntity> ents = new ArrayList<OptEntity>(size);
		Random rand = new Random();
		int ind = 0;
		StateInfo si = null;
		SimOptEntity ent = null;
		for(int i=0; i<size; i++)
		{
			ind = rand.nextInt(size);
			while(infos.get(ind)==null)
			{
				ind = rand.nextInt(size);
			}
			si = infos.get(ind);
			//System.out.println(si.fitness);
			infos.set(ind, null);
			ent = new SimOptEntity();
			ent.info = si;
			ents.add(ent);
		}
		
		return ents;
	}
	
}
