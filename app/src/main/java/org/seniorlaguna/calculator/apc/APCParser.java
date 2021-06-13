//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package org.seniorlaguna.calculator.apc;



//#line 2 "APCParser.y"
  import java.io.*;
  import java.math.*;
  import java.util.*;
//#line 21 "APCParser.java"




public class APCParser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class APCParserVal is defined in APCParserVal.java


String   yytext;//user variable to return contextual strings
APCParserVal yyval; //used to return semantic vals from action routines
APCParserVal yylval;//the 'lval' (result) I got from yylex()
APCParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new APCParserVal[YYSTACKSIZE];
  yyval=new APCParserVal();
  yylval=new APCParserVal();
  valptr=-1;
}
void val_push(APCParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
APCParserVal val_pop()
{
  if (valptr<0)
    return new APCParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
APCParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new APCParserVal();
  return valstk[ptr];
}
final APCParserVal dup_yyval(APCParserVal val)
{
  APCParserVal dup = new APCParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short CONSTANT=257;
public final static short NUMBER=258;
public final static short IDENTIFIER=259;
public final static short NEG=260;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yylen[] = {                            2,
    1,    1,    1,    3,    4,    3,    4,    3,    3,    2,
    3,    2,    3,    4,
};
final static short yydefred[] = {                         0,
    3,    2,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   12,    0,   13,    0,    0,
    0,    0,    0,   14,    7,    5,
};
final static short yydgoto[] = {                          6,
    7,
};
final static short yysindex[] = {                       -40,
    0,    0,  -11,  -40,  -40,    0,  -21,  -40,  -36,  -35,
  -40,  -40,  -40,  -40,  -40,    0,  -28,    0,  -19,  -17,
  -36,  -36,  -36,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    4,    0,    3,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   11,   40,
   27,   37,   47,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   91,
};
final static int YYTABLESIZE=219;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          5,
   16,   16,   10,    1,    4,   18,   14,   12,   16,   11,
    6,   13,   24,   14,   12,   16,   11,   25,   13,   26,
   14,   12,   14,   11,   14,   13,    9,   13,    8,   13,
    0,    0,    0,    0,    0,    0,    8,    0,    0,    4,
    0,    0,    0,   10,   10,   10,   11,   10,    0,   10,
    0,    6,    0,    6,    0,    6,    0,   15,   15,    0,
    0,    0,    0,    0,    0,   15,    0,    9,    9,    9,
    0,    9,   15,    9,   15,    0,   15,    8,    8,    8,
    4,    8,    4,    8,    4,    0,    0,   11,   11,   11,
    0,   11,    0,   11,    9,   10,    0,    0,   17,    0,
    0,   19,   20,   21,   22,   23,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    2,    3,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   37,   37,    0,    0,   45,   41,   42,   43,   37,   45,
    0,   47,   41,   42,   43,   37,   45,   37,   47,   37,
   42,   43,   42,   45,   42,   47,    0,   47,   40,   47,
   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,   -1,    0,
   -1,   -1,   -1,   41,   42,   43,    0,   45,   -1,   47,
   -1,   41,   -1,   43,   -1,   45,   -1,   94,   94,   -1,
   -1,   -1,   -1,   -1,   -1,   94,   -1,   41,   42,   43,
   -1,   45,   94,   47,   94,   -1,   94,   41,   42,   43,
   41,   45,   43,   47,   45,   -1,   -1,   41,   42,   43,
   -1,   45,   -1,   47,    4,    5,   -1,   -1,    8,   -1,
   -1,   11,   12,   13,   14,   15,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=260;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",null,
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'^'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"CONSTANT","NUMBER","IDENTIFIER","NEG",
};
final static String yyrule[] = {
"$accept : query",
"query : exp",
"exp : NUMBER",
"exp : CONSTANT",
"exp : exp '+' exp",
"exp : exp '+' exp '%'",
"exp : exp '-' exp",
"exp : exp '-' exp '%'",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : '-' exp",
"exp : exp '^' exp",
"exp : exp '%'",
"exp : '(' exp ')'",
"exp : IDENTIFIER '(' exp ')'",
};

//#line 39 "APCParser.y"
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
//#line 252 "APCParser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 21 "APCParser.y"
{ result = (BigDecimal) val_peek(0).obj; }
break;
case 2:
//#line 23 "APCParser.y"
{ yyval.obj = (BigDecimal) val_peek(0).obj; }
break;
case 3:
//#line 24 "APCParser.y"
{ yyval.obj = calculator.resolveConstant(val_peek(0).sval); }
break;
case 4:
//#line 25 "APCParser.y"
{ yyval.obj = calculator.add((BigDecimal) val_peek(2).obj, (BigDecimal) val_peek(0).obj); }
break;
case 5:
//#line 26 "APCParser.y"
{ yyval.obj = calculator.add((BigDecimal) val_peek(3).obj, calculator.toPercentage((BigDecimal) val_peek(1).obj, (BigDecimal) val_peek(3).obj)); }
break;
case 6:
//#line 27 "APCParser.y"
{ yyval.obj = calculator.subtract((BigDecimal) val_peek(2).obj, (BigDecimal) val_peek(0).obj); }
break;
case 7:
//#line 28 "APCParser.y"
{ yyval.obj = calculator.subtract((BigDecimal) val_peek(3).obj, calculator.toPercentage((BigDecimal) val_peek(1).obj, (BigDecimal) val_peek(3).obj)); }
break;
case 8:
//#line 29 "APCParser.y"
{ yyval.obj = calculator.multiply((BigDecimal) val_peek(2).obj, (BigDecimal) val_peek(0).obj); }
break;
case 9:
//#line 30 "APCParser.y"
{ yyval.obj = calculator.divide((BigDecimal) val_peek(2).obj, (BigDecimal) val_peek(0).obj); }
break;
case 10:
//#line 31 "APCParser.y"
{ yyval.obj = calculator.multiply((BigDecimal) val_peek(0).obj, calculator.toBigDecimal("-1")); }
break;
case 11:
//#line 32 "APCParser.y"
{ yyval.obj = calculator.pow((BigDecimal) val_peek(2).obj, (BigDecimal) val_peek(0).obj); }
break;
case 12:
//#line 33 "APCParser.y"
{ yyval.obj = calculator.toPercentage((BigDecimal) val_peek(1).obj, null); }
break;
case 13:
//#line 34 "APCParser.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 14:
//#line 35 "APCParser.y"
{ yyval.obj = calculator.callFunction(val_peek(3).sval, (BigDecimal) val_peek(1).obj); }
break;
//#line 457 "APCParser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public APCParser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public APCParser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
