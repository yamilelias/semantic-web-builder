package test.math.operations;

import java.util.Stack;

public class Symbol implements INode
{
    private String text;
    
    @Override
    public void init(String text)
    {
        this.text = text;
    }

    @Override
    public void execute(Stack<INode> stack)
    {
        stack.push(this);
    }
    
    @Override
    public String toMathString()
    {
        return "/Symbol:" + text + "/";
    }
    

}
