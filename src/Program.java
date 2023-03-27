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
        args = new String[] { "//Users/justinneigh//Downloads//Homework6_470//src//tests//succ_01.minc" };

        if(args.length <= 0)
            return;
        java.io.Reader r = new java.io.FileReader(args[0]);

        Compiler compiler = new Compiler(r);
        compiler.Compile();
    }
}
