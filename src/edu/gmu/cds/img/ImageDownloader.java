/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.img;

import edu.gmu.cds.sdss.SDSSCasQuery;
import edu.gmu.cds.util.FileUtil;
import edu.gmu.cds.util.URLUtil;

public class ImageDownloader 
{
	public static final int DR8 = 1;
	public static final int DR7 = 2;
	public static final int DSS_POSS1_RED = 3;
	public static final int DSS_POSS1_BLUE = 4;
	public static final int DSS_POSS2_RED = 5;
	public static final int DSS_POSS2_BLUE = 6;
	public static final int DSS_POSS2_IR = 7;
	public static final int DR9 = 8;
	public static final int DR17 = 9;
	
	/*
    <option value="poss2ukstu_red" selected>POSS2/UKSTU Red</option>
    <option value="poss2ukstu_blue" >POSS2/UKSTU Blue</option>
    <option value="poss2ukstu_ir" >POSS2/UKSTU IR</option>
    <option value="poss1_red" >POSS1 Red</option>
    <option value="poss1_blue" >POSS1 Blue</option>
    <option value="quickv" >Quick-V</option>
    <option value="phase2_gsc2" >HST Phase 2 (GSC2)</option>
    <option value="phase2_gsc1" >HST Phase 2 (GSC1)</option>
    </select>
    */
	public static final String DR8CASIMGURL = "http://skyservice.pha.jhu.edu/DR8/ImgCutout/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
    public static final String DR7CASIMGURL = "http://casjobs.sdss.org/ImgCutoutDR7/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
    //public static final String MAST_DSSURL  = "http://stdatu.stsci.edu/cgi-bin/dss_search?v=PLATE&r=RA&d=DEC&e=J2000&h=NY&w=NX&f=gif&c=none&fov=NONE&v3=";
    public static final String MAST_DSSURL  = "https://archive.stsci.edu/cgi-bin/dss_search?v=PLATE&r=RA&d=DEC&e=J2000&h=NY&w=NX&f=gif&c=none&fov=NONE&v3=";
    public static final String DR9CASIMGURL = "http://skyservice.pha.jhu.edu/DR9/ImgCutout/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
	public static final String DR17CASIMGURL = SDSSCasQuery.CASIMGURL_DR17;
    /**
     * 
     * @param type
     * @param ra
     * @param dec
     * @param filename
     */
    public static String[] downloadImageByRaDec(int type, double ra, double dec, String filename)
	{
		return downloadImageByRaDec(type,ra,dec,filename,0.25,300,300);
	}
	
    /**
     * Specify a preferred scale.  Will work to ensure image width though scale may be altered.
     * 
     * @param type
     * @param ra
     * @param dec
     * @param filename
     * @param scale
     * @param widthArcMin
     * @return
     */
    public static String[] downloadImageByRaDec(int type, double ra, double dec, String filename, double scale, double widthArcMin)
    {
    	int nx = 0;
    	int ny = 0;
    	
    	nx = ny = (int)(widthArcMin*60.0d/scale);
    	switch(type)
		{
    	    // sdss needs to ensure not a huge image
			case DR8:
			case DR7:
			case DR9:
			case DR17:
				if(nx > 1024)
				{
					scale = (widthArcMin*60.0d/1024.0d);
					nx = ny = 1024;
				}
				break;
		}
    	
        return downloadImageByRaDec(type,ra,dec,filename,scale,nx,ny);	
    }
    
	/**
	 * 
	 * @param type
	 * @param ra
	 * @param dec
	 * @param filename
	 * @param scale arcsecond per pixel
	 * @param nx
	 * @param ny
	 */
	public static String[] downloadImageByRaDec(int type, double ra, double dec, String filename, double scale, int nx, int ny)
	{
		double dnx = 0;
		double dny = 0;
		
		String url = DR8CASIMGURL;
		String ext = "jpg";
		
		boolean convertPixToAngles = false;
		
		switch(type)
		{
			case DR8:
			default:
				url = DR8CASIMGURL;
				ext = "jpg";
				break;
			case DR7:
				url = DR7CASIMGURL;
				ext = "jpg";
				break;
			case DR9:
				url = DR9CASIMGURL;
				ext = "jpg";
				break;
			case DR17:
				url = DR17CASIMGURL;
				ext = "jpg";
				break;
			case DSS_POSS1_RED:
				url = MAST_DSSURL.replaceAll("PLATE", "poss1_red");
				ext = "gif";
				convertPixToAngles = true;
			case DSS_POSS1_BLUE:
				url = MAST_DSSURL.replaceAll("PLATE", "poss1_blue");
				ext = "gif";
				convertPixToAngles = true;
			case DSS_POSS2_RED:
				url = MAST_DSSURL.replaceAll("PLATE", "poss2ukstu_red");
				ext = "gif";
				convertPixToAngles = true;
			case DSS_POSS2_BLUE:
				url = MAST_DSSURL.replaceAll("PLATE", "poss2ukstu_blue");
				ext = "gif";
				convertPixToAngles = true;
			case DSS_POSS2_IR:
				url = MAST_DSSURL.replaceAll("PLATE", "poss2ukstu_ir");
				ext = "gif";
				convertPixToAngles = true;
		}
		
		filename = FileUtil.ensureExtension(filename, ext);
		
		if(convertPixToAngles)
		{
		    // we need to express dnx and dny as arcminutes
			dnx = scale*nx/60.0d;
			dny = scale*ny/60.0d;
		}
		
		url = url.replaceAll("RA",String.valueOf(ra));
		url = url.replaceAll("DEC",String.valueOf(dec));
		url = url.replaceAll("SCALE",String.valueOf(scale));
		
		if(!convertPixToAngles)
		{
			url = url.replaceAll("NX",String.valueOf(nx));
			url = url.replaceAll("NY",String.valueOf(ny));
		}
		else
		{
			// instead of pixels, it is actually arcminutes
			url = url.replaceAll("NX",String.valueOf(dnx));
			url = url.replaceAll("NY",String.valueOf(dny));			
		}
		
		//System.out.println(url);
        URLUtil.downloadURL(url, filename);
        
        return new String[]{filename,url};
    }

}
