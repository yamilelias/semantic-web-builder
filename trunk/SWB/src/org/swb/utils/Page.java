package org.swb.utils;

import java.util.Date;

public class Page
{
    private String url;
    private Date lastModif;
    private float priority = 0.5f;
    
    public String getUrl()
    {
        return url;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }
    public Date getLastModif()
    {
        return lastModif;
    }
    public void setLastModif(Date lastModif)
    {
        this.lastModif = lastModif;
    }
    public float getPriority()
    {
        return priority;
    }
    public void setPriority(float priority)
    {
        this.priority = priority;
    }    
}
