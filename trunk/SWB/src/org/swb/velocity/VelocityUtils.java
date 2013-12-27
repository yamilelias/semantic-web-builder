package org.swb.velocity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.swb.utils.PropertiesLoader;

public class VelocityUtils
{
    protected static VelocityEngine ve = null;
    
    static
    {
        try
        {
            ve = new VelocityEngine();
            ve.init(PropertiesLoader.loadProperties("velocity", VelocityUtils.class.getClassLoader()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Velocity Utils not initialized.");
        }
    }
    
    public static void render(String template, Map model, File destFile) throws IOException
    {
        PrintWriter out = null;
        try
        {
            out = new PrintWriter(destFile, "UTF-8");
            VelocityContext ctx = new VelocityContext(model);
            Template tmp = ve.getTemplate(template); 
            tmp.merge(ctx, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IOException(e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        VelocityUtils.render("templates/page.vm", null, new File("demo.html"));
        
        System.out.println("Fi");
    }

}
