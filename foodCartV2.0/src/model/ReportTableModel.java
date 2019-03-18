/**
 * 
 */
package model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * This is the TableModel for Reporting Panel, allows Double and String in same model
 * @author Anwar
 *
 */
public class ReportTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public ReportTableModel(Object rowData[][], Object columnNames[]) {
	         super(rowData, columnNames);
	      }
		
	    public ReportTableModel(int rowCount, int columnCount) {
	    	super( rowCount,  columnCount);
	    }
		
		@Override
	      public Class getColumnClass(int col) {
	        if (col == 6 || col == 5 || col == 3)       //double column accepts only doubles
	        {  return Double.class; }
	        else { 
	        	if (col == 4) { return Integer.class; }
	        	else return String.class; }  //other columns accept String values
	    }
	}
