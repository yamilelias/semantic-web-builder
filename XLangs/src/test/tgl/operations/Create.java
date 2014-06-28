package test.tgl.operations;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;

import test.tgl.ContextObjects;
import test.tgl.Utils;
import test.tgl.xml.XMLModif;

public class Create implements IOperation
{
    @Override
    public void execute(XMLModif xmlModif, Map context, String params)
    {
        String[] mainParams = params.split(" ");
        
        Point2D point = Utils.createPoint(mainParams[0], context);
        
        int width = (int)point.getX();
        int height = (int)point.getY();
        
        xmlModif.apply("/@width=" + width);
        xmlModif.apply("/@height=" + height);

        AffineTransform globalT = new AffineTransform();
        if (mainParams.length>1)
        {
            String corner = mainParams[1];
            if (corner.equalsIgnoreCase("DL"))
            {
                double[] values = {1,0,0,-1,0,height};
                globalT = new AffineTransform(values);
            }
        }
        
        context.put(ContextObjects.GLOBAL_TRANSFORM, globalT);
        context.put(ContextObjects.LOCAL_TRANSFORM, new AffineTransform());
    }
}
