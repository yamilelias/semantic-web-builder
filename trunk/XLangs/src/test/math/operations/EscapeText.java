package test.math.operations;

import java.util.Stack;

public class EscapeText implements INode
{
    private INode node = null;
    
    @Override
    public void init(String text)
    {
    }

    @Override
    public void execute(Stack<INode> stack)
    {
        node = stack.pop();
        stack.push(this);
    }
    
    @Override
    public String toMathString()
    {
        return "\\text{" + node.toMathString() + "}";
    }
}
