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
  private JPanel navbPanelLeft = new JPanel(new BorderLayout());
  private JPanel navbPanelCenter = new JPanel(new BorderLayout());
  private JPanel navbPanelRight = new JPanel(new BorderLayout());
  private JButton dayForwardB = new JButton();
  private JButton monthForwardB = new JButton();
  private JButton todayB = new JButton();
  private JButton dayBackB = new JButton();
  private JButton monthBackB = new JButton();
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
    
    navbPanelRight.setAlignmentX((float) 0.0);
    navbPanelRight.setMinimumSize(new Dimension(50, 24));
    navbPanelRight.setOpaque(false);
    navbPanelRight.setPreferredSize(new Dimension(50, 24));
    
    monthForwardB.setAction(monthForwardAction);
    monthForwardB.setMinimumSize(new Dimension(24, 24));
    monthForwardB.setOpaque(false);
    monthForwardB.setPreferredSize(new Dimension(24, 24));
    monthForwardB.setRequestFocusEnabled(false);
    monthForwardB.setBorderPainted(false);
    monthForwardB.setFocusPainted(false);
    monthForwardB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forwardmonth.png")));
    monthForwardB.setText("");
    monthForwardB.setToolTipText(Local.getString("One month forward"));
        
    dayForwardB.setAction(dayForwardAction);
    dayForwardB.setMinimumSize(new Dimension(24, 24));
    dayForwardB.setOpaque(false);
    dayForwardB.setPreferredSize(new Dimension(24, 24));
    dayForwardB.setRequestFocusEnabled(false);
    dayForwardB.setBorderPainted(false);
    dayForwardB.setFocusPainted(false);
    dayForwardB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/forward.png")));
    dayForwardB.setText("");
    dayForwardB.setToolTipText(Local.getString("One day forward"));
    
    //Center Panel Today Button
    
    navbPanelCenter.setMinimumSize(new Dimension(20, 24));
    navbPanelCenter.setOpaque(false);
    navbPanelCenter.setPreferredSize(new Dimension(20, 24));
    
    todayB.setAction(todayAction);
    todayB.setMinimumSize(new Dimension(24, 24));
    todayB.setOpaque(false);
    todayB.setPreferredSize(new Dimension(24, 24));
    todayB.setRequestFocusEnabled(false);
    todayB.setBorderPainted(false);
    todayB.setFocusPainted(false);
    todayB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/today.png")));
    todayB.setText("");
    todayB.setToolTipText(Local.getString("To today"));
    
    //Left Panel of Calender + Buttons
    
    navbPanelLeft.setAlignmentX((float) 1.5);
    navbPanelLeft.setMinimumSize(new Dimension(50, 24));
    navbPanelLeft.setOpaque(false);
    navbPanelLeft.setPreferredSize(new Dimension(50, 24));
    
    dayBackB.setAction(dayBackAction);
    dayBackB.setMinimumSize(new Dimension(24, 24));
    dayBackB.setOpaque(false);
    dayBackB.setPreferredSize(new Dimension(24, 24));
    dayBackB.setRequestFocusEnabled(false);
    dayBackB.setToolTipText("");
    dayBackB.setBorderPainted(false);
    dayBackB.setFocusPainted(false);
    dayBackB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/back.png")));
    dayBackB.setText("");
    dayBackB.setToolTipText(Local.getString("One day back"));
    
    monthBackB.setAction(monthBackAction);
    monthBackB.setMinimumSize(new Dimension(24, 24));
    monthBackB.setOpaque(false);
    monthBackB.setPreferredSize(new Dimension(24, 24));
    monthBackB.setRequestFocusEnabled(false);
    monthBackB.setToolTipText("");
    monthBackB.setBorderPainted(false);
    monthBackB.setFocusPainted(false);
    monthBackB.setIcon(new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/backmonth.png")));
    monthBackB.setText("");
    monthBackB.setToolTipText(Local.getString("One month back"));
    
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
    navigationBar.add(navbPanelLeft, BorderLayout.WEST);
    navigationBar.add(navbPanelRight, BorderLayout.EAST);
    navigationBar.add(navbPanelCenter, BorderLayout.CENTER);
    navbPanelLeft.add(monthBackB, BorderLayout.WEST);
    navbPanelLeft.add(dayBackB, BorderLayout.EAST);
    navbPanelCenter.add(todayB, BorderLayout.CENTER);
    navbPanelRight.add(dayForwardB, BorderLayout.WEST);
    navbPanelRight.add(monthForwardB, BorderLayout.EAST);
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