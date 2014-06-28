package test.tgl.operations;

import java.util.Map;

import test.tgl.Utils;
import test.tgl.xml.XMLModif;

public class Set implements IOperation
{
    @Override
    public void execute(XMLModif xmlModif, Map context, String params)
    {
        String[] parts = params.split(" ");
        context.put(parts[0], Utils.executeExpression(parts[1], context));
    }

}
