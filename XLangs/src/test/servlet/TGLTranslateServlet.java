package test.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import test.tgl.Parser;

public class TGLTranslateServlet extends HttpServlet implements Constants
{
    private static final long serialVersionUID = 1L;
    
    private static final String ENCODING = "UTF-8";
    private static final String TEXT_JSON = "text/json";
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        execute(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        execute(req, resp);
    }

    private void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            String text = req.getParameter("text");
            System.out.println("Texto entrada: " + text);

            JSONObject result = new JSONObject();
            
            try
            {
                String svg = Parser.parse(text);
                result.put("svg", svg);
                System.out.println("SVG:\n" + svg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                result.put("error", e.getMessage());
            }
            
            resp.setHeader(CACHE_CONTROL, NO_CACHE);
            resp.setHeader(PRAGMA, NO_CACHE);
            resp.setCharacterEncoding(ENCODING);
            resp.setContentType(TEXT_JSON);
            
            OutputStream out = resp.getOutputStream();
            out.write(result.toString(3).getBytes(ENCODING));
            out.flush();
            out.close();
            
            System.out.println("JSON = " + result.toString(3));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
