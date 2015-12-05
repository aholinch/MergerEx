/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.search.SimRunner;
import edu.gmu.cds.util.FileUtil;

public class SimImageGenerator 
{
	public static Color DEFAULT_COLOR = new Color(255,255,255,75);
	
	public static StateInfo genRandom(TargetData td)
	{
		StateGenerator sg = new StateGenerator(td);
		return sg.randStateInfo();
	}
	
    public static ConvolveOp getGaussianBlurFilter()
    {
        float k[] = {1,4,7,4,1,
                             4,16,26,16,4,
                             7,26,41,26,7,
                             4,16,26,16,4,
                             1,4,7,4,1};

        for(int i=0;i<k.length;i++)
        {
                k[i] = (float)(k[i]/273.0);
        }

        Kernel kernel = new Kernel(5,5,k);

        ConvolveOp co = new ConvolveOp(kernel);

        return co;
    }

	
	public static BufferedImage genImage(TargetData td, StateInfo si, int size, int np, int ps)
	{
		BufferedImage bi = new BufferedImage(size,size,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = bi.getGraphics();
		ScatterPanel sp = new ScatterPanel();
		double scale[] = td.calculateScale();
		sp.setDataRange(scale[0], scale[1], scale[2], scale[3]);
		
		SimRunner sr = new SimRunner(si,sp,false,false);
		
		sr.setNumberOfParticles(np);
		sr.setColor(DEFAULT_COLOR);
		sr.setParticleSize(ps);
		
		sr.run();
		
		sp.paintComponent(g, size, size);
		
		return bi;
	}
	
	public static int gi(String str)
	{
		int num = 0;
		try{num = Integer.parseInt(str.trim());}catch(Exception ex){};
		return num;
	}
	
	public static void usage()
	{
		System.out.println("\n\n\n\tMust specify target file with -t");
		System.out.println("\tSimulation is specified with -s or -r to generate new random");
		System.out.println("\tUse -name to specify name of state");
		System.out.println("\tUse -f to specify image type (jpg,png,tiff)");
		System.out.println("\tUse -w to specify image width in pixels");
		System.out.println("\tUse optional -np to specify number of particles");
		
		System.out.println("\n\n");
	}
	
    public static void main(String args[])
    {
    	String targetFile = null;
    	String stateStr = null;
    	String name = null;
    	String format = "png";
    	int np = 10000;
    	int width = 256;
    	int ps = 2;
    	boolean isRand = false;
    	
    	int i=0;
    	for(i=0; i<args.length; i++)
    	{
    	    String arg = args[i].toLowerCase();
    	    if(arg.equals("-t") || arg.equals("-tgt"))
    	    {
    	    	i++;
    	    	targetFile = args[i];
    	    }
    	    else if(arg.equals("-s"))
    	    {
    	        i++;
    	        stateStr = args[i];
    	    }
    	    else if(arg.equals("-name"))
    	    {
    	        i++;
    	        name = args[i];
    	    }
    	    else if(arg.equals("-f"))
    	    {
    	        i++;
    	        format = args[i];
    	    }
    	    else if(arg.equals("-np"))
    	    {
    	    	i++;
    	    	np = gi(args[i]);
    	    }
    	    else if(arg.equals("-ps"))
    	    {
    	    	i++;
    	    	ps = gi(args[i]);
    	    }
    	    else if(arg.equals("-w"))
    	    {
    	    	i++;
    	    	width = gi(args[i]);
    	    }    	    
    	    else if(arg.equals("-r") || arg.equals("-rand"))
    	    {
    	    	isRand = true;
    	    }
    	    else if(arg.equals("-h") || arg.equals("-help"))
    	    {
    	    	usage();
    	    	System.exit(0);
    	    }
    	}
    	
    	/*
    	System.out.println("tf="+targetFile);
    	System.out.println("np="+np);
    	System.out.println("s="+stateStr);
    	System.out.println("rand="+isRand);
    	System.out.println("name="+name);
    	System.out.println("format="+format);
    	*/
    	
    	try
    	{
	    	// load target data
	    	TargetData td = TargetData.loadFromFile(targetFile);
	    	StateInfo si = null;
	    	
	    	if(stateStr == null)
	    	{
	    		if(!isRand)
	    		{
	    			usage();
	    			System.exit(0);
	    		}
	    		else
	    		{
	    			si = genRandom(td);
	    			String str = name + "\t" + si.toString() + "\n";
	    			FileUtil.writeStringToFile("states.txt", str,true);
	    		}
	    	}
	    	else
	    	{
	    		si = new StateInfo(stateStr);
	    	}
	    	
	    	BufferedImage bi = genImage(td,si,width,np,ps);
	    	ConvolveOp op = getGaussianBlurFilter();
	    	bi = op.filter(bi, null);
	    	
	    	String file = name + "." + format;
	    	ImageProcessor.writeImage(bi, file);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		System.out.println("\n\n\n");
    		usage();
    	}
    	System.exit(0);
    }
}
