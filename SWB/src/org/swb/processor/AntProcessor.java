package org.swb.processor;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;

public abstract class AntProcessor extends MatchingTask 
{
	protected File dir;
    protected File todir;

    public void setDir(File dir)
    {
        this.dir = dir;
    }
    public void setTodir(File todir)
    {
        this.todir = todir;
    }

    public void execute() throws BuildException 
	{
		if (dir == null || todir == null) 
		{
			throw new BuildException("dir must be specified");
		}

        // Log
        log("Processing: " + dir.getAbsolutePath() + " ---> " + todir.getAbsolutePath());
        
        // Obtenemos ficheros de origen
		DirectoryScanner dsSource = getDirectoryScanner(dir);
		String[] filesSource = dsSource.getIncludedFiles();
        
		for (int i = 0; i < filesSource.length; i++) 
		{
			String file = filesSource[i];
			File srcFile = new File(dir, file);
			
            try
            {
                process(srcFile);
            }
            catch (Exception e)
            {
                log("ERROR processing: " + file, 0);
                throw new BuildException(e);
            }
		}
	}
    
    protected abstract void process(File srcFile) throws IOException, Exception;
}
