package test.math;

import java.util.HashMap;
import java.util.Map;

public class SynonimExpressions
{
    private static final Map<String, String> SYNONIMS = new HashMap<String, String>();
    
    static
    {
        SYNONIMS.put("eta", "Symbol:eta");
        SYNONIMS.put("Eta", "Symbol:Eta");
    }
    
    public static String get(String op)
    {
        return SYNONIMS.get(op);
    }
    public static boolean contains(String op)
    {
        return SYNONIMS.containsKey(op);
    }
    
}
