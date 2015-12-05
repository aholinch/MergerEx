/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.xml;

/**
 * Minimal support for writing an XmlTag object to XML string.
 * 
 * @author aholinch
 *
 */
public class XmlWriter
{
	public static final String DEFAULT_INDENT = "  ";
    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
	protected String newLine = "\n";
	
	public XmlWriter()
	{
		
	}
	
	public String writeXmlDoc(XmlTag tag)
	{
		return HEADER + newLine + writeXml(tag);
	}
	
	public String writeXml(XmlTag tag)
	{
	    return writeXml(tag, "", DEFAULT_INDENT);	
	}
	
    public String writeXml(XmlTag tag, String indent, String indentIncrement)
    {
    	StringBuffer sb = new StringBuffer(100);
    	
    	sb.append(indent);
    	sb.append("<");
    	sb.append(tag.getName());
    	sb.append(">");
    	
    	int nc = tag.getNumChildren();
    	if(nc > 0)
    	{
    		sb.append(newLine);
    		if(tag.getValue() != null)
    		{
        		sb.append(indent);
        		sb.append(indentIncrement);
    			sb.append(tag.getValue().trim());
        		sb.append(newLine);    			
    		}
    		
    		String tmp = null;
    		for(int i=0; i<nc; i++)
    		{
    			tmp = writeXml(tag.getChild(i),indent+indentIncrement,indentIncrement);
    			
    			sb.append(tmp);
    			sb.append(newLine);
    		}
    		sb.append(indent);
    	}
    	else
    	{
    		if(tag.getValue() != null)
    		{
    			sb.append(tag.getValue().trim());
    		}
    	}
    	
    	sb.append("</");
    	sb.append(tag.getName());
    	sb.append(">");
    	
    	return sb.toString();
    }
}
