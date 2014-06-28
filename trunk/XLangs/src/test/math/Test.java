package test.math;

import java.util.Stack;

import test.math.operations.INode;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        Stack<INode> x = new Stack<INode>();

        // Parseamos una parte
        String text = "\"Var1 \\\"con\\\"\" \"Var2 otra var\" Concat >Var3 \"Var4\" Concat Symbol:PI sy:alfa Concat:3";
        System.out.println(text);
        TextParser.parse(text, x);
        
        // Seguimos parseando
        text = "Eta eta & Eta eta &S &S";
        TextParser.parse(text, x);
        
        // Seguimos parseando
        text = "Eta eta Eta eta &S:4";
        TextParser.parse(text, x);
        
        System.out.println("Elementos: " + x.size());
        for (INode node: x)
        {
            System.out.println(node.toMathString());
        }
    }
}
