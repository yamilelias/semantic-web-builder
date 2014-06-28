package test.tgl;

public class TestParser
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        String ops = "Create 100,200\nGoto 10,0\nRotate 30\nLine 10,0 20,100\n";
        System.out.println(ops);
        String result = Parser.parse(ops);
        System.out.println("***** Result ******");
        System.out.println(result);
        
        System.out.println("Fi");
    }
}
