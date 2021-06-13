//#############################################
//## file: APCParser.java
//## Generated by Byacc/j
//#############################################
package org.seniorlaguna.calculator.apc;

/**
 * BYACC/J Semantic Value for parser: APCParser
 * This class provides some of the functionality
 * of the yacc/C 'union' directive
 */
public class APCParserVal
{
/**
 * integer value of this 'union'
 */
public int ival;

/**
 * double value of this 'union'
 */
public double dval;

/**
 * string value of this 'union'
 */
public String sval;

/**
 * object value of this 'union'
 */
public Object obj;

//#############################################
//## C O N S T R U C T O R S
//#############################################
/**
 * Initialize me without a value
 */
public APCParserVal()
{
}
/**
 * Initialize me as an int
 */
public APCParserVal(int val)
{
  ival=val;
}

/**
 * Initialize me as a double
 */
public APCParserVal(double val)
{
  dval=val;
}

/**
 * Initialize me as a string
 */
public APCParserVal(String val)
{
  sval=val;
}

/**
 * Initialize me as an Object
 */
public APCParserVal(Object val)
{
  obj=val;
}
}//end class

//#############################################
//## E N D    O F    F I L E
//#############################################
