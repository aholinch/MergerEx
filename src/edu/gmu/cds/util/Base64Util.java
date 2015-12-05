/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

import java.util.Random;

/**
 * RFC 2045 http://www.faqs.org/rfcs/rfc2045.html
 * 
 * @author aholinch
 *
 */
public class Base64Util
{
	public static final int PAD_VALUE = 65;
	public static final char PAD_CHAR = '=';
	
	/**
	 * Get the binary representation of the string and convert it to base64.
	 * 
	 * @param strin
	 * @return
	 */
	public static String encodeStringAsBase64(String strin)
	{
		String strout = null;
		
		try
		{
			byte ba[] = strin.getBytes();
			strout = encode(ba);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return strout;
	}
	
	/**
	 * Treat the base64 string as a binary representation of a string.
	 * 
	 * @param strin
	 * @return
	 */
	public static String decodeBase64AsString(String strin)
	{
		String strout = null;
		
		try
		{
			byte ba[] = decode(strin);
			strout = new String(ba);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return strout;
	}
	
	/**
	 * Convert a byte array to a base64 string.
	 * 
	 * @param ba
	 * @return
	 */
	public static String encode(byte ba[])
	{
		String str = null;
		
		int numBytes = ba.length;
		
		// get num quanta
		int nq = numBytes/3;
		
		// get padding
		int np = numBytes - nq*3;
		
		if(np > 0)
		{
			nq++;
		}
		
		int ind = 0;
		
		int b1 = 0;
		int b2 = 0;
		int b3 = 0;
		
		int q1 = 0;
		int q2 = 0;
		int q3 = 0;
		int q4 = 0;
		
		StringBuffer sb = new StringBuffer(nq*4);
		
		for(int i=0; i<nq; i++)
		{		
			ind = i*3;
			b1 = ba[ind];
			if(ind+1 < numBytes)
			{
				b2 = ba[ind+1];
			}
			else
			{
				b2 = 0;
			}
			
			if(ind+2 < numBytes)
			{
				b3 = ba[ind+2];
			}
			else
			{
				b3 = 0;
			}
			
			q1 = (b1 & 0xfc) >> 2;
			q2 = ((b1 & 0x03) << 4) | ((b2 & 0xf0) >> 4);
			q3 = ((b2 & 0x0f) << 2) | ((b3 & 0xc0) >> 6);
			q4 = ((b3 & 0x3f));
			

			sb.append(intToChar(q1));
			sb.append(intToChar(q2));
			if(ind+1 < numBytes)
			{
				sb.append(intToChar(q3));
			}
			else
			{
				sb.append(PAD_CHAR);
			}

			if(ind+2 < numBytes)
			{
				sb.append(intToChar(q4));
			}
			else
			{
				sb.append(PAD_CHAR);
			}
		}
		
		str = sb.toString();
		
		return str;
	}
	
	/**
	 * Convert a base64 encoded string into a byte array.
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] decode(String str)
	{
		byte ba[] = null;
		
		int len = str.length();
		
		// get number of quanta
		int nq = len/4;
		
		// determine number of bytes
		int numBytes = nq*3;
		int np = 0;
		// adjust for padding
		if(str.endsWith("=="))
		{
			np = 2;
		}
		else if(str.endsWith("="))
		{
			np = 1;
		}
		
		numBytes-=np;
		
		ba = new byte[numBytes];
		
		// loop over quanta
		int q1 = 0;
		int q2 = 0;
		int q3 = 0;
		int q4 = 0;
		
		byte b1 = 0;
		byte b2 = 0;
		byte b3 = 0;
		for(int i=0; i<nq; i++)
		{
			q1 = charToInt(str.charAt(i*4));
			q2 = charToInt(str.charAt(i*4+1));
			q3 = charToInt(str.charAt(i*4+2));
			q4 = charToInt(str.charAt(i*4+3));

			// we now have 24 bits to distribute to 3 bytes
			b1 = (byte)(((q1&0x3f) << 2) | ((q2&0x30) >>> 4));
			b2 = (byte)(((q2&0x0f) << 4) | ((q3&0x3c) >>> 2));
			b3 = (byte)(((q3&0x03) << 6) | ((q4&0x3f)));
			
			ba[i*3] = b1;
			// check for padding
			if(q3 != PAD_VALUE)
			{
				ba[i*3+1] = b2;
			}
			if(q4 != PAD_VALUE)
			{
				ba[i*3+2] = b3;
			}
		}
		
		return ba;
	}
	
	/**
	 * Converts number to character.
	 * 
	 * @param i
	 * @return
	 */
	public static char intToChar(int i)
	{
	    char c = PAD_CHAR;
	    
	    if(i >= 0)
	    {
	    	if(i < 26)
	    	{
	    		c = (char)('A'+i);
	    	}
	    	else if(i < 52)
	    	{
	    		c = (char)('a'+i-26);
	    	}
	    	else if(i < 62)
	    	{
	    		c = (char)('0'+i-52);
	    	}
	    	else if(i == 62)
	    	{
	    		c = '+';
	    	}
	    	else if(i == 63)
	    	{
	    		c = '/';
	    	}
	    }
	    return c;
	}
	
	/**
	 * Converts character to number.  Array lookup would be faster.
	 * 
	 * @param c
	 * @return
	 */
    public static int charToInt(char c)
    {
    	int val = PAD_VALUE;
    	
    	// first the capital letters
    	if(c >= 'A' && c <= 'Z')
    	{
    		val = c-'A';
    	}
    	// lower case
    	else if(c >= 'a' && c <= 'z')
    	{
    		val = c-'a'+26;
    	}
    	// numbers
    	else if(c >= '0' && c <= '9')
    	{
    		val = c-'0'+52;
    	}
    	// specials
    	else if(c == '+')
    	{
    		val = 62;
    	}
    	else if(c == '/')
    	{
    		val = 63;
    	}
    	
    	return val;
    }
    
    public static void test(byte ba[]) throws Exception
    {
    	/*
    	sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
    	sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
    	
    	String str1 = enc.encode(ba).replaceAll("\r\n","").replaceAll("\n","");
    	String str2 = encode(ba);
	
    	if(!str1.equals(str2))
    	{
    		System.out.println(str1);
    		System.out.println(str2);
    		throw new Exception("encode mismatch");
    	}
    	
    	byte ba1[] = dec.decodeBuffer(str1);
    	byte ba2[] = decode(str2);
    	
    	if(!equals(ba1,ba2))
    	{
    		throw new Exception("decode mismatch");
    	}
    	*/
    }
    
    public static boolean equals(byte ba1[], byte ba2[])
    {	
    	int l1 = ba1.length;
    	int l2 = ba2.length;
    	
    	if(l1 != l2)
    	{
    		return false;
    	}
    	
    	for(int i=0; i<l1; i++)
    	{
    		if(ba1[i] != ba2[i])
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    public static byte[] randBA(Random rand)
    {
    	byte ba[] = null;
    	
    	int size = rand.nextInt(1024);
    	ba = new byte[size];
    	rand.nextBytes(ba);
    	return ba;
    }
    
}
