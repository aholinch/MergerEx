/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil 
{
	/**
	 * Compress the string into zip bytes.
	 * 
	 * @param name
	 * @param content
	 * @return
	 */
	public static byte[] zipString(String name, String content)
	{
		byte ba[] = content.getBytes();
		ba=zipBytes(name,ba);
		return ba;
	}
	
	/**
	 * Compress the array of bytes into a set of
	 * zip bytes.
	 * 
	 * @param ba
	 * @return
	 */
	public static byte[] zipBytes(String name, byte ba[])
	{
		byte baOut[] = null;
		
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;
		try
		{
			baos = new ByteArrayOutputStream(ba.length);
			zos = new ZipOutputStream(baos);
			ZipEntry ze = new ZipEntry(name);
			ze.setTime(System.currentTimeMillis());
			ze.setSize(ba.length);
			zos.putNextEntry(ze);
			zos.write(ba);
			zos.flush();
			zos.closeEntry();
			zos.flush();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			FileUtil.close(zos);
		}
		
		if(baos != null)
		{
			baOut = baos.toByteArray();
		}
		
		try
		{
			FileUtil.close(baos);
			baos = null;
		}
		catch(Exception ex)
		{
			
		}
		
		return baOut;
	}
	
    public static List<byte[]> getContents(String filename)
    {
    	FileInputStream fis = null;
    	
    	try
    	{
    		fis = new FileInputStream(filename);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return getContents(fis);
    }
    
    
    public static void dumpZip(String zipfile, String outputDir)
    {
    	InputStream is = null;
    	List<byte[]> contents = new ArrayList<byte[]>();
    	List<String> names = new ArrayList<String>();
    	
    	try
    	{
    		if(!outputDir.endsWith("/"))
    		{
    			outputDir += "/";
    		}
    		
    		is = FileUtil.getInputStream(zipfile);
    	    getContents(is,contents,names);
    	    int size = names.size();
    	    String name = null;
    	    FileOutputStream fos = null;
    	    ByteArrayInputStream bais = null;
    	    for(int i=0; i<size; i++)
    	    {
    	    	bais = new ByteArrayInputStream((byte[])contents.get(i));
    	    	name = (String)names.get(i);
    	    	name = outputDir+name;
    	    	fos = new FileOutputStream(name);
    	    	FileUtil.dumpStreamToStream(bais, fos);
    	    	fos.flush();
    	    	fos.close();
    	    	bais.close();
    	    	fos = null;
    	    	bais = null;
    	    }
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
    
    public static List<byte[]> getContents(InputStream is)
    {
    	List<byte[]> contents = new ArrayList<byte[]>();
    	List<String> names = new ArrayList<String>();
    	
    	getContents(is,contents,names);
    	
    	return contents;
    	//return names;
    }
    
    public static void getContents(InputStream is, List<byte[]> contents, List<String> names)
    {
    	ZipInputStream zis = null;
    	
    	if(is == null)
    	{
    		return;
    	}
    	
    	if(contents == null)
    	{
    		contents = new ArrayList<byte[]>();
    	}
    	
    	if(names == null)
    	{
    		names = new ArrayList<String>();
    	}
    	
    	try
    	{
    		zis = new ZipInputStream(is);
    		ZipEntry ze = null;
    		int nb = 0;
    		int BUF_SIZE = 2048;
    		int numRead = 0;
    		byte ba[] = new byte[BUF_SIZE];
    		ByteArrayOutputStream baos = null;
    		ze = zis.getNextEntry();
    		while(ze != null)
    		{
    			names.add(ze.getName());
    			nb = (int)ze.getSize();
    			if(nb < 0)
    			{
    				nb = 1024;
    			}
    			baos = new ByteArrayOutputStream(nb);
    			
    			numRead = zis.read(ba);
    			while(numRead > 0)
    			{
    				baos.write(ba,0,numRead);
    				numRead = zis.read(ba);
    			}
    			
    			contents.add(baos.toByteArray());
    			baos.close();
    			
    			zis.closeEntry();
    			ze = zis.getNextEntry();	
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(zis != null)try{zis.close();}catch(Exception ex){};
    	}
    }
    
}
