package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class PrettyPrintVisitor implements Visitor {

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    System.out.print("display ");
    n.e.accept(this);
    System.out.print(";");
  }
  
  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for ( int i = 0; i < n.cl.size(); i++ ) {
        System.out.println();
        n.cl.get(i).accept(this);
    }
  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    System.out.print("class ");
    n.i1.accept(this);
    System.out.println(" {");
    System.out.print("  public static void main (String [] ");
    n.i2.accept(this);
    System.out.println(") {");
    System.out.print("    ");
    n.s.accept(this);
    System.out.println("  }");
    System.out.println("}");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    System.out.print("class ");
    n.i.accept(this);
    System.out.println(" { ");
    for ( int i = 0; i < n.vl.size(); i++ ) {
        System.out.print("  ");
        n.vl.get(i).accept(this);
        if ( i+1 < n.vl.size() ) { System.out.println(); }
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        System.out.println();
        n.ml.get(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    System.out.print("class ");
    n.i.accept(this);
    System.out.println(" extends ");
    n.j.accept(this);
    System.out.println(" { ");
    for ( int i = 0; i < n.vl.size(); i++ ) {
        System.out.print("  ");
        n.vl.get(i).accept(this);
        if ( i+1 < n.vl.size() ) { System.out.println(); }
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        System.out.println();
        n.ml.get(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.type.accept(this);
    System.out.print(" ");
    n.i.accept(this);
    System.out.print(";");
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    System.out.print("  public ");
    n.type.accept(this);
    System.out.print(" ");
    n.i.accept(this);
    System.out.print(" (");
    for ( int i = 0; i < n.fl.size(); i++ ) {
        n.fl.get(i).accept(this);
        if (i+1 < n.fl.size()) { System.out.print(", "); }
    }
    System.out.println(") { ");
    for ( int i = 0; i < n.vl.size(); i++ ) {
        System.out.print("    ");
        n.vl.get(i).accept(this);
        System.out.println("");
    }
    for ( int i = 0; i < n.sl.size(); i++ ) {
        System.out.print("    ");
        n.sl.get(i).accept(this);
        if ( i < n.sl.size() ) { System.out.println(""); }
    }
    System.out.print("    return ");
    n.e.accept(this);
    System.out.println(";");
    System.out.print("  }");
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.type.accept(this);
    System.out.print(" ");
    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
    System.out.print("int []");
  }

  public void visit(BooleanType n) {
    System.out.print("boolean");
  }

  public void visit(IntegerType n) {
    System.out.print("int");
  }

  // String s;
  public void visit(IdentifierType n) {
    System.out.print(n.s);
  }

  // StatementList sl;
  public void visit(Block n) {
    System.out.println("{ ");
    for ( int i = 0; i < n.sl.size(); i++ ) {
        System.out.print("      ");
        n.sl.get(i).accept(this);
        System.out.println();
    }
    System.out.print("    } ");
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    System.out.print("if (");
    n.e.accept(this);
    System.out.println(") ");
    System.out.print("    ");
    n.s1.accept(this);
    System.out.println();
    System.out.print("    else ");
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    System.out.print("while (");
    n.e.accept(this);
    System.out.print(") ");
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    System.out.print("System.out.println(");
    n.e.accept(this);
    System.out.print(");");
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.i.accept(this);
    System.out.print(" = ");
    n.e.accept(this);
    System.out.print(";");
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.i.accept(this);
    System.out.print("[");
    n.e1.accept(this);
    System.out.print("] = ");
    n.e2.accept(this);
    System.out.print(";");
  }

  // Exp e1,e2;
  public void visit(And n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" && ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" < ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" + ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" - ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" * ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    System.out.print("[");
    n.e2.accept(this);
    System.out.print("]");
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
    System.out.print(".length");
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    System.out.print(".");
    n.i.accept(this);
    System.out.print("(");
    for ( int i = 0; i < n.el.size(); i++ ) {
        n.el.get(i).accept(this);
        if ( i+1 < n.el.size() ) { System.out.print(", "); }
    }
    System.out.print(")");
  }

  // int i;
  public void visit(IntegerLiteral n) {
    System.out.print(n.i);
  }

  public void visit(True n) {
    System.out.print("true");
  }

  public void visit(False n) {
    System.out.print("false");
  }

  // String s;
  public void visit(IdentifierExp n) {
    System.out.print(n.s);
  }

  public void visit(This n) {
    System.out.print("this");
  }

  // Exp e;
  public void visit(NewArray n) {
    System.out.print("new int [");
    n.e.accept(this);
    System.out.print("]");
  }

  // Identifier i;
  public void visit(NewObject n) {
    System.out.print("new ");
    System.out.print(n.i.s);
    System.out.print("()");
  }

  // Exp e;
  public void visit(Not n) {
    System.out.print("!");
    n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
    System.out.print(n.s);
  }
}