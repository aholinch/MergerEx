/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utilities for using properties.
 */
public class PropertyUtil
{
	/**
	 * Get the properties file.
	 * 
	 * @param filename
	 * @return
	 */
    public static Properties getProperties(String filename)
    {
    	Properties props = null;
    	
    	InputStream is = FileUtil.getInputStream(filename);
    	
    	if(is != null)
    	{
	    	try
	    	{
	    		props = new Properties();
	    		props.load(is);
	    	}
	    	catch(Exception ex)
	    	{
	    		
	    	}
    	}
    	
    	return props;
    }
    
    public static int parseInt(String val)
    {
    	int i = 0;
    	
    	if(val != null)
    	{
    		try
    		{
    			i = Integer.parseInt(val);
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    	return i;
    }
    
    /**
     * Get the integer property.
     * 
     * @param props
     * @param prop
     * @return
     */
    public static int getInt(Properties props, String prop)
    {
    	return getInt(props,prop,0);
    }
    
    public static int getInt(Properties props, String prop, int defaultVal)
    {
    	int i = defaultVal;
    	
    	String val = props.getProperty(prop);
    	
    	if(val != null)
    	{
    		i = parseInt(val);
    	}
    	
    	return i;
    }
    
    public static double parseDouble(String val)
    {
    	double i = 0;
    	
    	if(val != null)
    	{
    		try
    		{
    			i = Double.parseDouble(val);
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    	return i;
    }
    
    /**
     * Get the double property.
     * 
     * @param props
     * @param prop
     * @return
     */
    public static double getDouble(Properties props, String prop)
    {
    	return getDouble(props,prop,0);
    }
    
    public static double getDouble(Properties props, String prop, double defaultVal)
    {
    	double i = defaultVal;
    	
    	String val = props.getProperty(prop);
    	
    	if(val != null)
    	{
    		i = parseDouble(val);
    	}
    	
    	return i;
    }
    
    public static boolean getBoolean(Properties props, String prop)
    {
    	return getBoolean(props,prop,false);
    }
    
    public static boolean getBoolean(Properties props, String prop, boolean defaultVal)
    {
    	boolean flag = defaultVal;
    	String val = props.getProperty(prop);
    	if(val != null && val.length() > 0)
    	{
    	    char c = val.toLowerCase().charAt(0);
    	    if(c == 't' || c == 'y' || c == '1')
    	    {
    	    	flag = true;
    	    }
    	    else
    	    {
    	    	flag = false;
    	    }
    	}
    	return flag;
    }
}
