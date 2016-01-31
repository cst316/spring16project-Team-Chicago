package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.sf.memoranda.Event;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.util.Configuration;
import net.sf.memoranda.util.Local;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/*$Id: EventNotificationDialog.java,v 1.8 2004/10/18 19:08:56 ivanrise Exp $*/
public class EventNotificationDialog extends JFrame {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButton1 = new JButton();  // "OK" button
  JButton jButton2 = new JButton();	 // "Remove" button
  JButton jButton3 = new JButton();	 // "Edit" button
  Border border1;
  Border border2;
  Border border3;
  JPanel jPanel1 = new JPanel();
  JLabel textLabel = new JLabel();
  JLabel timeLabel = new JLabel();
  Border border4;
  Event ev; 

  public EventNotificationDialog(String title, String time, String text, Event ev) {
    super();
    this.setTitle(title);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      new ExceptionDialog(ex);
    }
    timeLabel.setText(time);
    timeLabel.setIcon(new ImageIcon(net.sf.memoranda.ui.TaskDialog.class.getResource(
            "resources/icons/event48.png")));
    textLabel.setText(text);
    this.setSize(300,200);
    this.setLocationRelativeTo(null);
    this.setVisible(true);    
    this.toFront();
    this.requestFocus();
    //jButton1.requestFocus();
    this.ev = ev; 
  }

  public EventNotificationDialog() {
    this("", "", "", null);
  }
  void jbInit() throws Exception {
    this.setResizable(false);
    this.setIconImage(new ImageIcon(EventNotificationDialog.class.getResource("resources/icons/jnotes16.png")).getImage());
    this.getContentPane().setBackground(new Color(251, 197, 63));
    border2 = BorderFactory.createEmptyBorder(0,30,0,30);
    border3 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,new Color(142, 142, 142)),BorderFactory.createEmptyBorder(0,30,0,30));
    border4 = BorderFactory.createEmptyBorder(10,10,0,10);
    panel1.setLayout(borderLayout1);
    panel1.setBackground(new Color(251, 197, 63));
    
    jButton1.setText(Local.getString("Ok"));
    jButton1.setBounds(150, 415, 95, 30);
    jButton1.setPreferredSize(new Dimension(80, 30));
    jButton1.setBackground(new Color(69, 125, 186));
    jButton1.setForeground(Color.white);
    jButton1.setDefaultCapable(true);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    
    // "Remove" button on event notification
    jButton2.setText(Local.getString("Remove"));
    jButton2.setBounds(150, 415, 95, 30);
    jButton2.setPreferredSize(new Dimension(80, 30));
    jButton2.setBackground(new Color(69, 125, 186));
    jButton2.setForeground(Color.white);
    jButton2.setDefaultCapable(true);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton2_actionPerformed(e);
      }
    });
    
    // "Edit" button on event notification
    jButton3.setText(Local.getString("Edit"));
    jButton3.setBounds(150, 415, 95, 30);
    jButton3.setPreferredSize(new Dimension(80, 30));
    jButton3.setBackground(new Color(69, 125, 186));
    jButton3.setForeground(Color.white);
    jButton3.setDefaultCapable(true);
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton3_actionPerformed(e);
      }
    });
    
    panel1.setBorder(border4);
    panel1.setMinimumSize(new Dimension(300, 200));
    panel1.setPreferredSize(new Dimension(300, 200));
    timeLabel.setFont(new java.awt.Font("Dialog", 0, 20));
    timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    textLabel.setHorizontalAlignment(SwingConstants.CENTER);
    getContentPane().add(panel1);
    panel1.add(jPanel1,  BorderLayout.SOUTH);
    jPanel1.add(jButton1, null);  // adds ok button
    jPanel1.add(jButton2, null);  // adds remove button
    jPanel1.add(jButton3, null);  // adds edit button
    jPanel1.setBackground(new Color(251, 197, 63));
    panel1.add(textLabel, BorderLayout.CENTER);
    panel1.add(timeLabel, BorderLayout.NORTH);
    playSoundNotification();
  }
  
  /**
   * Closes the notice window if ok button pressed
   * @param e
   */
  void jButton1_actionPerformed(ActionEvent e) {
       this.dispose();
  }
  
  /**
   * Calls method to delete event if remove button pressed.
   * @param e
   */
  void jButton2_actionPerformed(ActionEvent e) {
      EventsPanel.removeEventB_actionPerformed(e, ev);
      this.dispose();
  }
  
  /**
   * Calls method to bring up edit event if edit button pressed
   * @param e
   */
  void jButton3_actionPerformed(ActionEvent e) {
      EventsPanel.editEventB_actionPerformed(e, ev);
      this.dispose();
  }
  
  private void playSoundNotification() {
		if (Configuration.get("NOTIFY_SOUND").equals("DISABLED"))
			return;
		if (Configuration.get("NOTIFY_SOUND").equals("BEEP")) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
		if (Configuration.get("NOTIFY_SOUND").equals("")) {
			Configuration.put("NOTIFY_SOUND", "DEFAULT");
			Configuration.saveConfig();
		}
		URL url;
		if (Configuration.get("NOTIFY_SOUND").equals("DEFAULT"))
			url =
				EventNotificationDialog.class.getResource(
					"resources/beep.wav");
		else
			try {
				url =
					new File(Configuration.get("NOTIFY_SOUND").toString())
						.toURL();
			} catch (Exception ex) {
				url =
					EventNotificationDialog.class.getResource(
						"resources/beep.wav");
			}
		try {
			AudioClip clip = Applet.newAudioClip(url);
			clip.play();
		} catch (Exception ex) {
			new ExceptionDialog(ex, "Error loading audioclip from "+url, "Check the location and type of audioclip file.");
		}
	}
}