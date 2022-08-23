/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sdss;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.gmu.cds.util.FileUtil;
import jodd.http.Cookie;
import jodd.http.HttpBrowser;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * updating this in August 2022.  DR7, DR8, DR9 no longer working.  Focusing on DR17.
 * 
 * @author aholinch
 *
 */
public class SDSSCasQuery 
{
	public static final String TABLE_PHOTO  = "PhotoObjAll";
	public static final String TABLE_SPEC   = "SpecPhotoAll";
	public static final String TABLE_PHOTOZ  = "Photoz";
	public static final String TABLE_PHOTOZ2  = "Photoz2";
	
	/** Get fields from PhotoObjAll, just append objID */
	public static final String SQL1_DR7 = "select " + SDSSObject.FIELDS1_DR7 + " from "+TABLE_PHOTO+" where objID=";
	public static final String SQL1_DR8 = "select " + SDSSObject.FIELDS1_DR8 + " from "+TABLE_PHOTO+" where objID=";
	public static final String SQL1_DR9 = "select " + SDSSObject.FIELDS1_DR9 + " from "+TABLE_PHOTO+" where objID=";
	public static final String SQL1_DR17 = "select " + SDSSObject.FIELDS1_DR17 + " from "+TABLE_PHOTO+" where objID=";
	
	/** Get fields from PhotoObjAll, just replace ra,dec */
	public static final String SQL1COORDS_DR7 = "select " + SDSSObject.FIELDS1P_DR7 + " from "+TABLE_PHOTO+" p, dbo.fGetNearbyObjEq(RA,DEC,RAD) n where p.objID = n.objID and p.type=3";
	public static final String SQL1COORDS_DR8 = "select " + SDSSObject.FIELDS1P_DR8 + " from "+TABLE_PHOTO+" p, dbo.fGetNearbyObjEq(RA,DEC,RAD) n where p.objID = n.objID and p.type=3";
	public static final String SQL1COORDS_DR9 = "select " + SDSSObject.FIELDS1P_DR9 + " from "+TABLE_PHOTO+" p, dbo.fGetNearbyObjEq(RA,DEC,RAD) n where p.objID = n.objID and p.type=3";
	public static final String SQL1COORDS_DR17 = "select " + SDSSObject.FIELDS1P_DR17+ " from "+TABLE_PHOTO+" p, dbo.fGetNearbyObjEq(RA,DEC,RAD) n where p.objID = n.objID and p.type=3 order by n.distance";

	/** Get fields from SpecPhotoAll just append objID */
	public static final String SQL2 = "select " + SDSSObject.FIELDS2 + " from "+TABLE_SPEC+" where objID=";

	/** Get fields from PhotoZ, just append objID */
	public static final String SQL3 = "select " + SDSSObject.FIELDS2 + " from "+TABLE_PHOTOZ+" where objID=";

	/** Get fields from PhotoZ2, just append objID */
	public static final String SQL4 = "select " + SDSSObject.FIELDS3 + " from "+TABLE_PHOTOZ2+" where objID=";
	
	// DR8
	public static final String CASSQLURL_DR8 = "http://skyserver.sdss3.org/dr8/en/tools/search/x_sql.asp?cmd=SQL&name=TABLE&format=xml";
	public static final String CASIMGURL_DR8 = "http://skyservice.pha.jhu.edu/DR8/ImgCutout/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
	// DR7
	public static final String CASSQLURL_DR7 = "http://cas.sdss.org/astro/en/tools/search/x_sql.asp?cmd=SQL&name=TABLE&format=xml";
    public static final String CASIMGURL_DR7 = "http://casjobs.sdss.org/ImgCutoutDR7/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
    // DR9
	public static final String CASSQLURL_DR9 = "http://skyserver.sdss3.org/dr9/en/tools/search/x_sql.asp?cmd=SQL&name=TABLE&format=xml";
	public static final String CASIMGURL_DR9 = "http://skyservice.pha.jhu.edu/DR9/ImgCutout/getjpeg.aspx?ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
	// DR17
	public static final String CASSQLURL_DR17 = "http://skyserver.sdss.org/dr17/SearchTools/sql?searchtool=SQL&TaskName=Skyserver.Search.SQL&syntax=NoSyntax&cmd=SSQQLL&format=XML&TableName=&ReturnHtml=true";
	public static final String CASIMGURL_DR17 = "https://skyserver.sdss.org/dr17/SkyServerWS/ImgCutout/getjpeg?TaskName=Skyserver.Explore.Image&ra=RA&dec=DEC&scale=SCALE&width=NX&height=NY";
   
	public static final int DR7 = 7;
    public static final int DR8 = 9;
    public static final int DR9 = 10;
    public static final int DR17 = 11;
    
    public int type = DR17;
    
    protected HttpBrowser httpClient;
    protected boolean hasGottenCookies;
    protected String rvt = null;
    
	public SDSSCasQuery()
	{
		this(DR17);
	}
	
	public SDSSCasQuery(int type)
	{
		this.type = type;
		httpClient = new HttpBrowser();
	}
	
	public String getSQL1()
	{
		String sql = SQL1_DR7;
		if(type == DR8)
		{
			sql = SQL1_DR8;
		}
		else if(type == DR9)
		{
			sql = SQL1_DR9;
		}
		else if(type == DR17)
		{
			sql = SQL1_DR17;
		}
		
		return sql;
	}
	
	public String getSQL1COORDS()
	{
		String sql = SQL1COORDS_DR7;
		if(type == DR8)
		{
			sql = SQL1COORDS_DR8;
		}
		else if(type == DR9)
		{
			sql = SQL1COORDS_DR9;
		}
		else if(type == DR17)
		{
			sql = SQL1COORDS_DR17;
		}

		return sql;
	}
	
	public String getImgURL()
	{
		String url = CASIMGURL_DR7;
		if(type == DR8)
		{
			url = CASIMGURL_DR8;
		}
		else if(type == DR9)
		{
			url = CASIMGURL_DR9;
		}
		else if(type == DR17)
		{
			url = CASIMGURL_DR17;
		}

		return url;
	}
	
	public String getSQLURL()
	{
		String url = CASSQLURL_DR7;
		if(type == DR8)
		{
			url = CASSQLURL_DR8;
		}
		else if(type == DR9)
		{
			url = CASSQLURL_DR9;
		}
		else if(type == DR17)
		{
			url = CASSQLURL_DR17;
		}

		return url;
	}
	
	public void downloadImageByRaDec(double ra, double dec, String filename)
	{
		downloadImageByRaDec(ra,dec,filename,0.25);
	}
	
	public void downloadImageByRaDec(double ra, double dec, String filename, double scale)
	{
		downloadImageByRaDec(ra,dec,filename,scale,1024,1024,false,false,false);
	}
	
	public void downloadImageByRaDec(double ra, double dec, String filename, double scale, int nx, int ny, boolean getSpectra, boolean getPhotos, boolean invert)
	{
    	InputStream is = null;
    	OutputStream os = null;
    	try
    	{
    		String url = getImgURL();
    		url = url.replaceAll("RA",String.valueOf(ra));
    		url = url.replaceAll("DEC",String.valueOf(dec));
    		url = url.replaceAll("SCALE",String.valueOf(scale));
    		url = url.replaceAll("NX",String.valueOf(nx));
    		url = url.replaceAll("NY",String.valueOf(ny));
    		
    		boolean addOptions = getSpectra || getPhotos || invert;
    		String optionParams = "";
    		String options = "opt=";
    		
    		if(getSpectra)
    		{
    			options += "S";
    			optionParams = "&SpecObjs=on";
    		}
    		
    		if(getPhotos)
    		{
    			options += "P";
    			optionParams += "&PhotoObjs=on";
    		}
    		
    		if(invert)
    		{
    			options += "I";
    			optionParams += "&InvertImage=on";
    		}

    		if(addOptions)
    		{
    			url += "&"+options+"&query="+optionParams;
    		}
    		else
    		{
    			url += "&opt=&query=";
    		}
    		
    		URL urlObj = new URL(url);
    		is = urlObj.openStream();
    		byte ba[] = new byte[2048];
    		
    		os = new FileOutputStream(filename);
    		BufferedInputStream bis = new BufferedInputStream(is);
    		int numRead = bis.read(ba);
    		
    		while(numRead > 0)
    		{
    			os.write(ba,0,numRead);
    			numRead = bis.read(ba);
    		}
    		os.flush();
    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(is != null)try{is.close();}catch(Exception ex){};
    		if(os != null)try{os.flush();os.close();}catch(Exception ex){};
    	}
	}
	
	/**
	 * Query the CAS server for the SDSS object id.  Will default
	 * to retrieving spectral information if available.
	 * 
	 * @param objId
	 * @return
	 */
	public SDSSObject getObjectById(String objId)
	{
		return getObjectById(objId, true);
	}
	
	/**
	 * Query the CAS server for the SDSS object id.  The user
	 * can specify whether to try to retrieve the redshift information.
	 * 
	 * @param objId
	 * @param getRedshift
	 * @return
	 */
	public SDSSObject getObjectById(String objId, boolean getRedshift)
	{
		SDSSObject obj = null;
		String xml = null;
		
		// get photo info
		xml = getXMLFromObjIdSql(getSQL1(),TABLE_PHOTO,objId);
		
		System.out.println(xml);
		if(xml != null && xml.trim().length() > 0)
		{
			obj = SDSSObject.parseFromXMLString(xml);
		}
		
		// check to see if the user wants the redshift
		if(getRedshift && obj != null)
		{
			xml = getXMLFromObjIdSql(SQL2,TABLE_SPEC,objId);
			//System.out.println(xml);
			obj.setSpecInfo(xml);
			
			xml = getXMLFromObjIdSql(SQL3,TABLE_PHOTOZ,objId);
			//System.out.println(xml);
			obj.setPhotoZInfo(xml);

			// No photoz2 anymore
			/*
			xml = getXMLFromObjIdSql(SQL4,TABLE_PHOTOZ2,objId);
			System.out.println(xml);
			obj.setPhotoZ2Info(xml);
			*/
			obj.setDerivedValues();
		}
		
		return obj;
	}
	
	
	/**
	 * Query the CAS server for the SDSS ra,dec.  Will default
	 * to retrieving spectral information if available.
	 * 
	 * @param ra degrees
	 * @param dec degrees
	 * @return
	 */
	public SDSSObject getObjectByRaDec(double ra, double dec)
	{
		return getObjectByRaDec(ra, dec, true, false);
	}
	
	/**
	 * Query the CAS server for the SDSS ra,dec.  The user
	 * can specify whether to try to retrieve the redshift information.
	 * 
	 * @param ra degrees
	 * @param dec degrees
	 * @param getRedshift
	 * @return
	 */
	public SDSSObject getObjectByRaDec(double ra, double dec, boolean getRedshift, boolean useStrictGalaxyDefinition)
	{
		SDSSObject obj = null;
		String xml = null;
		
		// get photo info
		xml = getXMLFromRaDecSql(getSQL1COORDS(),TABLE_PHOTO,ra,dec);
		
		if(xml != null && xml.trim().length() > 0)
		{
			List<SDSSObject> objs = SDSSObject.parseObjectsFromXMLString(xml);
			
			if(objs != null && objs.size() > 0)
			{
				int size = objs.size();
				SDSSObject tmpObj = null;
				
				// in general, our SQL will return objects sorted by
				// distance from RA,DEC supplied.  We need to look for
				// first object with a primary_target type that is galaxy
				// related
				if(useStrictGalaxyDefinition)
				{
					for(int i=0; i<size; i++)
					{
						tmpObj = (SDSSObject)objs.get(i);
						//System.out.println(tmpObj.primaryTarget +"\t" + tmpObj.hasGalaxyPrimaryTarget());
						
						if(tmpObj.hasGalaxyPrimaryTarget())
						{
							obj = tmpObj;
							break;
						}
					}
				}

				if(obj == null)
				{
					obj = (SDSSObject)objs.get(0);
				}
			}
		}
		
		// check to see if the user wants the redshift
		if(getRedshift && obj != null)
		{
			String objId = obj.objID;
			
			xml = getXMLFromObjIdSql(SQL2,TABLE_SPEC,objId);
			obj.setSpecInfo(xml);
			
			xml = getXMLFromObjIdSql(SQL3,TABLE_PHOTOZ,objId);
			obj.setPhotoZInfo(xml);

			// No photoz2 anymore
			/*
			xml = getXMLFromObjIdSql(SQL4,TABLE_PHOTOZ2,objId);
			System.out.println(xml);
			obj.setPhotoZ2Info(xml);
			*/

			
			obj.setDerivedValues();
		}
		
		return obj;
	}
	/**
	 * Query the specified table with the sql that takes objID as qualifier.
	 * 
	 * @param sql
	 * @param table
	 * @param objId
	 * @return
	 */
	public String getXMLFromObjIdSql(String sql, String table, String objId)
	{
		String url = getSQLURL();
		sql += objId;
		// ensure no strange characters in the URL
		try
		{
			//sql = java.net.URLEncoder.encode(sql,"UTF-8");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		

		if(!hasGottenCookies)
		{
			initCookies();
		}
		
		url = url.replaceAll("SSQQLL", sql);
		
		url+=rvt;
		//System.out.println(url);
		
		String xml = getStringFromURL(url,true);

		return xml;
	}
	/**
	 * Query the specified table with the sql that takes ra and dec as qualifier.
	 * 
	 * @param sql
	 * @param table
	 * @param ra degrees
	 * @param dec degrees
	 * @return
	 */
	public String getXMLFromRaDecSql(String sql, String table, double ra, double dec)
	{
		String url = getSQLURL();
		sql = sql.replaceAll("RAD",String.valueOf(0.75));
		sql = sql.replaceAll("RA",String.valueOf(ra));
		sql = sql.replaceAll("DEC",String.valueOf(dec));
//System.out.println(sql);
		// ensure no strange characters in the URL
		try
		{
			//sql = java.net.URLEncoder.encode(sql,"UTF-8");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		url = url.replaceAll("TABLE",table);
		url = url.replaceAll("SSQQLL",sql);

		//System.out.println(url);		


		System.out.println(sql);
		if(!hasGottenCookies)
		{
			initCookies();
		}
		
		url+=rvt;
		

		String xml = getStringFromURL(url,true);
		return xml;
	}
	
	public Cookie[] cookies = null;
	
	/**
	 * Fetch the URL content and return as a string.
	 * 
	 * @param urlStr
	 * @return
	 */
	public String getStringFromURL(String urlStr)
	{
		return getStringFromURL(urlStr, false);
	}
	
	public String getStringFromURL(String urlStr, boolean doPost)
	{

		String str = null;
		
		try
		{
			
			HttpRequest req = HttpRequest.get(urlStr);
			if(doPost)
			{
				int ind = urlStr.indexOf('?');
				String params = urlStr.substring(ind+1).trim();
				urlStr = urlStr.substring(0,ind);
				req = HttpRequest.post(urlStr);

				//params = params.replace('&', '\n');
				req.body(params);
				req.header("Content-type","application/x-www-form-urlencoded");
				
			}
			/*
			System.out.println("Setting cookies to the next request\t"+cookies);
			if(cookies != null)for(int i=0;i<cookies.length; i++)
			{
				Cookie c = cookies[i];
				System.out.println("input cookie\t"+c.getName()+"\t"+c.getPath()+"\t"+c.getValue());
			}
			*/

			if(cookies != null)req.cookies(cookies);
			req.header("User-Agent", "Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0");
			req.header("Referer","http://skyserver.sdss.org/dr17/SearchTools/sql");
			req.header("Origin","http://skyserver.sdss.org");
			req.header("Host","skyserver.sdss.org");
			//req.header("DNT","1");
			//req.header("Connection","keep-alive");
			req.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
			
			//System.out.println(req.toString());
			HttpResponse resp = httpClient.sendRequest(req);
			
			if(!hasGottenCookies)cookies = resp.cookies();
			str = resp.bodyText();

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return str;
	}
	
	protected void initCookies()
	{
		try
		{
			// the sql server wants you to hit a query page first, snag some cookies and __RequestVerificationToken
		
			String url = "http://skyserver.sdss.org/dr17/SearchTools/sql";
			String output = getStringFromURL(url);
			
			int ind = output.indexOf("<input name=\"__RequestVerificationToken\"");
			if(ind > 0)
			{
				output = output.substring(ind);
				ind = output.indexOf("/>");
				output = output.substring(0,ind);
				ind = output.indexOf("value=\"");
				output = output.substring(ind+7).trim();
				ind = output.indexOf('"');
				output = output.substring(0,ind).trim();
				
				rvt = "&__RequestVerificationToken="+output;
				this.hasGottenCookies=true;
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
