/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.obj;

public class ObjectInfo implements RADec
{
    public String name = "";
    public String type = "";
    public String source = "";
    public double raDeg = 0;
    public double decDeg = 0;
    public double redshift = 0;
    public double mass = 0;
    public double magnitude = 0;
    public double px = 0;
    public double py = 0;
    
    public ObjectInfo()
    {
    	
    }
    
    public ObjectInfo(String src)
    {
    	source = src;
    }
    
    public String getSource()
    {
    	return source;
    }
    
    public void setSource(String src)
    {
    	source = src;
    }
    
    public String getName()
    {
    	return name;
    }
    
    public void setName(String str)
    {
    	name = str;
    }
    
    public String getType()
    {
    	return type;
    }

    public void setType(String str)
    {
    	type = str;
    }
    
    public double getRADeg()
    {
    	return raDeg;
    }
    
    public void setRADeg(double deg)
    {
    	raDeg = deg;
    }
    
    public double getRAHr()
    {
    	return raDeg/15.0d;
    }
    
    public void setRAHr(double hr)
    {
    	raDeg = 15.0d*hr;
    }
    
    public double getDecDeg()
    {
    	return decDeg;
    }
    
    public void setDecDeg(double deg)
    {
    	decDeg = deg;
    }
    
    public double getRedshift()
    {
    	return redshift;
    }
    
    public void setRedshift(double val)
    {
    	redshift = val;
    }
        
    public double getMass()
    {
    	return mass;
    }
    
    public void setMass(double val)
    {
    	mass = val;
    }
    
    public double getPx()
    {
    	return px;
    }
    
    public void setPx(double val)
    {
    	px = val;
    }
    
    public double getPy()
    {
    	return py;
    }
    
    public void setPy(double val)
    {
    	py = val;
    }
    
    public String toString()
    {
    	return String.valueOf(name)+","+String.valueOf(raDeg)+","+String.valueOf(decDeg)+","+String.valueOf(redshift);
    }
}
