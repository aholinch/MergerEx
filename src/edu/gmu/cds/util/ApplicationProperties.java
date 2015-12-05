/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.Color;
import java.util.Properties;

import edu.gmu.cds.obj.ObjectQuery;

public class ApplicationProperties extends BaseProperties 
{
	public static final String WORKING_DIR   = "workingdir";
	public static final String IMAGE_SRC     = "imagesrc";
	public static final String PART_COLOR    = "partcolor";
	public static final String PART_SIZE     = "partsize";
	public static final String PART_COUNT    = "partcount";
	public static final String ANIMATE       = "animate";
	public static final String SHOW_STEREO   = "stereo";
		
    public String workingDir = null;
    public String imageSrc = null;
    public Color particleColor = null;
    public int particleSize = 2;
    public int particleCount = 2000;
    public boolean animateRandom = true;
    public boolean showStereo = false;
    
    private static String sync = "mutex";
    
    private static ApplicationProperties props = null;
    
    private ApplicationProperties()
    {
    	setDefaults();
    }
    
    public static ApplicationProperties getInstance()
    {
    	synchronized(sync)
    	{
    		if(props == null)
    		{
    			props = new ApplicationProperties();
    			props.load();
    		}
    	}
    	
    	return props;
    }
    
	@Override
	public String getComments() 
	{
		return "Merger UI prefs";
	}

	@Override
	public Properties getProperties() 
	{
		Properties props = new Properties();
		props.setProperty(WORKING_DIR, workingDir);
		props.setProperty(IMAGE_SRC, imageSrc);
		if(particleColor != null)
		{
			String str = String.valueOf(particleColor.getRed());
			str +=","+String.valueOf(particleColor.getGreen());
			str +=","+String.valueOf(particleColor.getBlue());
			str +=","+String.valueOf(particleColor.getAlpha());
			props.setProperty(PART_COLOR, str);
		}
		props.setProperty(PART_SIZE, String.valueOf(particleSize));
		props.setProperty(PART_COUNT, String.valueOf(particleCount));
		props.setProperty(ANIMATE, String.valueOf(animateRandom));
		props.setProperty(SHOW_STEREO, String.valueOf(showStereo));
		
		return props;
	}

	@Override
	public void load(Properties props) 
	{
		workingDir = props.getProperty(WORKING_DIR);
		if(!workingDir.endsWith("/")) workingDir += "/";
		imageSrc = props.getProperty(IMAGE_SRC);
		String tmp = props.getProperty(PART_COLOR);
		if(tmp != null)
		{
			try
			{
				String sa[] = tmp.split(",");
				int r,g,b,a;
				r = Integer.parseInt(sa[0]);
				g = Integer.parseInt(sa[1]);
				b = Integer.parseInt(sa[2]);
				
				if(sa.length == 3)
				{
					particleColor = new Color(r,g,b);
				}
				else if(sa.length == 4)
				{
					a = Integer.parseInt(sa[3]);
					particleColor = new Color(r,g,b,a);					
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		particleSize = PropertyUtil.getInt(props, PART_SIZE, particleSize);
		particleCount = PropertyUtil.getInt(props, PART_COUNT, particleCount);
		animateRandom = PropertyUtil.getBoolean(props, ANIMATE, animateRandom);
		showStereo = PropertyUtil.getBoolean(props, SHOW_STEREO, showStereo);
	}

	@Override
	public void setDefaults() 
	{
		initDir();
		defaultFilename="merger.props";
		workingDir = getDefaultDir();
		imageSrc = "DR7";
		particleColor = new Color(255,255,255,50);
		particleSize = 2;
	}
	
	public String getWorkingDir()
	{
		return workingDir;
	}
	
	public void setWorkingDir(String dir)
	{
		workingDir = dir;
		if(workingDir != null)
		{
			workingDir = workingDir.replace('\\', '/');
		}
	}
	
	public String getImageSource()
	{
		return imageSrc;
	}
	
	public void setImageSource(String src)
	{
		imageSrc = src;
	}
	
	public int getImageSourceType()
	{
		int type = 0;
		
		if("DR7".equals(imageSrc))
		{
			type = ObjectQuery.SDSSDR7;
		}
		else if("DR8".equals(imageSrc))
		{
			type = ObjectQuery.SDSSDR8;
		}
		else if("DR9".equals(imageSrc))
		{
			type = ObjectQuery.SDSSDR9;
		}
		else if("NED".equals(imageSrc))
		{
			type = ObjectQuery.NED;
		}
		
		return type;
	}

	public void setParticleColor(Color color)
	{
		particleColor = color;
	}
	
	public Color getParticleColor()
	{
		return particleColor;
	}
	
	public void setParticleSize(int size)
	{
		particleSize = size;
	}
	
	public int getParticleSize()
	{
		return particleSize;
	}
	
	public void setParticleCount(int cnt)
	{
		particleCount = cnt;
	}
	
	public int getParticleCount()
	{
		return particleCount;
	}
	
	public void setAnimateRandom(boolean flag)
	{
		animateRandom = flag;
	}
	
	public boolean getAnimateRandom()
	{
		return animateRandom;
	}
	
	public void setShowStereo(boolean flag)
	{
		showStereo = flag;
	}
	
	public boolean getShowStereo()
	{
		return showStereo;
	}
}
