package test.tgl.operations;

import java.awt.geom.Point2D;
import java.util.Map;

import test.tgl.Utils;
import test.tgl.xml.XMLModif;

public class Line implements IOperation
{
    @Override
    public void execute(XMLModif xmlModif, Map context, String params)
    {
        String[] points = params.split(" ");
        Point2D[] drawPoints = Utils.createPoints(points, context);
        
        Point2D p1 = drawPoints[0];
        Point2D p2 = drawPoints[1];
        
        xmlModif.apply("/line[+]");
        xmlModif.apply("@x1=" + p1.getX());
        xmlModif.apply("@y1=" + p1.getY());
        xmlModif.apply("@x2=" + p2.getX());
        xmlModif.apply("@y2=" + p2.getY());
        
        xmlModif.apply("@style=stroke:rgb(255,0,0);stroke-width:2");
    }
}
