package org.swb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class UTF2UTF
{
	private static final String DIR = "web/book_en";
	public static void main(String[] args) throws IOException
	{
		File dir = new File(DIR);
		File[] files = dir.listFiles();
		
		for (File f: files)
		{
			if (f.getName().endsWith(".txt")) exec(f);
		}
	}
	
	private static void exec(File f) throws IOException
	{
		System.out.println(f.getAbsolutePath());
		
		String name = f.getName();
		int i = name.indexOf("_spaeng.txt");
		name = name.substring(0,i);
		
		System.out.println(name);
		
		File stxtFile = new File(f.getParentFile(), name + ".stxt");
		System.out.println(stxtFile.getAbsolutePath());
		
		String content = FileUtils.readFileToString(f, "UTF-8");
		FileUtils.writeStringToFile(stxtFile, content, "UTF-8");
	}
}
