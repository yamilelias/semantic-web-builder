package org.swb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ConcatPages
{
	private static final String DIR = "web/book_es";
	private static final String FILE = "all.txt";
	
	public static void main(String[] args) throws IOException
	{
		File dir = new File(DIR);
		File[] files = dir.listFiles();
		
		String result = "";
		for (File f: files)
		{
			if (f.getName().endsWith(".stxt")) result += exec(f);
		}
		
		FileUtils.writeStringToFile(new File(FILE), result, "ISO-8859-1");
	}
	
	private static String exec(File f) throws IOException
	{
		String content = FileUtils.readFileToString(f, "UTF-8");
		return content;
	}
}
