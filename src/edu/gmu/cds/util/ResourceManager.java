/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Used to manage the resource bundle for applications.
 * 
 * @author aholinch
 *
 */
public class ResourceManager 
{
	public static final String sync = new String("sync");
	
	private static ResourceManager instance = null;
	
	private HashMap<String,Locale> localesByResource;
	
	private List<ResourceListener> listeners = null;
	
	private String defaultResourceName = "MergerEx";
	
	/**
	 * Private constructor for singleton.
	 */
    private ResourceManager()
    {
    	listeners = new ArrayList<ResourceListener>();
    	localesByResource = new HashMap<String,Locale>();
    }
    
    /**
     * getInstance method for ResourceManager
     * 
     * @return
     */
    public static ResourceManager getInstance()
    {
    	synchronized(sync)
    	{
    		if(instance == null)
    		{
    			instance = new ResourceManager();
    		}
    	}
    	
    	return instance;
    }
    
    /**
     * Set the default resource name.
     * 
     * @param resourceName
     */
    public void setDefaultResourceName(Locale locale, String resourceName)
    {
    	defaultResourceName = resourceName;
    	localesByResource.put(resourceName,locale);
    }
    
    /**
     * Set the current locale.  Will trigger update of localized resources.
     * 
     * @param locale
     */
    public void setLocale(Locale locale, String resourceName)
    {
    	if(locale != null && resourceName != null)
    	{
    		Locale currentLocale = (Locale)localesByResource.get(resourceName);
    		if(currentLocale == null || !locale.getDisplayName().equals(currentLocale.getDisplayName()))
    		{
    			localesByResource.put(resourceName,locale);
    			updateLocalizedResources();
    		}
    	}
    }
    
    /**
     * Return the current locale.
     * 
     * @return
     */
    public Locale getCurrentLocale(String resourceName)
    {
    	return (Locale)localesByResource.get(resourceName);
    }
    
    public ResourceBundle getDefaultResourceBundle()
    {
    	return getCurrentResourceBundle(defaultResourceName);
    }
    
    /**
     * Return the resource bundle for the current locale.
     * 
     * @return
     */
    public ResourceBundle getCurrentResourceBundle(String resourceName)
    {
    	ResourceBundle rb = null;
    	Locale locale = (Locale)localesByResource.get(resourceName);

    	rb = getBundle(resourceName, locale);
    	
    	return rb;
    }
    
    /**
     * Informs listeners that they should update their localized resources. 
     */
    protected void updateLocalizedResources()
    {
    	updateLocalizedResourcesImpl();
    }
    
    /**
     * Actual implementation for updating localized resources.  It can be threaded.
     */
    private void updateLocalizedResourcesImpl()
    {
    	ResourceBundle rb = getCurrentResourceBundle(defaultResourceName);
    	int size = listeners.size();
    	ResourceListener rl = null;
    	for(int i=0; i<size; i++)
    	{
    		rl = (ResourceListener)listeners.get(i);
    		rl.updateLocalizedResources(rb);
    	}
    }
    
    /**
     * Add a listener.
     * 
     * @param rl
     */
    public void addResourceListener(ResourceListener rl)
    {
    	if(rl != null)
    	{
    		listeners.add(rl);
    	}
    }
    
    /**
     * Remove the resource listener.
     * 
     * @param rl
     */
    public void removeResourceListener(ResourceListener rl)
    {
    	if(rl != null)
    	{
    		listeners.remove(rl);
    	}
    }

    /**
     * Remove all listeners.
     */
    public void clearListeners()
    {
    	listeners.clear();
    }
    
    /**
     * Loads the properties bundle.
     * 
     * @return
     */
    protected ResourceBundle getBundle(String resourceName, Locale locale)
    {
    	ResourceBundle rb = null;
    	if(locale == null)
    	{
    		locale = Locale.getDefault();
    	}
    	
    	try
    	{
    		rb = new PropertyResourceBundle(getResourcesInputStream(resourceName,locale));
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	if(rb == null)
    	{
    		rb = ResourceBundle.getBundle(resourceName, locale);
    	}
        return rb;
    }
    
    private static ClassLoader getContextClassLoader() 
    {
        return Thread.currentThread().getContextClassLoader() != null ? Thread
            .currentThread().getContextClassLoader() : ResourceManager.class
            .getClassLoader() != null ? ResourceManager.class.getClassLoader()
            : ClassLoader.getSystemClassLoader();
    }

    private InputStream getResourcesInputStream(String resourceName, Locale locale)
	{
		InputStream is = null;
		
		is = getIS(resourceName+".properties",false);
		//is = FileUtil.getInputStream("/"+resourceName+".properties");
		if(is == null)
		{
			is = getIS("/"+resourceName+".properties",true);
			//is = FileUtil.getInputStream(resourceName+".properties");	
		}
		
		if(is == null)
		{
			is = getIS("locales/"+resourceName+".properties",true);			
		}

		if(is == null)
		{
			is = getIS("/locales/"+resourceName+".properties",true);			
		}
		
		if(is == null)
		{
			is = getIS("../locales/"+resourceName+".properties",true);
		}

		InputStream defaultIs = is;
		
		// update this later to search for other locales
		String cc = locale.getCountry();
		String lang = locale.getLanguage();
		if(lang.equals("en") && (cc.equals("US") || cc.equals("GB")))
		{
			return defaultIs;
		}
		
		if(cc != null && cc.length() > 0 && lang!= null && lang.length() > 0)
		{
			cc = "_"+lang+"_"+cc;
			//is = FileUtil.getInputStream("/" + resourceName + cc +".properties");
			is = getIS(resourceName + cc +".properties",false);
			if(is == null)
			{
				//is = FileUtil.getInputStream(resourceName + cc +".properties");
				is = getIS("/"+resourceName + cc +".properties",true);
			}
		}
		
		if(is == null)
		{
			is = defaultIs;
		}
		
		return is;
	}
    
    private InputStream getIS(String name, boolean allowFallback)
    {
    	ClassLoader loader = getContextClassLoader();
    	
    	InputStream is = null;
    	try
    	{
    		is = loader.getResourceAsStream(name);
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	
    	if(is == null && allowFallback)
    	{
    		is = FileUtil.getInputStream(name);
    	}
    	return is;
    }

	/**
     * For classes interested in when the locale changes.
     * 
     * @author aholinch
     *
     */
    public static interface ResourceListener
    {
    	public void updateLocalizedResources(ResourceBundle rb);
    }
}
