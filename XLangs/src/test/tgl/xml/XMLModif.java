package test.tgl.xml;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLModif
{
    private static final Logger log = Logger.getLogger(XMLModif.class.getCanonicalName());
    
    private final Element root;
    private final Document document;
    
    private Element lastElement = null;
    private String valueApply = null;
    private String valueApplyAdd = null;
    private boolean navigation = false;
    
    public XMLModif(Document document)
    {
        this.document = document;
        this.root = document.getDocumentElement();
    }
    
    public void apply(String exp)
    {
        log.info("Apply: " + exp);
        
        // Init valores que dependen de exp
        valueApply = null;
        valueApplyAdd = null;
        navigation = false;

        // Miramos si es navegación forzada
        if (exp.startsWith(">>"))
        {
            exp = exp.substring(2);
            navigation = true;
        }
        
        // Miramos si la expresión es += o =
        int indexPlusEqual = exp.indexOf("+=");
        if (indexPlusEqual != -1)
        {
            valueApplyAdd = exp.substring(indexPlusEqual+2);
            exp = exp.substring(0,indexPlusEqual);
        }
        else
        {
            int indexEqual = exp.indexOf("=");
            if (indexEqual != -1)
            {
                valueApply = exp.substring(indexEqual+1);
                exp = exp.substring(0, indexEqual);
            }
        }
        
        // Init root si es necesario
        if (exp.startsWith("/"))
        {
            navigation = true;
            lastElement = root;
            exp = exp.substring(1);
        }
        
        // Init último elemento
        if (lastElement == null) lastElement = root;

        // Navigate (conservando last si era el inicial)
        Element auxLast = lastElement;
        navigate(exp);
        if (!navigation) lastElement = auxLast; // Si no hay navegación se restaura
    }

    private void navigate(String exp)
    {
        // Navegando
        if (log.isLoggable(Level.FINE)) log.log(Level.FINE, "Navegando: " + exp);
        
        // Miramos si es el último o no
        int index = exp.indexOf('/');
        
        if (index == -1) // último
        {
            // Creamos 
            Element e = createOrRetrieve(exp);
            
            // Insertamos valor o navegamos
            if (e != null)
            {
                lastElement = e;
                if (valueApplyAdd!=null) e.appendChild(document.createTextNode(valueApplyAdd));
                else if (valueApply!=null) e.setTextContent(valueApply);
            }
            else
            {
                if (valueApply != null) lastElement.setAttribute(exp.substring(1), valueApply);
                else throw new IllegalArgumentException("Expresión atributo no puede ser nula");
            }
        }
        else // No último
        {
            // Creamos ruta
            String eStr = exp.substring(0,index);
            Element e = createOrRetrieve(eStr);
            if (e != null) lastElement = e;
            exp = exp.substring(index+1);
            navigate(exp);
        }
    }

    private Element createOrRetrieve(String exp)
    {
        // Validamos que sea ir atrás
        if (exp.equals(".."))
        {
            Node result = (Node)lastElement.getParentNode();
            if (!(result instanceof Element))
            {
                log.log(Level.WARNING, "No se ha encontrado un parent... retornamos root");
                result = root;
            }
            return (Element) result;
        }
        
        // Validamos que no sea atributo final
        if (exp.startsWith("@")) return null;

        // Creamos o obtenemos
        String modif = "uc"; // UNIQUE OR CREATION (UC), + (ADD), L (LAST), NUM (NUMBER)

        int i1 = exp.indexOf('[');
        int i2 = exp.indexOf(']');
        if (i1!=-1)
        {
            if (i2 == -1 || i2<i1) throw new IllegalArgumentException("Expresión no válida: " + exp);
            modif = exp.substring(i1+1, i2);
            exp = exp.substring(0, i1);
        }
        
        // Obtenemos elemento dependiendo de modif
        if (modif.equalsIgnoreCase("uc"))
        {
            return getUniqueOrCreate(exp);
        }
        else if (modif.equalsIgnoreCase("add") || modif.equals("+"))
        {
            return getAdd(exp);
        }
        else if (modif.equalsIgnoreCase("add order") || modif.equals("+!"))
        {
            return getAddOrder(exp);
        }
        else if (modif.equalsIgnoreCase("l") || modif.equalsIgnoreCase("last"))
        {
            return getLast(exp);
        }
        else if (isNumber(modif))
        {
            return getArrayPosition(exp, modif);
        }
        else
        {
            throw new IllegalArgumentException("Expresión no válida: " + exp + " -> " + modif);
        }
    }

    private boolean isNumber(String modif)
    {
        try
        {
            Integer.parseInt(modif);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private Element getUniqueOrCreate(String exp)
    {
        Element result = null;
        NodeList nodes = lastElement.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                Element e = (Element) n;
                if (e.getTagName().equals(exp))
                {
                    if (result != null)
                    {
                        throw new IllegalArgumentException("Existe más de un nodo: " + exp);
                    }
                    else
                    {
                        result = e;
                    }
                }
            }
        }
        
        if (result == null)
        {
            result = document.createElement(exp);
            lastElement.appendChild(result);
        }
        
        return result;
    }

    private Element getAdd(String exp)
    {
        Element e = document.createElement(exp);
        lastElement.appendChild(e);
        return e;
    }

    private Element getAddOrder(String exp)
    {
        boolean lastIsExp = false;
        Node insertBeforeNode = null;
        
        NodeList nodes = lastElement.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++)
        {
            // Obtenemos nodo
            Node n = nodes.item(i);
            
            // Miramos el nombre
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(exp))
            {
                lastIsExp = true;
                insertBeforeNode = null;
            }
            else
            {
                if (lastIsExp) insertBeforeNode = n;
                lastIsExp = false;
            }
        }
        
        // Creamos e insertamos en el lugar adecuado
        Element e = document.createElement(exp);
        
        // Insertamos en el lugar adecuado
        if (insertBeforeNode == null)   lastElement.appendChild(e);
        else                            lastElement.insertBefore(e, insertBeforeNode);
        
        // Retornamos
        return e;
    }
    
    private Element getLast(String exp)
    {
        Element result = null;
        NodeList nodes = lastElement.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                Element e = (Element) n;
                if (e.getTagName().equals(exp)) result = e;
            }
        }
        
        // Si resultado nulo -> error!! No se ha encontrado
        if (result == null) throw new IllegalArgumentException("Nodo no encontrado: " + exp);
        return result;
    }

    private Element getArrayPosition(String exp, String modif)
    {
        int index = Integer.parseInt(modif);
        
        NodeList nodes = lastElement.getChildNodes();
        for (int i = 0; i<nodes.getLength(); i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                Element e = (Element) n;
                if (e.getTagName().equals(exp))
                {
                    index--;
                    if (index == 0) return e;
                }
            }
        }
        
        throw new IllegalArgumentException("Nodo no encontrado: " + exp);
    }

}
