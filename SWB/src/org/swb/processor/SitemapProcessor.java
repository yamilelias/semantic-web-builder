package org.swb.processor;

import info.semantictext.Node;
import info.semantictext.parser.FileParser;
import info.semantictext.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.swb.utils.Page;

public class SitemapProcessor extends MatchingTask
{
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final String EXTENSION = ".stxt";
    
    private File[] dirs;
    private String[] webDirs;
    private File outFile;
    private String urlPrefix;
    
    public void setDirs(String dirs)
    {
        String[] dd = dirs.split(",");
        this.dirs = new File[dd.length];
        for (int i = 0; i<dd.length; i++) this.dirs[i] = new File(dd[i].trim());
    }

    public void setOutFile(File outFile)
    {
        this.outFile = outFile;
    }

    public void setWebDirs(String webDirs)
    {
        String[] dd = webDirs.split(",");
        this.webDirs = new String[dd.length];
        for (int i = 0; i<dd.length; i++) this.webDirs[i] = dd[i].trim();
    }
    public void setUrlPrefix(String prefix)
	{
		this.urlPrefix = prefix;
	}

	public void execute() throws BuildException 
    {
        try
        {
            String result = generateSitemap(dirs, webDirs);
            if (outFile != null)
            {
                FileUtils.writeStringToFile(outFile, result, Constants.ENCODING);
            }
            else
            {
                System.out.println("Sitemap = " + result);
            }
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
    }
    
    public String generateSitemap(File[] dirs, String[] webDirs) throws IOException
    {
        List<Page> pages = new ArrayList<Page>();
        for(int i = 0; i<dirs.length; i++)
        {
            File dir = dirs[i];
            String webDir = webDirs[i];
            
            File[] files = dir.listFiles();
            for (File f: files)
            {
                Page p = createPage(f, webDir);
                if (p!=null) pages.add(p);
            }
        }
        
        // Vamos recorriendo recursos insertando
        StringBuffer out = new StringBuffer();
        out.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        for (Page page: pages)
        {
            String url = getUrl(page); 
            if (url!=null) out.append(url).append('\n');
        }
        out.append("</urlset>");
        
        return out.toString();
    }
    
    private String getUrl(Page page) throws UnsupportedEncodingException
    {
        // Creamos resultado
        StringBuffer result = new StringBuffer();
        
        result.append("<url><loc>").append(urlPrefix + page.getUrl()).append("</loc>");
        if (page.getLastModif()!=null)
        {
            result.append("<lastmod>").append(SDF.format(page.getLastModif())).append("</lastmod>");
        }
        if (page.getPriority()!=0)
        {
            result.append("<priority>" + page.getPriority() + "</priority>"); 
        }
        result.append("</url>");
        
        return result.toString();
    }
    
    private Page createPage(File f, String webDir) throws IOException
    {
    	if (f.isDirectory() || !f.getName().endsWith(EXTENSION)) return null;
    	
        // Creamos result
        Page result = new Page();
        
        // Create name
        result.setUrl(webDir + '/' + parseName(f.getName()));
        
        // Intentamos parsear
        Node node = FileParser.parse(f);
        
        // Obtenemos metadata
        node = node.getChild("metadata");
        if (node == null) return result;
        
        // Obtenemos nodos
        Node priority = node.getChild("priority");
        if (priority != null)
        {
            result.setPriority(Float.parseFloat(priority.getTvalue()));
        }
        
        Node lastmodif = node.getChild("last_modif");
        if (lastmodif != null)
        {
            try
            {
                result.setLastModif(SDF.parse(lastmodif.getTvalue()));
            }
            catch (Exception e)
            {
                throw new IOException("Error parsing: " + f, e);
            }
        }
        
        // Retorno de resultado
        return result;
    }
    
    private static String parseName(String name)
    {
        int i = name.lastIndexOf(".stxt");
        if (i==-1) return name;
        return name.substring(0, i) + ".html";
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        SitemapProcessor processor = new SitemapProcessor();
        processor.setDirs("web/pages_es,web/book_es,web/pages_en,web/book_en");
        processor.setWebDirs("/es,/es/book,/en,/en/book");
        processor.execute();
        
        System.out.println("Fi");
    }
}

