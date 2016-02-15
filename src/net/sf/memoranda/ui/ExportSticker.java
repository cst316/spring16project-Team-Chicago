package net.sf.memoranda.ui;

import java.io.*;
import java.nio.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;


import net.sf.memoranda.EventsManager;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class ExportSticker {

        private String name; 
        
        /*public static Document _doc = null;
        static Element _root = null;

        static {
                CurrentStorage.get().openEventsManager();
                if (_doc == null) {
                        _root = new Element("eventslist");
/*                        _root.addNamespaceDeclaration("jnevents", NS_JNEVENTS);
                        _root.appendChild(
                                new Comment("This is JNotes 2 data file. Do not modify.")); */
/*                        _doc = new Document(_root);
                } else
                        _root = _doc.getRootElement();

        }*/
        
        public ExportSticker(String x) {
                this.name = remove1(x);
        }

        /**
         * Function to eliminate special chars from a string
         */
        public static String remove1(String input) {
            
            String original = "Ã¡Ã Ã¤Ã©Ã¨Ã«Ã­Ã¬Ã¯Ã³Ã²Ã¶ÃºÃ¹uÃ±Ã�Ã€Ã„Ã‰ÃˆÃ‹Ã�ÃŒÃ�Ã“Ã’Ã–ÃšÃ™ÃœÃ‘Ã§Ã‡";
            
            String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
            String output = input;
            for (int i=0; i<original.length(); i++) {
            
                output = output.replace(original.charAt(i), ascii.charAt(i));
            }
            return output;
        }
               /**
               *Method: export
               *Inputs:
               *Returns: boolean result
       		   *Description: Export a text file to a specfic folder
             */
        public boolean export(){
                boolean result = true;
                //String fs = System.getProperty("file.separator");
                String contents = getSticker();
                JFileChooser fx = new JFileChooser(new java.io.File("."));

                fx.setDialogTitle("export location");
               fx.setFileFilter(new FileTypeFilter(".txt","Text File"));
                int exportResult = fx.showSaveDialog(null);
                if (exportResult == JFileChooser.APPROVE_OPTION){
                	File fi = fx.getSelectedFile();
                
                try {
                	//File file = new File(this.name+"."+src);
                		String tempLocation = fi.getPath();
                		tempLocation = tempLocation + ".txt";	
                        FileWriter fwrite=new FileWriter(tempLocation);
                        fwrite.write(contents);
                        fwrite.flush();
                        fwrite.close();
                        JOptionPane.showMessageDialog(null,Local.getString("Document created!"));
            
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,Local.getString("failed to create your document"));
        }
                
                }        
                        
                return result;
        }
        /**
        *Method: export HTML
        *Inputs:
        *Returns: boolean result
		   *Description: Export a htlm file to a specfic folder
      */
        public boolean exporthtml(){
            boolean result = true;
            //String fs = System.getProperty("file.separator");
            String contents = getSticker();
            JFileChooser fx = new JFileChooser(new java.io.File("."));

            fx.setDialogTitle("export location");
           fx.setFileFilter(new FileTypeFilter(".html","HTML File"));
            int exportResult = fx.showSaveDialog(null);
            if (exportResult == JFileChooser.APPROVE_OPTION){
            	File fi = fx.getSelectedFile();
            
            try {
            	//File file = new File(this.name+"."+src);
            		String tempLocation = fi.getPath();
            		tempLocation = tempLocation + ".html";	
                    FileWriter fwrite=new FileWriter(tempLocation);
                    fwrite.write(contents);
                    fwrite.flush();
                    fwrite.close();
                    JOptionPane.showMessageDialog(null,Local.getString("Document created!"));
        
        
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,Local.getString("failed to create your document"));
    }
            
            }        
                    
            return result;
    }

        public String getSticker(){
                Map stickers = EventsManager.getStickers();
        String result = "";
        String nl = System.getProperty("line.separator"); 
                for (Iterator i = stickers.keySet().iterator(); i.hasNext();) {
            String id = (String)i.next();
            result += (String)(((Element)stickers.get(id)).getValue())+nl;
            }
            
                return result;
        }
        
        /*public static String getStickers() {
                String result ="";
                Elements els = _root.getChildElements("sticker");
                for (int i = 0; i < els.size(); i++) {
                        Element se = els.get(i);
                        m.put(se.getAttribute("id").getValue(), se.getValue());
                }
                return m;
        }*/
        
        
        
}