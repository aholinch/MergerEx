/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A very simple implementation of an XML tag.
 * 
 * @author aholinch
 *
 */
public class XmlTag
{
	/**
	 * The tag name.
	 */
    protected String name = null;
    
    /**
     * The value of the tag.
     */
    protected String value = null;
    
    /**
     * holds a list of children tag.  uses 1.4 notation for compatibility.
     */
    @SuppressWarnings("unchecked")
	protected List children = null;
    
    /**
     * A map for getting children by tag name.
     */
    @SuppressWarnings("unchecked")
	protected HashMap hmChildren = null;
    
    /**
     * Default Constructor.
     */
    @SuppressWarnings("unchecked")
	public XmlTag()
    {
    	name = "tag";
    	children = new ArrayList();
    	hmChildren = new HashMap();
    }
    
    /**
     * Accepts tag name.
     * 
     * @param name
     */
    public XmlTag(String name)
    {
    	this();
    	this.name = name;
    }
    
    /**
     * Accepts tag name and children.
     * 
     * @param name
     * @param value
     */
    public XmlTag(String name, String value)
    {
    	this(name);
    	this.value = value;
    }
    
    /**
     * Set the name for this tag.
     * 
     * @param name
     */
    public void setName(String name)
    {
    	this.name = name;
    }
    
    /**
     * Return the tag name.
     * 
     * @return
     */
    public String getName()
    {
    	return name;
    }
    
    /**
     * Sets the value of this tag.
     * 
     * @param value
     */
    public void setValue(String value)
    {
    	this.value = value;
    }
    
    public void setValue(int value)
    {
    	this.value = String.valueOf(value);
    }
    
    public void setValue(long value)
    {
    	this.value = String.valueOf(value);
    }
    
    public void setValue(double value)
    {
    	this.value = String.valueOf(value);
    }
    
    public void setValue(boolean value)
    {
    	if(value)
    	{
    		this.value = "true";
    	}
    	else
    	{
    		this.value = "false";
    	}
    }
    
    /**
     * Gets the value of this tag.
     * 
     * @return
     */
    public String getValue()
    {
    	return value;
    }
    
    /**
     * Return the value as an int.
     * 
     * @return
     */
    public int getIntValue()
    {
    	int num = 0;
    	try{num = Integer.parseInt(value.trim());}catch(Exception ex){};
    	return num;
    }
    
    /**
     * Return the value as a long.
     * 
     * @return
     */
    public long getLongValue()
    {
    	long num = 0;
    	try{num = Long.parseLong(value.trim());}catch(Exception ex){};
    	return num;
    }
    
    /**
     * Return the value as a double.
     * 
     * @return
     */
    public double getDoubleValue()
    {
    	double num = 0;
    	try{num = Double.parseDouble(value.trim());}catch(Exception ex){};
    	return num;
    }
    
    public boolean getBooleanValue()
    {
    	if(value == null || value.trim().length() == 0)
    	{
    		return false;
    	}
    	
    	String str = value.trim().toLowerCase();
    	if(str.charAt(0) == 't')
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Add a child.
     * 
     * @param child
     */
    @SuppressWarnings("unchecked")
	public void addChild(XmlTag child)
    {
    	children.add(child);
    	if(child != null)
    	{
    		String name = child.getName();
    		List tmp = (List)hmChildren.get(name);
    		if(tmp == null)
    		{
    			tmp = new ArrayList(1);
    			hmChildren.put(name,tmp);
    		}
    		tmp.add(child);
    	}
    }
    
    /**
     * Returns the first child tag with the given name.
     * 
     * @param name
     * @return
     */
    public XmlTag getChild(String name)
    {
    	return getChild(name,false);
    }
    
    @SuppressWarnings("unchecked")
	public XmlTag getChild(String name, boolean tryCaseInsensitive)
    {
    	XmlTag tag = null;
    	
    	List list = getChildren(name);
    	if(list == null && tryCaseInsensitive)
    	{
    		list = getChildren(name.toLowerCase());
    	}
    	if(list != null && list.size() > 0)
    	{
    		tag = (XmlTag)list.get(0);
    	}
    	
    	return tag;
    }
    
    /**
     * Returns the list of children with that tag name.
     * 
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
	public List getChildren(String name)
    {
    	return (List)hmChildren.get(name);
    }
    
    /**
     * Get the number of children.
     * 
     * @return
     */
    public int getNumChildren()
    {
    	return children.size();
    }
    
    /**
     * Get a specific child.
     * 
     * @param i
     * @return
     */
    public XmlTag getChild(int i)
    {
    	return (XmlTag)children.get(i);
    }
    
    /**
     * Remove all children from this tag.
     */
    public void clearChildren()
    {
    	children.clear();
    	hmChildren.clear();
    }
    
    public String toString()
    {
    	return "name="+name +", value="+value +", numChildren=" + String.valueOf(getNumChildren());
    }
}
