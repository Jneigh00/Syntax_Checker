import java.util.*;

public class Parser
{
    public static final int ENDMARKER   =  0;
    public static final int LEXERROR    =  1;
    public static final int INT         = 11;
    public static final int PTR         = 12;
    public static final int BEGIN       = 13;
    public static final int END         = 14;
    public static final int LPAREN      = 15;
    public static final int RPAREN      = 16;
    public static final int ASSIGN      = 17;
    public static final int EXPROP      = 18;
    public static final int TERMOP      = 19;
    public static final int SEMI        = 20;
    public static final int INT_LIT     = 21;
    public static final int IDENT       = 22;
    public static final int FUNCRET     = 23;
    public static final int FUNC        = 24;
    public static final int LBRACKET    = 25;
    public static final int RBRACKET    = 26;
    public static final int RELOP       = 27;
    public static final int COMMA       = 28;
    public static final int DOT         = 29;
    public static final int IF          = 30;
    public static final int NEW         = 31;
    public static final int VAR         = 32;
    public static final int CALL        = 33;
    public static final int ELSE        = 34;
    public static final int BOOL_LIT    = 35;
    public static final int BOOL        = 36;
    public static final int WHILE       = 37;
    public static final int PRINT       = 38;
    public static final int RETURN      = 39;
    public static final int ELEMOF      = 40;
    public static final int SIZEOF      = 41;

    public class Token
    {
        public int       type;
        public ParserVal attr;
        public Token(int type, ParserVal attr) {
            this.type   = type;
            this.attr   = attr;
        }
    }

    public ParserVal yylval;
    Token _token;
    Lexer _lexer;
    Compiler _compiler;
    public ParseTree.Program _parsetree;
    public String            _errormsg;
    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        _compiler  = compiler;
        _parsetree = null;
        _errormsg  = null;
        _lexer     = new Lexer(r, this);
        _token     = null;                  // _token is initially null
        Advance();                          // make _token to point the first token by calling Advance()
    }

    public void Advance() throws Exception
    {
        int token_type = _lexer.yylex();                                    // get next/first token from lexer
        if(token_type ==  0)      _token = new Token(ENDMARKER , null);     // if  0 => token is endmarker
        else if(token_type == -1) _token = new Token(LEXERROR  , yylval);   // if -1 => there is a lex error
        else                      _token = new Token(token_type, yylval);   // otherwise, set up _token
    }

    public String Match(int token_type) throws Exception
    {
        boolean match = (token_type == _token.type);
        String lexeme = "";

        if(match == false)                          // if token does not match
            throw new Exception("token mismatch");  // throw exception (indicating parsing error in this assignment)

        if(_token.type == FUNC){
            lexeme = "func";
        }
        if(_token.type == INT){
            lexeme = "int";
        }
        if(_token.type == INT_LIT){
            lexeme = _token.attr.sval;
        }
        if(_token.type == BOOL_LIT){
            if(_token.attr.sval.equals("true")){
                lexeme = "true";
            }
            else{
                lexeme = "false";
            }
        }
        if(_token.type == PTR){
            lexeme = "ptr";
        }
        if(_token.type == BEGIN){
            lexeme = "{";
        }
        if(_token.type == END){
            lexeme = "}";
        }
        if(_token.type == LPAREN){
            lexeme = "(";
        }
        if(_token.type == RPAREN){
            lexeme = ")";
        }
        if(_token.type == ASSIGN){
            lexeme = "<-";
        }
        if(_token.type == EXPROP){
            if(_token.attr.sval.equals("+")){
                lexeme = "+";
            }
            if(_token.attr.sval.equals("-")){
                lexeme = "-";
            }
            if(_token.attr.sval.equals("or")){
                lexeme = "or";
            }
        }
        if(_token.type == TERMOP){
            if(_token.attr.sval.equals("*")){
                lexeme = "*";
            }
            if(_token.attr.sval.equals("/")){
                lexeme = "/";
            }
            if(_token.attr.sval.equals("and")){
                lexeme = "and";
            }
        }
        if(_token.type == SEMI){
            lexeme = ";";
        }
        if(_token.type == IDENT){
            lexeme = _token.attr.sval;
        }
        if(_token.type == FUNCRET){
            lexeme = "->";
        }
        if(_token.type == LBRACKET){
            lexeme = "[";
        }
        if(_token.type == RBRACKET){
            lexeme = "]";
        }
        if(_token.type == RBRACKET){
            lexeme = "]";
        }
        if(_token.type == RELOP){
            if(_token.attr.sval.equals("<")){
                lexeme = "<";
            }
            if(_token.attr.sval.equals(">")){
                lexeme = ">";
            }
            if(_token.attr.sval.equals("<=")){
                lexeme = "<=";
            }
            if(_token.attr.sval.equals(">=")){
                lexeme = ">=";
            }
            if(_token.attr.sval.equals("=")){
                lexeme = "=";
            }
            if(_token.attr.sval.equals("!=")){
                lexeme = "!=";
            }
        }
        if(_token.type == COMMA){
            lexeme = ",";
        }
        if(_token.type == DOT){
            lexeme = ".";
        }
        if(_token.type == IF){
            lexeme = "if";
        }
        if(_token.type == NEW){
            lexeme = "new";
        }
        if(_token.type == VAR){
            lexeme = "var";
        }
        if(_token.type == CALL){
            lexeme = "call";
        }
        if(_token.type == ELSE){
            lexeme = "else";
        }
        if(_token.type == BOOL){
            lexeme = "bool";
        }
        if(_token.type == WHILE){
            lexeme = "while";
        }
        if(_token.type == PRINT){
            lexeme = "print";
        }
        if(_token.type == RETURN){
            lexeme = "return";
        }
        if(_token.type == ELEMOF){
            lexeme = "elemof";
        }
        if(_token.type == SIZEOF){
            lexeme = "sizeof";
        }

        if(_token.type != ENDMARKER)    // if token is not endmarker,
            Advance();                  // make token point next token in input by calling Advance()


        return lexeme;
    }

    public int yyparse() throws Exception
    {
        try
        {
            _parsetree = program();
            return 0;
        }
        catch(Exception e)
        {
            _errormsg = e.getMessage();
            return -1;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //      program -> decl_list
    //    decl_list -> decl_list'
    //   decl_list' -> fun_decl decl_list'  |  eps
    //     fun_decl -> FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls stmt_list END
    //    prim_type -> INT
    //       params -> eps
    //  local_decls -> local_decls'
    // local_decls' -> eps
    //    stmt_list -> stmt_list'
    //   stmt_list' -> eps
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public ParseTree.Program program() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                List<ParseTree.FuncDecl> funcsTest = decl_list();
                ParseTree.Program v2 = new ParseTree.Program(funcsTest);
                return v2;
            case ENDMARKER:
                List<ParseTree.FuncDecl> funcs = decl_list();
                String v1 = Match(ENDMARKER);
                return new ParseTree.Program(funcs);
        }
        throw new Exception("error");
    }


    public List<ParseTree.FuncDecl> decl_list() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                return decl_list_();
            case ENDMARKER:
                String v1 = Match(ENDMARKER);
                return decl_list_();
        }
        throw new Exception("error");
    }
    public List<ParseTree.FuncDecl> decl_list_() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                ParseTree.FuncDecl       v1 = fun_decl  ();
                List<ParseTree.FuncDecl> v2 = decl_list_();
                v2.add(0, v1);
                return v2;
            case ENDMARKER:
                String v3 = Match(ENDMARKER);
                return new ArrayList<ParseTree.FuncDecl>();
        }
        throw new Exception("error");
    }
    public ParseTree.FuncDecl fun_decl() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                String                    v01 = Match(FUNC)   ;
                String                    v02 = Match(IDENT)  ;
                String                    v03 = Match(LPAREN) ;
                List<ParseTree.Param>     v04 = params()      ;
                String                    v05 = Match(RPAREN) ;
                String                    v06 = Match(FUNCRET);
                ParseTree.PrimType        v07 = prim_type()   ;
                String                    v08 = Match(BEGIN)  ;
                List<ParseTree.LocalDecl> v09 = local_decls() ;
                List<ParseTree.Stmt>      v10 = stmt_list()   ;
                String                    v11 = Match(END)    ;
                return new ParseTree.FuncDecl(v02,v07,v04,v09,v10);
        }
        throw new Exception("error");
    }
    public ParseTree.PrimType prim_type() throws Exception
    {
        switch(_token.type)
        {
            case INT:
            {
                String v1 = Match(INT);
                return new ParseTree.PrimTypeInt();
            }
            case BOOL:
            {
                String v2 = Match(BOOL);
                return new ParseTree.PrimTypeBool();
            }
        }
        throw new Exception("error");
    }
    public List<ParseTree.Param> params() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return new ArrayList<ParseTree.Param>();
            case INT:
            {
                List<ParseTree.Param> v1 = param_list();
                return v1;
            }
            case BOOL:
            {
                List<ParseTree.Param> v2 = param_list();
                return v2;
            }
        }
        throw new Exception("error");
    }
    public List<ParseTree.LocalDecl> local_decls() throws Exception
    {
        switch(_token.type)
        {
            case END:
                return local_decls_();
            case IDENT:
                return local_decls_();
            case BEGIN:
                return local_decls_();
            case WHILE:
                return local_decls_();
            case IF:
                return local_decls_();
            case VAR:
                return local_decls_();
            case PRINT:
                return local_decls_();
            case RETURN:
                return local_decls_();
        }
        throw new Exception("error");
    }
    public List<ParseTree.LocalDecl> local_decls_() throws Exception
    {
        switch(_token.type)
        {
            case END:
                return new ArrayList<ParseTree.LocalDecl>();
            case IDENT:
                return new ArrayList<ParseTree.LocalDecl>();
            case BEGIN:
                return new ArrayList<ParseTree.LocalDecl>();
            case WHILE:
                return new ArrayList<ParseTree.LocalDecl>();
            case IF:
                return new ArrayList<ParseTree.LocalDecl>();
            case PRINT:
                return new ArrayList<ParseTree.LocalDecl>();
            case RETURN:
                return new ArrayList<ParseTree.LocalDecl>();
            case VAR:
                ParseTree.LocalDecl v1 = local_decl();
                List<ParseTree.LocalDecl> v2 = local_decls_();
                v2.add(0, v1);
                return v2;
        }
        throw new Exception("error");
    }
    public List<ParseTree.Stmt> stmt_list() throws Exception
    {
        switch(_token.type)
        {
            case END:
            case IDENT:
            case BEGIN:
            case WHILE:
            case IF:
            case PRINT:
            case RETURN:
                return stmt_list_();

        }
        throw new Exception("error");
    }
    public List<ParseTree.Stmt> stmt_list_() throws Exception
    {
        switch(_token.type)
        {
            case END:
                return new ArrayList<ParseTree.Stmt>();
            case IDENT:
                ParseTree.Stmt v1 = stmt();
                List<ParseTree.Stmt> v2 = stmt_list_();
                v2.add(0, v1);
                return v2;
            case BEGIN:
                ParseTree.Stmt v3 = stmt();
                List<ParseTree.Stmt> v4 = stmt_list_();
                v4.add(0, v3);
                return v4;
            case WHILE:
                ParseTree.Stmt v5 = stmt();
                List<ParseTree.Stmt> v6 = stmt_list_();
                v6.add(0, v5);
                return v6;
            case IF:
                ParseTree.Stmt v7 = stmt();
                List<ParseTree.Stmt> v8 = stmt_list_();
                v8.add(0, v7);
                return v8;
            case PRINT:
                ParseTree.Stmt v9 = stmt();
                List<ParseTree.Stmt> v10 = stmt_list_();
                v10.add(0, v9);
                return v10;
            case RETURN:
                ParseTree.Stmt v11 = stmt();
                List<ParseTree.Stmt> v12 = stmt_list_();
                v12.add(0, v11);
                return v12;
        }
        throw new Exception("error");
    }

    public ParseTree.StmtWhile while_stmt() throws Exception{

        switch (_token.type)
        {
            case WHILE:
                String v1 = Match(WHILE);
                String v2 = Match(LPAREN);
                ParseTree.Expr v3  = expr();
                String v4 = Match(RPAREN);
                ParseTree.Stmt v5 = stmt();
                return new ParseTree.StmtWhile(v3, v5);
        }
        throw new Exception("error");
    }

    public ParseTree.TypeSpec_ type_specP() throws Exception{

        switch (_token.type){
            case IDENT:
            {
                return new ParseTree.TypeSpec_Value();
            }
            case LBRACKET:
            {
                String v1 = Match(LBRACKET);
                String v2 = Match(RBRACKET);
            }
        }

        return null;
    }

    public ParseTree.TypeSpec type_spec() throws  Exception{

        switch (_token.type){
            case INT:
            case BOOL: {
                ParseTree.PrimType v1 = prim_type();
                ParseTree.TypeSpec_ v2 = type_specP();
                return new ParseTree.TypeSpec(v1, v2);
            }
        }
        throw new Exception("error");
    }

    public ParseTree.Term_ termP() throws  Exception{
        switch (_token.type){
            case RBRACKET:
            case SEMI:
            case RELOP:
            case EXPROP:
            case COMMA:
            case RPAREN:
                return null;
            case TERMOP:
                String v1 = Match(TERMOP);
                ParseTree.Factor v2 = factor();
                ParseTree.Term_ v3 = termP();
                return new ParseTree.Term_(v1, v2, v3);
        }
        return null;
    }

    public ParseTree.Term term() throws  Exception{
        switch (_token.type){
            case BOOL_LIT:
            case ELEMOF:
            case IDENT:
            case INT_LIT:
            case LPAREN:
            case NEW:
            case SIZEOF:
            case CALL: {
                ParseTree.Factor v1 = factor();
                ParseTree.Term_ v2 = termP();
                return new ParseTree.Term(v1, v2);
            }
        }
        throw new Exception("error");
    }

    public ParseTree.Stmt stmt() throws  Exception{
        switch (_token.type){
            case IDENT:
                ParseTree.StmtAssign v1 = assign_stmt();
                return v1;
            case BEGIN:
                ParseTree.StmtCompound v2 = compound_stmt();
                return v2;
            case WHILE:
                ParseTree.StmtWhile v3 = while_stmt();
                return v3;
            case IF:
                ParseTree.StmtIf v4 = if_stmt();
                return v4;
            case PRINT:
                ParseTree.StmtPrint v5 = print_stmt();
                return v5;
            case RETURN:
                ParseTree.StmtReturn v6 = return_stmt();
                return v6;
        }
        throw new Exception("error");
    }

    public ParseTree.StmtReturn return_stmt() throws  Exception{
        switch (_token.type){
            case RETURN:
                String v1 = Match(RETURN);
                ParseTree.Expr v2 = expr();
                String v3 = Match(SEMI);
                return new ParseTree.StmtReturn(v2);
        }
        throw new Exception("error");
    }

    public ParseTree.StmtPrint print_stmt() throws Exception{
        switch (_token.type){
            case PRINT:
                String v1 = Match(PRINT);
                ParseTree.Expr v2 = expr();
                String v3 = Match(SEMI);
                return new ParseTree.StmtPrint(v2);
        }
        throw new Exception("error");
    }

    public  List<ParseTree.Param> param_listP() throws Exception{
        switch (_token.type){
            case RPAREN:
            {
                return new ArrayList<ParseTree.Param>();
            }
            case COMMA:
                String v1 = Match(COMMA);
                ParseTree.Param v2 = param();
                List<ParseTree.Param> v3 = param_listP();
                v3.add(0,v2);
                return v3;
        }
        return null;
    }

    public  List<ParseTree.Param> param_list() throws Exception{
        switch (_token.type){
            case BOOL:
            case INT:
                ParseTree.Param v1 = param();
                List<ParseTree.Param> v2 = param_listP();
                v2.add(0, v1);
                return v2;
        }
        return null;
    }

    public ParseTree.Param param() throws Exception{
        switch (_token.type){
            case INT:
                ParseTree.TypeSpec v1 = type_spec();
                String v2 = Match(IDENT);
                return new ParseTree.Param(v2, v1);
        }

        return null;
    }

    public ParseTree.LocalDecl local_decl() throws Exception {
        switch (_token.type){
            case IDENT:
            case BEGIN:
            case WHILE:
            case END:
            case IF:
            case VAR:
                String v1 = Match(VAR);
                ParseTree.TypeSpec v2 = type_spec();
                String v3 = Match(IDENT);
                String v4 = Match(SEMI);
                return new ParseTree.LocalDecl(v3, v2);
            case PRINT:
            case RETURN:
        }
        return null;
    }

    public ParseTree.StmtIf if_stmt() throws Exception{
        switch (_token.type){
            case IF:
                String v1 = Match(IF);
                String v2 = Match(LPAREN);
                ParseTree.Expr v3 = expr();
                String v4 = Match(RPAREN);
                ParseTree.Stmt v5 = stmt();
                String v6 = Match(ELSE);
                ParseTree.Stmt v7 = stmt();
                return new ParseTree.StmtIf(v3, v5, v7);
        }
        return null;
    }

    public ParseTree.Factor factor() throws Exception{
        switch (_token.type){
            case BOOL_LIT:
                String v1 = Match(BOOL_LIT);
                return new ParseTree.FactorBoolLit((Boolean.parseBoolean(v1)));
            case CALL:
                String v2 = Match(CALL);
                String v3 = Match(IDENT);
                String v4 = Match(LPAREN);
                List<ParseTree.Arg> v5 = args();
                String v6 = Match(RPAREN);
                return new ParseTree.FactorCall(v3, v5);
            case ELEMOF:
                String v7 = Match(ELEMOF);
                String v8 = Match(IDENT);
                String v9 = Match(LBRACKET);
                ParseTree.Expr v10 = expr();
                String v11 = Match(RBRACKET);
                return new ParseTree.FactorElemof(v8, v10);
            case IDENT:
                String v12 = Match(IDENT);
                return new ParseTree.FactorIdent(v12);
            case LPAREN:
                String v13 = Match(LPAREN);
                ParseTree.Expr v14 = expr();
                String v15 = Match(RPAREN);
                return new ParseTree.FactorParen(v14);
            case INT_LIT:
                String v16 = Match(INT_LIT);
                int v50 = Integer.parseInt(v16);
                return new ParseTree.FactorIntLit(Integer.parseInt(v16));
            case NEW:
                String v17 = Match(NEW);
                ParseTree.PrimType v18 = prim_type();
                String v19 = Match(LBRACKET);
                ParseTree.Expr v20 = expr();
                String v21 = Match(RBRACKET);
                return new ParseTree.FactorNew(v18, v20);
            case SIZEOF:
                String v22 = Match(SIZEOF);
                String v23 = Match(IDENT);
                return new ParseTree.FactorSizeof(v23);
        }
        return null;
    }

    public ParseTree.Expr_ exprP() throws Exception{
        switch (_token.type){
            case RPAREN:
            {
                return null;
            }
            case COMMA:
            {
                return null;
            }
            case EXPROP:
                String v1 = Match(EXPROP);
                ParseTree.Term v2 = term();
                return exprP();
            case RELOP:
                String v3 = Match(RELOP);
                ParseTree.Term v4 = term();
                return exprP();
            case SEMI:
            {
                return null;
            }
            case RBRACKET:
            {
                return null;
            }
        }
        return null;
    }

    public ParseTree.Expr expr() throws Exception {
        switch (_token.type){
            case CALL:
            case IDENT:
            case ELEMOF:
            case INT_LIT:
            case LPAREN:
            case NEW:
            case SIZEOF:
            case BOOL_LIT:
                ParseTree.Term v1 = term();
                ParseTree.Expr_ v2 = exprP();
                return new ParseTree.Expr(v1, v2);
        }
        return null;
    }

    public ParseTree.StmtCompound compound_stmt() throws Exception{
        switch (_token.type){
            case BEGIN:
                String v1 = Match(BEGIN);
                List<ParseTree.LocalDecl> v2 = local_decls();
                List<ParseTree.Stmt> v3 = stmt_list();
                String v4 = Match(END);
                return new ParseTree.StmtCompound(v2, v3);
        }
        return null;
    }

    public ParseTree.StmtAssign assign_stmt() throws Exception {
        switch (_token.type){
            case IDENT:
                String v1 = Match(IDENT);
                String v2 = Match(ASSIGN);
                ParseTree.Expr v3 = expr();
                String v4 = Match(SEMI);
                return new ParseTree.StmtAssign(v1, v3);
        }
        return null;
    }

    public List<ParseTree.Arg> args() throws Exception {
        switch (_token.type){
            case CALL:
            case ELEMOF:
            case IDENT:
            case INT_LIT:
            case LPAREN:
            case NEW:
            case SIZEOF:
            case BOOL_LIT:
                return arg_list();
            case RPAREN:
            {
                return new ArrayList<ParseTree.Arg>();
            }

        }

        return null;
    }

    public List<ParseTree.Arg> args_listP() throws Exception {
        switch (_token.type){
            case RPAREN:
            {
                return new ArrayList<ParseTree.Arg>();
            }
            case COMMA:
                String v1 = Match(COMMA);
                ParseTree.Expr v2 = expr();
                List<ParseTree.Arg> v3 = args_listP();
                return v3;
        }
        return null;
    }

    public List<ParseTree.Arg> arg_list() throws Exception{
        switch (_token.type){
            case CALL:
            case ELEMOF:
            case IDENT:
            case INT_LIT:
            case LPAREN:
            case NEW:
            case SIZEOF:
            case BOOL_LIT:
                ParseTree.Expr v1 = expr();
                List<ParseTree.Arg> v2 = args_listP();
                v2.add(0, new ParseTree.Arg(v1));
                return v2;
        }
        return null;
    }


}
