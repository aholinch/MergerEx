/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.contour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.gmu.cds.img.ContourFinder;
import edu.gmu.cds.img.ContourPoint;
import edu.gmu.cds.img.HuAnalysis;
import edu.gmu.cds.img.HuMoment;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.img.ZernikeMoment;
import edu.gmu.cds.img.ZernikeSorter;
import edu.gmu.cds.sim.StateGenerator;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.search.SimRunner;
import edu.gmu.cds.util.ApplicationProperties;
import edu.gmu.cds.util.FileUtil;

public class ZernikeContourPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected JButton btnNext = null;
	protected JLabel lblDist = null;
	protected JLabel lblCur = null;
	
	protected StateInfo bestInfo = null;
	protected double bstDist = Double.MAX_VALUE;
	protected List<List<ContourPoint>> bstContours = null;
	protected List<ZernikeMoment> bstMoms = null;
	
	protected StateInfo info = null;
	protected double dist = 0;
	protected List<List<ContourPoint>> contours = null;
	protected List<ZernikeMoment> moms = null;
	
	protected List<List<ContourPoint>> tgtContours = null;
	protected List<List<ContourPoint>> tgtApproxContours = null;
	protected List<ZernikeMoment> tgtMoms = null;
	
	protected ImagePanel tgtPanel = null;
	protected ImagePanel tgtCnts = null;
	protected ImagePanel bestPanel = null;
	protected ImagePanel bestCnts = null;
	protected ImagePanel simPanel = null;
	protected ImagePanel simCnts = null;
	
	protected ApplicationProperties props = null;

	protected TargetData targetData = null;
	protected StateGenerator stateGenerator = null;
	protected ScatterPanel sp = null;
	protected double scale[] = null;
	protected List<String> nameList = null;
	protected List<List<ZernikeMoment>> momList = null;
	protected int imgCnt = 0;
	protected int zorder = 30;
	
	public ZernikeContourPanel()
	{
		super();
		props = ApplicationProperties.getInstance();

		initGUI();
	}
	
	protected void initGUI()
	{
		setLayout(new BorderLayout());
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		
		lblDist = new JLabel("   ");
		lblCur = new JLabel("   ");
		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(btnNext);
		topPanel.add(new JLabel("   "));
		topPanel.add(new JLabel("Best Distance:"));
		topPanel.add(lblDist);
		topPanel.add(new JLabel("   "));
		topPanel.add(new JLabel("Distance:"));
		topPanel.add(lblCur);
		
		tgtPanel = new ImagePanel();
		tgtCnts = new ImagePanel();
		bestPanel = new ImagePanel();
		bestCnts = new ImagePanel();
		simPanel = new ImagePanel();
		simCnts = new ImagePanel();
		
		tgtPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		tgtCnts.setBorder(BorderFactory.createLineBorder(Color.RED));
		bestPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		bestCnts.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		simPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		simCnts.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JPanel imgPanel = new JPanel(new GridLayout(3,2));
		imgPanel.add(tgtPanel);
		imgPanel.add(tgtCnts);
		imgPanel.add(bestPanel);
		imgPanel.add(bestCnts);
		imgPanel.add(simPanel);
		imgPanel.add(simCnts);
		
		add(imgPanel,BorderLayout.CENTER);
		add(topPanel,BorderLayout.NORTH);
	}
	
	public void setTargetData(TargetData data)
	{
		targetData = data;
		stateGenerator = new StateGenerator(data);
		BufferedImage image = ImageProcessor.readImage(data.getTargetImageFile());
		image = ImageProcessor.resize(image, 512, 512);
		tgtPanel.setImage(image);
		tgtPanel.repaint();
	    scale = targetData.calculateScale();

		// Compute Target Contours and Moments
		//image = ImageProcessor.readImage(data.getTargetImageFile());
	    image = ImageProcessor.copyImage(image);
	    ImageProcessor.threshold(image, 10, false);
	    int rgb = Color.black.getRGB();
	    rgb = image.getRGB(2, 2);
	    List<List<ContourPoint>> pnts = ContourFinder.findContours(image, rgb);
	    tgtContours = HuAnalysis.findUsefulContours(pnts,image.getWidth(),image.getHeight(),0,0,0,0);
	    List<List<ContourPoint>> approxPnts = new ArrayList<List<ContourPoint>>();
	    for(int c=0;c<tgtContours.size();c++)
    	{
	    	approxPnts.add(ContourFinder.approximateContour(tgtContours.get(c), 5));
	    	System.out.println(tgtContours.get(c).size() + "\t" + approxPnts.get(c).size());
    	}
	    tgtApproxContours=approxPnts;
	    image = ContourFinder.paintContours(image,approxPnts, Color.GREEN);
	    //image = ContourFinder.paintContours(image,tgtContours, Color.GREEN);
		tgtCnts.setImage(image);
		tgtCnts.repaint();
		tgtMoms = ZernikeMoment.calculateMoments(tgtContours, zorder, image.getWidth(), image.getHeight(), false);
		
		/*
		int w = (int)(0.125*image.getWidth());
		BufferedImage bit = ZernikeMoment.constructImage(tgtMoms, zorder, w,w);
		ImageProcessor.writeImage(bit, "recon.png");
		*/
	}
	

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object src = event.getSource();
		if(src == btnNext)
		{
			//doNext();
			spawnNext();
		}
		
	}
	
	public void spawnNext()
	{
		
		//nameList = new ArrayList<String>(200);
		//momList = new ArrayList<List<ZernikeMoment>>(200);
		imgCnt = 0;
		Runnable r = new Runnable()
		{
			public void run()
			{
				btnNext.setEnabled(false);
				try
				{
					for(int i=0; i<2000; i++)
					{
						imgCnt++;
						doNext();
						Thread.sleep(100);
					}
					//analyzeLists();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					btnNext.setEnabled(true);
				}
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
	public void doNext()
	{
		if(sp == null)
		{
			sp = new ScatterPanel();
		}
		sp.setDataRange(scale[0], scale[1], scale[2], scale[3]);
		info = stateGenerator.randStateInfo();
		double mins[] = SimRunner.getInteractionInfo(info);
		while(mins[3]<0.5)
		{
			info = stateGenerator.randStateInfo();
			mins = SimRunner.getInteractionInfo(info);
		}
		SimRunner sr = new SimRunner(info, sp, false, true);
		sr.setColor(Color.WHITE);
		sr.setParticleSize(5);
		sr.setNumberOfParticles(2000);
		sr.run();
		
		simPanel.setImage(info.image);
		simPanel.repaint();
		
		// Compute Contours
		float n = (float)(3.0d/9.0d);
	    float sharpen[] = new float[]{
	    		n,n,n,
	    		n,n,n,
	    		n,n,n
	    };
	    
	    Kernel kernel = new Kernel(3, 3, sharpen);

	    ConvolveOp op = new ConvolveOp(kernel);
	    //BufferedImage bi = ImageProcessor.copyImage(info.image);
	    BufferedImage bi = ImageProcessor.convertToGray(info.image);
	    
	    dumpPixels(bi,"copy");
	    
	    bi = op.filter(bi, null);
		bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    bi = op.filter(bi, null);
	    
	    dumpPixels(bi,"after filter");

	    
	    ImageProcessor.threshold(bi, 100, false);

	    dumpPixels(bi,"after thresh");
	    
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

	    dumpPixels(bi,"after morph");

	    ImageProcessor.threshold(bi, 100, false);

	    dumpPixels(bi,"after thres2");


	    int rgb = Color.black.getRGB();
	    rgb = bi.getRGB(2, 2);
	    rgb = 0;
	    
		//List<List<ContourPoint>> pnts = ContourFinder.findContours(bi, rgb);
		List<List<ContourPoint>> pnts = ContourFinder.findContoursBinary(bi);

		contours = HuAnalysis.findUsefulContours(pnts,bi.getWidth(),bi.getHeight(),0,0,0,0);
		List<List<ContourPoint>> approxPnts = new ArrayList<List<ContourPoint>>();
	    for(int c=0;c<contours.size();c++)
    	{
	    	approxPnts.add(ContourFinder.approximateContour(contours.get(c), 5));
	    	System.out.println(contours.get(c).size() + "\t" + approxPnts.get(c).size());
    	}
	    
		
		bi = ContourFinder.paintContours(bi,approxPnts, Color.GREEN);
		//bi = ContourFinder.paintContours(bi,contours, Color.GREEN);
		simCnts.setImage(bi);
		
		/*
		String file = String.valueOf(imgCnt);
		while(file.length()<5)file = "0"+file;
		file = "/tmp/moms/"+file+".png";
		ImageProcessor.writeImage(bi, file);
*/
		simCnts.repaint();
		moms = ZernikeMoment.calculateMoments(contours, zorder, bi.getWidth(), bi.getHeight(), false);


	    //nameList.add(file);
	    //momList.add(moms);
	    
		//double da[] = ZernikeSorter.dist(tgtMoms, moms);
		//dist = da[0]+0.5*da[1];
		
		
	    dist = ZernikeSorter.dist(tgtMoms, moms)[0];
		lblCur.setText(String.valueOf(dist));
	    
	    if(dist<bstDist)
	    {
	    	bstDist = dist;
	    	lblDist.setText(String.valueOf(dist));
	    	bstContours = contours;
	    	bstMoms = moms;
	    	bestInfo = info;
	    	bestPanel.setImage(bestInfo.image);
	    	bestCnts.setImage(simCnts.getImage());
	    	bestPanel.repaint();
	    	bestCnts.repaint();
	    	
	    	System.out.println("\n\nNew best");
	    	System.out.println(dist);
	    	System.out.println(info);
	    }
	}
	
	public void analyzeLists()
	{
		int size = nameList.size();
		String name1 = null;
		String name2 = null;
		List<ZernikeMoment> mom1 = null;
		List<ZernikeMoment> mom2 = null;
		
		double dist = 0;
		double bestDist = 0;
		StringBuffer sb = new StringBuffer(10000);
		
		for(int i=0; i<size; i++)
		{
			name1 = nameList.get(i);
			mom1 = momList.get(i);
			bestDist = Double.MAX_VALUE;
			for(int j=0; j<size; j++)
			{
				if(i==j)continue;
				mom2 = momList.get(j);
				
			    dist = ZernikeSorter.dist(mom1, mom2)[0];
				if(dist < bestDist)
				{
					bestDist = dist;
					name2 = nameList.get(j);
				}
			}
			
			sb.append("<br><img src=\"");
			sb.append(name1);
			sb.append("\"> <img src=\"");
			sb.append(name2);
			sb.append("\"> &nbsp; ").append(bestDist);
			sb.append("\n");
		}
		FileUtil.writeStringToFile("/tmp/moms/index.html", sb.toString());
	}
	
	public void dumpPixels(BufferedImage bi, String label)
	{
		if(1>0) return;
		System.out.println("\n\n"+label);
		int nc = 3;
		WritableRaster wr = bi.getRaster();
		String str = null;
		int nb = wr.getNumBands();
		for(int y=0; y<nc; y++)
		{
			for(int x = 0; x<nc; x++)
			{
				str = String.valueOf(wr.getSample(x, y, 0));
				for(int i=1; i<nb; i++)
				{
					str += ","+String.valueOf(wr.getSample(x, y, i));
				}
				System.out.print(str+"\t");
			}
			System.out.println("");
		}
	}

}
