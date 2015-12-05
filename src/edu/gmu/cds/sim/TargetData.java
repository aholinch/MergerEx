/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

import java.io.FileWriter;
import java.util.Properties;

import edu.gmu.cds.util.ApplicationProperties;
import edu.gmu.cds.util.FileUtil;
import edu.gmu.cds.util.PropertyUtil;

public class TargetData 
{
    public String name;
    public String imageFile;
    public String targetImageFile;  // usually gray scale, will be edited
    public String currentDir;
    
    public int imageHeight; // pixels
    public int imageWidth;  // pixels
    public double imageSizeArcMin; 
    public double centerRA;
    public double centerDec;
    
    public DiskInfo primaryDisk;
    public DiskInfo secondaryDisk;
    
    public double mins[] = null;
    public double maxs[] = null;
    public String labels[] = {"rx","ry","rz","vx","vy","vz","m1","m2","r1","r2"};
    
    public TargetData()
    {
    	
    }
    
    public void save(String file)
    {
    	FileWriter fw = null;
    	
    	try
    	{
    		// we write directly into file because properties class doesn't guarantee order
    		fw = new FileWriter(file);
    	
    		currentDir = FileUtil.getDirectoryName(file);
    		
    		fw.write("#target data file written " + (new java.util.Date()).toString() + "\n");
    		fw.write("name="+String.valueOf(name)+"\n");
    		fw.write("imagefile="+String.valueOf(imageFile)+"\n");
    		fw.write("tgtimagefile="+String.valueOf(targetImageFile)+"\n");
    		fw.write("imageheight="+String.valueOf(imageHeight)+"\n");
    		fw.write("imagewidth="+String.valueOf(imageWidth)+"\n");
    		fw.write("centerra="+String.valueOf(centerRA)+"\n");
    		fw.write("centerdec="+String.valueOf(centerDec)+"\n");
    		fw.write("imagesizearcmin="+String.valueOf(imageSizeArcMin)+"\n");
    		
    		fw.write("\n#primary disk info\n");
    		writeDiskInfo(fw,primaryDisk,"p");
    		fw.write("\n#secondary disk info\n");
    		writeDiskInfo(fw,secondaryDisk,"s");
    		
    		writeSimRanges(fw);
    		
    		fw.flush();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		if(fw != null)try{fw.close();}catch(Exception ex){};
    	}
    }
    
    protected void writeDiskInfo(FileWriter fw, DiskInfo info, String prefix) throws Exception
    {
    	if(info == null) return;
    	fw.write(prefix+"name="+info.getObjectName()+"\n");
    	fw.write(prefix+"xc="+info.getXC()+"\n");
    	fw.write(prefix+"yc="+info.getYC()+"\n");
    	fw.write(prefix+"ra="+info.getRADeg()+"\n");
    	fw.write(prefix+"dec="+info.getDecDeg()+"\n");
    	fw.write(prefix+"redshift="+info.getRedshift()+"\n");
    	fw.write(prefix+"distance="+info.getDistanceMpc()+"\n");
    	fw.write(prefix+"mass="+info.getMassSM()+"\n");
    	fw.write(prefix+"theta="+info.getThetaDeg()+"\n");
    	fw.write(prefix+"phi="+info.getPhiDeg()+"\n");
    	fw.write(prefix+"radius="+info.getRadArcMin()+"\n");
    }
    
    protected DiskInfo readDiskInfo(Properties props, String prefix)
    {
    	DiskInfo info = new DiskInfo();
    	
    	info.setObjectName(props.getProperty(prefix+"name"));
    	info.setXC(PropertyUtil.getDouble(props,prefix+"xc"));
    	info.setYC(PropertyUtil.getDouble(props,prefix+"yc"));
    	info.setRADeg(PropertyUtil.getDouble(props,prefix+"ra"));
    	info.setDecDeg(PropertyUtil.getDouble(props,prefix+"dec"));
    	info.setDistanceMpc(PropertyUtil.getDouble(props,prefix+"distance"));
    	info.setRedshift(PropertyUtil.getDouble(props,prefix+"redshift"));
    	info.setMassSM(PropertyUtil.getDouble(props,prefix+"mass"));
    	info.setThetaDeg(PropertyUtil.getDouble(props,prefix+"theta"));
    	info.setPhiDeg(PropertyUtil.getDouble(props,prefix+"phi"));
    	info.setRadArcMin(PropertyUtil.getDouble(props,prefix+"radius"));
    	
    	return info;
    }
    
    protected void writeSimRanges(FileWriter fw) throws Exception
    {
    	fw.write("\n#simulation ranges\n");
    	
    	if(mins == null || maxs == null) return;
    	
    	for(int i=0; i<10; i++)
    	{
    	    fw.write(labels[i]+"min="+mins[i]+"\n");	
    	    fw.write(labels[i]+"max="+maxs[i]+"\n");	
    	}
    	
    	// 11 and 12 are different
    	fw.write("theta1range="+mins[10]+"\n");
    	fw.write("phi1range="+maxs[10]+"\n");
    	fw.write("theta2range="+mins[11]+"\n");
    	fw.write("phi2range="+maxs[11]+"\n");
    }
    
    protected void readSimRanges(Properties props)
    {
    	mins = new double[12];
    	maxs = new double[12];
    	
    	for(int i=0; i<10; i++)
    	{
    		mins[i] = PropertyUtil.getDouble(props,labels[i]+"min");
    		maxs[i] = PropertyUtil.getDouble(props,labels[i]+"max");
    	}
    	
    	mins[10] = PropertyUtil.getDouble(props,"theta1range");
    	maxs[10] = PropertyUtil.getDouble(props,"phi1range");
    	
    	mins[11] = PropertyUtil.getDouble(props,"theta2range");
    	maxs[11] = PropertyUtil.getDouble(props,"phi2range");
    }
    
    public void setCurrentDir(String dir)
    {
    	currentDir = dir;
    }
    
    public String getCurrentDir()
    {
    	return currentDir;
    }
    
    public void load(String file)
    {
    	Properties props = PropertyUtil.getProperties(file);
    	
    	currentDir = FileUtil.getDirectoryName(file);
    	
    	name = props.getProperty("name");
    	imageFile = props.getProperty("imagefile");
    	targetImageFile = props.getProperty("tgtimagefile");
    	imageHeight = PropertyUtil.getInt(props, "imageheight");
    	imageWidth = PropertyUtil.getInt(props, "imagewidth");
    	imageSizeArcMin = PropertyUtil.getDouble(props, "imagesizearcmin");
    	centerRA = PropertyUtil.getDouble(props, "centerra");
    	centerDec = PropertyUtil.getDouble(props, "centerdec");
    	
    	primaryDisk = readDiskInfo(props,"p");
    	secondaryDisk = readDiskInfo(props,"s");
    	
    	readSimRanges(props);
    }
    
    public DiskInfo getPrimaryDiskInfo()
    {
    	return primaryDisk;
    }
    
    public void setPrimaryDiskInfo(DiskInfo info)
    {
    	primaryDisk = info;
    }
    
    public DiskInfo getSecondaryDiskInfo()
    {
    	return secondaryDisk;
    }
    
    public void setSecondaryDiskInfo(DiskInfo info)
    {
    	secondaryDisk = info;
    }    
    
    public void setName(String str)
    {
    	name = str;
    }
    
    public String getName()
    {
    	return name;
    }
    
    public void setImageFile(String str)
    {
    	imageFile = str;
    	if(str != null)
    	{
    		imageFile = FileUtil.getFilename(str);
    	}
    }
    
    public String getImageFile()
    {
    	return getFilenameIfExists(imageFile);
    }
    
    public void setTargetImageFile(String str)
    {
    	targetImageFile = str;
    	if(str != null)
    	{
    		targetImageFile = FileUtil.getFilename(str);
    	}
    }
    
    public String getTargetImageFile()
    {
    	return getFilenameIfExists(targetImageFile);
    }
    
    protected String getFilenameIfExists(String filename)
    {
    	String str = FileUtil.ensureFileExists(filename, currentDir);
    	if(str == null)
    	{
    		str = FileUtil.ensureFileExists(filename, ApplicationProperties.getInstance().getWorkingDir());
    	}
    	
    	if(str == null)
    	{
    		str = FileUtil.ensureFileExists(filename, ApplicationProperties.getInstance().getDefaultDir());
    	}
    	
    	if(str != null) return str;
    	
    	return filename;
    }
    
    public void setImageHeight(int num)
    {
    	imageHeight = num;
    }
    
    public int getImageHeight()
    {
    	return imageHeight;
    }
    
    public void setImageWidth(int num)
    {
    	imageWidth = num;
    }
    
    public int getImageWidth()
    {
    	return imageWidth;
    }
    
    public void setImageSizeArcMin(double width)
    {
    	imageSizeArcMin = width;
    }
    
    public double getImageSizeArcMin()
    {
    	return imageSizeArcMin;
    }
    
    public void setCenterRA(double raDeg)
    {
    	centerRA = raDeg;
    }
    
    public double getCenterRA()
    {
    	return centerRA;
    }
    
    public void setCenterDec(double decDeg)
    {
    	centerDec = decDeg;
    }
    
    public double getCenterDec()
    {
    	return centerDec;
    }
    
    public double[] getMinimums()
    {
    	return mins;
    }
    
    public void setMinimums(double vals[])
    {
    	mins = vals;
    }
    
    public double[] getMaximums()
    {
    	return maxs;
    }
    
    public void setMaximums(double vals[])
    {
    	maxs = vals;
    }
    
    /**
     * Calculates the suggested scale for ScatterPanel and gnuplot to
     * use to match the image.
     * @return
     */
    public double[] calculateScale()
    {
	    double width = getImageWidth();
	    double height = getImageHeight();
	    
	    double px = getPrimaryDiskInfo().getXC();
	    double py = getPrimaryDiskInfo().getYC();
	    double sx = getSecondaryDiskInfo().getXC();
	    double sy = getSecondaryDiskInfo().getYC();
	    
	    double pd = Math.sqrt((px-sx)*(px-sx)+(py-sy)*(py-sy));
	    
	    double mins[] = getMinimums();
	    double rx = mins[0];
	    double ry = mins[1];
	    double id = Math.sqrt(rx*rx+ry*ry);
	    
	    double simx = width/pd*id;
	    double simy = height/pd*id;
	    
	    double minx = -0.5*simx;
	    double maxx = 0.5*simx;
	    double miny = -0.5*simy;
	    double maxy = 0.5*simy;	
	    
	    return new double[]{minx,maxx,miny,maxy};
    }
    
    public static TargetData loadFromFile(String file)
    {
    	TargetData pd = new TargetData();
    	pd.load(file);
    	return pd;
    }
    
    public static interface TargetDataHandler
    {
    	public boolean dataHasChanged();
    	
    	public void getInfoFromTargetData(TargetData data);

    	public void setInfoToTargetData(TargetData data);
    }
}
