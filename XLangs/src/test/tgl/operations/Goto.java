package test.tgl.operations;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;

import test.tgl.ContextObjects;
import test.tgl.Utils;
import test.tgl.xml.XMLModif;

public class Goto implements IOperation
{
    @Override
    public void execute(XMLModif xmlModif, Map context, String params)
    {
        // TODO Auto-generated method stub
    	System.out.println("***************************************");
    	System.out.println("***************************************");
        System.out.println("Executing: " + Goto.class.getCanonicalName());
        
        Point2D point 	= Utils.createPoint(params, context);
        Point2D origen 	=  Utils.createPoint("0,0", context);
        AffineTransform t = (AffineTransform) context.get(ContextObjects.LOCAL_TRANSFORM);
        if (t == null)
        {
        	t = new AffineTransform();
        	context.put(ContextObjects.LOCAL_TRANSFORM, t);
        }
        
        t.setToTranslation(point.getX()-origen.getX(), point.getY()-origen.getY());
        System.out.println(params);
        System.out.println(point);
        System.out.println(t);
    }
    
    public static void main(String[] args)
    {
        Point2D.Double p = new Point2D.Double(10,10);
        System.out.println("p = " + p);
        AffineTransform t = new AffineTransform();
        System.out.println(t);
        t.rotate(Math.PI/2);
        t.translate(10, 0);
        t.transform(p, p);
        System.out.println(t);
        System.out.println("p=" + p);
    }
}
