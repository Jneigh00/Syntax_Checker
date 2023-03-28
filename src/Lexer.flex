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
%column
%line

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

"func"                              { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.FUNC       ; }
"call"                              { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.CALL       ; }
"return"                            { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RETURN     ; }
"var"                               { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.VAR        ; }
"if"                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.IF         ; }
"else"                              { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.ELSE       ; }
"while"                             { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.WHILE      ; }
"print"                             { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.PRINT      ; }
"sizeof"                            { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.SIZEOF     ; }
"elemof"                            { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.ELEMOF     ; }
"{"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.BEGIN      ; }
"}"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.END        ; }
"("                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RPAREN     ; }
"["                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.LBRACKET   ; }
"]"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RBRACKET   ; }
"int"                               { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.INT        ; }
"bool"                              { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.BOOL       ; }
"new"                               { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.NEW        ; }
"<-"                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.ASSIGN     ; }
"->"                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.FUNCRET    ; }
"<"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
"="                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
"!="                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
">"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
"<="                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
">="                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.RELOP      ; }
"+"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.EXPROP     ; }
"-"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.EXPROP     ; }
"or"                                { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.EXPROP     ; }
"*"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.TERMOP     ; }
"/"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.TERMOP     ; }
"and"                               { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.TERMOP     ; }
";"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.SEMI       ; }
","                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.COMMA      ; }
"."                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.DOT        ; }
"true"                              { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.BOOL_LIT   ; }
"false"                             { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.BOOL_LIT   ; }
"@"                                 { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.PTR        ; }
{int}                               { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.INT_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal((String)yytext()); column=yycolumn+1; return Parser.IDENT      ; }
{linecomment}                       { /* skip */ }
{newline}                           { lineno++; column = 1; /* skip */ }
{whitespace}                        { column += column += yytext().length(); /* skip */ }
{blockcomment}                      {
                                      String[] skipLines = yytext().split("\n");
                                       lineno+= skipLines.length -1;                /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
