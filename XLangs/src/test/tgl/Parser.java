package test.tgl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import test.tgl.operations.IOperation;
import test.tgl.xml.XMLCreator;
import test.tgl.xml.XMLModif;

public class Parser
{
    private static final String ENCODING = "UTF-8";
    private static final String PREFIX = IOperation.class.getPackage().getName();
    
    public static String parse(String in) throws Exception
    {
        // Creamos array de salida
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Creamos documento
        Document document = XMLCreator.createDocument("svg");

        // Creamos modif expression
        XMLModif modif = new XMLModif(document);
        
        modif.apply("/@style=border:1px solid black;");
        
        // Creamos contexto
        Map context = new HashMap();
        
        // Creamos comandos
        String[] commands = in.split("\n");
        for (String command: commands)
        {
            execute(command, modif, context);
        }

        // Simplificamos // TODO Verificar
        //XMLCreator.deleteEmptyNodes(document);
        
        // Generamos resultado
        XMLCreator.printDocument(document, out, ENCODING, false, true, 3);
        
        return new String(out.toByteArray(), ENCODING);
    }

    private static void execute(String command, XMLModif modif, Map context) throws Exception
    {
        command = command.trim();
        if (command.length()==0 || command.startsWith("//")) return;
        
        int i = command.indexOf(' ');
        String params = "";
        if (i != -1)
        {
            params = command.substring(i+1);
            command = command.substring(0,i);
        }
        
        if (Synonims.contains(command)) command = Synonims.get(command);
        
        IOperation op = (IOperation) Parser.class.getClassLoader().loadClass(PREFIX + '.' + command).newInstance();
        op.execute(modif, context, params);
    }
}
