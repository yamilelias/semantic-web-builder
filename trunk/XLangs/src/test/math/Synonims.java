package test.math;

import java.util.HashMap;
import java.util.Map;

public class Synonims
{
    private static final Map<String, String> SYNONIMS = new HashMap<String, String>();
    
    static
    {
        SYNONIMS.put("sy", "Symbol");
        SYNONIMS.put("&", "Concat");
        SYNONIMS.put("&S", "ConcatWithSpace");
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
