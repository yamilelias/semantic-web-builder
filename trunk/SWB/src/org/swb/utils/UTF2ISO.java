package org.swb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class UTF2ISO
{
	private static final String DIR = "web/translate_pages";
	public static void main(String[] args) throws IOException
	{
		File dir = new File(DIR);
		File[] files = dir.listFiles();
		
		for (File f: files)
		{
			if (f.getName().endsWith(".stxt")) exec(f);
		}
	}
	
	private static void exec(File f) throws IOException
	{
		System.out.println(f.getAbsolutePath());
		String name = f.getName().substring(0, f.getName().length()-5);
		File txtFile = new File(f.getParentFile(), name + ".txt");
		System.out.println(txtFile.getAbsolutePath());
		
		String content = FileUtils.readFileToString(f, "UTF-8");
		FileUtils.writeStringToFile(txtFile, content, "Cp1252");
	}
}
