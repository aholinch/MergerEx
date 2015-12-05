/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A set of utilities for manipulating files.
 */
public class FileUtil
{
	/**
	 * Ensure filename ends in this extension.  It will replace last
	 * extension if not matching or add it if no extension.
	 * 
	 * @param filename
	 * @param ext
	 * @return
	 */
	public static String ensureExtension(String filename, String ext)
	{
		if(!ext.startsWith("."))
		{
			ext = "."+ext;
		}
		int ind = filename.lastIndexOf('.');
		if(ind == -1)
		{
			filename += ext;
		}
		else
		{
			String str = filename.substring(ind);
			if(!str.equalsIgnoreCase(ext))
			{
				filename = filename.substring(0,ind)+ext;
			}
		}
		return filename;
	}
	
	/**
	 * Copy the contents of the first file to the second file.
	 * 
	 * @param srcFile
	 * @param dstFile
	 */
	public static void copyFile(String srcFile, String dstFile)
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		byte ba[] = new byte[10240];
		int numRead = 0;
		try
		{
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(dstFile);
			
			numRead = fis.read(ba);
			while(numRead > 0)
			{
				fos.write(ba,0,numRead);
				numRead = fis.read(ba);
			}
			
			fos.flush();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			close(fis);
			close(fos);
		}
	}
	
    /**
     * Return an input stream to the file.  Check the file system first
     * next look on the classpath.
     */
    public static InputStream getInputStream(String filename)
    {
        InputStream is = null;

        try
        {
            FileInputStream fis = new FileInputStream(filename);

            is = fis;
        }
        catch(Exception ex)
        {
            // that's OK it might be in a jar file or elsewhere on the classpath.

            //ex.printStackTrace();
        }

        // check the classpath
        if(is == null)
        {
            try
            {
                is = edu.gmu.cds.util.FileUtil.class.getResourceAsStream(filename);
            }
            catch(Exception ex)
            {
            	//ex.printStackTrace();
            }
        }

        if(is == null)
        {
            try
            {
                is = edu.gmu.cds.util.FileUtil.class.getResourceAsStream("/"+filename);
            }
            catch(Exception ex)
            {
            	//ex.printStackTrace();
            }
        }

        if(is == null)
        {
            try
            {
                URL url = edu.gmu.cds.util.FileUtil.class.getResource(filename);
                is = url.openStream();
            }
            catch(Exception ex)
            {
            	//ex.printStackTrace();
            }
        }

        if(is == null)
        {
            try
            {
                URL url = edu.gmu.cds.util.FileUtil.class.getResource("/"+filename);
                is = url.openStream();
            }
            catch(Exception ex)
            {
            	//ex.printStackTrace();
            }
        }
        
        return is;
    }

    /**
     * Get a reader to the specified resource.
     */
    public static Reader getReader(String filename)
    {
        InputStreamReader isr = null;

        InputStream is = getInputStream(filename);

        if(is != null)
        {
            try
            {
                isr = new InputStreamReader(is);
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
        }

        return isr;
    }
    
    /**
     * Close the output stream and trap any exceptions.
     */
    public static void close(OutputStream os)
    {
    	if(os != null)
        {
    		try
	        {
                os.close();
	        }
	        catch(Exception ex)
	        {
	        }
    	}
    }
    
    public static void close(Writer fw)
    {
    	if(fw != null)
    	{
    		try{fw.close();}catch(Exception ex){};
    	}
    }
    
    /**
     * Close the input stream and trap any exceptions.
     */
    public static void close(InputStream is)
    {
    	if(is != null)
        {
    		try
	        {
                is.close();
	        }
	        catch(Exception ex)
	        {
	        }
    	}
    }
    
    /**
     * Close the reader.
     * 
     * @param r
     */
    public static void close(Reader r)
    {
    	if(r != null)
    	{
    		try
    		{
    			r.close();
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    }
    
    public static void writeStringToFile(String filename, String str)
    {
    	writeStringToFile(filename,str,false);
    }
    public static void writeStringToFile(String filename, String str, boolean append)
    {
    	FileWriter fw = null;
    	try
    	{
    		fw = new FileWriter(filename,append);
    		fw.write(str);
    		fw.flush();
    		fw.close();
    		fw = null;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		close(fw);
    	}
    }
    
    /**
     * Dump the input stream to the specified file.
     * 
     * @param is
     * @param filename
     */
    public static void dumpStreamToFile(InputStream is, String filename)
    {
    	FileOutputStream fos = null;
    	
    	try
    	{
    		fos = new FileOutputStream(filename);
    		dumpStreamToStream(is,fos);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		close(fos);
    	}
    }
    
    /**
     * Dump the input stream to the output stream.
     * 
     * @param is
     * @param os
     */
    public static void dumpStreamToStream(InputStream is, OutputStream os)
    {
    	byte ba[] = new byte[2048];
    	int numRead = 0;
    	try
    	{
    		numRead = is.read(ba);
    		while(numRead > 0)
    		{
    			os.write(ba,0,numRead);
    			numRead = is.read(ba);	
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(os != null)try{os.flush();}catch(Exception ex){};
    	}
    }
    
    /**
     * Read the bytes from the file.
     * 
     * @param filename
     * @return
     */
    public static byte[] getBytesFromFile(String filename)
    {
    	byte ba[] = null;
    	
    	InputStream is = getInputStream(filename);
    	try
    	{
    		ba = getBytesFromStream(is);
    	}
    	finally
    	{
    		close(is);
    	}
    	
    	return ba;
    }
    
    /**
     * Convert the inputstream into a byte array.
     * 
     * @param is
     * @return
     */
    public static byte[] getBytesFromStream(InputStream is)
    {
    	return getBytesFromStream(is,2048);
    }
    
    /**
     * Convert the inputstream into a byte array.
     * 
     * @param is
     * @return
     */
    @SuppressWarnings("unchecked")
	public static byte[] getBytesFromStream(InputStream is, int bufSize)
    {
    	byte ba[] = new byte[bufSize];
    	
    	try
    	{
    		List bytes = new ArrayList();
    		List lengths = new ArrayList();
    		
    		int tot = 0;
	    	int numRead = is.read(ba);
	    	while(numRead > 0)
	    	{
	    		tot+=numRead;
	    		bytes.add(ba);
	    		lengths.add(new Integer(numRead));
    			ba = new byte[bufSize];
	    		numRead = is.read(ba);
	    	}
	    	
	    	ba = new byte[tot];
	    	byte baTmp[] = null;
	    	
	    	int offset = 0;
	    	int size = bytes.size();
	    	for(int i=0; i<size; i++)
	    	{
	    		baTmp = (byte[])bytes.get(i);
	    		numRead = ((Integer)lengths.get(i)).intValue();
	    		/*
	    		numRead = baTmp.length;
	    		if(offset + numRead > tot)
	    		{
	    			numRead = tot-offset;
	    		}
	    		*/
	    		System.arraycopy(baTmp,0,ba,offset,numRead);
	    		
	    		offset+=numRead;
	    	}
	    	bytes.clear();
	    	lengths.clear();
	    	bytes = null;
	    	lengths = null;
    	}
    	catch(Exception ex)
    	{
    		ba = null;
    		ex.printStackTrace();
    	}
    	
    	return ba;
    }
            
    /**
     * Read the contents of the file into a string.
     * 
     * @param filename
     * @return
     */
    public static String getStringFromFile(String filename)
    {
        String str = null;
       
        byte ba[] = getBytesFromFile(filename);
        
        if(ba != null && ba.length > 0)
        {
        	str = new String(ba);
        }
        
        return str;
    }
    
    /**
     * Write the given lines to a file, uses \n for new lines
     * @param filename
     * @param lines
     */
    @SuppressWarnings("unchecked")
	public static void dumpLinesToFile(String filename, List lines)
    {
    	FileWriter fw = null;
    	
    	try
    	{
    		fw = new FileWriter(filename);
    		int size = lines.size();
    		for(int i=0; i<size; i++)
    		{
    			fw.write(lines.get(i).toString());
    			fw.write("\n");
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(fw != null)try{fw.flush();}catch(Exception ex){};
    		close(fw);
    	}
    }
    
    /**
     * Return the lines of a file.
     * 
     * @param filename
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List getLinesFromFile(String filename)
    {
    	List lines = new ArrayList();
    	//System.out.println(filename);
    	Reader r = getReader(filename);
    	
    	try
    	{
    		LineNumberReader lnr = new LineNumberReader(r);
    		
    		String line = null;
    		
    		line = lnr.readLine();
    		while(line != null)
    		{
    			lines.add(line);
    			line = lnr.readLine();
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    	    close(r);	
    	}
    	
    	return lines;
    }
 
    /**
     * Reads the lines from the file starting where specified for the
     * number of lines requested.
     * 
     * @param filename
     * @param start
     * @param numLines
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List getLinesFromFile(String filename, int start, int numLines)
    {
    	List lines = new ArrayList();
    	//System.out.println(filename);
    	Reader r = getReader(filename);
    	
    	try
    	{
    		LineNumberReader lnr = new LineNumberReader(r);
    		
    		String line = null;
    		
    		line = lnr.readLine();
    		int ln = 0;
    		while(line != null && ln < start)
    		{
    			ln++;
    			line = lnr.readLine();
    		}
    		
    		ln = 0;
    		while(line != null && ln < numLines)
    		{
    			ln++;
    			lines.add(line);
    			line = lnr.readLine();
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    	    close(r);	
    	}
    	
    	return lines;
    }

    /**
     * Returns a gray scale image from the stream.
     * 
     * @param is
     * @return
     */
    public static BufferedImage getImageFromStream(InputStream is)
    {
    	return getImageFromStream(is,BufferedImage.TYPE_BYTE_GRAY);
    }
    
    /**
     * Returns an image of the specified pixel type.
     * 
     * @param is
     * @param type
     * @return
     */
    public static BufferedImage getImageFromStream(InputStream is, int type)
    {
    	BufferedImage bi = null;
		try
		{
			ImageIcon icon = new ImageIcon(FileUtil.getBytesFromStream(is));
			int ny = icon.getIconHeight();
			int nx = icon.getIconWidth();

			bi = new BufferedImage(nx, ny, type);
			Graphics2D g = bi.createGraphics();
			g.drawImage(icon.getImage(), 0, 0, null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			FileUtil.close(is);
		}
		
		return bi;
    }

    /**
     * Removes any directory information from string and returns filename.
     * 
     * @param filename
     * @return
     */
	public static String getFilename(String filename)
	{
		if(filename == null)
		{
			return null;
		}
		
		filename = filename.replace('\\','/');
		int ind = filename.lastIndexOf('/');
		if(ind == -1)
		{
			return filename;
		}
		
		if(ind == (filename.length()-1))
		{
			return "";
		}
		
		return filename.substring(ind+1);
	}
	
    /**
     * Gets the directory portion of the filename.
     * 
     * @param filename
     * @return
     */
	public static String getDirectoryName(String filename)
	{
		if(filename == null)
		{
			return null;
		}
		
		filename = filename.replace('\\','/');
		int ind = filename.lastIndexOf('/');
		if(ind == -1)
		{
			return "";
		}
		
		if(ind == (filename.length()-1))
		{
			return filename;
		}
		
		return filename.substring(0,ind+1);
	}

	/**
	 * Attempts to get the data from the url (HTTP GET).
	 * It will use an internal cache while it's reading
	 * and then put the whole thing in a single byte array.
	 * This causes the utility to need twice the amount of 
	 * memory to build the output.
	 * 
	 * @param urlStr
	 * @return
	 */
	public static byte[] downloadBytesFromURL(String urlStr)
	{
		byte baOut[] = null;

		HttpURLConnection conn = null;
		InputStream is = null;
		
		try
		{
			URL url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			is = conn.getInputStream();

			baOut = getBytesFromStream(is);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			FileUtil.close(is);
			if(conn != null)try{conn.disconnect();}catch(Exception ex){};
		}
		
		return baOut;
	}

	/**
	 * Add the contents of the file to the existing zip.
	 * 
	 * @param newZipFile
	 * @param oldZipFile
	 */
	@SuppressWarnings("unchecked")
	public static void addFileToZip(String newZipFile, String oldZipFile, String newFile, String excludeExtension)
	{
		ZipFile oldZip = null;
		ZipOutputStream zos = null;
		InputStream zis = null;
		ZipEntry ze = null;
		String filename = null;
		
		try
		{
			zos = new ZipOutputStream(new FileOutputStream(newZipFile));
			oldZip = new ZipFile(oldZipFile);
			Enumeration entries = oldZip.entries();
			while(entries.hasMoreElements())
			{
				ze = (ZipEntry)entries.nextElement();
				filename = ze.getName();
				if(filename != null && !filename.endsWith(excludeExtension))
				{
					zis = oldZip.getInputStream(ze);
					zos.putNextEntry(ze);
					dumpStreamToStream(zis,zos);
					zos.closeEntry();
				}
			}
			
			File f = new File(newFile);
			zis = new FileInputStream(newFile);
			ze = new ZipEntry(f.getName());
			ze.setTime(f.lastModified());
			ze.setSize(f.length());
			
			zos.putNextEntry(ze);
			dumpStreamToStream(zis,zos);
			zos.closeEntry();
			
			zos.flush();
			zos.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{oldZip.close();}catch(Exception ex){};
			close(zos);
			close(zis);
		}
	}

	public static String getExtension(String file) 
	{
		String ext = "";
		int ind = file.lastIndexOf('.');
		if(ind > -1)
		{
			ext = file.substring(ind+1);
		}
		return ext;
	}
	
	public static FileFilter buildFilter(String description, String... extensions)
	{
		FileFilter f = new FileNameExtensionFilter(description, extensions) ;
		
		return f;
	}
	
	/**
	 * Returns null if the file cannot be found as is or in the alternate dir
	 * 
	 * @param filename
	 * @param alternateDir
	 * @return
	 */
	public static String ensureFileExists(String filename, String alternateDir)
	{
		String out = null;
		
		File f = null;
		f = new File(filename);
		if(f.exists())
		{
			out = f.getAbsolutePath();
		}
		
		if(out == null && alternateDir != null)
		{
			if(!alternateDir.endsWith("/"))
			{
				alternateDir += "/";
			}
			f = new File(alternateDir + f.getName());
			if(f.exists())
			{
				out = f.getAbsolutePath();
			}
		}
		
		return out;
	}

}
