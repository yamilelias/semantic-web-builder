package test.math.operations;

import java.util.Stack;

public interface INode
{
    public void init(String text);
    public void execute(Stack<INode> stack);
    public String toMathString();
}
