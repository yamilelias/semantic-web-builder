package org.swb.processor;

import info.semantictext.Node;
import info.semantictext.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;

public class IndexProcessor extends MatchingTask
{
    private File dir;
    private File outFile;
    private File templateFile;
    
    public void setDir(File dir)
    {
        this.dir = dir;
    }
    public void setOutFile(File outFile)
    {
        this.outFile = outFile;
    }
    public void setTemplateFile(File templateFile)
	{
		this.templateFile = templateFile;
	}
    
	public void execute() throws BuildException 
    {
        try
        {
        	// Generamos index
            String result = generateIndex();
            
            // Parseamos template
            Node node = FileParser.parse(templateFile);
            node.getChild("text").setValue(result);
            
            FileUtils.writeStringToFile(outFile, node.toSTXT(), "UTF-8");
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
    }
    
    private String generateIndex() throws IOException
    {
        // Leemos páginas
        File[] files = dir.listFiles();
        StringBuffer result = new StringBuffer();
        for (File f: files)
        {
        	if (!f.isFile() || !f.getName().endsWith(".stxt")) continue;

        	// Obtenemos nombre
        	String name = f.getName().substring(0, f.getName().length()-".stxt".length());
        	
            // Obtenemos nodo
            Node node = FileParser.parse(f);
            if (node.getChilds("title").size()==0 || node.getChilds("text").size()==0) continue;
            appendRoot(result, node, name);
            
            List<Node> childs = node.getChilds();
            
            int num = 0;
            for (Node n: childs)
            {
                if (appendChild(result, n, name, num)) num++;
            }
        }
        
        // Creamos nuevo texto
        return result.toString();
    }
    
    private void appendRoot(StringBuffer result, Node node, String name)
    {
        result.append("\n=== [[" + name + ".html | " + node.getChild("title").getTvalue() + "]] === \n");
    }
    
    private boolean appendChild(StringBuffer result, Node n, String name, int num)
    {
        if (n.getCanonicalName().equalsIgnoreCase("header"))
        {
            result.append("* [[").append(name + ".html#index_" + num + " | ").append(n.getTvalue()).append("]]\n");
            return true;
        }
        else if (n.getCanonicalName().equalsIgnoreCase("subheader"))
        {
            result.append("** [[").append(name + ".html#index_" + num + " | ").append(n.getTvalue()).append("]]\n");
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        IndexProcessor processor = new IndexProcessor();
        processor.setDir(new File("web/book_es"));
        processor.setOutFile (new File("web/book_es/chapter_00_c.stxt"));
        processor.setTemplateFile (new File("web/book_es/chapter_00_c.stxt"));
        processor.execute();
        
        processor.setDir(new File("web/book_en"));
        processor.setOutFile (new File("web/book_en/chapter_00_c.stxt"));
        processor.setTemplateFile (new File("web/book_en/chapter_00_c.stxt"));
        processor.execute();
        
        System.out.println("Fi");
    }
}

