/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sdss;

import java.util.ArrayList;
import java.util.List;

import edu.gmu.cds.obj.ObjectInfo;
import edu.gmu.cds.xml.XmlReader;
import edu.gmu.cds.xml.XmlTag;

public class SDSSObject extends ObjectInfo 
{
    public SDSSObject()
    {
    	super("SDSS");
    }
    
    public SDSSObject(String src)
    {
    	super(src);
    }
    
	public static final long TARGET_GALAXY_RED = 0x00000020l;	 
	public static final long TARGET_GALAXY = 0x00000040l;	 
	public static final long TARGET_GALAXY_BIG = 0x00000080l;	 
	public static final long TARGET_GALAXY_BRIGHT_CORE = 0x00000100l;
	
	public static final String OBJID = "objID";
	public static final String RA = "ra";
	public static final String DEC = "dec";
	public static final String GALLON = "b";
	public static final String GALLAT = "l";
	public static final String DRU = "dered_u";
	public static final String DRG = "dered_g";
	public static final String DRR = "dered_r";
	public static final String DRI = "dered_i";
	public static final String DRZ = "dered_z";
	public static final String PRU = "petroRad_u";
	public static final String PRG = "petroRad_g";
	public static final String PRR = "petroRad_r";
	public static final String PRI = "petroRad_i";
	public static final String PRZ = "petroRad_z";
	public static final String PR50U = "petroR50_u";
	public static final String PR50G = "petroR50_g";
	public static final String PR50R = "petroR50_r";
	public static final String PR50I = "petroR50_i";
	public static final String PR50Z = "petroR50_z";
	public static final String PR90U = "petroR90_u";
	public static final String PR90G = "petroR90_g";
	public static final String PR90R = "petroR90_r";
	public static final String PR90I = "petroR90_i";
	public static final String PR90Z = "petroR90_z";
	public static final String PRIMARY_TARGET = "primTarget";
	public static final String SECONDARY_TARGET = "secTarget";
	
	public static final String Z = "z";
	public static final String ZERR = "zErr";
	
	public static final String Z2 = "photozcc2";
	public static final String Z2ERR = "photozerrcc2";

	public static final String FIELDS1_DR8 = ""+OBJID + "," +
	RA + "," + DEC + "," + GALLON + "," + GALLAT + "," + DRU + "," + DRG + "," + DRR + "," + DRI + "," +
	DRZ + "," + PRU + "," + PRG + "," + PRR + "," + PRI + "," + PRZ + "," + PR50U + "," + PR50G + "," +
	PR50R + "," + PR50I + "," + PR50Z + "," + PR90U + "," + PR90G + "," + PR90R + "," + PR90I + "," + PR90Z;
	
	public static final String FIELDS1P_DR8 = "p."+OBJID + ",p." +
	RA + ",p." + DEC + ",p." + GALLON + ",p." + GALLAT + ",p." + DRU + ",p." + DRG + ",p." + DRR + ",p." + DRI + ",p." +
	DRZ + ",p." + PRU + ",p." + PRG + ",p." + PRR + ",p." + PRI + ",p." + PRZ + ",p." + PR50U + ",p." + PR50G + ",p." +
	PR50R + ",p." + PR50I + ",p." + PR50Z + ",p." + PR90U + ",p." + PR90G + ",p." + PR90R + ",p." + PR90I + ",p." + PR90Z;

	public static final String FIELDS1_DR9 = ""+OBJID + "," +
	RA + "," + DEC + "," + GALLON + "," + GALLAT + "," + DRU + "," + DRG + "," + DRR + "," + DRI + "," +
	DRZ + "," + PRU + "," + PRG + "," + PRR + "," + PRI + "," + PRZ + "," + PR50U + "," + PR50G + "," +
	PR50R + "," + PR50I + "," + PR50Z + "," + PR90U + "," + PR90G + "," + PR90R + "," + PR90I + "," + PR90Z;
	
	public static final String FIELDS1P_DR9 = "p."+OBJID + ",p." +
	RA + ",p." + DEC + ",p." + GALLON + ",p." + GALLAT + ",p." + DRU + ",p." + DRG + ",p." + DRR + ",p." + DRI + ",p." +
	DRZ + ",p." + PRU + ",p." + PRG + ",p." + PRR + ",p." + PRI + ",p." + PRZ + ",p." + PR50U + ",p." + PR50G + ",p." +
	PR50R + ",p." + PR50I + ",p." + PR50Z + ",p." + PR90U + ",p." + PR90G + ",p." + PR90R + ",p." + PR90I + ",p." + PR90Z;

	public static final String FIELDS1_DR17 = FIELDS1_DR9;
	public static final String FIELDS1P_DR17 = FIELDS1P_DR9;
	
	// DR7 values
	
	public static final String FIELDS1_DR7 = ""+OBJID + "," +
	RA + "," + DEC + "," + GALLON + "," + GALLAT + "," + DRU + "," + DRG + "," + DRR + "," + DRI + "," +
	DRZ + "," + PRU + "," + PRG + "," + PRR + "," + PRI + "," + PRZ + "," + PR50U + "," + PR50G + "," +
	PR50R + "," + PR50I + "," + PR50Z + "," + PR90U + "," + PR90G + "," + PR90R + "," + PR90I + "," + PR90Z + ","+
	PRIMARY_TARGET + "," + SECONDARY_TARGET;
	
	public static final String FIELDS1P_DR7 = "p."+OBJID + ",p." +
	RA + ",p." + DEC + ",p." + GALLON + ",p." + GALLAT + ",p." + DRU + ",p." + DRG + ",p." + DRR + ",p." + DRI + ",p." +
	DRZ + ",p." + PRU + ",p." + PRG + ",p." + PRR + ",p." + PRI + ",p." + PRZ + ",p." + PR50U + ",p." + PR50G + ",p." +
	PR50R + ",p." + PR50I + ",p." + PR50Z + ",p." + PR90U + ",p." + PR90G + ",p." + PR90R + ",p." + PR90I + ",p." + PR90Z + ",p."+
	PRIMARY_TARGET + ",p." + SECONDARY_TARGET;
	
	public static final String FIELDS2 = Z + "," + ZERR;
	
	public static final String FIELDS3 = Z2 + "," + Z2ERR;
	
	// Get these from PhotoObjAll
	protected String objID = null; // objid
	
	// magnitudes
	protected double u; // dered_u = modelMag-extinction
	protected double g; // dered_g = modelMag-extinction
	protected double r; // dered_r = modelMag-extinction
	protected double i; // dered_i = modelMag-extinction
	protected double z; // dered_z = modelMag-extinction
	
	// radii
	protected double petroRadU; // petroRad_u
	protected double petroRadG; // petroRad_g
	protected double petroRadR; // petroRad_r
	protected double petroRadI; // petroRad_i
	protected double petroRadZ; // petroRad_z
	
	protected double petroR50U; // petroR50_u
	protected double petroR50G; // petroR50_g
	protected double petroR50R; // petroR50_r
	protected double petroR50I; // petroR50_i
	protected double petroR50Z; // petroR50_z

	protected double petroR90U; // petroR90_u
	protected double petroR90G; // petroR90_g
	protected double petroR90R; // petroR90_r
	protected double petroR90I; // petroR90_i
	protected double petroR90Z; // petroR90_z

	// positions
	protected double galLon = 0; // galactic longitude, b
	protected double galLat = 0; // galactic latitude, l
	
	// redshifts
	public double specZ = 0;  // get from SpecPhotoAll, z
	protected double specZErr = 0; // zErr
	
	protected double photoZ = 0; // get from Photoz, z
	protected double photoZErr = 0; // zErr
	
	protected double photoZ2 = 0; // get from Photoz2, photozcc2
	protected double photoZ2Err = 0; // photozerrcc2
	
	protected long primaryTarget = 0;
	protected long secondaryTarget = 0;
	
	public static List<SDSSObject> parseObjectsFromXMLString(String xml)
	{
		List<SDSSObject> list = null;
		String tmpXml = xml.toLowerCase();
		if(tmpXml.contains("<item") && tmpXml.contains("</item>"))
		{
			list = parseObjectsFromXMLItemsString(xml);
		}
		else if(tmpXml.contains("<row") && tmpXml.contains("</row>"))
		{
			list = parseObjectsFromXMLTagString(xml);
		}
		else
		{
			list = parseObjectsFromXMLAttributesString(xml);
		}
		return list;
	}	
	
	@SuppressWarnings("unchecked")
	public static List<SDSSObject> parseObjectsFromXMLTagString(String xml)
	{
		List<SDSSObject> list = new ArrayList<SDSSObject>(5);
		xml = xml.toLowerCase();
		int ind1 = xml.indexOf("<answer>");
		int ind2 = xml.indexOf("</answer>");
		if(ind2 > ind1 && ind1 > -1)
		{
	        XmlReader r = new XmlReader();
	        XmlTag tag = r.parseTag(xml.substring(ind1,ind2+9));

			List<XmlTag> rows = (List<XmlTag>)tag.getChildren("row");
			for(int i=0; i<rows.size(); i++)
			{
				list.add(parseFromXMLTag(rows.get(i)));
			}
		}
		return list;
	}

	public static List<SDSSObject> parseObjectsFromXMLItemsString(String xml)
	{
		List<SDSSObject> list = new ArrayList<SDSSObject>(5);
		xml = xml.toLowerCase();
		int ind1 = xml.indexOf("<row>");
		int ind2 = xml.indexOf("</row>");
		while(ind1 > 0 && ind2 > ind1)
		{
			String str = xml.substring(ind1,ind2+6);
			SDSSObject obj = SDSSObject.parseFromXMLItemStringRow(str);
			if(obj != null)
			{
				list.add(obj);
			}
			ind1 = xml.indexOf("<row>",ind2);
			if(ind1>0)ind2 = xml.indexOf("</row>",ind1);
		}
		return list;
	}

	public static List<SDSSObject> parseObjectsFromXMLAttributesString(String xml)
	{
		List<SDSSObject> list = new ArrayList<SDSSObject>(5);
		//System.out.println(xml);
		SDSSObject obj = null;
		// find <Row and </Row>
		String tmpXml = xml.toLowerCase();
		
		int ind = tmpXml.indexOf("<row");
		if(ind == -1)
		{
			return null;
		}

		int ind2 = tmpXml.indexOf("/>",ind);
		if(ind2 == -1)
		{
			return null;
		}

		tmpXml = xml.substring(ind,ind2+2);
		// We have at least one row, let's parse it
		obj = parseFromXMLAttributeString(tmpXml);
		list.add(obj);
		
		String lc = xml.toLowerCase();
		
		ind = lc.indexOf("<row",ind2);
		while(ind >-1)
		{
			ind2 = lc.indexOf("/>",ind);
			if(ind2 > -1)
			{
				tmpXml = xml.substring(ind,ind2+2);
				obj = parseFromXMLAttributeString(tmpXml);
				list.add(obj);
				ind = lc.indexOf("<row",ind2);
			}
			else
			{
				ind = -1;
			}
		}
		
		return list;
	}
	
	public static SDSSObject parseFromXMLString(String xml)
	{
		if(xml.toLowerCase().contains("item name"))
		{
			return parseFromXMLItemString(xml);			
		}
		else if(xml.toLowerCase().contains("</row>"))
		{
			return parseFromXMLTagString(xml);
		}
		else
		{
			return parseFromXMLAttributeString(xml);
		}
	}
	
	/**
	 * Construct an SdssObject and populate from xml.  This method expects
	 * values from the PhotoObjAll table.  No spec (redshift) data is handled
	 * here.
	 * 
	 * @param xml
	 * @return
	 */
	public static SDSSObject parseFromXMLAttributeString(String xml)
	{
		String tmpXml = xml;
		
		SDSSObject obj = new SDSSObject();
		//System.out.println(tmpXml);
		
		// get fields
		obj.objID = getStringParameter(tmpXml,OBJID);
		obj.name = obj.objID;
		obj.setRADeg(getDoubleParameter(tmpXml,RA));
		obj.setDecDeg(getDoubleParameter(tmpXml,DEC));
		obj.galLon = getDoubleParameter(tmpXml,GALLON);
		obj.galLat = getDoubleParameter(tmpXml,GALLAT);
		
		obj.u = getDoubleParameter(tmpXml,DRU);
		obj.g = getDoubleParameter(tmpXml,DRG);
		obj.r = getDoubleParameter(tmpXml,DRR);
		obj.i = getDoubleParameter(tmpXml,DRI);
		obj.z = getDoubleParameter(tmpXml,DRZ);
		obj.magnitude = obj.g;
		
		obj.petroRadU = getDoubleParameter(tmpXml,PRU);
		obj.petroRadG = getDoubleParameter(tmpXml,PRG);
		obj.petroRadR = getDoubleParameter(tmpXml,PRR);
		obj.petroRadI = getDoubleParameter(tmpXml,PRI);
		obj.petroRadZ = getDoubleParameter(tmpXml,PRZ);
		obj.petroR50U = getDoubleParameter(tmpXml,PR50U);
		obj.petroR50G = getDoubleParameter(tmpXml,PR50G);
		obj.petroR50R = getDoubleParameter(tmpXml,PR50R);
		obj.petroR50I = getDoubleParameter(tmpXml,PR50I);
		obj.petroR50Z = getDoubleParameter(tmpXml,PR50Z);
		obj.petroR90U = getDoubleParameter(tmpXml,PR90U);
		obj.petroR90G = getDoubleParameter(tmpXml,PR90G);
		obj.petroR90R = getDoubleParameter(tmpXml,PR90R);
		obj.petroR90I = getDoubleParameter(tmpXml,PR90I);
		obj.petroR90Z = getDoubleParameter(tmpXml,PR90Z);
		obj.primaryTarget = getLongParameter(tmpXml,PRIMARY_TARGET);
		obj.secondaryTarget = getLongParameter(tmpXml,SECONDARY_TARGET);
		
		return obj;
	}
	
	public static SDSSObject parseFromXMLItemString(String xml)
	{
		SDSSObject obj = null;
		xml = xml.toLowerCase();
		int ind1 = xml.indexOf("<row");
		int ind2 = xml.indexOf("</row>");
		if(ind2 > ind1 && ind1 > -1)
		{
			String str = xml.substring(ind1,ind2+6);
			obj = parseFromXMLItemStringRow(str);
		}
		return obj;
	}
	
	public static SDSSObject parseFromXMLItemStringRow(String xml)
	{
	
		SDSSObject obj = new SDSSObject();
		int ind1 = 0;
		int ind2 = 0;
		String str = xml.substring(5,xml.length()-6).trim();
		str = str.replaceAll("</item>", "\n");
		str = str.replaceAll("\r\n", "\n");
		str = str.replaceAll("\n\n", "\n");
		str = str.replaceAll("\n\n", "\n");
		String items[] = str.split("\n");
		
		String item = null;
		String name = null;
		String val = null;
		for(int i=0; i<items.length; i++)
		{
			item = items[i];
			ind1 = item.indexOf("name=");
			if(ind1<0)continue;
			ind1+=6;
			ind2=item.indexOf('"',ind1);
			name = item.substring(ind1,ind2);
			ind2 = item.indexOf('>');
			val = item.substring(ind2+1).trim();
			//System.out.println(name + "\t" + val + "\t" + item);
			
			if(name.equalsIgnoreCase(OBJID))
			{
				obj.objID=val;
				obj.name = val;
			}
			else if(name.equalsIgnoreCase(RA))
			{
				obj.raDeg=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(DEC))
			{
				obj.decDeg=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(GALLON))
			{
				obj.galLon=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(GALLAT))
			{
				obj.galLat=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(DRU))
			{
				obj.u=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(DRG))
			{
				obj.g=Double.parseDouble(val);
				obj.magnitude=obj.g;
			}
			else if(name.equalsIgnoreCase(DRR))
			{
				obj.r=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(DRI))
			{
				obj.i=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(DRZ))
			{
				obj.z=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PRU))
			{
				obj.petroRadU=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PRG))
			{
				obj.petroRadG=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PRR))
			{
				obj.petroRadR=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PRI))
			{
				obj.petroRadI=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PRZ))
			{
				obj.petroRadZ=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR50U))
			{
				obj.petroR50U=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR50G))
			{
				obj.petroR50G=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR50R))
			{
				obj.petroR50R=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR50I))
			{
				obj.petroR50I=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR50Z))
			{
				obj.petroR50Z=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR90U))
			{
				obj.petroR90U=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR90G))
			{
				obj.petroR90G=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR90R))
			{
				obj.petroR90R=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR90I))
			{
				obj.petroR90I=Double.parseDouble(val);
			}
			else if(name.equalsIgnoreCase(PR90Z))
			{
				obj.petroR90Z=Double.parseDouble(val);
			}
		}

		return obj;
	}
	/**
	 * Construct an SdssObject and populate from xml.  This method expects
	 * values from the PhotoObjAll table.  No spec (redshift) data is handled
	 * here.
	 * 
	 * @param xml
	 * @return
	 */
	public static SDSSObject parseFromXMLTagString(String xml)
	{
		SDSSObject obj = new SDSSObject();
		xml = xml.toLowerCase();
		int ind1 = xml.indexOf("<row");
		int ind2 = xml.indexOf("</row>");
		if(ind2 > ind1 && ind1 > -1)
		{
			XmlReader r = new XmlReader();
			XmlTag tag = r.parseTag(xml.substring(ind1,ind2+6));
			obj = parseFromXMLTag(tag);
		}
		return obj;
	}
	
	public static SDSSObject parseFromXMLTag(XmlTag row)
	{
		SDSSObject obj = new SDSSObject();
		//System.out.println(tmpXml);
	
		XmlTag tmpTag = null;
		
		tmpTag = row.getChild(OBJID, true);
		if(tmpTag != null)
		{
			obj.objID = tmpTag.getValue();
			obj.name = obj.objID;
		}
		
		// get fields
		obj.setRADeg(getDoubleChildValue(row,RA));
		obj.setDecDeg(getDoubleChildValue(row,DEC));
		obj.galLon = getDoubleChildValue(row,GALLON);
		obj.galLat = getDoubleChildValue(row,GALLAT);
		
		obj.u = getDoubleChildValue(row,DRU);
		obj.g = getDoubleChildValue(row,DRG);
		obj.r = getDoubleChildValue(row,DRR);
		obj.i = getDoubleChildValue(row,DRI);
		obj.z = getDoubleChildValue(row,DRZ);
		obj.magnitude = obj.g;
		
		obj.petroRadU = getDoubleChildValue(row,PRU);
		obj.petroRadG = getDoubleChildValue(row,PRG);
		obj.petroRadR = getDoubleChildValue(row,PRR);
		obj.petroRadI = getDoubleChildValue(row,PRI);
		obj.petroRadZ = getDoubleChildValue(row,PRZ);
		obj.petroR50U = getDoubleChildValue(row,PR50U);
		obj.petroR50G = getDoubleChildValue(row,PR50G);
		obj.petroR50R = getDoubleChildValue(row,PR50R);
		obj.petroR50I = getDoubleChildValue(row,PR50I);
		obj.petroR50Z = getDoubleChildValue(row,PR50Z);
		obj.petroR90U = getDoubleChildValue(row,PR90U);
		obj.petroR90G = getDoubleChildValue(row,PR90G);
		obj.petroR90R = getDoubleChildValue(row,PR90R);
		obj.petroR90I = getDoubleChildValue(row,PR90I);
		obj.petroR90Z = getDoubleChildValue(row,PR90Z);
		obj.primaryTarget = (long)getDoubleChildValue(row,PRIMARY_TARGET);
		obj.secondaryTarget = (long)getDoubleChildValue(row,SECONDARY_TARGET);
		
		return obj;
	}
	
	public static double getDoubleChildValue(XmlTag tag, String childName)
	{
		double num = 0;
		XmlTag tmp = tag.getChild(childName, true);
		if(tmp != null) num = tmp.getDoubleValue();
		return num;
	}
	
	
	/**
	 * Parse the string attribute from the xml object.
	 * 
	 * @param xml
	 * @param attribute
	 * @return
	 */
	public static String getStringParameter(String xml, String attribute)
	{
		String str = "";
		try
		{
			int ind = xml.indexOf(attribute+"=");
			ind = xml.indexOf("\"",ind);
			int ind2 = xml.indexOf("\"",ind+1);
			str = xml.substring(ind+1,ind2);
		}
		catch(Exception ex)
		{
			
		}
		
		return str;
	}
	
	/**
	 * Parse the double attribute from the xml object.
	 * 
	 * @param xml
	 * @param attribute
	 * @return
	 */
	public static double getDoubleParameter(String xml, String attribute)
	{
		double val = 0;
		try
		{
			val = Double.parseDouble(getStringParameter(xml,attribute).trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return val;
	}
	
	/**
	 * Parse the long parameter fromt the xml.
	 * 
	 * @param xml
	 * @param attribute
	 * @return
	 */
	public static long getLongParameter(String xml, String attribute)
	{
		long val = 0;
		try
		{
			val = Long.parseLong(getStringParameter(xml,attribute).trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return val;
	}

    
	public String getObjID()
	{
		return objID;
	}
	
	public void setSpecInfo(String xml)
	{
		// find <Row and </Row>
		String tmpXml = xml.toLowerCase();
		if(tmpXml.contains("name=\"z\""))
		{
			int ind = tmpXml.indexOf("name=\"z\"");
			ind+=9;
			int ind2 = tmpXml.indexOf("<",ind);
			specZ = Double.parseDouble(tmpXml.substring(ind,ind2).trim());
			
			ind = tmpXml.indexOf("name=\"zerr\"");
			ind+=12;
			ind2 = tmpXml.indexOf("<",ind);
			specZErr = Double.parseDouble(tmpXml.substring(ind,ind2).trim());
			
			return;
		}

		int ind = tmpXml.indexOf("<row");
		if(ind == -1)
		{
			return;
		}
		
		int ind2 = tmpXml.indexOf("</row>");
		
		// treat as tag-based
		if(ind2 > -1)
		{
			XmlReader r = new XmlReader();
			XmlTag tag = r.parseTag(tmpXml.substring(ind,ind2+6));
			if(tag != null)
			{
				specZ = getDoubleChildValue(tag,Z);
				redshift=specZ;
				specZErr = getDoubleChildValue(tag,ZERR);
			}
			return;
		}
		
		ind2 = tmpXml.indexOf("/>",ind);
		if(ind2 == -1 || ind2 <= ind)
		{
			return;
		}
		tmpXml = xml.substring(ind,ind2+2);

		// get fields
		specZ = getDoubleParameter(tmpXml,Z);
		redshift = specZ;
		specZErr = getDoubleParameter(tmpXml,ZERR);
	}
    
	public void setPhotoZInfo(String xml)
	{
		// find <Row and </Row>
		String tmpXml = xml.toLowerCase();
		if(tmpXml.contains("name=\"z\""))
		{
			int ind = tmpXml.indexOf("name=\"z\"");
			ind+=9;
			int ind2 = tmpXml.indexOf("<",ind);
			photoZ = Double.parseDouble(tmpXml.substring(ind,ind2).trim());
			
			ind = tmpXml.indexOf("name=\"zerr\"");
			ind+=12;
			ind2 = tmpXml.indexOf("<",ind);
			photoZErr = Double.parseDouble(tmpXml.substring(ind,ind2).trim());
			
			return;
		}
		
		int ind = tmpXml.indexOf("<row");
		if(ind == -1)
		{
			return;
		}
		int ind2 = tmpXml.indexOf("</row>");
		
		// treat as tag-based
		if(ind2 > -1)
		{
			XmlReader r = new XmlReader();
			XmlTag tag = r.parseTag(tmpXml.substring(ind,ind2+6));
			if(tag != null)
			{
				photoZ = getDoubleChildValue(tag,Z);
				photoZErr = getDoubleChildValue(tag,ZERR);
			}
			return;
		}
		
		ind2 = tmpXml.indexOf("/>",ind);
		if(ind2 == -1 || ind2 <= ind)
		{
			return;
		}
		tmpXml = xml.substring(ind,ind2+2);

		// get fields
		photoZ = getDoubleParameter(tmpXml,Z);
		photoZErr = getDoubleParameter(tmpXml,ZERR);
	}	
    
	public void setPhotoZ2Info(String xml)
	{
		// find <Row and </Row>
		String tmpXml = xml.toLowerCase();
		
		int ind = tmpXml.indexOf("<row");
		if(ind == -1)
		{
			return;
		}		int ind2 = tmpXml.indexOf("</row>");
		
		// treat as tag-based
		if(ind2 > -1)
		{
			XmlReader r = new XmlReader();
			XmlTag tag = r.parseTag(tmpXml.substring(ind,ind2+6));
			if(tag != null)
			{
				photoZ2 = getDoubleChildValue(tag,Z);
				photoZ2Err = getDoubleChildValue(tag,ZERR);
			}
			return;
		}
		
		ind2 = tmpXml.indexOf("/>",ind);
		if(ind2 == -1 || ind2 <= ind)
		{
			return;
		}
		tmpXml = xml.substring(ind,ind2+2);

		// get fields
		photoZ2 = getDoubleParameter(tmpXml,Z2);
		photoZ2Err = getDoubleParameter(tmpXml,Z2ERR);
	}
	
    protected String getRaDecParams()
    {
    	String str = null;
    	str = "ra=" + String.valueOf(getRADeg());
    	str += "&dec=" + String.valueOf(getDecDeg());
    	return str;    	
    }
    
    public String formatHMS(double num, String delim)
    {
    	StringBuffer sb = new StringBuffer(100);
    	String sign = "";
    	if(num < 0)
    	{
    		sign = "-";
    		num*=-1.0d;
    	}
    	int hr = (int)num;
    	double frac = num-hr;
    	frac *=60.0d;
    	int min = (int)frac;
    	frac -= min;
    	frac *=60.0d;
    	
    	String fracStr = String.valueOf(frac);
    	if(fracStr.length() > 5)
    	{
    		fracStr = fracStr.substring(0,5);
    	}
    	
    	sb.append(sign).append(hr).append(delim).append(min).append(delim).append(fracStr);
    	
    	return sb.toString();
    }
      
    public String getRaHrString()
    {
    	return formatHMS(getRAHr(),":");
    }
    
    public String getDecDegString()
    {
    	return formatHMS(getDecDeg(),":");    	
    }
    
    public void setRADeg(int deg, int min, double sec)
    {
    	double mn = ((double)min)+sec/60.0d;
    	setRADeg(deg,mn);
    }
    
    public void setRADeg(int deg, double min)
    {
    	setRADeg(((double)deg)+min/60.0d);
    }

    public void setRAHr(int hr, int min, double sec)
    {
    	double mn = ((double)min)+sec/60.0d;
    	setRAHr(hr,mn);
    }
    
    public void setRAHr(int hr, double min)
    {
    	double ra = ((double)hr)+min/60.0d;
    	setRAHr(ra);
    }

    public void setRAHr(double hr)
    {
    	setRADeg(15.0*hr);
    }
    
    public void setDecDeg(String sgn, int deg, int min, double sec)
    {
    	double mn = ((double)min)+sec/60.0d;
    	setDecDeg(sgn,deg,mn);
    }
    
    public void setDecDeg(String sgn, int deg, double min)
    {
    	double dec = ((double)deg)+min/60.0d;
    	if(sgn != null && sgn.trim().equals("-"))
    	{
    		dec *= -1.0d;
    	}
    	
    	setDecDeg(dec);
    }
    
    public void setRAHr(String raStr)
    {
    	double d[] = parseDMS(raStr);
    	setRAHr((int)d[0],(int)d[1],d[2]);
    }
    
    public void setDecDeg(String decStr)
    {
    	double d[] = parseDMS(decStr);
    	int mult = 1;
    	if(d[0] < 0)
    	{
    		mult = -1;
    		d[0] = -1*d[0];
    	}
    	double deg = d[2]/60.0d;
    	deg += d[1];
    	deg = d[0] + deg/60.0d;
    	deg *= mult;
    	setDecDeg(deg);    	
    }
    
    public double[] parseDMS(String str)
    {
    	double d[] = new double[3];
    	if(str == null || str.trim().length() == 0)
    	{
    		return d;
    	}
    	
    	double deg = 0;
    	double min = 0;
    	double sec = 0;
    	
    	String delim = " ";
    	
    	if(str.indexOf(':') > -1)
    	{
    		delim = ":";
    	}
    	
    	String sa[] = str.split(delim);
    	if(sa[0].startsWith("+"))
    	{
    		sa[0] = sa[0].substring(1).trim();
    	}
    	
    	try{deg = Double.parseDouble(sa[0].trim());}catch(Exception ex){};
    	try{min = Double.parseDouble(sa[1].trim());}catch(Exception ex){};
    	try{sec = Double.parseDouble(sa[2].trim());}catch(Exception ex){};
    	
    	d[0] = deg;
    	d[1] = min;
    	d[2] = sec;
    	
    	return d;
    }
    
    public String toString()
    {
    	return objID + "," + formatHMS(getRAHr(),":") + "," + formatHMS(getDecDeg(),":");
    }
    
    /**
     * Set the relevant values for the pair data file.
     * 
     * @param pd
     */
    /*
	public void setPairDataPrimary(PairData pd)
	{
		pd.setSdssObjId(objID);
		pd.setDisplayName("SDSS " + objID);
		pd.setPrimaryName(objID);
		pd.setPrimaryMagnitude(g);
		pd.setPrimaryDEC(decDeg);
		pd.setPrimaryRA(getRaHr());
		pd.pu = u;
		pd.pg = g;
		pd.pr = r;
		pd.pi = i;
		pd.pz = z;
		
		// decide which redshift to use
		if(specZ > 0)
		{
			pd.predshift = specZ;
		}
		else
		{
			if(photoZErr < photoZ2Err)
			{
				pd.predshift = photoZ;
			}
			else
			{
				pd.predshift = photoZ2;
			}
		}
		pd.primaryVelocity = 3.0e5*pd.predshift;

	}
	*/
    
    /**
     * Ensures redshift is set and then calculates M2L
     */
    public void setDerivedValues()
    {
    	redshift = specZ;
    	if(specZ <=0)
    	{
    		if(photoZErr < photoZ2Err || photoZ2Err == 0)
    		{
    			redshift = photoZ;
    		}
    		else
    		{
    			redshift = photoZ2;
    		}
    	}
    	
    	double masses[] = Mass2Light.getMasses(u, g, r, i, z, redshift);
    	mass = masses[1];
    }
    
    public double[] getOutputValues()
    {
    	return new double[]{u,g,r,i,z,specZ,photoZ,photoZ2};
    }
    
	/**
	 * Set the relevant value for the pair data file.
	 * 
	 * @param pd
	 */
	/*
	public void setPairDataSecondary(PairData pd)
	{
		pd.setSecondaryName(objID);
		pd.setSecondaryMagnitude(g);
		pd.setSecondaryDEC(decDeg);
		pd.setSecondaryRA(getRaHr());
		pd.su = u;
		pd.sg = g;
		pd.sr = r;
		pd.si = i;
		pd.sz = z;
		pd.sredshift = specZ;
		
		// decide which redshift to use
		if(specZ > 0)
		{
			pd.sredshift = specZ;
		}
		else
		{
			if(photoZErr < photoZ2Err)
			{
				pd.sredshift = photoZ;
			}
			else
			{
				pd.sredshift = photoZ2;
			}
			
			// we can also see which photoZ is closest to primary z
			if(pd.predshift > 0)
			{
				if(Math.abs(photoZ - pd.predshift) <
				   Math.abs(photoZ2 - pd.predshift))
				{
					pd.sredshift = photoZ;
				}
				else
				{
					pd.sredshift = photoZ2;
				}
			}
		}

		pd.secondaryVelocity = 3.0e5*pd.sredshift;
	}
	*/
	/*
	public void setPrimaryMass(PairData pd)
	{
		double masses[] = Mass2Light.getMasses(u, g, r, i, z, pd.predshift);
		pd.setPrimaryMass(masses[1]);
		pd.setPrimaryMassMin(masses[0]);
		pd.setPrimaryMassMax(masses[2]);
	}
	
	public void setSecondaryMass(PairData pd)
	{
		double masses[] = Mass2Light.getMasses(u, g, r, i, z, pd.sredshift);
		pd.setSecondaryMass(masses[1]);
		pd.setSecondaryMassMin(masses[0]);
		pd.setSecondaryMassMax(masses[2]);
	}
	*/
	
	/**
	 * Returns true if any of the 4 galaxy primary target
	 * flags are set.
	 * 
	 * @return
	 */
	public boolean hasGalaxyPrimaryTarget()
	{
		if((primaryTarget & TARGET_GALAXY)==TARGET_GALAXY)
		{
			return true;
		}
		if((primaryTarget & TARGET_GALAXY_RED)==TARGET_GALAXY_RED)
		{
			return true;
		}
		if((primaryTarget & TARGET_GALAXY_BIG)==TARGET_GALAXY_BIG)
		{
			return true;
		}
		if((primaryTarget & TARGET_GALAXY_BRIGHT_CORE)==TARGET_GALAXY_BRIGHT_CORE)
		{
			return true;
		}
		
		return false;
	}
	
}
