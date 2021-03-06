package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.memoranda.Event;
import net.sf.memoranda.EventImpl;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.History;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.Configuration;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.util.Util;

/*$Id: EventsPanel.java,v 1.25 2005/02/19 10:06:25 rawsushi Exp $*/
public class EventsPanel extends JPanel {
	private final int _MAX_DAYS = 999;	// max number of days to allow custom events searching US-101 
	private final String _DAY_BUTTON = "Today";
	private final String _WEEK_BUTTON = "7 Days";
	private final String _MONTH_BUTTON = "30 Days";
	private final String _CUSTOM_BUTTON = "Custom Range";
	private final String _GO_BUTTON = "Go";
	
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar eventsToolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    JButton newEventB = new JButton();
    JButton editEventB = new JButton();
    JButton removeEventB = new JButton();
    
    // US-53 create view buttons
    private JRadioButton _dayRb = new JRadioButton(_DAY_BUTTON);
    private JRadioButton _weekRb = new JRadioButton(_WEEK_BUTTON);
    private JRadioButton _monthRb = new JRadioButton(_MONTH_BUTTON);
    private JRadioButton _customRb = new JRadioButton(_CUSTOM_BUTTON);	// US-101
    
    // create text field and go button for custom user input US-101
    private JTextField _customTextField = new JTextField();
    private JButton _goButton = new JButton(_GO_BUTTON);
    
    JScrollPane scrollPane = new JScrollPane();
    static private EventsTable _eventsTable = new EventsTable();	
    static private ExtendedEventsTable _exTable = new ExtendedEventsTable();
    JPopupMenu eventPPMenu = new JPopupMenu();
    JMenuItem ppEditEvent = new JMenuItem();
    JMenuItem ppRemoveEvent = new JMenuItem();
    JMenuItem ppNewEvent = new JMenuItem();
    static private DailyItemsPanel parentPanel = null;

    public EventsPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    void jbInit() throws Exception {
        eventsToolBar.setFloatable(false);
        historyBackB.setAction(History.historyBackAction);
        historyBackB.setFocusable(false);
        historyBackB.setBorderPainted(false);
        historyBackB.setToolTipText(Local.getString("History back"));
        historyBackB.setRequestFocusEnabled(false);
        historyBackB.setPreferredSize(new Dimension(24, 24));
        historyBackB.setMinimumSize(new Dimension(24, 24));
        historyBackB.setMaximumSize(new Dimension(24, 24));
        historyBackB.setText("");

        historyForwardB.setAction(History.historyForwardAction);
        historyForwardB.setBorderPainted(false);
        historyForwardB.setFocusable(false);
        historyForwardB.setPreferredSize(new Dimension(24, 24));
        historyForwardB.setRequestFocusEnabled(false);
        historyForwardB.setToolTipText(Local.getString("History forward"));
        historyForwardB.setMinimumSize(new Dimension(24, 24));
        historyForwardB.setMaximumSize(new Dimension(24, 24));
        historyForwardB.setText("");

        newEventB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_new.png")));
        newEventB.setEnabled(true);
        newEventB.setMaximumSize(new Dimension(24, 24));
        newEventB.setMinimumSize(new Dimension(24, 24));
        newEventB.setToolTipText(Local.getString("New event"));
        newEventB.setRequestFocusEnabled(false);
        newEventB.setPreferredSize(new Dimension(24, 24));
        newEventB.setFocusable(false);
        newEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newEventB_actionPerformed(e);
            }
        });
        newEventB.setBorderPainted(false);

        editEventB.setBorderPainted(false);
        editEventB.setFocusable(false);
        editEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editEventB_actionPerformed(e);
            }
        });
        editEventB.setPreferredSize(new Dimension(24, 24));
        editEventB.setRequestFocusEnabled(false);
        editEventB.setToolTipText(Local.getString("Edit event"));
        editEventB.setMinimumSize(new Dimension(24, 24));
        editEventB.setMaximumSize(new Dimension(24, 24));
        editEventB.setEnabled(true);
        editEventB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_edit.png")));

        removeEventB.setBorderPainted(false);
        removeEventB.setFocusable(false);
        removeEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeEventB_actionPerformed(e);
            }
        });
        removeEventB.setPreferredSize(new Dimension(24, 24));
        removeEventB.setRequestFocusEnabled(false);
        removeEventB.setToolTipText(Local.getString("Remove event"));
        removeEventB.setMinimumSize(new Dimension(24, 24));
        removeEventB.setMaximumSize(new Dimension(24, 24));
        removeEventB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_remove.png")));

        // US-53 creates view radio button set
        final ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(_dayRb);
        viewGroup.add(_weekRb);
        viewGroup.add(_monthRb);
        viewGroup.add(_customRb);	// US-101
        _dayRb.setSelected(true);  // makes today default       
        
        this.setLayout(borderLayout1);
        scrollPane.getViewport().setBackground(Color.white);
        _eventsTable.setMaximumSize(new Dimension(32767, 32767));
        _eventsTable.setRowHeight(24);
        _exTable.setMaximumSize(new Dimension(32767, 32767));
        _exTable.setRowHeight(24);
        eventPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
        ppEditEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppEditEvent.setText(Local.getString("Edit event") + "...");
        ppEditEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppEditEvent_actionPerformed(e);
            }
        });
        ppEditEvent.setEnabled(false);
        ppEditEvent.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_edit.png")));
        ppRemoveEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppRemoveEvent.setText(Local.getString("Remove event"));
        ppRemoveEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveEvent_actionPerformed(e);
            }
        });
        ppRemoveEvent.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_remove.png")));
        ppRemoveEvent.setEnabled(false);
        ppNewEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppNewEvent.setText(Local.getString("New event") + "...");
        ppNewEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewEvent_actionPerformed(e);
            }
        });
        ppNewEvent.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_new.png")));
        scrollPane.getViewport().add(_eventsTable, null);
        this.add(scrollPane, BorderLayout.CENTER);
        eventsToolBar.add(historyBackB, null);
        eventsToolBar.add(historyForwardB, null);
        eventsToolBar.addSeparator(new Dimension(8, 24));

        eventsToolBar.add(newEventB, null);
        eventsToolBar.add(removeEventB, null);
        eventsToolBar.addSeparator(new Dimension(8, 24));
        eventsToolBar.add(editEventB, null);
        
        // adds view radio buttons group to tool bar
        eventsToolBar.addSeparator(new Dimension(8, 24));
        eventsToolBar.add(_dayRb, null);
        eventsToolBar.add(_weekRb, null);
        eventsToolBar.add(_monthRb, null);
        eventsToolBar.add(_customRb, null);
        eventsToolBar.add(_customTextField, null);
        eventsToolBar.add(_goButton);
        eventsToolBar.addSeparator(new Dimension(550, 24));
        _customTextField.setVisible(false);
        _goButton.setVisible(false);
        
        // adds listeners for each view radio button
        _dayRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _dayRbEventB_actionPerformed(e);
            }
        });
        _weekRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _weekRbEventB_actionPerformed(e);
            }
        });
        _monthRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _monthRbEventB_actionPerformed(e);
            }
        });
        _customRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _customRbEventB_actionPerformed(e);
            }
        });
        _goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _goButtonEventB_actionPerformed(e);
            }
        });
        
        this.add(eventsToolBar, BorderLayout.NORTH);

        PopupListener ppListener = new PopupListener();
        scrollPane.addMouseListener(ppListener);
        _eventsTable.addMouseListener(ppListener);

        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {      
                _dayRb.setSelected(true);
                _eventsTable.initTable(d);  
                scrollPane.getViewport().add(_eventsTable, null);
                boolean enbl = d.after(CalendarDate.today()) || d.equals(CalendarDate.today());
                newEventB.setEnabled(enbl);           
                ppNewEvent.setEnabled(enbl);
                editEventB.setEnabled(false);
                ppEditEvent.setEnabled(false);
                removeEventB.setEnabled(false);
                ppRemoveEvent.setEnabled(false);
                
                // disables radio button views if not current date
                boolean setRb = d.equals(CalendarDate.today());
                if (!setRb){
                	_dayRb.setVisible(false);
                	_weekRb.setVisible(false);
                	_monthRb.setVisible(false);
                	_customRb.setVisible(false);
                	_customTextField.setVisible(false);
                	_goButton.setVisible(false);
                }
                else {
                	_dayRb.setVisible(true);
                	_weekRb.setVisible(true);
                	_monthRb.setVisible(true);
                	_customRb.setVisible(true);
                }
            }
        });

        _eventsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = _eventsTable.getSelectedRow() > -1;
                editEventB.setEnabled(enbl);
                ppEditEvent.setEnabled(enbl);
                removeEventB.setEnabled(enbl);
                ppRemoveEvent.setEnabled(enbl);
            }
        });
        editEventB.setEnabled(false);
        removeEventB.setEnabled(false);
        eventPPMenu.add(ppEditEvent);
        eventPPMenu.addSeparator();
        eventPPMenu.add(ppNewEvent);
        eventPPMenu.add(ppRemoveEvent);
		
		// remove events using the DEL key
		_eventsTable.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
				if(_eventsTable.getSelectedRows().length>0 
					&& e.getKeyCode()==KeyEvent.VK_DELETE)
					ppRemoveEvent_actionPerformed(null);
			}
			public void	keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){} 
		});
    }

    void editEventB_actionPerformed(ActionEvent e) {
        EventDialog dlg = new EventDialog(App.getFrame(), Local.getString("Event"));
        net.sf.memoranda.Event ev =
            (net.sf.memoranda.Event) _eventsTable.getModel().getValueAt(
                _eventsTable.getSelectedRow(),
                EventsTable.EVENT);
        
        dlg.timeSpin.getModel().setValue(ev.getTime());
        /*if (new CalendarDate(ev.getTime()).equals(CalendarDate.today())) 
            ((SpinnerDateModel)dlg.timeSpin.getModel()).setStart(new Date());
        else
        ((SpinnerDateModel)dlg.timeSpin.getModel()).setStart(CalendarDate.today().getDate());
        ((SpinnerDateModel)dlg.timeSpin.getModel()).setEnd(CalendarDate.tomorrow().getDate());*/    
        dlg.textField.setText(ev.getText());
        dlg.getAssociatedContactsPanel().populateContactPanel(ev.getContactIDs());
        int rep = ev.getRepeat();
        if (rep > 0) {
            dlg.startDate.getModel().setValue(ev.getStartDate().getDate());
            if (rep == EventsManager.REPEAT_DAILY) {
                dlg.dailyRepeatRB.setSelected(true);
                dlg.dailyRepeatRB_actionPerformed(null);
                dlg.daySpin.setValue(Integer.valueOf(ev.getPeriod()));	//Find Bugs fix
            }
            else if (rep == EventsManager.REPEAT_WEEKLY) {
                dlg.weeklyRepeatRB.setSelected(true);
                dlg.weeklyRepeatRB_actionPerformed(null);
		int d = ev.getPeriod() - 1;
		if(Configuration.get("FIRST_DAY_OF_WEEK").equals("mon")) {
		    d--;
		    if(d<0) d=6;
		}
                dlg.weekdaysCB.setSelectedIndex(d);
            }
            else if (rep == EventsManager.REPEAT_MONTHLY) {
                dlg.monthlyRepeatRB.setSelected(true);
                dlg.monthlyRepeatRB_actionPerformed(null);
                dlg.dayOfMonthSpin.setValue(Integer.valueOf(ev.getPeriod()));	//Find Bugs fix
            }
	    else if (rep == EventsManager.REPEAT_YEARLY) {
		dlg.yearlyRepeatRB.setSelected(true);
		dlg.yearlyRepeatRB_actionPerformed(null);
		dlg.dayOfMonthSpin.setValue(Integer.valueOf(ev.getPeriod()));	//Find Bugs fix
	    }
        if (ev.getEndDate() != null) {
           dlg.endDate.getModel().setValue(ev.getEndDate().getDate());
           dlg.enableEndDateCB.setSelected(true);
           dlg.enableEndDateCB_actionPerformed(null);
        }
		if(ev.getWorkingDays()) {
			dlg.workingDaysOnlyCB.setSelected(true);
		}
		
        }

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return; 
        
        EventsManager.removeEvent(ev, true);
        
		Calendar calendar = new GregorianCalendar(Local.getCurrentLocale()); //Fix deprecated methods to get hours
		//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
		calendar.setTime(((Date)dlg.timeSpin.getModel().getValue()));//Fix deprecated methods to get hours
		//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
		int hh = calendar.get(Calendar.HOUR_OF_DAY);//Fix deprecated methods to get hours
		//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
		int mm = calendar.get(Calendar.MINUTE);//Fix deprecated methods to get hours
		//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        
        //int hh = ((Date) dlg.timeSpin.getModel().getValue()).getHours();
        //int mm = ((Date) dlg.timeSpin.getModel().getValue()).getMinutes();
		
        String text = dlg.textField.getText();
        Date schedDate = dlg.getEventDate();	//US-53
        
        String[] contactIDs = _getContactIDs(dlg);
        
        if (schedDate == null) {
        	int newDay = DailyItemsPanel.currentDate.getDay();
        	int newMonth = DailyItemsPanel.currentDate.getMonth();
        	int newYear = DailyItemsPanel.currentDate.getYear();
        	Date newSchedDate = CalendarDate.toDate(newDay, newMonth, newYear);
        	schedDate = newSchedDate;
        }
        
        if (dlg.noRepeatRB.isSelected()) {
        	EventsManager.createEvent(CurrentDate.get(), hh, mm, text, schedDate, contactIDs);
        }
        else {
	    _updateEvents(dlg,hh,mm,text, contactIDs);
	}    
	saveEvents();
    }
    
    /**
     * Overloaded edit event method that allows the user to edit an event from the 
     * notification dialog.
     * @param e 
     * @param ev Event object
     */
    static void editEventB_actionPerformed(ActionEvent e, Event ev) {
        EventDialog dlg = new EventDialog(App.getFrame(), Local.getString("Event"));        
        dlg.timeSpin.getModel().setValue(ev.getTime());   
        dlg.textField.setText(ev.getText());
        int rep = ev.getRepeat();
        if (rep > 0) {
            dlg.startDate.getModel().setValue(ev.getStartDate().getDate());
            if (rep == EventsManager.REPEAT_DAILY) {
                dlg.dailyRepeatRB.setSelected(true);
                dlg.dailyRepeatRB_actionPerformed(null);
                dlg.daySpin.setValue(Integer.valueOf(ev.getPeriod()));   	//Find Bugs fix            
            }
            else if (rep == EventsManager.REPEAT_WEEKLY) {
                dlg.weeklyRepeatRB.setSelected(true);
                dlg.weeklyRepeatRB_actionPerformed(null);
		int d = ev.getPeriod() - 1;
		if(Configuration.get("FIRST_DAY_OF_WEEK").equals("mon")) {
		    d--;
		    if(d<0) d=6;
		}
                dlg.weekdaysCB.setSelectedIndex(d);
            }
            else if (rep == EventsManager.REPEAT_MONTHLY) {
                dlg.monthlyRepeatRB.setSelected(true);
                dlg.monthlyRepeatRB_actionPerformed(null);
                dlg.dayOfMonthSpin.setValue(Integer.valueOf(ev.getPeriod()));	//Find Bugs fix
            }
	    else if (rep == EventsManager.REPEAT_YEARLY) {
		dlg.yearlyRepeatRB.setSelected(true);
		dlg.yearlyRepeatRB_actionPerformed(null);
		dlg.dayOfMonthSpin.setValue(Integer.valueOf(ev.getPeriod()));	//Find Bugs fix
	    }
        if (ev.getEndDate() != null) {
           dlg.endDate.getModel().setValue(ev.getEndDate().getDate());
           dlg.enableEndDateCB.setSelected(true);
           dlg.enableEndDateCB_actionPerformed(null);
        }
		if(ev.getWorkingDays()) {
			dlg.workingDaysOnlyCB.setSelected(true);
		}
		
        }

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        EventsManager.removeEvent(ev, true);
        
		Calendar calendar = new GregorianCalendar(Local.getCurrentLocale()); //Fix deprecated methods to get hours
		calendar.setTime(((Date)dlg.timeSpin.getModel().getValue()));//Fix deprecated methods to get hours
		int hh = calendar.get(Calendar.HOUR_OF_DAY);//Fix deprecated methods to get hours
		int mm = calendar.get(Calendar.MINUTE);//Fix deprecated methods to get hours
		
		Date schedDate = dlg.getEventDate();
        if (schedDate == null) {
        	int newDay = DailyItemsPanel.currentDate.getDay();
        	int newMonth = DailyItemsPanel.currentDate.getMonth();
        	int newYear = DailyItemsPanel.currentDate.getYear();
        	Date newSchedDate = CalendarDate.toDate(newDay, newMonth, newYear);
        	schedDate = newSchedDate;
        }
        
        String text = dlg.textField.getText();
        
        String[] contactIDs = _getContactIDs(dlg);
        
        if (dlg.noRepeatRB.isSelected())
   	    EventsManager.createEvent(CurrentDate.get(), hh, mm, text, schedDate, contactIDs);
        else {
	     _updateEvents(dlg,hh,mm,text, contactIDs);
	}    
	saveEvents();
    }

    void newEventB_actionPerformed(ActionEvent e) {
        Calendar cdate = CurrentDate.get().getCalendar();
        // round down to hour
        cdate.set(Calendar.MINUTE,0);  
        Util.debug("Default time is " + cdate);
        
    	newEventB_actionPerformed(e, null, cdate.getTime(), cdate.getTime());
    }
    
    void newEventB_actionPerformed(ActionEvent e, String tasktext, Date startDate, Date endDate) {
    	EventDialog dlg = new EventDialog(App.getFrame(), Local.getString("New event"));
    	Dimension frmSize = App.getFrame().getSize();
    	Point loc = App.getFrame().getLocation();
    	if (tasktext != null) {
    		dlg.textField.setText(tasktext);
    	}
		dlg.startDate.getModel().setValue(startDate);
		dlg.endDate.getModel().setValue(endDate);
		dlg.timeSpin.getModel().setValue(startDate);

    	dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
    	dlg.setEventDate(startDate);
		dlg.setVisible(true);
    	if (dlg.CANCELLED)
    		return;
    	Calendar calendar = new GregorianCalendar(Local.getCurrentLocale()); //Fix deprecated methods to get hours
    	//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
    	calendar.setTime(((Date)dlg.timeSpin.getModel().getValue()));//Fix deprecated methods to get hours
    	//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
    	int hh = calendar.get(Calendar.HOUR_OF_DAY);//Fix deprecated methods to get hours
    	//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
    	int mm = calendar.get(Calendar.MINUTE);//Fix deprecated methods to get hours
    	//by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
    	
    	//int hh = ((Date) dlg.timeSpin.getModel().getValue()).getHours();
    	//int mm = ((Date) dlg.timeSpin.getModel().getValue()).getMinutes();
    	String text = dlg.textField.getText();
    	
    	Date schedDate = dlg.getEventDate();  			
		CalendarDate eventCalendarDate = new CalendarDate(dlg.getEventDate());
		
		String[] contactIDs = _getContactIDs(dlg);
		
    	if (dlg.noRepeatRB.isSelected())
    		EventsManager.createEvent(eventCalendarDate, hh, mm, text, schedDate, contactIDs);
    	else {
    		_updateEvents(dlg,hh,mm,text, contactIDs);
    	}
    	saveEvents();
    }
    
    private static String[] _getContactIDs(EventDialog dlg) {
    	Enumeration<String> contactIDEnum = dlg.getAssociatedContactsPanel().getContactIDs();
        Vector<String> contactIDVector = new Vector<String>();
        for (String id; contactIDEnum.hasMoreElements();) {
        	id = contactIDEnum.nextElement();
        	contactIDVector.add(id);
        }
        String[] contactIDArray = new String[contactIDVector.size()];
        contactIDVector.toArray(contactIDArray);
        return contactIDArray;
    }

    static private void saveEvents() {
	CurrentStorage.get().storeEventsManager();
        _eventsTable.refresh();
        EventsScheduler.init();
        parentPanel.calendar.jnCalendar.updateUI();
        parentPanel.updateIndicators();
    }

	private static void _updateEvents(EventDialog dlg, int hh, int mm, String text, String[] contactIDs) {
		int repeatType;
		int period;
		final CalendarDate startDate = new CalendarDate((Date) dlg.startDate.getModel().getValue());
		CalendarDate endDate = null;
		if (dlg.enableEndDateCB.isSelected()) {
			endDate = new CalendarDate((Date) dlg.endDate.getModel().getValue());
		}
		if (dlg.dailyRepeatRB.isSelected()) {
			repeatType = EventsManager.REPEAT_DAILY;
			period = ((Integer) dlg.daySpin.getModel().getValue()).intValue();
		}
		else if (dlg.weeklyRepeatRB.isSelected()) {
			repeatType = EventsManager.REPEAT_WEEKLY;
			period = dlg.weekdaysCB.getSelectedIndex() + 1;

			if (Configuration.get("FIRST_DAY_OF_WEEK").equals("mon")) {
				if (period == 7) {
					period = 1;
				}
				else {
					period++;
				}
			}
		}
		else if (dlg.yearlyRepeatRB.isSelected()) {
			repeatType = EventsManager.REPEAT_YEARLY;
			period = startDate.getCalendar().get(Calendar.DAY_OF_YEAR);
			if ((startDate.getYear() % 4) == 0 && startDate.getCalendar().get(Calendar.DAY_OF_YEAR) > 60) {
				period--;
			}
		}
		else {
			repeatType = EventsManager.REPEAT_MONTHLY;
			period = ((Integer) dlg.dayOfMonthSpin.getModel().getValue()).intValue();
		}

		EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hh, mm, text, dlg.workingDaysOnlyCB.isSelected(),
				contactIDs);
	}

    void removeEventB_actionPerformed(ActionEvent e) {
		String msg;
		net.sf.memoranda.Event ev;
		boolean displayMessage = true;

		if(_eventsTable.getSelectedRows().length > 1) { 
			msg = Local.getString("Remove") + " " + _eventsTable.getSelectedRows().length 
				+ " " + Local.getString("events") + "\n" + Local.getString("Are you sure?");
		}
		else {
			ev = (net.sf.memoranda.Event) _eventsTable.getModel().getValueAt(
                _eventsTable.getSelectedRow(),
                EventsTable.EVENT);
			
			if (ev.isRepeatable()) {
				displayMessage = false;
			}
			msg = Local.getString("Remove event") + "\n'" 
				+ ev.getText() + "'\n" + Local.getString("Are you sure?");
		}
		
		// bypasses the confirmation popup if deleting one selected repeating event
		if (displayMessage) {
			final int n =
					JOptionPane.showConfirmDialog(
					App.getFrame(),
					msg,
					Local.getString("Remove event"),
					JOptionPane.YES_NO_OPTION);
			if (n != JOptionPane.YES_OPTION) {
				return;
			}
		}

        for(int i=0; i< _eventsTable.getSelectedRows().length;i++) {
			ev = (net.sf.memoranda.Event) _eventsTable.getModel().getValueAt(
                  _eventsTable.getSelectedRows()[i], EventsTable.EVENT);
        EventsManager.removeEvent(ev, false);
		}
        _eventsTable.getSelectionModel().clearSelection();
/*        CurrentStorage.get().storeEventsManager();
        eventsTable.refresh();
        EventsScheduler.init();
        parentPanel.calendar.jnCalendar.updateUI();
        parentPanel.updateIndicators();
*/ saveEvents();  
  }
    
    /**
     * Overloaded removeEventB_actionPerformed method to allow users to remove an event from 
     * the notification dialog.
     * @param e
     * @param ev Event object
     */
    static void removeEventB_actionPerformed(ActionEvent e, Event ev) {
        EventsManager.removeEvent(ev, false);
        saveEvents();  
    }
    
    /**
     * Converts the days range entered by the user in the custom text box from a 
     * String to an integer and checks if the input is valid. US-101
     * 
     * @param customDaysText	string value of number entered in text field
     * @param mock				boolean indicating if method is under test
     * @return dayRange			int value of days in range
     */
    public int getDayRange(String customDaysText, boolean mock) { 	
    	
    	int dayRange = 0;
    	
    	// verifies if input is a number
    	try {
    		dayRange = Integer.parseInt(customDaysText);
    	}
    	catch (NumberFormatException ex) {
    		if (!mock) {
    			JOptionPane.showMessageDialog(null, "Please enter a valid number ranging "
    					+ "from 1 to 999", "Notice", JOptionPane.WARNING_MESSAGE);
    		}
    		return -1;
    	}
    			
    	// checks size of input number
    	if (dayRange > _MAX_DAYS || dayRange < 1) {
    		if (!mock) {
    			JOptionPane.showMessageDialog(null, "Please enter a number of days ranging from 1 to 999", 
	  			    "Notice", JOptionPane.WARNING_MESSAGE);
    		}
    		return -1;
    	}
    	else {
    		return dayRange;
    	}
    }
    
    /**
     * Method: dayRbEventB_actionPerformed()
     * Inputs: ActionEvent e
     * Returns: void
     * 
     * Description: Performs actions when the day radio button is selected. This is the default 
     * view as normal - meaning editing can be performed. US-53.
     */
    private void _dayRbEventB_actionPerformed(ActionEvent e) {
    	_customTextField.setVisible(false);
    	_goButton.setVisible(false);
        newEventB.setEnabled(true);           
        ppNewEvent.setEnabled(true);
    	scrollPane.getViewport().add(_eventsTable, null);
    	_eventsTable.refresh();
    }
    
    /**
     * Method: weekRbEventB_actionPerformed()
     * Inputs: ActionEvent e
     * Returns: void
     * 
     * Description: Performs actions when the 7 day radio button is selected. Opens up new table 
     * to display all events from current day +6. US-53.
     */
    private void _weekRbEventB_actionPerformed(ActionEvent e) {
    	_customTextField.setVisible(false);
    	_goButton.setVisible(false);
        newEventB.setEnabled(false);           
        ppNewEvent.setEnabled(false);
        editEventB.setEnabled(false);
        ppEditEvent.setEnabled(false);
        removeEventB.setEnabled(false);
        ppRemoveEvent.setEnabled(false);
        scrollPane.getViewport().add(_exTable, null);
        _exTable.initWeekTable(CalendarDate.today());               
    }
    
    /**
     * Method: monthRbEventB_actionPerformed()
     * Inputs: ActionEvent e
     * Returns: void
     * 
     * Description: Performs actions when the 30 day radio button is selected. Opens up new table 
     * to display all events from current day +29. US-53.
     */
    private void _monthRbEventB_actionPerformed(ActionEvent e) {
    	_customTextField.setVisible(false);
    	_goButton.setVisible(false);
        newEventB.setEnabled(false);           
        ppNewEvent.setEnabled(false);
        editEventB.setEnabled(false);
        ppEditEvent.setEnabled(false);
        removeEventB.setEnabled(false);
        ppRemoveEvent.setEnabled(false);
        scrollPane.getViewport().add(_exTable, null);
        _exTable.initMonthTable(CalendarDate.today());
    }
    
    /**
     * When the custom day range radio button is selected, the custom range text
     * field is displayed. US-101
     * 
     * @param e	indicates the user selected the custom range button
     */
	private void _customRbEventB_actionPerformed(ActionEvent e) {
        newEventB.setEnabled(false);           
        ppNewEvent.setEnabled(false);
        editEventB.setEnabled(false);
        ppEditEvent.setEnabled(false);
        removeEventB.setEnabled(false);
        ppRemoveEvent.setEnabled(false);
        _customTextField.setEnabled(true);
        _customTextField.setVisible(true);
        _goButton.setEnabled(true);
        _goButton.setVisible(true);
        eventsToolBar.revalidate();
    }
    
    /**
     * When the user enters a custom day range, the go button is pressed and
     * creates and displays the table for the events for the current day plus day range. US-101
     * 
     * @param e	indicates goButton selected by user
     */
    private void _goButtonEventB_actionPerformed(ActionEvent e) {  
    	String customDaysText = _customTextField.getText().trim();
        int dayRange = getDayRange(customDaysText, false);
        scrollPane.getViewport().add(_exTable, null);
        _exTable.initCustomRangeTable(CalendarDate.today(), dayRange);
    }
    
    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (_eventsTable.getSelectedRow() > -1))
                editEventB_actionPerformed(null);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                eventPPMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }
    void ppEditEvent_actionPerformed(ActionEvent e) {
        editEventB_actionPerformed(e);
    }
    void ppRemoveEvent_actionPerformed(ActionEvent e) {
        removeEventB_actionPerformed(e);
    }
    void ppNewEvent_actionPerformed(ActionEvent e) {
        newEventB_actionPerformed(e);
    }
    
}