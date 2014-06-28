package test.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import test.math.operations.INode;

public class TextParser
{
    private static final String PREFIX = INode.class.getPackage().getName() + '.';
    
    public static void parse(String text, Stack<INode> x) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String[] parts = text.split("\\n");
        List<String> tokens = new ArrayList<String>();
        for (String part: parts) tokens.addAll(parseStringTokens(part));
        
        for (int i = 0; i<tokens.size(); i++)
        {
            String token = tokens.get(i);
            
            System.out.println("Parsing: " + token);
            if (SynonimExpressions.contains(token))
            {
                token = SynonimExpressions.get(token);
                System.out.println(" --> " + token);
            }
            
            if (token.startsWith("\""))
            {
                if (!token.endsWith("\"")) throw new IllegalArgumentException("Token no válido: " + token);
                executeOperation(x, "Text", token.substring(1, token.length()-1), i);
            }
            else if (token.startsWith(">") && token.length()>1)
            {
                executeOperation(x, "Text", token.substring(1), i);
            }
            else
            {
                int aux = token.indexOf(':');
                if (aux == -1) executeOperation(x, token, null, i);
                else         executeOperation(x, token.substring(0, aux), token.substring(aux+1), i);
            }
        }
    }

    private static void executeOperation(Stack<INode> stack, String operation, String params, int numOp)
    {
        int num = 1;
        int numI = operation.indexOf("#"); 
        if (numI != -1)
        {
            num = Integer.parseInt(operation.substring(numI+1));
            operation = operation.substring(0, numI);
        }            
        
        if (Synonims.contains(operation)) operation = Synonims.get(operation);
        
        for (int i = 1; i<=num; i++)
        {
            INode node = null;
            try
            {
                node = (INode) Test.class.getClassLoader().loadClass(PREFIX + operation).newInstance();
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException((numOp+1) + ": Operación no válida: " + operation);
            }
            node.init(params);
            node.execute(stack);
        }
    }
    
    private static List<String> parseStringTokens(String text)
    {
        List<String> result = new ArrayList<String>();
        
        String token = "";
        for (int i = 0; i<text.length(); i++)
        {
            char curChar = text.charAt(i);
            
            if (curChar == ' ')
            {
                if (token.startsWith("\""))
                {
                    token += curChar;
                }
                else
                {
                    result.add(token);
                    token = "";
                }
            }
            else if (curChar == '"')
            {
                if (token.length()==0) token += curChar;
                else
                {
                    if (token.endsWith("\\")) token = token.substring(0, token.length()-1) + curChar;
                    else if (token.startsWith("\""))
                    {
                        token += curChar;
                        result.add(token);
                        token = "";
                    }
                }
            }
            else
            {
                token += curChar;
            }
        }
        if (token.length()>0) result.add(token);
        
        // Eliminamos nulos
        List<String> resultParsed = new ArrayList<String>();
        for (String aux: result)
        {
            aux = aux.trim();
            if (aux.length()>0) resultParsed.add(aux);
        }        
        
        // Retorno resultado parseado
        return resultParsed;
    }
}
