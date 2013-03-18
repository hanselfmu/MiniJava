/*
 * JFlex specification for the lexical analyzer for a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 * CSE 401/P501 Au11
 */


package Scanner;

import java_cup.runtime.*;
import Parser.sym;

%%

%public
%final
%class scanner
%unicode
%cup
%line
%column

/* Code copied into the generated scanner class.  */
/* Can be referenced in scanner action code. */
%{
  // Return new symbol objects with line and column numbers in the symbol 
  // left and right fields. This abuses the original idea of having left 
  // and right be character positions, but is   // is more useful and 
  // follows an example in the JFlex documentation.
  private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Return a readable representation of symbol s (aka token)
  public String symbolToString(Symbol s) {
    String rep;
    switch (s.sym) {
      case sym.BECOMES: return "BECOMES";
      case sym.SEMICOLON: return "SEMICOLON";
      case sym.COMMA: return "COMMA";
      case sym.PLUS: return "PLUS";
      case sym.MINUS: return "MINUS";
      case sym.LPAREN: return "LPAREN";
      case sym.RPAREN: return "RPAREN";
      case sym.LBRACKET: return "LBRACKET";
      case sym.RBRACKET: return "RBRACKET";
      case sym.LCURL: return "LCURL";
      case sym.RCURL: return "RCURL";
      case sym.MULT: return "MULT";
      case sym.IF: return "IF";
      case sym.ELSE: return "ELSE";
      case sym.THIS: return "THIS";
      case sym.WHILE: return "WHILE";
      case sym.NOT: return "NOT";
      case sym.DOT: return "DOT";
      case sym.LENGTH: return "LENGTH";
      case sym.NEW: return "NEW";
      case sym.INT: return "INT"; 
      case sym.BOOLEAN: return "BOOLEAN";
      case sym.PUBLIC: return "PUBLIC";
      case sym.CLASS: return "CLASS";
      case sym.STATIC: return "STATIC";
      case sym.VOID: return "VOID";
      case sym.MAIN: return "MAIN";
      case sym.STRING: return "STRING";
      case sym.EXTENDS: return "EXTENDS";
      case sym.RETURN: return "RETURN";
      case sym.LESS: return "LESS";
      case sym.BAND: return "BAND";
      case sym.TRUE: return "TRUE";
      case sym.FALSE: return "FALSE";
      case sym.PRINT: return "PRINT";
      case sym.INTEGER: return "INTEGER(" + (String)s.value + ")";
      case sym.IDENTIFIER: return "ID(" + (String)s.value + ")";
      case sym.EOF: return "<EOF>";
      case sym.error: return "<ERROR>";
      default: return "<UNEXPECTED TOKEN " + s.toString() + ">";
    }
  }
%}

/* Helper definitions */

letter = [a-zA-Z]
digit = [0-9]
pos_digit = [1-9]
eol = [\r\n]
white = {eol}|[ \t]
input_character = [^\r\n]

%%
/* Token definitions */

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */

"System.out.println" { return symbol(sym.PRINT); }

/* operators */
"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"=" { return symbol(sym.BECOMES); }
"*" { return symbol(sym.MULT); }
"<" { return symbol(sym.LESS); }
"&&" { return symbol(sym.BAND); }
"!" { return symbol(sym.NOT); }

/* delimiters */
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }
"{" { return symbol(sym.LCURL); }
"}" { return symbol(sym.RCURL); }
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
";" { return symbol(sym.SEMICOLON); }
"," { return symbol(sym.COMMA); }

/* types */ 
"boolean" { return symbol(sym.BOOLEAN); }
"int" { return symbol(sym.INT); }

/* truth values - as expressions */
"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }

/* others */
"." { return symbol(sym.DOT); }
"length" { return symbol(sym.LENGTH); }
"this" { return symbol(sym.THIS); }
"new" { return symbol(sym.NEW); }
"public" { return symbol(sym.PUBLIC); }
"class" { return symbol(sym.CLASS); }
"static" { return symbol(sym.STATIC); }
"void" { return symbol(sym.VOID); }
"main" { return symbol(sym.MAIN); }
"String" { return symbol(sym.STRING); }
"extends" { return symbol(sym.EXTENDS); }
"return" { return symbol(sym.RETURN); }
"while" { return symbol(sym.WHILE); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }

/* integers */
{digit}|{pos_digit}{digit}* { return symbol(sym.INTEGER, yytext()); }

/* identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* whitespace */
{white}+ { /* ignore whitespace */ }

/* comments */

/* This will match a line comment and simply ignore it. Note that the 
  end-of-line character is optional to handle the case where a line comment 
  is on the last line of the program */

"//"{input_character}*{eol}? { /* ignore comment */ }

/* ignores comments like this comment */
"/*" ~"*/" { /* ignore comment */ }

/* lexical errors (put last so other matches take precedence). Also sets error flag to 
   to true. */
. { 
    System.err.println(
        "\nunexpected character in input: '" + yytext() + "' at line " +
        (yyline+1) + " column " + (yycolumn+1));
    return symbol(sym.error);
  }