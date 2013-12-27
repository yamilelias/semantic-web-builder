package org.swb.servlet;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class LangServlet extends HttpServlet
{
    private static final String REFERER = "referer";
    private static final String LANG = "lang";
    private static final String ES = "es";
    private static final String EN = "en";
    private static final String PATH = "/";
    private static final String INDEX = "/index.html";
    private static final int SECONDS_COOKIE = 3600*24*365;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        // Get lang
        String lang = req.getParameter(LANG);
        
        // Validate lang
        if (lang == null || !lang.equals(ES)) lang = EN;
        
        // Insert cookie
        Cookie c = new Cookie(LANG, lang);
        c.setMaxAge(SECONDS_COOKIE);
        c.setPath(PATH);
        resp.addCookie(c);
        
        // Get referer
        String refererURI = req.getHeader(REFERER);
        try
        {
            refererURI = new URI(refererURI).getPath();
            refererURI = '/' + lang + refererURI.substring(3);
        }
        catch (Exception e)
        {
            refererURI = '/' + lang + INDEX;
        }
        
        // Redirect
        resp.sendRedirect(refererURI);
    }
}
