/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

public class DiskInfo 
{
    public String targetName;
    public String objectName;
    public double xc;
    public double yc;
    public double raDeg;
    public double decDeg;
    public double redshift;
    public double distanceMpc;
    public double massSM;
    public double thetaDeg;
    public double phiDeg;
    public double radArcMin;
    
    public DiskInfo()
    {
    	
    }
    
    public String getTargetName()
    {
    	return targetName;
    }
    
    public void setTargetName(String str)
    {
    	targetName = str;
    }
    
    public String getObjectName()
    {
    	return objectName;
    }
    
    public void setObjectName(String str)
    {
    	objectName = str;
    }
    
    public double getXC()
    {
    	return xc;
    }
    
    public void setXC(double num)
    {
    	xc = num;
    }
    
    public double getYC()
    {
    	return yc;
    }
    
    public void setYC(double num)
    {
    	yc = num;
    }
    
    public double getRADeg()
    {
    	return raDeg;
    }
    
    public void setRADeg(double num)
    {
    	raDeg = num;
    }
    
    public double getDecDeg()
    {
    	return decDeg;
    }
    
    public void setDecDeg(double num)
    {
    	decDeg = num;
    }
    
    public double getRedshift()
    {
    	return redshift;
    }
    
    public void setRedshift(double val)
    {
    	redshift=val;
    }
    
    public double getDistanceMpc()
    {
    	return distanceMpc;
    }
    
    public void setDistanceMpc(double val)
    {
    	distanceMpc = val;
    }
    
    public double getMassSM()
    {
    	return massSM;
    }
    
    public void setMassSM(double val)
    {
    	massSM = val;
    }
    
    public double getThetaDeg()
    {
    	return thetaDeg;
    }
    
    public void setThetaDeg(double val)
    {
    	thetaDeg = val;
    }
    
    public double getPhiDeg()
    {
    	return phiDeg;
    }
    
    public void setPhiDeg(double val)
    {
    	phiDeg = val;
    }
    
    public double getRadArcMin()
    {
    	return radArcMin;
    }
    
    public void setRadArcMin(double val)
    {
    	radArcMin = val;
    }
}
