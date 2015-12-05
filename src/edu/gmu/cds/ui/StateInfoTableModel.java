/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.ui.search.EnhancePanel;

public class StateInfoTableModel extends AbstractTableModel 
{
	private static final long serialVersionUID = 1L;
	
	protected List<StateInfo> infos = null;
	protected DataHelper dataHelper = null;
	
	public StateInfoTableModel()
	{
		super();
		dataHelper = new SimHelper();
	}
	
	public StateInfoTableModel(List<StateInfo> infos)
	{
		super();
		setStateInfos(infos);
		dataHelper = new SimHelper();
	}
	
	public void setStateInfos(List<StateInfo> infos)
	{
		this.infos = infos;
		fireTableDataChanged();
	}
	
	public StateInfo getInfo(int row)
	{
		return infos.get(row);
	}
	
	@Override
	public String getColumnName(int col)
	{
		return dataHelper.getColumnName(col);
	}

	@Override
	public int getColumnCount() 
	{
		return dataHelper.getColumnCount();
	}

	@Override
	public int getRowCount() 
	{
		if(infos == null) return 0;
		return infos.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		if(infos == null) return "";
		StateInfo info = infos.get(rowIndex);
		return dataHelper.getValue(info, columnIndex);
	}
	
	public static interface DataHelper
	{
        public int getColumnCount();
        public String getColumnName(int col);
        public Object getValue(StateInfo info, int col);
	}

	public static class SimHelper implements DataHelper
	{

		@Override
		public int getColumnCount() 
		{
			return 11;
		}

		@Override
		public String getColumnName(int col) 
		{
			String name = "";
			switch(col)
			{
				case 0:
					name = "Fitness";
					break;
				case 1:
					name = "M1";
					break;
				case 2:
					name = "M2";
					break;
				case 3:
					name = "Rz";
					break;
				case 4:
					name = "Vx";
					break;
				case 5:
					name = "Vy";
					break;
				case 6:
					name = "Vz";
					break;
				case 7:
					name = EnhancePanel.theta+"1";
					break;
				case 8:
					name = EnhancePanel.phi+"1";
					break;
				case 9:
					name = EnhancePanel.theta+"2";
					break;
				case 10:
					name = EnhancePanel.phi+"2";
					break;
			}
			return name;
		}

		@Override
		public Object getValue(StateInfo info, int col) 
		{
			Object out = "";
			switch(col)
			{
				case 0:
					out = info.fitness;
					break;
				case 1:
					out = info.m1;
					break;
				case 2:
					out = info.m2;
					break;
				case 3:
					out = info.rz;
					break;
				case 4:
					out = info.vx;
					break;
				case 5:
					out = info.vy;
					break;
				case 6:
					out = info.vz;
					break;
				case 7:
					out = info.theta1;
					break;
				case 8:
					out = info.phi1;
					break;
				case 9:
					out = info.theta2;
					break;
				case 10:
					out = info.phi2;
					break;
			}
			return out;
		}
	}
}
