package org.swb.servlet;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet
{
    private static final String REDIRECT_ES = "/es/index.html";
    private static final String REDIRECT_EN = "/en/index.html";
    private static final String LANG_HEAD = "Accept-Language";
    private static final String ES = "es";
    private static final String LANG = "lang";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        // Prepare lang
        String lang = null;
        
        // Check cookie language
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
        {
            for (Cookie c: cookies)
            {
                if (c.getName().equals(LANG))
                {
                    lang = c.getValue();
                    break;
                }
            }
        }
        
        // Check lang header if not in cookies
        if (lang == null) lang = req.getHeader(LANG_HEAD);
        
        // Make redirect
        if (lang != null && lang.equalsIgnoreCase(ES)) resp.sendRedirect(REDIRECT_ES);
        else resp.sendRedirect(REDIRECT_EN);
    }
}
