/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLUtil 
{
    public static void downloadURL(String urlStr, String filename)
    {
    	URLConnection conn = null;
    	URL url = null;
    	InputStream is = null;
    	try
    	{
    		url = new URL(urlStr);
    		conn = url.openConnection();
    		is = conn.getInputStream();
    		FileUtil.dumpStreamToFile(is, filename);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		FileUtil.close(is);
    	}
    }
    
    public static String getURLContent(String urlStr)
    {
    	ByteArrayOutputStream baos = null;
    	String out = null;
    	URLConnection conn = null;
    	URL url = null;
    	InputStream is = null;
    	try
    	{
    		url = new URL(urlStr);
    		conn = url.openConnection();
    		is = conn.getInputStream();
    		baos = new ByteArrayOutputStream(4000);
    		FileUtil.dumpStreamToStream(is, baos);
    		baos.flush();
    		out = baos.toString("UTF8");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		FileUtil.close(is);
    		FileUtil.close(baos);
    	}
    	return out;
    }
}
