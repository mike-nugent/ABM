package utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class AionLogFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) 
	{
		if(f.isDirectory()) return true;
		if(f.getName().endsWith(".log"))
		{
			return true;
		}
		return false;
	}

	@Override 
	public String getDescription() 
	
	{
		return null;
	}

}
