package test.tgl.xml;

import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLCreator
{
    private static final String NO          = "no";
    private static final String YES         = "yes";
    private static final String IDENT 		= "{http://xml.apache.org/xslt}indent-amount";
    
	public static Document createDocument(String mainTag) throws ParserConfigurationException
	{
        // Creamos documento
        DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
        DocumentBuilder db          = dbf.newDocumentBuilder();
        Document document           = db.newDocument();
	    
        // Creamos root
        Element root = document.createElement(mainTag);
        document.appendChild(root);
        
        return document;
	}
	
	public static void printDocument(Document document, OutputStream out, String encoding, 
	        boolean xmlDeclaration, boolean identation, int identNumber) throws TransformerException
	{
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, xmlDeclaration ? NO : YES);
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
        transformer.setOutputProperty(OutputKeys.INDENT, identation ? YES : NO);
        if (identation) transformer.setOutputProperty(IDENT, Integer.toString(identNumber));
        
        DOMSource source = new DOMSource(document.getDocumentElement());
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
	}

    public static void deleteEmptyNodes(Document document)
    {
        // Obtenemos root
        Element root = document.getDocumentElement();
        
        // Contamos nodos iniciales
        int init = 0;
        int end  = -1;
        while (end < init)
        {
            init = count(root);
            removeNodes(root);
            end = count(root);
        }
    }
    
    private static int count(Node node)
    {
        int num = 0;
        
        NodeList childs = node.getChildNodes();
        for (int i = 0; i<childs.getLength(); i++)
        {
            num += count(childs.item(i));
        }
        num += childs.getLength();
        
        return num;
    }
    
    private static void removeNodes(Node node) 
    {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) 
        {
            removeNodes(list.item(i));
        }
        boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
        boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
        if (emptyElement || emptyText) 
        {
            if (node.getParentNode()!=null)
            {
                node.getParentNode().removeChild(node);
            }
        }
    }    
}
