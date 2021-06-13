package org.seniorlaguna.calculator.apc;
import java.math.*;

%%
%class APCLexer
%unicode
%column
%standalone
%byaccj

%{
    private APCParser parser;

    public APCLexer(java.io.Reader r, APCParser parser) {
        this(r);
        this.parser = parser;
    }

%}

INTEGER = 0 | [1-9][0-9]*
FLOAT = {INTEGER} "." | {INTEGER} "." 0* {INTEGER}
IDENTIFIER = [a-zA-Z]+[0-9]*

%%

"e" | "π"  { parser.yylval = new APCParserVal(yytext());
             return APCParser.CONSTANT; }

{INTEGER} | {FLOAT} { parser.yylval = new APCParserVal(parser.calculator.toBigDecimal(yytext()));
         return APCParser.NUMBER; }

{IDENTIFIER} | "!" | "√" { parser.yylval = new APCParserVal(yytext());
                return APCParser.IDENTIFIER; }

"+" | "-" | "*" | "/" | "^" | "(" | ")" | "," | "%" { return (int) yycharat(0); }