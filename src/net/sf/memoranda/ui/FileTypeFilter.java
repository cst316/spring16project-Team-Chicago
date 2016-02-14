/*
*File: Type Filter
*Author: Vikas Bhagat
*Date 2/11/16
*Description: created class and method to take string extension
*				and description for FileExplorer 
*/
package net.sf.memoranda.ui;

import java.io.File;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser.*;

/**
* Class: FileTypeFilter
* Description:: 
*/
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