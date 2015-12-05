/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.gmu.cds.img.ImageDownloader;
import edu.gmu.cds.img.ImageListener;
import edu.gmu.cds.img.ImageProcessor;
import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectInfoListener;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.CursorInfoPanel;
import edu.gmu.cds.ui.ImagePanel;
import edu.gmu.cds.ui.TableLayoutPanel;
import edu.gmu.cds.ui.helper.ClipboardHelper;
import edu.gmu.cds.ui.helper.PositionInfoHelper;
import edu.gmu.cds.ui.helper.UIHelper;
import edu.gmu.cds.util.ApplicationProperties;

public class ThumbnailEditor extends JPanel implements ActionListener, ObjectInfoListener, TargetData.TargetDataHandler
{
	private static final long serialVersionUID = 1L;
	
	protected static final String RA_TIP     = "RA in dd.ddd for image center";
	protected static final String DEC_TIP    = "Dec in dd.ddd for image center";
	protected static final String WIDTH_TIP  = "Image width in arcmin";
	
	protected JPanel sidePanel = null;
	protected ImagePanel imagePanel = null;
	protected CursorInfoPanel cursorPanel = null;
	protected PositionInfoHelper posHelper = null;
	protected ImageListener imageListener = null;
	
	protected JButton btnRefresh = null;
	protected JCheckBox chkRecenter = null;
	
	protected JTextField txtRA = null;
	protected JTextField txtDec = null;
	protected JTextField txtWidth = null;
	
	protected JLabel lblRA = null;
	protected JLabel lblDec = null;
	protected JLabel lblWidth = null;
	
	protected String imageFilename = null;
	protected String imageURL = null;
	protected boolean dataChanged = false;
	
	public ThumbnailEditor()
	{
		super(new BorderLayout());
		initGUI();
		posHelper = new PositionInfoHelper(imagePanel,cursorPanel);
		posHelper.setObjectInfoListener(this);
	}
	
	public void setImageListener(ImageListener list)
	{
		imageListener = list;
	}
	
	protected void initGUI()
	{
		imagePanel = new ImagePanel();
		add(imagePanel,BorderLayout.CENTER);
		sidePanel = buildSidePanel();
		cursorPanel = new CursorInfoPanel();
		JPanel tmpPanel = new JPanel(new GridLayout(2,1));
		tmpPanel.add(sidePanel);
		tmpPanel.add(cursorPanel);
		
		UIHelper.setMinPrefSize(tmpPanel, 150, 400);

		add(tmpPanel,BorderLayout.EAST);
	}
	
	public JPanel buildSidePanel()
	{
		TableLayoutPanel panel = new TableLayoutPanel();
		double hgt = 1.0d/5.0d;
		
		// cols, rows
		double size[][] = {{2,0.3,2,0.7,2},{2,hgt,2,hgt,2,hgt,2,hgt,2,hgt,2}};
		panel.setLayout(new TableLayout(size));
		
		txtRA       = new JTextField(5);
		txtDec      = new JTextField(5);
		txtWidth    = new JTextField(5);
		
		lblRA       = new JLabel("RA:");
		lblDec      = new JLabel("Dec:");
		lblWidth    = new JLabel("Width:");
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(this);
		
		panel.add(lblRA,panel.getRC(1, 1));
		panel.add(txtRA,panel.getRC(1, 3));
		panel.add(lblDec,panel.getRC(3, 1));
		panel.add(txtDec,panel.getRC(3, 3));
		panel.add(lblWidth,panel.getRC(5, 1));
		panel.add(txtWidth,panel.getRC(5, 3));
		
		panel.add(btnRefresh,panel.getRC(7, 1, 1, 3));
		
		lblRA.setToolTipText(RA_TIP);
		txtRA.setToolTipText(RA_TIP);
		lblDec.setToolTipText(DEC_TIP);
		txtDec.setToolTipText(DEC_TIP);
		lblWidth.setToolTipText(WIDTH_TIP);
		txtWidth.setToolTipText(WIDTH_TIP);
		
		chkRecenter = new JCheckBox("Recenter on Click");
		panel.add(chkRecenter,panel.getRC(9, 1, 1, 3));
		
		txtWidth.setText("10.0");
		
		return panel;
	}
	
	public void spawnDownload()
	{
		Runnable r = new Runnable(){
			public void run()
			{
				Cursor origCursor = ThumbnailEditor.this.getCursor();
				ThumbnailEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try
				{
					downloadThumbnail();
				}
				finally
				{
					ThumbnailEditor.this.setCursor(origCursor);
				}
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
	
	public void downloadThumbnail()
	{
		double ra = UIHelper.getNum(txtRA);
		double dec = UIHelper.getNum(txtDec);
		double width = UIHelper.getNum(txtWidth);
		
		ApplicationProperties props = ApplicationProperties.getInstance();
		
        String thumbName = props.getWorkingDir();
        String src = props.getImageSource();
        int code = ImageDownloader.DSS_POSS2_RED;
        double scale = 1.0d;
        String ext = ".gif";
        if(src!=null)
        {
        	if(src.equals("DR7"))
        	{
        		scale = 0.25d;
        		code = ImageDownloader.DR7;
        		ext = ".jpg";
        	}
        	else if(src.equals("DR8"))
        	{
        		scale = 0.25d;
        		code = ImageDownloader.DR8;
        		ext = ".jpg";
        	}
        	else if(src.equals("DR9"))
        	{
        		scale = 0.25d;
        		code = ImageDownloader.DR9;
        		ext = ".jpg";
        	}
        }
        
        thumbName += "tmp" + ext;
        
        String str[] = ImageDownloader.downloadImageByRaDec(code, ra, dec, thumbName, scale, width);
        imageFilename = str[0];
        imageURL = str[1];
        ClipboardHelper.copyStringToSystemClipboard(imageURL);
        
        BufferedImage image = ImageProcessor.readImage(thumbName);
        imagePanel.setImage(image);
        imagePanel.repaint();
        dataChanged = true;
        
        // Update model - convert width to degrees
        posHelper.updateModelInfo(image.getWidth(), image.getHeight(), ra, dec, width/60.0d, width/60.0d);
        
        if(imageListener!=null)
        {
        	imageListener.newImageAvailable(this,image,ra,dec,width);
        }
	}

	@Override
	public void actionPerformed(ActionEvent evt) 
	{
		Object src = evt.getSource();
		if(src == btnRefresh)
		{
			spawnDownload();
		}
	}
	
	public void setCenterLocation(double raDeg, double decDeg)
	{
		DecimalFormat df = new DecimalFormat("0.00000");
		
		txtRA.setText(df.format(raDeg));
		txtDec.setText(df.format(decDeg));
	}

	@Override
	public void foundObject(ObjectInfo info) 
	{
		if(info == null) return;
		if(!chkRecenter.isSelected()) return;
		setCenterLocation(info.getRADeg(),info.getDecDeg());
		spawnDownload();
	}
	
	@Override
	public boolean dataHasChanged()
	{
		return dataChanged;
	}
	
	public void getInfoFromTargetData(TargetData data)
	{
		double ra = data.getCenterRA();
		double dec = data.getCenterDec();
		setCenterLocation(ra,dec);
		
		double width = data.getImageSizeArcMin();
		txtWidth.setText(String.valueOf(width));
		
		imageFilename = data.getImageFile();
		
		// load the image
        BufferedImage image = ImageProcessor.readImage(imageFilename);
        imagePanel.setImage(image);
        imagePanel.repaint();
        dataChanged = false;
        posHelper.updateModelInfo(image.getWidth(), image.getHeight(), ra, dec, width/60.0d, width/60.0d);
	}
	
	public void setInfoToTargetData(TargetData data)
	{
		data.setImageFile(imageFilename);
		double ra = UIHelper.getNum(txtRA);
		double dec = UIHelper.getNum(txtDec);
		double width = UIHelper.getNum(txtWidth);
		
		data.setCenterRA(ra);
		data.setCenterDec(dec);
		data.setImageSizeArcMin(width);
        dataChanged = false;

		BufferedImage image = imagePanel.getImage();
		if(image != null)
		{
			data.setImageHeight(image.getHeight());
			data.setImageWidth(image.getWidth());
		}
	}
}
