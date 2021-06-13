%{
  import java.io.*;
  import java.math.*;
  import java.util.*;
%}

%token <sval> CONSTANT /* constant */
%token <obj> NUMBER  /* a number */
%token <sval> IDENTIFIER /* function call */

%type <obj> exp

%left '-' '+'
%left '/' '*'
%left NEG
%right '^'         /* exponentiation */

%%

query:
    exp { result = (BigDecimal) $1; }

exp:     NUMBER       { $$ = (BigDecimal) $1; }
       | CONSTANT     { $$ = calculator.resolveConstant($1); }
       | exp '+' exp  { $$ = calculator.add((BigDecimal) $1, (BigDecimal) $3); }
       | exp '+' exp '%' { $$ = calculator.add((BigDecimal) $1, calculator.toPercentage((BigDecimal) $3, (BigDecimal) $1)); }
       | exp '-' exp  { $$ = calculator.subtract((BigDecimal) $1, (BigDecimal) $3); }
       | exp '-' exp '%' { $$ = calculator.subtract((BigDecimal) $1, calculator.toPercentage((BigDecimal) $3, (BigDecimal) $1)); }
       | exp '*' exp  { $$ = calculator.multiply((BigDecimal) $1, (BigDecimal) $3); }
       | exp '/' exp  { $$ = calculator.divide((BigDecimal) $1, (BigDecimal) $3); }
       | '-' exp %prec NEG { $$ = calculator.multiply((BigDecimal) $2, calculator.toBigDecimal("-1")); }
       | exp '^' exp  { $$ = calculator.pow((BigDecimal) $1, (BigDecimal) $3); }
       | exp '%'      { $$ = calculator.toPercentage((BigDecimal) $1, null); }
       | '(' exp ')'  { $$ = $2; }
       | IDENTIFIER '(' exp ')' { $$ = calculator.callFunction($1, (BigDecimal) $3); }
       ;

%%
  /* a reference to the lexer object */
  public Calculator calculator;
  private APCLexer lexer;
  public BigDecimal result;

  /* interface to the lexer */
  private int yylex () {
    int yyl_return = -1;
    try {
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }

  /* error reporting */
  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }

  public BigDecimal callFunction(String functionName, BigDecimal number) {
    return number;
  }

  /* lexer is created in the constructor */
  public APCParser(Calculator calculator, Reader r) {
    this.calculator = calculator;
    lexer = new APCLexer(r, this);
  }