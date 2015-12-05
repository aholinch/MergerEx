/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import edu.gmu.cds.img.ContourFinder;
import edu.gmu.cds.img.ContourPoint;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.sim.SimSummary;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.stats.Histogram;
import edu.gmu.cds.stats.HistogramPanel;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.StateInfoTableModel;

/**
 * Provides access to fitness distribution and range of simulation values.
 * 
 * @author aholinch
 *
 */
public class StatsPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	protected JLabel lblNumBeta = null;
	protected JLabel lblNumSims = null;
	protected JLabel lblNumSelected = null;
	protected JLabel lblNumEnhanced = null;
	protected JLabel lblNumEvaluated = null;
	protected JLabel lblNumWars = null;
	
	protected SimSummary simSummary = null;
	
	protected JTabbedPane tabs = null;
	
	protected JScrollPane jsp = null;
	protected JTable simTable = null;
	protected StateInfoTableModel tableModel = null;
	
	protected JScrollPane jsp2 = null;
	protected JPanel orbitPanel = null;
	
	protected HistogramPanel histIncFit = null;
	protected HistogramPanel histIncSel = null;
	protected HistogramPanel histIncRej = null;
	protected HistogramPanel histEccFit = null;
	protected HistogramPanel histEccSel = null;
	protected HistogramPanel histEccRej = null;
	protected HistogramPanel histMrFit = null;
	protected HistogramPanel histMrSel = null;
	protected HistogramPanel histMrRej = null;
	protected HistogramPanel histThetaFit = null;
	protected HistogramPanel histThetaSel = null;
	protected HistogramPanel histThetaRej = null;
	protected HistogramPanel histPhiFit = null;
	protected HistogramPanel histPhiSel = null;
	protected HistogramPanel histPhiRej = null;
	
	public StatsPanel()
	{
		super(new BorderLayout());
		initGUI();
		addComponentListener(new UpdateCheck());
	}
	
	protected void initGUI()
	{
		// table information
		tableModel = new StateInfoTableModel();
		simTable = new JTable(tableModel);
		jsp = new JScrollPane(simTable);
		simTable.addMouseListener(new SimPopUp());
		
		// summary info
		lblNumBeta = new JLabel("0");
		lblNumSims = new JLabel("0");
		lblNumSelected = new JLabel("0");
		lblNumEnhanced = new JLabel("0");
		lblNumEvaluated = new JLabel("0");
		lblNumWars = new JLabel("0");
		
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		
		JPanel tmpPanel = new JPanel(new GridLayout(3,2));

		tmpPanel.add(new JLabel("# Random Sims Rejected:"));
		tmpPanel.add(lblNumBeta);
		
		tmpPanel.add(new JLabel("# Simulations Run:"));
		tmpPanel.add(lblNumSims);

		tmpPanel.add(new JLabel("# Simulations Selected:"));
		tmpPanel.add(lblNumSelected);

		topPanel.add(tmpPanel);

		tmpPanel = new JPanel(new GridLayout(3,2));

		tmpPanel.add(new JLabel("# Simulations Enhanced:"));
		tmpPanel.add(lblNumEnhanced);

		tmpPanel.add(new JLabel("# Simulations Evaluated:"));
		tmpPanel.add(lblNumEvaluated);
		
		tmpPanel.add(new JLabel("# Merger Wars Competitions:"));
		tmpPanel.add(lblNumWars);
		
		topPanel.add(tmpPanel);
		
		orbitPanel = buildOrbitPanel();
		
		jsp2 = new JScrollPane(orbitPanel);

		tabs = new JTabbedPane();
		tabs.addTab("States", jsp);
		tabs.addTab("Orbit Distribution", jsp2);
		
		add(tabs,BorderLayout.CENTER);
		add(topPanel,BorderLayout.NORTH);
	}
	
	protected JPanel buildOrbitPanel()
	{
		JPanel panel = new JPanel(new GridLayout(5,3));
		Dimension dim = new Dimension(600,1000);
		panel.setPreferredSize(dim);
		panel.setSize(dim);
		histIncFit = new HistogramPanel("Inclination - Top Fitness");
		histIncSel = new HistogramPanel("Inclination - All Selected");
		histIncRej = new HistogramPanel("Inclination - Rejected");
		histEccFit = new HistogramPanel("Eccentricity - Top Fitness");
		histEccSel = new HistogramPanel("Eccentricity - All Selected");
		histEccRej = new HistogramPanel("Eccentricity - Rejected");
		histMrFit = new HistogramPanel("Mass Ratio - Top Fitness");
		histMrSel = new HistogramPanel("Mass Ratio - All Selected");
		histMrRej = new HistogramPanel("Mass Ratio - Rejected");
		histThetaFit = new HistogramPanel("Theta - Top Fitness");
		histThetaSel = new HistogramPanel("Theta - All Selected");
		histThetaRej = new HistogramPanel("Theta - Rejected");
		histPhiFit = new HistogramPanel("Phi - Top Fitness");
		histPhiSel = new HistogramPanel("Phi - All Selected");
		histPhiRej = new HistogramPanel("Phi - Rejected");

		panel.add(histIncFit);
		panel.add(histIncSel);
		panel.add(histIncRej);
		panel.add(histEccFit);
		panel.add(histEccSel);
		panel.add(histEccRej);
		panel.add(histMrFit);
		panel.add(histMrSel);
		panel.add(histMrRej);
		panel.add(histThetaFit);
		panel.add(histThetaSel);
		panel.add(histThetaRej);
		panel.add(histPhiFit);
		panel.add(histPhiSel);
		panel.add(histPhiRej);
		
		return panel;
	}
	
	public void setSimSummary(SimSummary summary)
	{
		simSummary = summary;
		tableModel.setStateInfos(simSummary.selectedStates);
	}
	
	public void refreshValues()
	{
		if(simSummary == null) return;
		
		// do we need to do this every time?
		simSummary.update();
		
		lblNumBeta.setText(String.valueOf(simSummary.numBetaRejected));
		lblNumSims.setText(String.valueOf(simSummary.numViewed));
		lblNumSelected.setText(String.valueOf(simSummary.numSelected));
		lblNumEnhanced.setText(String.valueOf(simSummary.numEnhanced));
		lblNumEvaluated.setText(String.valueOf(simSummary.numEvaluated));
		lblNumWars.setText(String.valueOf(simSummary.numMergerWarsCompetitions));
		
		refreshHistograms();
		
		tableModel.fireTableDataChanged();
	}
	
	public void refreshHistograms()
	{
	    //double incbins[] = {0,15,30,45,60,75,90,105,120,135,150,165};
	    double incbins[] = {0,10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170};
	    double eccbins[] = {0,0.25,0.5,0.75,1,1.5,2,2.5,3,3.5,4,4.5,5,10,20};
	    double mrbins[] = {0,0.25,0.5,0.75,1,1.5,2,2.5,3,3.5,4,4.5,5,10,20};
	    double thetabins[] = {0,15,30,45,60,75,90,105,120,135,150,165,180,195,210,225,240,255,270,285,300,315,330};
	    double phibins[] = {0,10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170};
	    
	    List<List<Double>> fitVals = null;
	    List<List<Double>> selVals = null;
	    List<List<Double>> rejVals = null;
	    
	    Histogram hist0 = null;
	    Histogram hist1 = null;
	    Histogram hist2 = null;

	    
	    fitVals = getValues(simSummary.selectedStates,0.75);
	    selVals = getValues(simSummary.selectedStates,0);
	    rejVals = getValues(simSummary.notSelectedStates,0);
	    
	    
	    hist0 = Histogram.createHistogram(incbins, fitVals.get(0));
	    hist1 = Histogram.createHistogram(incbins, selVals.get(0));
	    hist2 = Histogram.createHistogram(incbins, rejVals.get(0));
	    histIncFit.setHistogram(hist0);
	    histIncSel.setHistogram(hist1);
	    histIncRej.setHistogram(hist2);
	    
	    
	    hist0 = Histogram.createHistogram(eccbins, fitVals.get(1));
	    hist1 = Histogram.createHistogram(eccbins, selVals.get(1));
	    hist2 = Histogram.createHistogram(eccbins, rejVals.get(1));
	    histEccFit.setHistogram(hist0);
	    histEccSel.setHistogram(hist1);
	    histEccRej.setHistogram(hist2);
	    
	    
	    hist0 = Histogram.createHistogram(mrbins, fitVals.get(2));
	    hist1 = Histogram.createHistogram(mrbins, selVals.get(2));
	    hist2 = Histogram.createHistogram(mrbins, rejVals.get(2));
	    histMrFit.setHistogram(hist0);
	    histMrSel.setHistogram(hist1);
	    histMrRej.setHistogram(hist2);
	    
	    	    
	    hist0 = Histogram.createHistogram(thetabins, fitVals.get(3));
	    hist1 = Histogram.createHistogram(thetabins, selVals.get(3));
	    hist2 = Histogram.createHistogram(thetabins, rejVals.get(3));
	    histThetaFit.setHistogram(hist0);
	    histThetaSel.setHistogram(hist1);
	    histThetaRej.setHistogram(hist2);  
	    
	    
	    hist0 = Histogram.createHistogram(phibins, fitVals.get(4));
	    hist1 = Histogram.createHistogram(phibins, selVals.get(4));
	    hist2 = Histogram.createHistogram(phibins, rejVals.get(4));
	    histPhiFit.setHistogram(hist0);
	    histPhiSel.setHistogram(hist1);
	    histPhiRej.setHistogram(hist2);

	    histIncFit.repaint();
	    histIncSel.repaint();
	    histIncRej.repaint();
	    histEccFit.repaint();
	    histEccSel.repaint();
	    histEccRej.repaint();
	    histMrFit.repaint();
	    histMrSel.repaint();
	    histMrRej.repaint();
	    histThetaFit.repaint();
	    histThetaSel.repaint();
	    histThetaRej.repaint();
	    histPhiFit.repaint();
	    histPhiSel.repaint();
	    histPhiRej.repaint();
	}
	
	protected List<List<Double>> getValues(List<StateInfo> infos, double minFitness)
	{
		List<List<Double>> out = new ArrayList<List<Double>>();
		int size = infos.size();
		List<Double> out1 = new ArrayList<Double>(size);
		List<Double> out2 = new ArrayList<Double>(size);
		List<Double> out3 = new ArrayList<Double>(size);
		List<Double> out4 = new ArrayList<Double>(size);
		List<Double> out5 = new ArrayList<Double>(size);
		StateInfo info = null;
		
		for(int i=0; i<size; i++)
		{
			info = infos.get(i);
			if(info.fitness < minFitness)continue;
			
			if(info.orbParams == null)
			{
				simSummary.calculateOrbitParameters(info);
			}
			out1.add(Math.toDegrees(info.orbParams[2]));
			out2.add(Math.toDegrees(info.orbParams[1]));
			out3.add(info.m1/info.m2);
			out4.add(info.theta1);
			out5.add(info.phi1);
		}
		
		out.add(out1);
		out.add(out2);
		out.add(out3);
		out.add(out4);
		out.add(out5);
		
		return out;
	}
	
	protected class UpdateCheck implements ComponentListener
    {

		@Override
		public void componentHidden(ComponentEvent e) 
		{
		}

		@Override
		public void componentMoved(ComponentEvent e) 
		{
		}

		@Override
		public void componentResized(ComponentEvent e) 
		{
		}

		@Override
		public void componentShown(ComponentEvent e) 
		{
			refreshValues();
		}
    }
	
	public class SimPopUp implements MouseListener
	{
		protected ImagePanel imagePanel = null;
		
		public SimPopUp()
		{
			imagePanel = new ImagePanel();	
			int size = 300;
			Dimension dim = new Dimension(size,size);
			imagePanel.setMinimumSize(dim);
			imagePanel.setPreferredSize(dim);
			imagePanel.setSize(dim);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(e.getClickCount() < 2)
			{
				return;
			}
			
			int row = simTable.getSelectedRow();
			StateInfo info = tableModel.getInfo(row);
			if(info.image != null)
			{
				imagePanel.setImage(info.image);
				JOptionPane.showMessageDialog(StatsPanel.this, imagePanel, "Simulation Snapshot", JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(StatsPanel.this, "Image not yet available.");
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
