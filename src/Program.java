public class Program
{
    public static void main(String[] args) throws Exception
    {
        //java.io.Reader r = new java.io.StringReader
        //("func main ()->int\n"
        //+"{\n"
        //+"}\n"
        //);
        //
        args = new String[] { "src/tests/fail_15c.minc" };

        if(args.length <= 0)
            return;
        java.io.Reader r = new java.io.FileReader(args[0]);

        Compiler compiler = new Compiler(r);
        compiler.Compile();
    }
}
