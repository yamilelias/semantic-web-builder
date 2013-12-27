package org.swb.utils;

import static ys.wikiparser.Utils.*;
import ys.wikiparser.WikiParser;

/**
 * This example program illustrates usage of WikiParser class.
 * 
 * @author Yaroslav Stavnichiy (yarosla@gmail.com)
 * 
 */
public class WikiRender
{

    /**
     * InternalWikiParser - customized WikiParser. Customization is done by overriding
     * appendXxx() methods. This allows implementation-specific handling of
     * hyperlinks, images, and placeholders.
     * 
     */
    private static class InternalWikiParser extends WikiParser
    {
        public InternalWikiParser(String wikiText)
        {
            super();
            HEADING_LEVEL_SHIFT = 0;
            parse(wikiText);
        }

        public static String renderXHTML(String wikiText)
        {
            return new InternalWikiParser(wikiText).toString();
        }

        @Override
        protected void appendImage(String text)
        {
            super.appendImage(text);
        }
        
        @Override
        protected void appendLink(String text)
        {
            String[] link = split(text, '|');
            
            if (link.length==3)
            {
            	sb.append("<a href=\"" + parseLink(link[0]) + "\" target=\""+ link[2] + "\">");
            }
            else
            {
            	sb.append("<a href=\"" + parseLink(link[0]) + "\">");
            }
            sb.append(escapeHTML(unescapeHTML(link.length >= 2 && !isEmpty(link[1].trim()) ? link[1] : link[0])));
            sb.append("</a>");
        }

        @Override
        protected void appendMacro(String text)
        {
            if ("TOC".equals(text))
            {
                super.appendMacro(text); // use default
            }
            else if ("My-macro".equals(text))
            {
                sb.append("{{ My macro output }}");
            }
            else
            {
                super.appendMacro(text);
            }
        }
        
        public String parseLink(String link)
        {
        	if (link.indexOf('@')==-1) return link;
        	else return "mailto:"+link;
        }
    }

    
    public static String render(String wikiText)
    {
        return InternalWikiParser.renderXHTML(wikiText);
    }
    
    public static void main(String[] args)
    {
        String wikiText = "Hola Mundo!!!!\n\n"
                + "**Esto es una prueba**\nOtra cosa!!!: [[demo.html]]\n\n"
                + "Otra cosa!!!: [[otro link.html|Un link!!]]";
        
        String result = render(wikiText);
        System.out.println(result);
    }

}
