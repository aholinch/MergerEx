/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Abstract implementation of a managed properties object.
 * It can load and save from the user's home directory.
 * 
 * @author aholinch
 *
 */
public abstract class BaseProperties
{
    protected String defaultDir = null;
    protected String defaultFilename = null;
    
    /**
     * Return the property value as a boolean.
     * 
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanProperty(Properties props, String key, boolean defaultValue)
    {
    	boolean val = defaultValue;
    	
    	String value = props.getProperty(key);
    	if(value != null)
    	{
    		value = value.toLowerCase().trim();
    		if(value.length() > 0)
    		{
    			char c = value.charAt(0);
    			if(c == 't' || c == 'y')
    			{
    				val = true;
    			}
    			else
    			{
    				val = false;
    			}
    		}
    	}
    	
    	return val;
    }
    
    /**
     * Set the boolean property.
     * 
     * @param props
     * @param key
     * @param value
     */
    public void setBooleanProperty(Properties props, String key, boolean value)
    {
    	String str = "false";
    	
    	if(value)
    	{
    		str = "true";
    	}
    	
    	props.setProperty(key,str);
    }
    
    /**
     * Save the properties to the default file.
     */
    public void save()
    {
    	initDir();
    	
    	File fdir = new File(getDefaultDir());
    	if(!fdir.exists())
    	{
    		try
    		{
    			fdir.mkdirs();
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    	
    	save(getDefaultDir() + getDefaultFilename());
    }
    
    /**
     * Save the properties to the file.
     * 
     * @param filename
     */
    public void save(String filename)
    {
    	Properties props = getProperties();
    	
    	FileOutputStream fos = null;
    	try
    	{
    		fos = new FileOutputStream(filename);
    		props.store(fos,getComments());
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		FileUtil.close(fos);
    	}
    }
    
    /**
     * Initialize the value of DIR.
     */
    protected void initDir()
    {
    	// get user home directory
    	if(getDefaultDir() == null)
    	{
    		String tmpDir = System.getProperty("user.home");
    		tmpDir = tmpDir.replace('\\','/');
    		if(!tmpDir.endsWith("/"))
    		{
    			tmpDir += "/";
    		}
    		
    	    setDefaultDir(tmpDir  + ".jspam/");
    	}
    }
    
    /**
     * Load the properties from their default location.
     * This is in ~user/.jspam/jspam.properties
     *
     */
    public void load()
    {
    	initDir();
    	String filename = getDefaultDir()+getDefaultFilename();
    	load(filename);
    }
    
    /**
     * Determines whether the properties file exists at
     * default location.
     * 
     * @return
     */
    public boolean exists()
    {
    	boolean exists = false;
    	String filename = getDefaultDir()+getDefaultFilename();
    	
    	try
    	{
    		File f = new File(filename);
    		if(f.exists())
    		{
    			exists = true;
    		}
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	
    	return exists;
    }
    
    /**
     * Load the properties from the specified file.
     * 
     * @param filename
     */
    public void load(String filename)
    {
    	Properties props = PropertyUtil.getProperties(filename);
    	if(props == null)
    	{
    		props = PropertyUtil.getProperties(getDefaultFilename());
    	}
    	if(props == null)
    	{
    		// no properties found
    		setDefaults();
    		props = getProperties();
    		save();
    	}
    	load(props);
    }
    
    /**
     * Will return null if not initialized.
     * 
     * @return
     */
    public String getDefaultDir()
    {
    	return defaultDir;
    }
    
    /**
     * Set the default dir.
     * 
     * @param dir
     */
    public void setDefaultDir(String dir)
    {
    	defaultDir = dir;
    }
    
    /**
     * Should not return null;
     * 
     * @return
     */
    public String getDefaultFilename()
    {
    	return defaultFilename;
    }
    
    /**
     * Returns the values of this object as a properties object.
     * 
     * @return
     */
    public abstract Properties getProperties();
    
    /**
     * Sets the values to this object from the properties.
     * 
     * @param props
     */
    public abstract void load(Properties props);
    
    /**
     * Set the default values.
     */
    public abstract void setDefaults();
    
    /**
     * Get the comment string if any to go in the file.
     * 
     * @return
     */
    public abstract String getComments();
}
