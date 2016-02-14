package net.sf.memoranda.ui;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import net.sf.memoranda.util.Local;

public class ImportSticker {

String name;        
        
        public ImportSticker(String x) {
                name = x;
        }

        public boolean import_file(){
                

          
                JOptionPane.showMessageDialog(null,Local.getString("can not import your document"));
                return true;
        }
        
        
}