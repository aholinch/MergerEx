/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.obj;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.sdss.Mass2Light;
import edu.gmu.cds.sdss.SDSSCasQuery;
import edu.gmu.cds.util.URLUtil;
import edu.gmu.cds.xml.XmlReader;
import edu.gmu.cds.xml.XmlTag;
import jodd.http.HttpBrowser;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;

/**
 * Utilities designed to return galaxy and galaxy pair info by name and position.
 * 
 * @author aholinch
 *
 */
public class ObjectQuery 
{
	public static final int NED = 1;
	public static final int SDSSDR7 = 2;
	public static final int SDSSDR8 = 3;
	public static final int SDSSDR9 = 4;
	public static final int SDSSDR17 = 5;
	
    //public static final String NED_NAME = "http://ned.ipac.caltech.edu/cgi-bin/nph-objsearch?objname=NAME&extend=no&hconst=73&omegam=0.27&omegav=0.73&corr_z=1&out_csys=Equatorial&out_equinox=J2000.0&obj_sort=RA+or+Longitude&of=ascii_tab&zv_breaker=30000.0&list_limit=5&img_stamp=YES";
    //public static final String NED_POS = "http://ned.ipac.caltech.edu/cgi-bin/nph-objsearch?in_csys=Equatorial&in_equinox=J2000.0&lon=RAd&lat=DEC&radius=2&hconst=73&omegam=0.27&omegav=0.73&corr_z=1&z_constraint=Unconstrained&z_value1=&z_value2=&z_unit=z&ot_include=ANY&nmp_op=ANY&out_csys=Equatorial&out_equinox=J2000.0&obj_sort=Distance+to+search+center&of=ascii_tab&zv_breaker=30000.0&list_limit=5&img_stamp=NO&search_type=Near+Position+Search";
    
    public static final String NED_POS = "http://ned.ipac.caltech.edu/cgi-bin/objsearch?search_type=Near+Position+Search&in_csys=Equatorial&in_equinox=J2000.0&lon=RAd&lat=DECd&radius=1&out_csys=Equatorial&out_equinox=J2000.0&of=xml_main";
    public static final String NED_NAME = "http://ned.ipac.caltech.edu/cgi-bin/objsearch?objname=NAME&extend=no&out_csys=Equatorial&out_equinox=J2000.0&of=xml_main";
    
    // image url
    //public static final String https://irsa.ipac.caltech.edu/applications/finderchart/servlet/api?mode=getImage&locstr=ARP+082&subsetsize=3.25&thumbnail_size=small&survey=DSS&grid=false&dss_bands=poss2ukstu_red&type=jpgurl
    
    public static ObjectInfo queryByName(int type, String name)
    {
    	ObjectInfo info = null;
    	switch(type)
    	{
    		case NED:
    		default:
    			info = queryNEDByName(name);
    			break;
    		case SDSSDR7:
    		case SDSSDR8:
    		case SDSSDR9:
    		case SDSSDR17:
    			info = querySDSSByName(type, name);
        		break;
    	}
    	return info;
    }
    
    public static ObjectInfo querySDSSByName(int type, String name)
    {
    	int sdssType = SDSSCasQuery.DR7;
    	if(type == SDSSDR8)
    	{
    		sdssType = SDSSCasQuery.DR8;
    	}
    	else if(type == SDSSDR9)
    	{
    		sdssType = SDSSCasQuery.DR9;
    	}
    	else if(type == SDSSDR17)
    	{
    		sdssType = SDSSCasQuery.DR17;
    	}
    	
    	SDSSCasQuery query = new SDSSCasQuery(sdssType);
    	
    	ObjectInfo info = query.getObjectById(name);
    	
    	return info;
    }
    
    @SuppressWarnings("deprecation")
	public static ObjectInfo queryNEDByName(String name)
    {
        String url = NED_NAME;
		name = URLEncoder.encode(name);
        url = url.replace("NAME", name);

        String info = null;
        try {
        	
        	info = URLUtil.getURLContent(url);
        	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        List<ObjectInfo> infos = parseNEDResults(info);
        ObjectInfo objOut = null;

        for(int i=0; i<infos.size(); i++)
        {
        	String type = infos.get(i).getType().toLowerCase();
        	if(type.equals("g") || type.equals("gpair"))
        	{
        		objOut = infos.get(i);
        		break;
        	}
        }
        return objOut;
    }


    public static ObjectInfo queryByPos(int type, double raDeg, double decDeg)
    {
    	ObjectInfo info = null;
    	switch(type)
    	{
    		case NED:
    		default:
    			info = queryNEDByPos(raDeg,decDeg);
    			break;
    		case SDSSDR7:
    		case SDSSDR8:
    		case SDSSDR9:
    		case SDSSDR17:
    			info = querySDSSByPos(type,raDeg,decDeg);
        		break;
    	}
    	return info;
    }

    
    public static ObjectInfo queryNEDByPos(double raDeg, double decDeg)
    {
        String url = NED_POS;
        
        url = url.replace("RA", String.valueOf(raDeg));
        url = url.replace("DEC", String.valueOf(decDeg));

        String info = URLUtil.getURLContent(url);
        
        List<ObjectInfo> infos = parseNEDResults(info);
        ObjectInfo objOut = null;

        for(int i=0; i<infos.size(); i++)
        {
        	String type = infos.get(i).getType().toLowerCase();
        	if(type.equals("g") || type.equals("gpair"))
        	{
        		objOut = infos.get(i);
        		break;
        	}
        }
        return objOut;
    }
    
    public static ObjectInfo querySDSSByPos(int type, double raDeg, double decDeg)
    {
    	int sdssType = SDSSCasQuery.DR7;
    	if(type == SDSSDR8)
    	{
    		sdssType = SDSSCasQuery.DR8;
    	}
    	else if(type == SDSSDR9)
    	{
    		sdssType = SDSSCasQuery.DR9;
    	}
    	else if(type == SDSSDR17)
    	{
    		sdssType = SDSSCasQuery.DR17;
    	}
    	
    	SDSSCasQuery query = new SDSSCasQuery(sdssType);
    	
    	ObjectInfo info = query.getObjectByRaDec(raDeg, decDeg);
    	
    	return info;
    }

    public static List<ObjectInfo> parseNEDResults(String info)
    {
    	List<ObjectInfo> infos = new ArrayList<ObjectInfo>();
    	
    	System.out.println(info);
    	
    	try
    	{
	    	Jerry.JerryParser jerryParser = Jerry.jerry();
	    	((LagartoDOMBuilder) jerryParser.getDOMBuilder()).enableXmlMode();
	    	Jerry doc = jerryParser.parse(info);
	    	Jerry data = doc.$("TABLEDATA");
	    	
	    	Jerry rows = data.$("TR");
	    	Node cells[] = null;
	    	ObjectInfo oi = null;
	    	
	    	int size = rows.size();
	    	for(int i=0; i<size; i++)
	    	{
	    		cells = rows.get(i).getChildNodes();
	    		oi = new ObjectInfo();
	    		infos.add(oi);
	    		oi.setName(cells[1].getTextContent().trim());
	    		oi.setType(cells[4].getTextContent().trim());
	    		oi.setRADeg(Double.parseDouble(cells[2].getTextContent().trim()));
	    		oi.setDecDeg(Double.parseDouble(cells[3].getTextContent().trim()));
	    		try{oi.setRedshift(Double.parseDouble(cells[6].getTextContent().trim()));}catch(Exception ex) {}
	    		try{
	    			String mag = cells[8].getTextContent().trim();
	    			int len = mag.length();
        			while(len > 0 && !Character.isDigit(mag.charAt(len-1)))
        			{
        				mag = mag.substring(0,len-1);
        				mag = mag.trim();
        				len = mag.length();
        			}
        			oi.magnitude = getNum(mag);
	    		}catch(Exception ex) {}
	    			
	    		
	    		double lum = Mass2Light.getL(oi.magnitude, oi.redshift, Mass2Light.Msun_B);
	        	double mass = lum;
	        	oi.mass=mass;
	    	}
    	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return infos;
    }
    
    public static List<ObjectInfo> parseNEDResultsOrig(String info)
    {
    	List<ObjectInfo> infos = new ArrayList<ObjectInfo>();
    	
    	int ind = info.toLowerCase().indexOf("no.\t");
        if(ind > -1)
        {
        	info = info.substring(ind);
        	String lines[] = info.split("\n");
        	
        	String sah[] = lines[0].split("\t");
        	for(int j=1; j<lines.length; j++)
        	{
        		ObjectInfo obj = new ObjectInfo("NED");
        		String sa[] = lines[j].split("\t");
	        	for(int i=0; i<sah.length; i++)
	        	{
	        	
	        		sah[i]=sah[i].toLowerCase().trim();
	        		
	        		if(sah[i].equals("redshift"))
	        		{
	        			obj.redshift = getNum(sa[i]);
	        		}
	        		else if(sah[i].equals("ra(deg)"))
	        		{
	        			obj.raDeg = getNum(sa[i]);
	        		}
	        		else if(sah[i].equals("dec(deg)"))
	        		{
	        			obj.decDeg = getNum(sa[i]);
	        		}
	        		else if(sah[i].equals("object name"))
	        		{
	        			obj.name = sa[i].trim();
	        		}
	        		else if(sah[i].equals("type"))
	        		{
	        			obj.setType(sa[i]);
	        		}
	        		else if(sah[i].startsWith("magnitude"))
	        		{
	        			String str = sa[i].trim();
	        			int len = str.length();
	        			while(len > 0 && !Character.isDigit(str.charAt(len-1)))
	        			{
	        				str = str.substring(0,len-1);
	        				str = str.trim();
	        				len = str.length();
	        			}
	        			obj.magnitude = getNum(str);
	        		}
	        	}
	        	
	        	double lum = Mass2Light.getL(obj.magnitude, obj.redshift, Mass2Light.Msun_B);
	        	double mass = lum;
	        	obj.mass=mass;
	        	
	        	infos.add(obj);
        	}
        }
    	return infos;
    }
    
    private static double getNum(String str)
    {
    	double num = 0;
    	try{num = Double.parseDouble(str.trim());}catch(Exception ex){};
    	return num;
    }
    
    public static void arpquery(String args[])
    {
    	String name = "";
    	for(int i=1; i<339; i++)
    	{
    		name = "Arp " + String.valueOf(i);
    		System.out.println(name);
    		System.out.println(queryByName(NED,name));
    		try{Thread.sleep(1000);}catch(Exception ex){};
        }
    	//System.out.println(queryByName(NED,"Arp 86"));
    	//System.out.println(queryNEDByPos(122.81125,25.19306));
    	System.exit(0);
    }
}
