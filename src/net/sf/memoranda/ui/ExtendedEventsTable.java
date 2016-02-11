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
import net.sf.memoranda.util.Util;

public class ExtendedEventsTable extends EventsTable {

    Vector events = new Vector();
    /**
     * Constructor for EventsTable.
     */
    public ExtendedEventsTable() {
        super();
        setModel(new ExtendedEventsTableModel());
    }

    public void initWeekTable(CalendarDate d) {
        events = (Vector)EventsManager.getEventsForWeek(d);
        getColumnModel().getColumn(0).setPreferredWidth(120);	// date column
        getColumnModel().getColumn(0).setMaxWidth(120);			
        getColumnModel().getColumn(1).setPreferredWidth(60);	// time column
        getColumnModel().getColumn(1).setMaxWidth(60);			
        clearSelection();
        updateUI();
    }
    
    public void initMonthTable(CalendarDate d) {
        events = (Vector)EventsManager.getEventsForMonth(d);
        getColumnModel().getColumn(0).setPreferredWidth(120);	// date column
        getColumnModel().getColumn(0).setMaxWidth(120);			
        getColumnModel().getColumn(1).setPreferredWidth(80);	// time column
        getColumnModel().getColumn(1).setMaxWidth(80);			
        clearSelection();
        updateUI();
    }
    

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return new javax.swing.table.DefaultTableCellRenderer() {

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
                    //comp.setFont(new java.awt.Font("Dialog", 1, 12));
                    comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                  }
                }
                return comp;
            }
        };

    }

    private class ExtendedEventsTableModel extends AbstractTableModel {

        String[] columnNames = {
            Local.getString("Date"),
            Local.getString("Time"),
            Local.getString("Text")
        };

        ExtendedEventsTableModel() {
            super();
        }

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
			int i;
			try {
				i = events.size();
			}
			catch(NullPointerException e) {
				i = 1;
			}
			return i;
        }

        public Object getValueAt(int row, int col) {
           Event ev = (Event)events.get(row);
           if (col == 0) {
        	   if (ev.isRepeatable()) {
        		   return "Reoccuring";
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
