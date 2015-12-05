/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.helper;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardHelper 
{
    public static void copyStringToSystemClipboard(String str)
    {
    	Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    	StringSelection strSel = new StringSelection(str);
    	clip.setContents(strSel, null);
    }
    
    public static void copyImageToSystemClipboard(Image image)
    {
    	Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    	ImageSelection imgSel = new ImageSelection(image);
    	clip.setContents(imgSel, null);
    }
    
    public static class ImageSelection implements Transferable
    {
    	protected Image image;
    	
    	public ImageSelection(Image image)
    	{
    		this.image = image;
    	}
    	
		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			return image;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{DataFlavor.imageFlavor};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if(flavor == null || !flavor.equals(DataFlavor.imageFlavor))
			{
				return false;
			}
			return true;
		}
    	
    }
}
