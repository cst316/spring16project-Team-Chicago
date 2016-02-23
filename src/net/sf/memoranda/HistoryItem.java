/**
 * HistoryItem.java
 * Created on 07.03.2003, 18:31:39 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;

/**
 * 
 */
/*$Id: HistoryItem.java,v 1.4 2004/10/06 19:15:43 ivanrise Exp $*/
public class HistoryItem {
    
    private CalendarDate _date;
    private Project _project;
    /**
     * Constructor for HistoryItem.
     */
    public HistoryItem(CalendarDate date, Project project) {
        _date = date;
        _project = project;
    }
    
    public HistoryItem(Note note) {
        _date = note.getDate();
        _project = note.getProject();
    }
    
    public CalendarDate getDate() {
       return _date;
    }
    
    public Project getProject() {
       return _project;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_date == null) ? 0 : _date.hashCode());
		result = prime * result + ((_project == null) ? 0 : _project.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HistoryItem)) {
			return false;
		}
		final HistoryItem other = (HistoryItem) obj;
		if (_date == null) {
			if (other._date != null) {
				return false;
			}
		}
		else if (!_date.equals(other._date)) {
			return false;
		}
		if (_project == null) {
			if (other._project != null) {
				return false;
			}
		}
		else if (!_project.equals(other._project)) {
			return false;
		}
		return true;
	}

}
