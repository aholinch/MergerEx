/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.target;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import edu.gmu.cds.img.ImageListener;
import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.obj.ObjectInfoListener;
import edu.gmu.cds.sim.TargetData;
import edu.gmu.cds.ui.ObjectLocationQueryPanel;

public class TargetPanel extends JPanel implements ObjectInfoListener, TargetData.TargetDataHandler
{
	private static final long serialVersionUID = 1L;

	protected ObjectLocationQueryPanel objectQueryPanel;
	protected ThumbnailEditor thumbnailEditor;
	
	
	public TargetPanel()
	{
		super(new BorderLayout());
		objectQueryPanel = new ObjectLocationQueryPanel();
		thumbnailEditor = new ThumbnailEditor();
		
		objectQueryPanel.setObjectInfoListener(this);
		
		
		add(thumbnailEditor,BorderLayout.CENTER);
		add(objectQueryPanel,BorderLayout.NORTH);
	}

	public void setImageListener(ImageListener list)
	{
		thumbnailEditor.setImageListener(list);
	}
	
	@Override
	public void foundObject(ObjectInfo info) 
	{
		thumbnailEditor.setCenterLocation(info.getRADeg(),info.getDecDeg());
		thumbnailEditor.spawnDownload();
	}
	
	@Override
	public boolean dataHasChanged()
	{
		return thumbnailEditor.dataHasChanged();
	}
	
	public void getInfoFromTargetData(TargetData data)
	{
		thumbnailEditor.getInfoFromTargetData(data);
	}
	
	public void setInfoToTargetData(TargetData data)
	{
		thumbnailEditor.setInfoToTargetData(data);
	}
}
