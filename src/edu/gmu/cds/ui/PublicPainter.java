/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.awt.Graphics;

public interface PublicPainter 
{
	public void paintComponent(Graphics gr);    
	public void paintComponent(Graphics gr, int width, int height);    
}
