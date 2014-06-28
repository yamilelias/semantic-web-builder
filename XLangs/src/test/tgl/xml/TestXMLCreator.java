package test.tgl.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

public class TestXMLCreator
{
    public static void main(String[] args) throws Exception
    {
        OutputStream out = null;
        try
        {
            out = new FileOutputStream(new File("out.xml"));
            
            // Creamos documento
            Document document = XMLCreator.createDocument("response");
    
            // Apply expression
            XMLModif modif = new XMLModif(document);
            
            modif.apply("/elem1/elem2");
            modif.apply("elem2");
            modif.apply("/elem3/elem4");
            modif.apply("/elem1[1]/elem2[1]/elem5");
    
            modif.apply("/@att1=1");
            modif.apply("/elem1/elem2");
            modif.apply("elemx/x=x");
            modif.apply("elemy/y=x");
            modif.apply("/elem1/@att1=1");
            modif.apply("/elem1/@att2=2");
            modif.apply("/elem1/@att3=3");
            modif.apply("elemz/z=x");
            
            modif.apply("/elem3[1]/elem4=Mundo");
            modif.apply("/elem1/elem3/elem5=Como te llamas <>?");
            
            modif.apply("/elem1[+]/elem2");
            modif.apply("/elem1[+]/elem2");
            modif.apply("/elem1[+]/elem2");
            modif.apply("/elem1[+]/elem2");
            
            modif.apply("/elem1[1]/elem2+=INICIAL LINEA");
            modif.apply("/elem1[last]/elem2=FINAL LINEA");
            
            //XMLCreator.deleteEmptyNodes(document);
            
            // Generamos resultado
            XMLCreator.printDocument(document, out, "ISO-8859-1", false, true, 3);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

}
