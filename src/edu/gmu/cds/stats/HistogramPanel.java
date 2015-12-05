/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.stats;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HistogramPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected Histogram hist = null;
	
	protected int labelOffY = 30;
	protected int plotOffXL = 25;
	protected int plotOffXR = 5;
	protected int plotOffY = 20;
	protected String title = null;
	
	protected int maxBinLabels = 5;
	protected boolean skipMaxBin = true;
	protected boolean showPerc = true;
	
	public HistogramPanel()
	{
		super();
	}
	
	public HistogramPanel(String title)
	{
		super();
		setTitle(title);
	}
	
	public void setHistogram(Histogram hist)
	{
		this.hist = hist;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setNoLabels()
	{
		plotOffY=2;
		labelOffY=2;
		plotOffXL=2;
		plotOffXR=2;
	}
	
	public void paint(Graphics g)
	{
		int w = getWidth();
		int h = getHeight();

		if(hist == null || hist.cnts == null) return;
		
		int pw = w-plotOffXL-plotOffXR;
		int ph = h-plotOffY-labelOffY;
		
		Font f1 = g.getFont();
		f1 = f1.deriveFont(Font.BOLD);
		Font f2 = f1.deriveFont((0.8f*f1.getSize()));
		f2 = f2.deriveFont(Font.PLAIN);
		
		FontMetrics fm = g.getFontMetrics(f1);
		int tw = fm.stringWidth(title);
		int th = fm.getHeight() + fm.getDescent();
		
		FontMetrics fm2 = g.getFontMetrics(f2);
		int th2 = fm2.getHeight() + fm2.getDescent();

		g.setFont(f2);
		double max = hist.getMaxCount();
		int nc = (int)max;
		if(nc > 10) nc = 10;
		double divy = ((double)ph)/((double)(nc));
		//System.out.println("max = " + max + "\tdivy="+divy);
		double y = 0;
		double x = 0;
		String str = null;
		int lbl = 0;
		double dlbl = 0;
		if(nc > 0)
		{
			for(int i=0; i<=nc; i++)
			{
				x = plotOffXL-5;
				y = i*divy+plotOffY;
				g.drawLine((int)x,(int)y,(int)(x+5),(int)y);
				//lbl = (int)(100.0d*(1.0d-(double)(i*nc)/(double)max));
				//dlbl = lbl/100.0d;
				//str = String.valueOf(dlbl);
				str = String.valueOf((int)(max*(1.0d-(double)i/(double)nc)));
				g.drawString(str, (int)(x-fm2.stringWidth(str))-2, (int)(y+0.25*th2));
			}
		}
		
		int nb = hist.bins.length;
		double divx = ((double)pw)/((double)nb);
		int modb = 0;
		if(nb <= maxBinLabels)
		{
			modb = 1;
		}
		else
		{
			modb = (nb-2)/maxBinLabels;
			if(skipMaxBin)
			{
				modb = (nb-1)/maxBinLabels;
			}
		}
		
		for(int i=0; i<nb; i++)
		{
		    y = plotOffY+ph+5;
		    x = (i+0.5)*divx + plotOffXL;
		    g.drawLine((int)x,(int)y,(int)x,(int)(y-5));
		    if(i==0 || (i==nb-1 && !skipMaxBin) || i%modb == 0)
		    {
		    	g.drawString(hist.labels[i], (int)(x-0.5*fm2.stringWidth(hist.labels[i])), (int)(y+th2+2));
		    }
		}
		
		g.translate(plotOffXL, plotOffY);
		paintChartArea(g,pw,ph);
		g.translate(-plotOffXL, -plotOffY);
		
		g.setFont(f1);
		if(title != null)
		{
			int startX = (int)(plotOffXL+0.5*(pw-tw));
			int startY = (int)(0.5*(plotOffY+th-10));
			g.setColor(Color.black);
			g.drawString(title,startX,startY);
		}
	}
	
	protected void paintChartArea(Graphics g, int w, int h)
	{
		// fill background
		g.setColor(Color.WHITE);
		g.fillRect(0,0,w,h);
		
		// draw outline
		g.setColor(Color.black);
		g.drawRect(0, 0, w, h);
		
		
		// draw boxes
		g.setColor(Color.BLUE);
		int nb = hist.cnts.length;
		double divx = ((double)w)/((double)nb);
		double max = hist.getMaxCount();
		double divy = ((double)h)/((double)(max));

		double x,y;
		
		for(int i=0; i<nb; i++)
		{
			x = i*divx;
			y = h-hist.cnts[i]*divy;
			g.setColor(Color.BLUE);
			g.fillRect((int)x,(int)y,(int)divx,(int)(hist.cnts[i]*divy));
			g.setColor(Color.BLACK);
			g.drawRect((int)x,(int)y,(int)divx,(int)(hist.cnts[i]*divy));
		}
	}
	
}
