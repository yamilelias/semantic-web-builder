package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

public class XML
{
	public static void main(String[] args) throws IOException
	{
		String f = FileUtils.readFileToString(new File("file.txt"));
		System.out.println(StringEscapeUtils.escapeXml(f));
	}
}
