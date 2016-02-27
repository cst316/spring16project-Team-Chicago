package net.sf.memoranda.ui;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.*;
import java.io.BufferedReader;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;


/**ImportStricker class. 
 * 
 * Finds file and reads file then creates stickies
 */
public class ImportSticker {

	 private String _name;        
	 private JEditorPane _viewer = new JEditorPane("text/html", "");
	
	/**Import Sticker Constructor. 
	 * 
	 * @pram x String 
	*/	
	public ImportSticker(String x) {
		_name = x;
	}
	
	/**Import Sticker file creates stickies. 
	 * 
	 * @return boolean 
	*/
	public boolean importFile(){
    	boolean result = true;
    	String current;
    	final JFileChooser fx = new JFileChooser(new java.io.File("."));
    	fx.setDialogTitle("Import Location");
        fx.setFileFilter(new FileTypeFilter(".html","HTML File"));
        final int exportResult = fx.showSaveDialog(null);
        if (exportResult == JFileChooser.APPROVE_OPTION){
        	final File fi = fx.getSelectedFile(); 
        	final String tempLocation = fi.getPath();
        	try {
        		final BufferedReader  br = new BufferedReader(new FileReader(tempLocation));
        		try {
        			while ((current = br.readLine()) != null) {
        				EventsManager.createSticker(current, 1);
        				CurrentStorage.get().storeEventsManager();
        			}
        		}	
        		catch(IOException e) {
        			e.printStackTrace();
        			JOptionPane.showMessageDialog(null,Local.getString("Import Failed"));
        			result = false;
        			br.close();
        		}
        	} 
        	catch (Exception e) {
        		e.printStackTrace();
        		JOptionPane.showMessageDialog(null,Local.getString("Import Failed"));
        		result = false;
        	}       
        }              
        return result;
	}
                

        
}