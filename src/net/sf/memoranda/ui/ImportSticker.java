package net.sf.memoranda.ui;


import javax.swing.SwingUtilities;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import net.sf.memoranda.util.Local;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.EventNotificationListener;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.History;
import net.sf.memoranda.NoteList;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectListener;
import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.ResourcesList;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.AgendaGenerator;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.util.Util;
import nu.xom.Element;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**ImportStricker class 
 * 
 * Finds file and reads file then creates stickies
 */
public class ImportSticker {

	 private String name;        
	 private JEditorPane viewer = new JEditorPane("text/html", "");
	
	public ImportSticker(String x) {
		name = x;
	}
	
	/**Import Sticker file creates stickies 
	 * 
	 * @return boolean
	 */
    public boolean importFile(){
    	boolean result = true;
    	String current;
    	JFileChooser fx = new JFileChooser(new java.io.File("."));
        fx.setDialogTitle("Import Location");
        fx.setFileFilter(new FileTypeFilter(".html","HTML File"));
        int exportResult = fx.showSaveDialog(null);
        if (exportResult == JFileChooser.APPROVE_OPTION){
        	File fi = fx.getSelectedFile(); 
        		try {
        			String tempLocation = fi.getPath();
                    System.out.println(tempLocation);
                    BufferedReader br = new BufferedReader(new FileReader(tempLocation));
                    while ((current = br.readLine()) != null) {
                    EventsManager.createSticker(current, 1);
                    CurrentStorage.get().storeEventsManager();
                    }
                         
                } catch (IOException e) {
                    	e.printStackTrace();
                    	JOptionPane.showMessageDialog(null,Local.getString("Import Failed"));
                   }	
                    
        }              
        return result;
    }
                

        
}