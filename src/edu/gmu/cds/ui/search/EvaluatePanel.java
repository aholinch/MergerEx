/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.gmu.cds.sim.MergerWars;
import edu.gmu.cds.sim.SimSummary;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.UIHelper;

/**
 * A Merger Wars style evaluation of selected simulations.
 * 
 * @author aholinch
 *
 */
public class EvaluatePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	protected TableLayoutPanel warsPanel = null;
	protected ImagePanel imagePanel = null;
	protected ScatterPanel spLeft = null;   // only used if they want to view simulation
	protected ScatterPanel spRight = null;   // only used if they want to view simulation
	protected ImagePanel imageLeft = null;
	protected ImagePanel imageRight = null;
	protected JButton btnNeither = null;
	
	protected MergerWars mergerWars = null;
	protected StateInfo tourn[] = null;
	protected SimSummary simSummary = null;
	
	protected boolean hasStarted = false;
	
	public EvaluatePanel()
	{
		super(new BorderLayout());
		initGUI();
	}
	
	protected void initGUI()
	{
		warsPanel = buildWarsPanel();
		add(UIHelper.getCenterFloatPanel(warsPanel),BorderLayout.CENTER);
		
		JLabel tmpLabel = new JLabel("Click on the simulation that is a better match to the target image.");
		JPanel tmpPanel = new JPanel(new GridLayout(3,1,3,3));
		JPanel tmp2Panel = new JPanel(new FlowLayout());
		tmpPanel.add(tmp2Panel);
		tmpPanel.add(new JLabel(""));
		tmpPanel.add(tmpLabel);
		add(tmpPanel,BorderLayout.NORTH);
		
		ReadyCheck rc = new ReadyCheck();
		addComponentListener(rc);
		
		mergerWars = new MergerWars();
		WarsClick wc = new WarsClick();
		btnNeither.addActionListener(wc);
		imageLeft.addMouseListener(wc);
		imageRight.addMouseListener(wc);
	}
	
	protected TableLayoutPanel buildWarsPanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		double hgt = 1.0d/7.0d;
		double size[][] = {{2,0.25,1,0.25,1,0.25,1,0.25,2},{2,3*hgt,2,3*hgt,2,hgt,2}};
		panel.setLayout(new TableLayout(size));
		
		imagePanel = new ImagePanel();
		spLeft = new ScatterPanel();
		spRight = new ScatterPanel();
		imageLeft = new ImagePanel();
		imageRight = new ImagePanel();
		btnNeither = new JButton("Neither is a Good Match");
		
		//panel.add(spLeft,panel.getRC(3, 1, 3, 3));
		//panel.add(spRight,panel.getRC(3, 5, 3, 7));
		panel.add(imagePanel,panel.getRC(1, 3, 1, 5));
		panel.add(imageLeft,panel.getRC(3, 1, 3, 3));
		panel.add(imageRight,panel.getRC(3, 5, 3, 7));
		
		JPanel tmpPanel = new JPanel(new FlowLayout());
		tmpPanel.add(btnNeither);
		panel.add(tmpPanel,panel.getRC(5, 1,5,7));

		/*
		SquareKeeper sk = new SquareKeeper();
		imagePanel.addComponentListener(sk);
		imageLeft.addComponentListener(sk);
		imageRight.addComponentListener(sk);
		*/
		
		int width = 510;
		int height = 505;
		panel.setSize(width,height);
		panel.setPreferredSize(new Dimension(width,height));
		panel.setMinimumSize(new Dimension((int)(0.9*width),(int)(0.9*height)));
		return panel;
	}
	
	public void setStateInfoList(List<StateInfo> states)
	{
		mergerWars.setStates(states);
	}
	
	public void setSimSummary(SimSummary summary)
	{
		simSummary = summary;
		// do not set the list from this method
	}
	
    public void setTargetImage(BufferedImage image)
    {
    	imagePanel.setImage(image);
    	imagePanel.repaint();
    }

    public void updateImages()
    {
    	imageLeft.setImage(tourn[mergerWars.getLeftInd()].image);
    	imageRight.setImage(tourn[mergerWars.getRightInd()].image);
    	
    	imageLeft.repaint();
    	imageRight.repaint();
    }
    
    protected class WarsClick extends MouseAdapter implements ActionListener
    {

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			Object src = e.getSource();
			int ind = 0;
			boolean handleClick = false;
			
			if(src == imageLeft)
			{
				ind = 1;
				handleClick = true;
			}
			else if(src == imageRight)
			{
				ind = 2;
				handleClick = true;
			}
			
			if(handleClick)
			{
				if(simSummary != null)simSummary.numMergerWarsCompetitions++;
				mergerWars.setWinner(ind);
				updateImages();
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object src = e.getSource();
			if(src != btnNeither) return;
			
			if(simSummary != null)simSummary.numMergerWarsCompetitions++;
			mergerWars.setWinner(0);
			updateImages();
		}
    }
    
    protected class ReadyCheck implements ComponentListener
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
			if(!hasStarted && mergerWars.isReady())
			{
				hasStarted = true;
				mergerWars.nextRound();
				tourn = mergerWars.getTournamentArray();
				updateImages();
			}
		}
    	
    }
}
