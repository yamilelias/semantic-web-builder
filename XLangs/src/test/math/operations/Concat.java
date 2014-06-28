package test.math.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Concat implements INode
{
    private int num = 2;
    private List<INode> nodes = new ArrayList<INode>();
    
    @Override
    public void init(String text)
    {
        if (text != null && text.length()>0)
        {
            num = Integer.parseInt(text);
            if (num <= 1) throw new IllegalArgumentException("Número deber ser > 1");
        }
    }

    @Override
    public void execute(Stack<INode> stack)
    {
        if (stack.size()<num) throw new IllegalArgumentException("Número insuficiente de elementos en Stack: " + stack.size() + ". Debería haber al menos: " + num);
        
        for (int i = 1; i<=num; i++)
        {
            nodes.add(stack.pop());
        }
        stack.add(this);
    }

    @Override
    public String toMathString()
    {
        StringBuffer result = new StringBuffer();
        for (int i = nodes.size()-1; i>=0; i--)
        {
            INode n = nodes.get(i);
            result.append(n.toMathString());
        }
        
        return result.toString();
    }

}
