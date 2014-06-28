package test.tgl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class Utils
{

    private static final JexlEngine jexl = new JexlEngine();
    static {
       jexl.setCache(512);
       jexl.setLenient(false);
       jexl.setSilent(false);
    }

    public static Object executeExpression(String exp, Map context)
    {
        Expression e = jexl.createExpression(exp);
        JexlContext jexlContext = new MapContext(context);
        return e.evaluate(jexlContext);
    }
    
    public static Point2D[] createPoints(String[] points, Map context)
    {
        Point2D[] result = new Point2D[points.length];
        
        for (int i = 0; i<points.length; i++)
        {
            String point = points[i];
            result[i] = createPoint(point, context);
        }

        return result;
    }

    public static Point2D createPoint(String point, Map context)
    {
        AffineTransform lt = (AffineTransform) context.get(ContextObjects.LOCAL_TRANSFORM);
        AffineTransform gt = (AffineTransform) context.get(ContextObjects.GLOBAL_TRANSFORM);
        
        boolean localPoint = point.startsWith("(")&&point.endsWith(")");
        if (localPoint)
        {
        	point = point.substring(1, point.length()-1);
        }
        
        String[] xy = point.split(",");
        Point2D result = new Point2D.Double(getValue(xy[0], context), getValue(xy[1], context));
        
        if (gt!=null)
        {
            gt.transform(result, result);
        }
        if(localPoint && lt!=null)
        {
            lt.transform(result, result);
        }
        
        return result;
    }
    
    private static double getValue(String value, Map context)
    {
    	Object o = executeExpression(value, context);
    	
            if (o instanceof Double) return (Double) o;
            if (o instanceof Float) return new Double((Float)o);
            if (o instanceof Integer) return new Double((Integer)o);
            else return Double.parseDouble(o.toString());
    }    

}
