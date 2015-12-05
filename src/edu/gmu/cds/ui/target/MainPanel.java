/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import edu.gmu.cds.img.ImageListener;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.util.ApplicationProperties;
import edu.gmu.cds.util.FileUtil;

public class MainPanel extends JPanel implements ActionListener, ImageListener, ChangeListener
{
	private static final long serialVersionUID = 1L;

	protected JMenuBar menuBar = null;
	protected JTabbedPane tabs = null;
	
	protected TargetPanel targetPanel = null;
    protected DiskSelectionPanel diskPanel = null;
    protected DiskSetupPanel diskSetupPanel = null;
    protected ParameterRangePanel paramPanel = null;
    
	protected JTextField txtName = null;
	
	protected JButton btnBrowse  = null;
	protected JButton btnRefresh = null;
	protected JButton btnSave    = null;
	
	protected JLabel lblName = null;
	
	protected File targetDir = null;
	
	protected JFileChooser jfc = null;
	
	protected ApplicationProperties props = null;
	
	protected TargetData targetData = null;
	
	public MainPanel()
	{
		super(new BorderLayout());
		props = ApplicationProperties.getInstance();
		initGUI();
	}
	
	public void initGUI()
	{
		tabs = new JTabbedPane();
		
		targetPanel = new TargetPanel();
		targetPanel.setImageListener(this);
		diskPanel = new DiskSelectionPanel();
		diskSetupPanel = new DiskSetupPanel();
		paramPanel = new ParameterRangePanel();
		
		tabs.addTab("Target", targetPanel);
		tabs.addTab("Disk Info", diskPanel);
		tabs.addTab("Disk Orientation", diskSetupPanel);
		tabs.addTab("Parameter Ranges", paramPanel);
		tabs.addChangeListener(this);
		
		add(buildTopPanel(),BorderLayout.NORTH);
		add(tabs,BorderLayout.CENTER);
		
		menuBar = buildMenu();
	}
	
	public JPanel buildTopPanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		double sz = 1.0d/7.0d;
		double size[][] = {{2,sz,1,2*sz,1,sz,1,sz,1,sz,1,sz,2},{2,TableLayout.FILL,2}};
		panel.setLayout(new TableLayout(size));
		
		lblName = new JLabel("Target Name:");
		txtName = new JTextField(10);
		btnBrowse = new JButton("Pick Dir");
		btnRefresh = new JButton("Refresh");
		btnSave = new JButton("Save");
		
		btnBrowse.addActionListener(this);
		btnSave.addActionListener(this);
		btnRefresh.addActionListener(this);
		
		panel.add(lblName,panel.getRC(1, 1));
		panel.add(txtName,panel.getRC(1, 3));
		panel.add(btnBrowse,panel.getRC(1,5));
		panel.add(btnRefresh,panel.getRC(1,9));
		panel.add(btnSave,panel.getRC(1,11));
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent evt) 
	{
		Object src = evt.getSource();
		if(src == btnBrowse)
		{
			pickDir();
		}
		else if(src == btnSave)
		{
			saveInfo();
		}
		else if(src == btnRefresh)
		{
			refreshInfo();
		}
	}
	
	public void pickDir()
	{
		if(jfc == null)
		{
			jfc = new JFileChooser();
		}
		
		File fdir = getTargetDir();
		
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		jfc.setCurrentDirectory(fdir);
		
		
		int retVal = jfc.showDialog(this,"Pick Dir for Target File");
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			// update targetDir
			targetDir = jfc.getCurrentDirectory();
			props.setWorkingDir(targetDir.getAbsolutePath().replace('\\', '/'));
		}
	}
	
	public boolean dataHasChanged()
	{
		if(targetPanel.dataHasChanged())
		{
			return true;
		}
		
		if(diskPanel.dataHasChanged())
		{
			return true;
		}
		
		if(diskSetupPanel.dataHasChanged())
		{
			return true;
		}
		
		if(paramPanel.dataHasChanged())
		{
			return true;
		}

		return false;
	}
	
	public void refreshInfo()
	{
		// fetch current info
		if(targetData == null)
		{
			targetData = new TargetData();
		}
		targetPanel.setInfoToTargetData(targetData);
		diskPanel.setInfoToTargetData(targetData);
		diskSetupPanel.setInfoToTargetData(targetData);
		paramPanel.setInfoToTargetData(targetData);
		
		// set current info
		setTargetData(targetData);
	}
	
	public void setTargetData(TargetData data)
	{
		targetData = data;
		if(data != null)
		{
			txtName.setText(data.getName());
		}
		targetPanel.getInfoFromTargetData(data);
		diskPanel.getInfoFromTargetData(data);
		diskSetupPanel.getInfoFromTargetData(data);
		paramPanel.getInfoFromTargetData(data);
		
		targetPanel.repaint();
		diskPanel.repaint();
		diskSetupPanel.repaint();
		paramPanel.repaint();
	}
	
	protected File getTargetDir()
	{
		if(targetDir == null)
		{
			targetDir = new File(props.getWorkingDir());
		}
		
		return targetDir;
	}
	
	public void saveInfo()
	{
		if(targetData == null)
		{
			targetData = new TargetData();
		}
		
		targetPanel.setInfoToTargetData(targetData);
		diskPanel.setInfoToTargetData(targetData);
		diskSetupPanel.setInfoToTargetData(targetData);
		paramPanel.setInfoToTargetData(targetData);
		
		getTargetDir();
		
		String name = getTargetName();
		targetData.setName(name);
		
		String outDir = targetDir.getAbsolutePath();
		outDir = outDir.replace('\\', '/');
		if(!outDir.endsWith("/")) outDir += "/";
		
		String tgtName = outDir + name + ".tgt";
		
		// copy temporary image to target image
		String origImg = targetData.getImageFile();
		if(origImg != null)
		{
			String ext = FileUtil.getExtension(origImg);
			String newImg = outDir + name + "." + ext;
			
			targetData.setImageFile(newImg);
			
			// don't overwrite existing image
			if(!origImg.equals(newImg))
			{
				FileUtil.copyFile(origImg, newImg);
			}
			
			if(!diskPanel.isImageSet())
			{
				String tgtImg = outDir + name + ".tgt.png";
				BufferedImage bi = targetPanel.thumbnailEditor.imagePanel.getImage();
				bi = ImageProcessor.convertToGray(bi);
				ImageProcessor.writeImage(bi, tgtImg);
				targetData.setTargetImageFile(tgtImg);
				diskPanel.setImage(bi);
				diskPanel.posHelper.updateModelInfo(targetData);
				diskSetupPanel.setImage(bi);
			}
		}
		
		targetData.save(tgtName);
	}
	
	public String getTargetName()
	{
		String name = txtName.getText().trim();
		
		name = name.replace(' ', '_');
		
		txtName.setText(name);
		return name;
	}
	
	public void makeNewTarget()
	{
		
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
	
	public void exitApp()
	{
		// TODO consider checking for save
		System.exit(0);
	}
	
	public class SaveAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public SaveAction()
		{
			super("Save");
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
			super("Open");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			openExistingTarget();
		}
	}
	
	public class NewAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public NewAction()
		{
			super("New");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			makeNewTarget();
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
	
	public JMenuBar buildMenu()
	{
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem exitItem = new JMenuItem("Exit");

		newItem.setAction(new NewAction());
		openItem.setAction(new OpenAction());
		saveItem.setAction(new SaveAction());
		exitItem.setAction(new ExitAction());
		
		fileMenu.add(newItem);
		fileMenu.addSeparator();
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		bar.add(fileMenu);
		
		JMenu prefsMenu = new JMenu("Preferences");
		JMenuItem editItem = new JMenuItem("Edit Preferences");
		prefsMenu.add(editItem);
		
		//bar.add(prefsMenu);
		
		return bar;
	}

	@Override
	public void newImageAvailable(Object src, BufferedImage bi, double ra, double dec, double arcmin) 
	{
		if(src == targetPanel || src == targetPanel.thumbnailEditor)
		{
			diskPanel.setImageSet(false);
			diskPanel.setImage(null);
			diskSetupPanel.setImageSet(false);
			diskSetupPanel.setImage(null);
			double width = arcmin/60.0d;
			diskPanel.posHelper.updateModelInfo(bi.getWidth(), bi.getHeight(), ra, dec, width, width);
			if(targetData != null)
			{
				targetData.setTargetImageFile(null);
			}
		}		
		// we could also do a convert to gray scale now though ???
	}
	

	@Override
	public void stateChanged(ChangeEvent e) 
	{
		Object src = e.getSource();
		if(src == tabs)
		{
			if(dataHasChanged())
			{
				refreshInfo();
			}
		}
	}
	
	public static void initFrame()
	{
		//System.setProperty("apple.laf.useScreenMenuBar", "true");

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
