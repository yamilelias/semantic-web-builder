package test.tgl.operations;

import java.util.Map;

import test.tgl.xml.XMLModif;

public interface IOperation
{
    public void execute(XMLModif xmlModif, Map context, String params);
}
