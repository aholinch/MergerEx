/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import info.clearthought.layout.TableLayout;

import javax.swing.JPanel;

public class TableLayoutPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;	

	public TableLayoutPanel()
	{
		super(new TableLayout());
	}
	
	public String getRC(int row, int col)
	{
		return String.valueOf(col)+", " + String.valueOf(row);
	}
	
	public String getRC(int row, int col, int endRow, int endCol)
	{
		return String.valueOf(col)+", " + String.valueOf(row)+ ", "+String.valueOf(endCol)+", " + String.valueOf(endRow);
	}
}
