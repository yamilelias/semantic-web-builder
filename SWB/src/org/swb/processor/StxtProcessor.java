package org.swb.processor;

import info.semantictext.parser.FileParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.swb.utils.PropertiesLoader;
import org.swb.utils.Utils;
import org.swb.utils.WikiRender;
import org.swb.velocity.VelocityUtils;

public class StxtProcessor extends AntProcessor
{
    private static final String EXTENSION = ".stxt";
    private static final int EXTENSION_SIZE = EXTENSION.length();
    
    private String extension = "html";
    private String template;
    private boolean copySource = false;
    private boolean checkLastModified = false;
    private File propsFile;
    private String propsKey = "props";
    
    public void setExtension(String extension)
    {
        this.extension = extension;
    }
    public void setTemplate(String template)
    {
        this.template = template;
    }
    public void setCopySource(boolean copySource)
    {
        this.copySource = copySource;
    }    
    public void setCheckLastModified(boolean checkLastModified)
    {
        this.checkLastModified = checkLastModified;
    }    
	public void setPropsFile(File propsFile)
	{
		this.propsFile = propsFile;
	}
	public void setPropsKey(String propsKey)
	{
		this.propsKey = propsKey;
	}
	
	@Override
    protected void process(File srcFile) throws IOException, Exception
    {
        // Creamos fichero
        String name = srcFile.getName();
        if (!name.endsWith(EXTENSION)) return;
        name = name.substring(0, name.length()-EXTENSION_SIZE);
        File destFile = new File(todir, name + '.' + extension);
        File destFileOrig = new File(todir, srcFile.getName());
        
        // Validamos modificado
        if (!checkLastModified || !destFile.exists() || destFile.lastModified()!=srcFile.lastModified())
        {
            // Creamos contexto
            Map context = new HashMap();
            addNodeDoc(context, srcFile);
            context.put("page_name", name + '.' + extension);
            context.put("name", name);
            
            // Añadimos propiedades
            if (propsFile != null)
            {
            	Properties props = PropertiesLoader.loadProperties(new FileInputStream(propsFile), this.getClass().getClassLoader());
            	context.put(propsKey, props);
            }
            
            // Añadimos otras propiedades
            addCustomParams(context, srcFile);
            
            // Creamos velocity
            VelocityUtils.render(template, context, destFile);
            destFile.setLastModified(srcFile.lastModified());
            
            if (copySource) FileUtils.copyFile(srcFile, destFileOrig);
        }
    }
    
    protected void addNodeDoc(Map params, File srcFile) throws IOException
    {
        // Creamos params
        params.put("doc", FileParser.parse(srcFile));
        params.put("wiki", new WikiRender());
        params.put("utils", new Utils());
    }
    
    protected void addCustomParams(Map params, File srcFile)
    {
    }
}
