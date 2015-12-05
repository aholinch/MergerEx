/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import java.awt.image.BufferedImage;

public interface ImageListener 
{
    public void newImageAvailable(Object src, BufferedImage bi, double raDeg, double decDeg, double arcMinWidth);
}
