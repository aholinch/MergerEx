/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.util;

public class PerformanceTest 
{
    public static void test2d(int n, int d, int iter)
    {
    	double x[][] = new double[n][d];
    	for(int i=0; i<iter; i++)
    	{
    		MonitorUtil.startCall("2diter");
    		for(int j=0; j<n; j++)
    		{
    			for(int k=0; k<d; k++)
    			{
    				x[j][k] = x[j][k]+1;
    			}
    		}
    		MonitorUtil.endCall("2diter");
    	}
    }

    public static void test1d(int n, int d, int iter)
    {
    	double x[] = new double[n*d];
    	int off = 0;
    	for(int i=0; i<iter; i++)
    	{
    		MonitorUtil.startCall("1diter");
    		for(int j=0; j<n; j++)
    		{
    			off = j*d;
    			for(int k=0; k<d; k++)
    			{
    				x[off+k]=x[off+k]+1;
    			}
    		}
    		MonitorUtil.endCall("1diter");
    	}
    }
    public static void test2dunroll(int n, int iter)
    {
    	int d = 7;
    	double x[][] = new double[n][d];
    	for(int i=0; i<iter; i++)
    	{
    		MonitorUtil.startCall("2diterunroll");
    		for(int j=0; j<n; j++)
    		{
   				x[j][0] = x[j][0]+1;
   				x[j][1] = x[j][1]+1;
   				x[j][2] = x[j][2]+1;
   				x[j][3] = x[j][3]+1;
   				x[j][4] = x[j][4]+1;
   				x[j][5] = x[j][5]+1;
   				x[j][6] = x[j][6]+1;
    		}
    		MonitorUtil.endCall("2diterunroll");
    	}
    }

    public static void test1dunroll(int n, int iter)
    {
    	int d = 7;
    	double x[] = new double[n*d];
    	int off = 0;
    	for(int i=0; i<iter; i++)
    	{
    		MonitorUtil.startCall("1diterunroll");
    		for(int j=0; j<n; j++)
    		{
    			off = j*d;
    			x[off]=x[off]+1;
    			x[off+1]=x[off+1]+1;
    			x[off+2]=x[off+2]+1;
    			x[off+3]=x[off+3]+1;
    			x[off+4]=x[off+4]+1;
    			x[off+5]=x[off+5]+1;
    			x[off+6]=x[off+6]+1;
    		}
    		MonitorUtil.endCall("1diterunroll");
    	}
    }
    
    public static void main(String args[])
    {
    	test2d(10000,7,2000);
    	test1d(10000,7,2000);
    	test2dunroll(10000,2000);
    	test1dunroll(10000,2000);
    	MonitorUtil.report();
    }
}
