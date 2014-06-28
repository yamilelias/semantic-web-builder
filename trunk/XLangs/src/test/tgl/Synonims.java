package test.tgl;

import java.util.HashMap;
import java.util.Map;

public class Synonims
{
    private static final Map<String, String> SYNONIMS = new HashMap<String, String>();
    
    static
    {
        SYNONIMS.put("Canvas", "Create");
        SYNONIMS.put("L", "Line");
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
