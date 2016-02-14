//Vikas Bhagat
//File Type Filter

package net.sf.memoranda.ui;

import java.io.File;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser.*;

public class FileTypeFilter extends FileFilter {

	private final String extension;
	private final String description;
	
	public FileTypeFilter(String extension, String description){
	
		this.extension = extension;
		this.description = description;
		}


@Override
public boolean accept(File file){
	if (file.isDirectory()){
		return true;
	}
	return file.getName().endsWith(extension);
}

@Override
public String getDescription() {
	return description + String.format(" (*%s)", extension);
}


}