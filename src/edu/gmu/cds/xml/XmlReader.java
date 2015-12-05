/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.xml;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * Parses an XML document into tags.  Does not enforce, nor depend, on
 * schemas or DTDs.
 * 
 * @author aholinch
 *
 */
public class XmlReader
{
    public XmlReader()
    {
    	
    }
    
    public XmlTag parseTag(File file)
    {
    	StringBuffer sb = new StringBuffer(1000);
    	
    	FileReader fr = null;
    	try
    	{
    		fr = new FileReader(file);
    		LineNumberReader lnr = new LineNumberReader(fr);
    		String line = lnr.readLine();
    		
    		while(line != null)
    		{
    			sb.append(line);
    			sb.append("\n");
    			line = lnr.readLine();
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(fr != null)try{fr.close();}catch(Exception ex){};
    	}
    	
    	return parseTag(sb.toString());
    }
    
    /**
     * Parse the first tag, and any children, found in this string.
     * 
     * @param str
     * @return
     */
    public XmlTag parseTag(String str)
    {
    	return parseTag(str, null);
    }
    
    /**
     * Parse the first occurence of tag, and any children, found in this string.
     * 
     * @param str
     * @param name
     * @return
     */
    public XmlTag parseTag(String str, String name)
    {
    	XmlTag tag = new XmlTag();
    	
    	// let's get past any header first
    	int ind = str.indexOf("?>");
    	if(ind > -1)
    	{
    		str = str.substring(ind+2).trim();
    	}
    	
    	ind = 0;
    	
    	ind = parseTag(str,name,tag,ind);
    	
    	return tag;
    }
    
    /**
     * Begins at startIndex looking for the specified <name> and populates
     * the given XmlTag.  If name is null, the next <text> is used as the name.
     * 
     * @param str
     * @param name
     * @param tag
     * @param startIndex
     * @return
     */
    protected int parseTag(String str, String name, XmlTag tag, int startIndex)
    {
    	int endIndex = startIndex;
    	
    	int ind = startIndex;
    	int ind2 = ind;
    	int ind3 = ind;
    	
    	// find name if needed
    	if(name == null)
    	{
    	    ind = str.indexOf("<",startIndex);
    	    if(ind == -1)
    	    {
    	    	return endIndex;
    	    }
    	    
    	    ind2 = str.indexOf(">",ind);
    	    if(ind2 == -1)
    	    {
    	    	return endIndex;
    	    }
    	    
    	    ind3 = str.indexOf(" ", ind);
    	    
    	    if(ind3 > -1 && ind3 < ind2)
    	    {
    	    	ind2 = ind3;
    	    }

    	    name = str.substring(ind+1,ind2).trim();	
    	}
    	
    	// set name
    	tag.setName(name);
    	
    	// find end of start tag
    	ind = str.indexOf(">",startIndex)+1;

    	// find start of end tag
    	ind2 = str.indexOf("</"+name+">",ind);
    	ind3 = ind2;

    	// we now have the value text
    	String value = str.substring(ind,ind2).trim();
    	
    	// check value for children tags
    	if(value.indexOf('<') > -1)
    	{
    	    // we have some number of children tags
    		XmlTag child = null;
    		
    		ind = value.length();
    		ind2 = 0;
    		
    		while(ind2 < ind)
    		{
    			child = new XmlTag();
    			
    			ind2 = parseTag(value,null,child,ind2);
    			if(child.getName() != null)
    			{
    				tag.addChild(child);
    			}
    		}
    	}
    	else
    	{
    		tag.setValue(value);
    	}
    	
    	// calculate the end index for this tag
    	endIndex =  ind3 + 3 + name.length();
    	
    	return endIndex;
    }
}
