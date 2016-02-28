package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.NoteList;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectListener;
import net.sf.memoranda.ResourcesList;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.util.Local;

/**
 * 
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: JNCalendarPanel.java,v 1.9 2004/04/05 10:05:44 alexeya Exp $*/
public class JNCalendarPanel extends JPanel {

  CalendarDate _date = CurrentDate.get();
  JPanel navigationBar = new JPanel(new BorderLayout());
  JPanel mntyPanel = new JPanel(new BorderLayout());
  private JPanel _navbPanelLeft = new JPanel(new BorderLayout());
  private JPanel _navbPanelCenter = new JPanel(new BorderLayout());
  private JPanel _navbPanelRight = new JPanel(new BorderLayout());
  private JButton _dayForwardB = new JButton();
  private JButton _monthForwardB = new JButton();
  private JButton _todayB = new JButton();
  private JButton _dayBackB = new JButton();
  private JButton _monthBackB = new JButton();
  JComboBox monthsCB = new JComboBox(Local.getMonthNames());
  BorderLayout borderLayout4 = new BorderLayout();
  JNCalendar jnCalendar = new JNCalendar(CurrentDate.get());
  JPanel jnCalendarPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JSpinner yearSpin = new JSpinner(new SpinnerNumberModel(jnCalendar.get().getYear(), 1980, 2999, 1));
  JSpinner.NumberEditor yearSpinner = new JSpinner.NumberEditor(yearSpin, "####");

  boolean ignoreChange = false;

  private Vector selectionListeners = new Vector();

  Border border1;
  Border border2;

  public JNCalendarPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      new ExceptionDialog(ex);
    }
  }

  public Action dayBackAction =
        new AbstractAction(
        	"Go one day back",
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/back16.png"))) {
        public void actionPerformed(ActionEvent e) {
            dayBackB_actionPerformed(e);
        }
  };
  
  public Action monthBackAction =
	        new AbstractAction(
	            "Go one month back",
	            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/backmonth16.png"))) {
	        public void actionPerformed(ActionEvent e) {
	            monthBackB_actionPerformed(e);
	        }
	  };
  
  public Action dayForwardAction =
        new AbstractAction(
            "Go one day forward",
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forward16.png"))) {
        public void actionPerformed(ActionEvent e) {
            dayForwardB_actionPerformed(e);
        }
  };
  
  public Action monthForwardAction =
	        new AbstractAction(
	            "Go one month forward",
	            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forwardmonth16.png"))) {
	        public void actionPerformed(ActionEvent e) {
	            monthForwardB_actionPerformed(e);
	        }
	  };
  
  public Action todayAction =
        new AbstractAction(
            "Go to today",
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/today16.png"))) {
        public void actionPerformed(ActionEvent e) {
            todayB_actionPerformed(e);
        }
  };
      
  void jbInit() throws Exception {
    todayAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.ALT_MASK));
    
    monthsCB.setRequestFocusEnabled(false);
    monthsCB.setMaximumRowCount(12);
    monthsCB.setPreferredSize(new Dimension(50 , 20));
    border1 = BorderFactory.createEmptyBorder(0,0,5,0);
    border2 = BorderFactory.createEmptyBorder();
    this.setLayout(new BorderLayout());

    //Right Side of Calender Panel + Buttons
    
    _navbPanelRight.setAlignmentX((float) 0.0);
    _navbPanelRight.setMinimumSize(new Dimension(50, 24));
    _navbPanelRight.setOpaque(false);
    _navbPanelRight.setPreferredSize(new Dimension(50, 24));
    
    _monthForwardB.setAction(monthForwardAction);
    _monthForwardB.setMinimumSize(new Dimension(24, 24));
    _monthForwardB.setOpaque(false);
    _monthForwardB.setPreferredSize(new Dimension(24, 24));
    _monthForwardB.setRequestFocusEnabled(false);
    _monthForwardB.setBorderPainted(false);
    _monthForwardB.setFocusPainted(false);
    _monthForwardB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forwardmonth.png")));
    _monthForwardB.setText("");
    _monthForwardB.setToolTipText(Local.getString("One month forward"));
        
    _dayForwardB.setAction(dayForwardAction);
    _dayForwardB.setMinimumSize(new Dimension(24, 24));
    _dayForwardB.setOpaque(false);
    _dayForwardB.setPreferredSize(new Dimension(24, 24));
    _dayForwardB.setRequestFocusEnabled(false);
    _dayForwardB.setBorderPainted(false);
    _dayForwardB.setFocusPainted(false);
    _dayForwardB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forward.png")));
    _dayForwardB.setText("");
    _dayForwardB.setToolTipText(Local.getString("One day forward"));
    
    //Center Panel Today Button
    
    _navbPanelCenter.setMinimumSize(new Dimension(20, 24));
    _navbPanelCenter.setOpaque(false);
    _navbPanelCenter.setPreferredSize(new Dimension(20, 24));
    
    _todayB.setAction(todayAction);
    _todayB.setMinimumSize(new Dimension(24, 24));
    _todayB.setOpaque(false);
    _todayB.setPreferredSize(new Dimension(24, 24));
    _todayB.setRequestFocusEnabled(false);
    _todayB.setBorderPainted(false);
    _todayB.setFocusPainted(false);
    _todayB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/today.png")));
    _todayB.setText("");
    _todayB.setToolTipText(Local.getString("To today"));
    
    //Left Panel of Calender + Buttons
    
    _navbPanelLeft.setAlignmentX((float) 1.5);
    _navbPanelLeft.setMinimumSize(new Dimension(50, 24));
    _navbPanelLeft.setOpaque(false);
    _navbPanelLeft.setPreferredSize(new Dimension(50, 24));
    
    _dayBackB.setAction(dayBackAction);
    _dayBackB.setMinimumSize(new Dimension(24, 24));
    _dayBackB.setOpaque(false);
    _dayBackB.setPreferredSize(new Dimension(24, 24));
    _dayBackB.setRequestFocusEnabled(false);
    _dayBackB.setToolTipText("");
    _dayBackB.setBorderPainted(false);
    _dayBackB.setFocusPainted(false);
    _dayBackB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/back.png")));
    _dayBackB.setText("");
    _dayBackB.setToolTipText(Local.getString("One day back"));
    
    _monthBackB.setAction(monthBackAction);
    _monthBackB.setMinimumSize(new Dimension(24, 24));
    _monthBackB.setOpaque(false);
    _monthBackB.setPreferredSize(new Dimension(24, 24));
    _monthBackB.setRequestFocusEnabled(false);
    _monthBackB.setToolTipText("");
    _monthBackB.setBorderPainted(false);
    _monthBackB.setFocusPainted(false);
    _monthBackB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/backmonth.png")));
    _monthBackB.setText("");
    _monthBackB.setToolTipText(Local.getString("One month back"));
    
    yearSpin.setPreferredSize(new Dimension(70, 20));
    yearSpin.setRequestFocusEnabled(false);
    yearSpin.setEditor(yearSpinner);
    navigationBar.setMinimumSize(new Dimension(202, 30));
    navigationBar.setOpaque(false);
    navigationBar.setPreferredSize(new Dimension(155, 30));
    jnCalendar.getTableHeader().setFont(new java.awt.Font("Dialog", 1, 10));
    jnCalendar.setFont(new java.awt.Font("Dialog", 0, 10));
    jnCalendar.setGridColor(Color.lightGray);
    jnCalendarPanel.setLayout(borderLayout5);
    this.add(navigationBar, BorderLayout.NORTH);
    navigationBar.add(_navbPanelLeft, BorderLayout.WEST);
    navigationBar.add(_navbPanelRight, BorderLayout.EAST);
    navigationBar.add(_navbPanelCenter, BorderLayout.CENTER);
    _navbPanelLeft.add(_monthBackB, BorderLayout.WEST);
    _navbPanelLeft.add(_dayBackB, BorderLayout.EAST);
    _navbPanelCenter.add(_todayB, BorderLayout.CENTER);
    _navbPanelRight.add(_dayForwardB, BorderLayout.WEST);
    _navbPanelRight.add(_monthForwardB, BorderLayout.EAST);
    this.add(mntyPanel,  BorderLayout.SOUTH);
    mntyPanel.add(monthsCB, BorderLayout.CENTER);
    mntyPanel.add(yearSpin,  BorderLayout.EAST);
    this.add(jnCalendarPanel,  BorderLayout.CENTER);
    jnCalendar.getTableHeader().setPreferredSize(new Dimension(200, 15));
    jnCalendarPanel.add(jnCalendar.getTableHeader(), BorderLayout.NORTH);
    jnCalendarPanel.add(jnCalendar, BorderLayout.CENTER);
    jnCalendar.addSelectionListener(new ActionListener()  {
      
    public void actionPerformed(ActionEvent e) {
        setCurrentDateDay(jnCalendar.get(), jnCalendar.get().getDay());
      }
    });

    monthsCB.setFont(new java.awt.Font("Dialog", 0, 11));

    monthsCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        monthsCB_actionPerformed(e);
      }
    });

    yearSpin.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        yearSpin_actionPerformed();
      }
    });
    CurrentProject.addProjectListener(new ProjectListener() {
            public void projectChange(Project p, NoteList nl, TaskList tl, ResourcesList rl) {}
            public void projectWasChanged() {
                jnCalendar.updateUI();
            }
        });


    refreshView();
    yearSpin.setBorder(border2);
    
  }

  public void set(CalendarDate date) {
    _date = date;
    refreshView();
  }

  public CalendarDate get() {
    return _date;
  }

  public void addSelectionListener(ActionListener al) {
        selectionListeners.add(al);
    }

  private void notifyListeners() {
        for (Enumeration en = selectionListeners.elements(); en.hasMoreElements();)
             ((ActionListener) en.nextElement()).actionPerformed(new ActionEvent(this, 0, "Calendar event"));
  }

  private void setCurrentDateDay(CalendarDate dt, int d) {
    if (ignoreChange) return;
    if (_date.equals(dt)) return;
    _date = new CalendarDate(d, _date.getMonth(), _date.getYear());
    notifyListeners();
  }

  private void refreshView() {
    ignoreChange = true;
    jnCalendar.set(_date);
    monthsCB.setSelectedIndex(new Integer(_date.getMonth()));
    yearSpin.setValue(new Integer(_date.getYear()));
    ignoreChange = false;
  }

  void monthsCB_actionPerformed(ActionEvent e) {
    if (ignoreChange) return;
    _date = new CalendarDate(_date.getDay(), monthsCB.getSelectedIndex(), _date.getYear());
    jnCalendar.set(_date);
    notifyListeners();
  }

  void yearSpin_actionPerformed() {
    if (ignoreChange) return;
    _date = new CalendarDate(_date.getDay(), _date.getMonth(), ((Integer)yearSpin.getValue()).intValue());
    jnCalendar.set(_date);
    notifyListeners();
  }

  void dayBackB_actionPerformed(ActionEvent e) {
	  Calendar cal = _date.getCalendar();
      cal.add(Calendar.DATE, -1); cal.getTime();
      _date = new CalendarDate(cal);
      refreshView();
      notifyListeners();
  }

	/**
	 * monthBackB action
	 * 
	 * Moves calender forward 30 days
	 */
  
  void monthBackB_actionPerformed(ActionEvent e) {
	  Calendar cal = _date.getCalendar();
	  cal.add(Calendar.DATE, -30); cal.getTime();
	  _date = new CalendarDate(cal);
	  refreshView();
	  notifyListeners();
  }

  void todayB_actionPerformed(ActionEvent e) {
	  _date = CalendarDate.today();
	  refreshView();
	  notifyListeners();
  }

  void dayForwardB_actionPerformed(ActionEvent e) {
	  Calendar cal = _date.getCalendar();
      cal.add(Calendar.DATE, 1); cal.getTime();
      _date = new CalendarDate(cal);
      refreshView();
      notifyListeners();
  }
	/**
	 * monthForwardB action
	 * 
	 * Moves calender forward 30 days
	 */
  void monthForwardB_actionPerformed(ActionEvent e) {
	  Calendar cal = _date.getCalendar();
	  cal.add(Calendar.DATE, 30); cal.getTime();
	  _date = new CalendarDate(cal);
	  refreshView();
	  notifyListeners();
  }



}