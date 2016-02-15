/*
 * File: ExtendedEventsTable.java
 * Author: Larry Naron
 * Date: 2/10/2016
 * 
 * Description: Creates a read-only table containing all events for a seven or thirty day period 
 * starting with the current day. The table is only visible when the user is viewing the current
 * day on the calendar.
 */

package net.sf.memoranda.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.memoranda.Event;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.util.Local;

/**
 * Class: ExtendedEventsTable
 * 
 * Description: Extends the EventsTable class to take in Vectors containing a seven day period or
 * thirty day period of Events. Contains the nested class ExtendedEventsTableModel.
 */
public class ExtendedEventsTable extends EventsTable {

    private Vector<Event> _events = new Vector<Event>();
    
    /**
     * Constructor for ExtendedEventsTable.
     * Description: Uses the EventsTableModel class.
     */
    public ExtendedEventsTable() {
        super();
        setModel(new ExtendedEventsTableModel());
    }

    /**
     * Method: initWeekTable()
     * Inputs: CalendarDate d
     * Returns: void
     * 
     * Description: Fills the table with all Events occurring in a seven day time period from the
     * current day.
     */
    public void initWeekTable(CalendarDate d) {
        _events = (Vector)EventsManager.getEventsForWeek(d);
        getColumnModel().getColumn(0).setPreferredWidth(100);	// date column
        getColumnModel().getColumn(0).setMaxWidth(100);			
        getColumnModel().getColumn(1).setPreferredWidth(60);	// time column
        getColumnModel().getColumn(1).setMaxWidth(60);			
        clearSelection();
        updateUI();
    }
    
    /**
     * Method: initMonthTable()
     * Inputs: CalendarDate d
     * Returns: void
     * 
     * Description: Fills the table with all Events occurring in a thirty day time period from the
     * current day.
     */
    public void initMonthTable(CalendarDate d) {
        _events = (Vector)EventsManager.getEventsForMonth(d);
        getColumnModel().getColumn(0).setPreferredWidth(100);	// date column
        getColumnModel().getColumn(0).setMaxWidth(100);			
        getColumnModel().getColumn(1).setPreferredWidth(80);	// time column
        getColumnModel().getColumn(1).setMaxWidth(80);			
        clearSelection();
        updateUI();
    }
    
    /**
     * Method: getCellRenderer()
     * Inputs: int row, int column
     * Returns: TableCellRenderer object
     * 
     * Description: Applies formatting of table item text.
     */
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return new javax.swing.table.DefaultTableCellRenderer() {

        	/**
        	 * Method: getTableCellRendererComponent()
        	 * Inputs: JTable table, Object value, bool isSelected, bool hasFocus, int row,
        	 * 	 	   int column
        	 * Returns: Component comp
        	 * 
        	 * Description: Sets features of the text for items in the table.
        	 */
            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
                Component comp;
                comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Event ev = (Event)getModel().getValueAt(row, EVENT);
                comp.setForeground(java.awt.Color.gray);
                if (ev.isRepeatable())
                    comp.setFont(comp.getFont().deriveFont(Font.ITALIC));
                if (CurrentDate.get().after(CalendarDate.today())) {
                  comp.setForeground(java.awt.Color.black);
                }                
                else if (CurrentDate.get().equals(CalendarDate.today())) {
                  if (ev.getTime().after(new Date())) {
                    comp.setForeground(java.awt.Color.black);
                    comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                  }
                }
                return comp;
            }
        };

    }

    /**
     * Class: ExtendedEventTableModel
     * 
     * Description: Sets up the table and fills cells.
     */
    private class ExtendedEventsTableModel extends AbstractTableModel {

        private String[] columnNames = {Local.getString("Date"),
        								Local.getString("Time"),
        								Local.getString("Text")
        							   };

        /**
         * Constructor
         */
        ExtendedEventsTableModel() {
            super();
        }

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
			int i;
			try {
				i = _events.size();
			}
			catch(NullPointerException e) {
				i = 1;
			}
			return i;
        }

        public Object getValueAt(int row, int col) {
           Event ev = (Event)_events.get(row);
           if (col == 0) {
        	   if (ev.isRepeatable()) {
        		   return ev.getRepSchedDate();
        	   }
        	   else {
        		   return ev.getSchedDate();
        	   }
           }
           else if (col == 1) {
               return ev.getTimeString();
           }
           else if (col == 2) {
        	   return ev.getText();
           }
           else if (col == EVENT_ID) {
               return ev.getId();
           }
           else return ev;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }
    }
}
