/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj

%{

  public Parser   parser;
  public int      lineno;
  public int      column;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;
  }
%}

int         = [0-9]+
identifier  = [a-zA-Z][a-zA-Z0-9_]*
newline     = \n
whitespace  = [ \t\r]+
linecomment = "##".*
blockcomment= "#{"[^]*"}#"

%%

"func"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.FUNC       ; }
"call"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.CALL       ; }
"return"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.RETURN     ; }
"var"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.VAR        ; }
"if"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.IF         ; }
"else"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.ELSE       ; }
"while"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.WHILE      ; }
"print"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.PRINT      ; }
"sizeof"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.SIZEOF     ; }
"elemof"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.ELEMOF     ; }
"{"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.BEGIN      ; }
"}"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.END        ; }
"("                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RPAREN     ; }
"["                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.LBRACKET   ; }
"]"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RBRACKET   ; }
"int"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT        ; }
"bool"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL       ; }
"new"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.NEW        ; }
"<-"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.ASSIGN     ; }
"->"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.FUNCRET    ; }
"<"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
"="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
"!="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
">"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
"<="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
">="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP      ; }
"+"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP     ; }
"-"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP     ; }
"or"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP     ; }
"*"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP     ; }
"/"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP     ; }
"and"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP     ; }
";"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.SEMI       ; }
","                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.COMMA      ; }
"."                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.DOT        ; }
"true"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT   ; }
"false"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT   ; }
"@"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.PTR        ; }
{int}                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal((Object)yytext()); return Parser.IDENT      ; }
{linecomment}                       { System.out.print(yytext()); /* skip */ }
{newline}                           { System.out.print(yytext()); lineno++; column = 1; /* skip */ }
{whitespace}                        { System.out.print(yytext()); column += yytext().length(); /* skip */ }
{blockcomment}                      { System.out.print(yytext());String[] skipLines = yytext().split("\n");
                                      lineno+= skipLines.length -1; column+=2;                      /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
