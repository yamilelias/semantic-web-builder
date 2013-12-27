package org.swb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class DeleteBN
{
	private static final String DIR = "web/book_en";
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

		String content = FileUtils.readFileToString(f, "UTF-8");
		content = StringUtils.replace(content, "\n\t\n\t", "");
		System.out.println(content);
		FileUtils.writeStringToFile(new File(f.getAbsolutePath() + ".stxt"), content, "UTF-8");
	}
}
