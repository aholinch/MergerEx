/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.sim.SimSummary;
import edu.gmu.cds.sim.StateGenerator;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.sim.StateInfoFile;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.ScatterPanel;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.util.ApplicationProperties;
import edu.gmu.cds.util.CryptUtil;
import edu.gmu.cds.util.FileUtil;

public class MainPanel extends JPanel implements SimulationHandler, StateClickListener
{
	private static final long serialVersionUID = 1L;
	
	protected JMenuBar menuBar = null;
	protected JTabbedPane tabs = null;
	
	protected SimPanel      simPanel      = null;
	protected ReviewPanel   reviewPanel   = null;
	protected EnhancePanel  enhancePanel  = null;
	protected EvaluatePanel evaluatePanel = null;
	protected StatsPanel    statsPanel    = null;
	
	protected TargetData targetData = null;
	protected File targetDir = null;
	protected HashMap<ScatterPanel,StateInfo> hmPanelStates = null;
	protected StateGenerator stateGenerator = null;
	
	protected ApplicationProperties props = null;
	
	protected JFileChooser jfc = null;
	
	protected ScatterPanel tmpSimPanel = null;
	protected SimSummary simSummary = null;
	protected StateInfoFile stateFile = null;
	protected int cnt = 0;
	
	// simulation preferences
	protected SimPrefPanel simPrefs = new SimPrefPanel();
	protected Color simColor = null;
	protected int particleSize = 2;
	protected int particleCount = 2000;
	protected boolean animateRandom = false;
	protected ExecutorService mainExecutor = null;
	
	public MainPanel()
	{
		super(new BorderLayout());
		
		props = ApplicationProperties.getInstance();
		hmPanelStates = new HashMap<ScatterPanel,StateInfo>();
		
		initGUI();
		initExecutors();
		
		simPanel.setSimulationHander(this);
		enhancePanel.setSimulationHandler(this);
		reviewPanel.imageListPanel.setStateSelectionListener(this);
		
		simSummary = new SimSummary();
		reviewPanel.imageListPanel.setStateInfos(simSummary.selectedStates);
		evaluatePanel.setStateInfoList(simSummary.selectedStates);	
		evaluatePanel.setSimSummary(simSummary);
		statsPanel.setSimSummary(simSummary);
		
		stateFile = new StateInfoFile(simSummary);
		stateFile.setSessionID(CryptUtil.genUniqueId());
		stateFile.setUserID(CryptUtil.getUsername());

		refreshPrefs();
	}
	
	public void initExecutors()
	{
		mainExecutor = Executors.newFixedThreadPool(5);
		simPanel.executor = Executors.newFixedThreadPool(2); // probably only needs 1
	}
	
	public void refreshPrefs()
	{
		ApplicationProperties prefs = ApplicationProperties.getInstance();
		simColor = prefs.getParticleColor();
		if(simColor != null)
		{
			SimRunner.DEFAULT_COLOR = simColor;
		}
		else
		{
			simColor = SimRunner.DEFAULT_COLOR;
		}
		animateRandom = prefs.getAnimateRandom();
		simPanel.setAnimate(animateRandom);
		simPanel.setShowStereo(prefs.getShowStereo());
		particleSize = prefs.getParticleSize();
		particleCount = prefs.getParticleCount();
	}
	
	protected void initGUI()
	{
		tabs = new JTabbedPane();
		
		simPanel = new SimPanel();
		reviewPanel = new ReviewPanel();
		enhancePanel = new EnhancePanel();
		evaluatePanel = new EvaluatePanel();
		statsPanel = new StatsPanel();
		
		tabs.add(simPanel,"Simulate");
		tabs.add(reviewPanel,"Review");
		tabs.add(enhancePanel,"Enhance");
		tabs.add(evaluatePanel,"Evaluate");
		tabs.add(statsPanel,"Statistics");
		
		add(tabs,BorderLayout.CENTER);
		
		menuBar = buildMenu();
	}
	
	public JMenuBar buildMenu()
	{
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem tgtItem  = new JMenuItem("Select Target");
		JMenuItem openItem = new JMenuItem("Open State File");
		JMenuItem saveItem = new JMenuItem("Save State File");
		JMenuItem exitItem = new JMenuItem("Exit");

		tgtItem.setAction(new SelectTargetAction());
		openItem.setAction(new OpenAction());
		saveItem.setAction(new SaveAction());
		exitItem.setAction(new ExitAction());
		
		fileMenu.add(tgtItem);
		fileMenu.addSeparator();
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		bar.add(fileMenu);
		
		JMenu prefsMenu = new JMenu("Preferences");
		JMenuItem editItem = new JMenuItem("Edit Preferences");
		editItem.setAction(new PrefsAction());
		prefsMenu.add(editItem);

		bar.add(prefsMenu);
		
		return bar;
	}

	public class SaveAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public SaveAction()
		{
			super("Save State File");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			saveInfo();
		}
	}
	
	public class OpenAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public OpenAction()
		{
			super("Open State File");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			openStatesFile();
		}
	}
	
	public class SelectTargetAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public SelectTargetAction()
		{
			super("Select Target");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			openExistingTarget();
		}
	}
	
	public class ExitAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public ExitAction()
		{
			super("Exit");
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			exitApp();
		}
	}
	
	public class PrefsAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public PrefsAction()
		{
			super("Edit");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object[] options = { "OK", "CANCEL" };
			simPrefs.setPrefsToPanel(props);
			int value = JOptionPane.showOptionDialog(MainPanel.this, simPrefs, "Simulation Preferences", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(value == JOptionPane.OK_OPTION)
			{
				simPrefs.setPanelToPrefs(props);
				props.save();
				refreshPrefs();
			}
		}
	}
	
	protected File getTargetDir()
	{
		if(targetDir == null)
		{
			targetDir = new File(props.getWorkingDir());
		}
		
		return targetDir;
	}
	
	public void setTargetData(TargetData data)
	{
		targetData = data;
		stateGenerator = new StateGenerator(data);
		BufferedImage image = ImageProcessor.readImage(data.getTargetImageFile());
		
		simPanel.setTargetImage(image);
		reviewPanel.setTargetImage(image);
		enhancePanel.setTargetImage(image);
		evaluatePanel.setTargetImage(image);
		
		simPanel.setTargetData(data);
		enhancePanel.setTargetData(data);
		
		// clone pane with updated scale
		tmpSimPanel = simPanel.sp1.clone(false);
		
		// TODO add a check for saving
		simSummary.reset(data.getName());
	}
	
	public void openExistingTarget()
	{
		if(jfc == null)
		{
			jfc = new JFileChooser();
		}
		
		File fdir = getTargetDir();
		
		jfc.setFileFilter((FileFilter) FileUtil.buildFilter("Target Files","tgt"));
		jfc.setCurrentDirectory(fdir);
		
		
		int retVal = jfc.showDialog(this,"Open Target File");
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			targetDir = jfc.getCurrentDirectory();
			props.setWorkingDir(targetDir.getAbsolutePath().replace('\\', '/'));
			File f = jfc.getSelectedFile();
			TargetData td = TargetData.loadFromFile(f.getAbsolutePath());
			setTargetData(td);
		}
	}
	
	public void initJFCForStates()
	{
		if(jfc == null)
		{
			jfc = new JFileChooser();
		}
		
		File fdir = getTargetDir();
		
		jfc.setFileFilter((FileFilter) FileUtil.buildFilter("State Files","txt"));
		jfc.setCurrentDirectory(fdir);		
	}
	
	public void openStatesFile()
	{
		initJFCForStates();		
		
		int retVal = jfc.showDialog(this,"Open Simulation State File");
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			//targetDir = jfc.getCurrentDirectory();
			//props.setWorkingDir(targetDir.getAbsolutePath().replace('\\', '/'));
			File f = jfc.getSelectedFile();
			List<StateInfo> infos = stateFile.read(f.getAbsolutePath());
			StateInfo info = null;
			for(int i=0; i<infos.size(); i++)
			{
				info = infos.get(i);
				ScatterPanel tmpPanel = tmpSimPanel.clone(false);
				runSimulation(info,tmpPanel,(int)(2.5*particleCount),false,false,true);
			}
			statsPanel.refreshValues();
		}
	}

	
	public void saveInfo()
	{
		initJFCForStates();		
		
		int retVal = jfc.showDialog(this,"Save Simulation State File");
		if(retVal == JFileChooser.APPROVE_OPTION)
		{	
			File f = jfc.getSelectedFile();
			stateFile.save(f.getAbsolutePath());
		}
	}
	
	public void exitApp()
	{
		// TODO consider checking for save
		System.exit(0);
	}

	@Override
	public void runRandomSimulation(ScatterPanel panel, boolean animate) 
	{
		runRandomSimulation(panel,particleCount,animate);
	}
	
	public void runRandomSimulation(ScatterPanel panel, int numParticles, boolean animate) 
	{
		if(stateGenerator == null) return;
		StateInfo info = stateGenerator.randStateInfo();
		runSimulation(info,panel,numParticles,animate);
	}

	@Override
	public void runRandomSimulations(ScatterPanel[] panels, boolean animate)
	{
		runRandomSimulations(panels,particleCount,animate);
	}
	
	public void runRandomSimulations(ScatterPanel[] panels, int numParticles, boolean animate)
	{
		if(stateGenerator == null) return;
		simPanel.setRunning(true);
		
		hmPanelStates.clear();
		
		List<SimRunner> runners = new ArrayList<SimRunner>();
		int size = panels.length;
		StateInfo info = null;
		double mins[] = null;
		
		for(int i=0; i<size; i++)
		{
			info = stateGenerator.randStateInfo();
			mins = SimRunner.getInteractionInfo(info);
			while(mins[3]<0.5)
			{
				simSummary.numBetaRejected++;
				info = stateGenerator.randStateInfo();
				mins = SimRunner.getInteractionInfo(info);
			}
			hmPanelStates.put(panels[i], info);
			SimRunner sr = new SimRunner(info,panels[i],animate,false);
			sr.setNumberOfParticles(numParticles);
			sr.setColor(simColor);
			sr.setParticleSize(particleSize);
			runners.add(sr);
			info.sessionId = getNextInfoID();
			simSummary.addStateInfo(info, false);
		}
		
		MultiRunner mr = new MultiRunner(runners,simPanel);
		if(mainExecutor != null)
		{
			mainExecutor.submit(mr);
		}
		else
		{
			Thread t = new Thread(mr);
			t.setName("Sim Runner");
			t.start();
		}
	}
	
	protected synchronized String getNextInfoID()
	{
		String id = stateFile.getSessionID();
		String tmp = String.valueOf(cnt++);
		while(tmp.length()<4)tmp ="0"+tmp;
		id = id +"_"+tmp;
		return id;
	}

	@Override
	public void runSimulation(StateInfo info, ScatterPanel panel, boolean animate) 
	{
		runSimulation(info,panel,particleCount,animate);
	}
	
	public void runSimulation(StateInfo info, ScatterPanel panel, int numParticles, boolean animate) 
	{
		runSimulation(info,panel,numParticles,animate,false,false);
	}
	public void runSimulation(StateInfo info, ScatterPanel panel, int numParticles, boolean animate, boolean runFast) 
	{
		runSimulation(info,panel,numParticles,animate,runFast,false);
	}	
	
	public void runSimulation(StateInfo info, ScatterPanel panel, int numParticles, boolean animate, boolean runFast, boolean genImage) 
	{
		SimRunner sr = new SimRunner(info, panel,animate,genImage);
		sr.setNumberOfParticles(numParticles);
		sr.setColor(simColor);
		sr.setParticleSize(particleSize);
		if(genImage)
		{
			sr.setParticleSize((int)(1.5*particleSize));
		}
		if(runFast)
		{
			sr.setPreferredH(0.02);
		}
		
		if(mainExecutor != null)
		{
			mainExecutor.submit(sr);
		}
		else
		{
			Thread t = new Thread(sr);
			t.setName("Sim Runner");
			t.start();
		}
	}

	@Override
	public void selectSimulation(ScatterPanel panel) 
	{
		StateInfo info = hmPanelStates.get(panel);
		selectSimulation(info);
	}

	@Override
	public void unselectSimulation(ScatterPanel panel)
	{
		StateInfo info = hmPanelStates.get(panel);
		unselectSimulation(info);
	}

	@Override
	public void selectSimulation(StateInfo info) 
	{
		if(info == null) return;
		
		if(info.image == null)
		{
			ScatterPanel tmpPanel = tmpSimPanel.clone(false);
			runSimulation(info,tmpPanel,(int)(2.5*particleCount),false,false,true);
		}
		info.isSelected = true;
		if(info.sessionId == null)
		{
			info.sessionId = getNextInfoID();
		}
		simSummary.selectState(info);
		reviewPanel.imageListPanel.refreshPreferredSize();;
	}

	@Override
	public void unselectSimulation(StateInfo info) 
	{	
		if(info == null) return;
		
		simSummary.unselectState(info);
		reviewPanel.imageListPanel.refreshPreferredSize();
	}
	
	@Override
	public SimSummary getSimSummary() 
	{
		return simSummary;
	}

	@Override
	public void incrementEnhanced() 
	{
		simSummary.numEnhanced++;
	}


	@Override
	public void updateSummary() 
	{
        simSummary.update();
	}

	@Override
	public void stateClicked(StateInfo info) 
	{
		if(info != null)
		{
			enhancePanel.stateInfoSelectedForEnhance(info);
		}
	}	

	protected static class MultiRunner implements Runnable
	{
		protected List<SimRunner> runners = null;
		protected SimPanel simPanel = null;
		
		public MultiRunner(List<SimRunner> srs, SimPanel sp)
		{
			runners = srs;
			simPanel = sp;
		}
		
		public void run()
		{
			int size = runners.size();
			for(int i=0; i<size; i++)
			{
				// run sequentially
				runners.get(i).run();
			}
			simPanel.setRunning(false);
		}
	}
	
	public static void initFrame()
	{
		UIHelper.setLookAndFeel();
		MainPanel mp = new MainPanel();
		
		JFrame f = new JFrame("Target Prep");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(700, 675);
		f.getContentPane().add(mp);
		f.setJMenuBar(mp.menuBar);
		f.setVisible(true);		
	}
	
	public static void main(String args[])
	{	
	     SwingUtilities.invokeLater(new Runnable() {
	    	 public void run() {
	    		 initFrame();
	         }
	     });
	}

}
