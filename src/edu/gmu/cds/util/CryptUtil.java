/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;

/**
 * Default encryption, decryption routine.  This version performs
 * simple Base64 encoding, not actual, reversible encryption.
 * 
 * @author aholinch
 *
 */
public class CryptUtil
{
	public static int idcnt = 0;
	public static Random idrand = new Random();
	
    public static String encrypt(String clear)
    {
    	String crypt = null;
    	
    	try
    	{
    		byte ba[] = clear.getBytes("UTF8");
    		for(int i=0; i<ba.length; i++)
    		{
    			ba[i]+=ba.length-i;
    		}
    		crypt = Base64Util.encode(ba);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return crypt;
    }
    
    public static String decrypt(String crypt)
    {
    	String clear = null;
    	try
    	{
    		byte ba[] = Base64Util.decode(crypt);
    		for(int i=0; i<ba.length; i++)
    		{
    			ba[i]-=ba.length-i;
    		}
    		clear = new String(ba,"UTF8");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return clear;
    }
    
	public static String genUniqueId()
	{
    	String id = null;
    	
    	long time = System.currentTimeMillis();
    	int rnd = idrand.nextInt();
    	if(rnd < 0)
    	{
    		rnd++;
    		rnd *= -1;
    	}
    	idcnt++;
    	
    	long tlow = time << 32;
    	tlow *= idcnt;
    	long thigh = time >> 32;
    	thigh *= 10;
    	
    	time = tlow + thigh;
    	if(time < 0)
    	{
    		time *= -1;
    	}
    	id = String.valueOf(time) + String.valueOf(idcnt) + String.valueOf(rnd);
    	
    	BigInteger bi = new BigInteger(id);
    	
    	id = bi.toString(36);
    	
    	return id;
	}
    
	public static String getUsername()
	{
		String user = System.getProperty("user.name");
		if(user == null)
		{
			user = System.getProperty("user.home");
			if(user != null)
			{
				File f = new File(user);
				user = f.getName();
			}
		}
		
		if(user == null)
		{
			user = "unknown";
		}
		
		return user;
	}
	
}
