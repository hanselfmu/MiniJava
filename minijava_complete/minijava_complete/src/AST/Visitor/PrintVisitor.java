package AST.Visitor;

import AST.*;

public class PrintVisitor implements Visitor {

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
	PrintIndent(n.getDepth());
    System.out.print("Display ");
    Println(n);
    n.e.accept(this);
  }
  
  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    System.out.println("Program");
    n.m.accept(this);
    for ( int i = 0; i < n.cl.size(); i++ ) {
        n.cl.get(i).accept(this);
    }
  }
   
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
	PrintIndent(n.getDepth());
    System.out.print("MainClass " + ": " + n.i1.t);
	Println(n); // prints out the line number in the form @line#
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
	PrintIndent(n.getDepth());
    System.out.print("ClassDeclSimpl " + ": " + n.i.t);
    Println(n);
    n.i.accept(this);
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.get(i).accept(this);
    }

  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) { 
	PrintIndent(n.getDepth());
    System.out.print("ClassDeclExtends " + ": " + n.i.t);
    Println(n);
    n.i.accept(this);
    n.j.accept(this);
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.get(i).accept(this);
    }

  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
	PrintIndent(n.getDepth());
    System.out.print("VarDecl ");
    Println(n);
    n.type.accept(this);
    n.i.accept(this);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
	PrintIndent(n.getDepth());
    System.out.print("MethodDecl ");
    Println(n);
    n.type.accept(this);
    n.i.accept(this);
    for ( int i = 0; i < n.fl.size(); i++ ) {
        n.fl.get(i).accept(this);
    }
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.sl.size(); i++ ) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this); 
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
	PrintIndent(n.getDepth());
    System.out.println("Formal");
    n.type.accept(this); 
    n.i.accept(this);  
  }

  public void visit(IntArrayType n) {
	PrintIndent(n.getDepth());
    System.out.println("Type int []" + ": " + n.t);
  }

  public void visit(BooleanType n) {
	PrintIndent(n.getDepth());
    System.out.println("Type boolean" + ": " + n.t);
  }

  public void visit(IntegerType n) {
	PrintIndent(n.getDepth());
    System.out.println("Type int" + ": " + n.t);
  }

  // String s;
  public void visit(IdentifierType n) {
	PrintIndent(n.getDepth()); 
    System.out.println(n.s + ": " + n.t);
  }

  // StatementList sl;
  public void visit(Block n) {
	PrintIndent(n.getDepth());
    System.out.println("Block");
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.get(i).accept(this);
    } 
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
	PrintIndent(n.getDepth());
    System.out.print("IfStatement ");
    Println(n);
    n.e.accept(this);    
    n.s1.accept(this);    
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
	PrintIndent(n.getDepth());
    System.out.print("WhileStatement ");
    Println(n);
    n.e.accept(this);
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
	PrintIndent(n.getDepth());
    System.out.print("Print ");
    Println(n);
    n.e.accept(this);
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
	  PrintIndent(n.getDepth()); 
    System.out.print("Assign ");
    Println(n);
    n.i.accept(this);
    n.e.accept(this);    
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
	PrintIndent(n.getDepth());
    System.out.print("ArrayAssign ");
    Println(n);
    n.i.accept(this);
    n.e1.accept(this);  
    n.e2.accept(this);  
  }

  // Exp e1,e2;
  public void visit(And n) {
	PrintIndent(n.getDepth());    
    System.out.println("And" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
	PrintIndent(n.getDepth());
    System.out.println("LessThan" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);   
  }

  // Exp e1,e2;
  public void visit(Plus n) {
	PrintIndent(n.getDepth());
    System.out.println("Plus" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
	PrintIndent(n.getDepth());
    System.out.println("Minus" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Times n) {
	PrintIndent(n.getDepth());
    System.out.println("Times" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
	PrintIndent(n.getDepth());
    System.out.println("ArrayLookup" + ": " + n.t);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
	PrintIndent(n.getDepth());
    System.out.println("ArrayLength" + ": " + n.t);
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
	PrintIndent(n.getDepth());
    System.out.println("Call" + ": " + n.t);
    n.e.accept(this);
    n.i.accept(this);
    for ( int i = 0; i < n.el.size(); i++ ) {
        n.el.get(i).accept(this);
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
    PrintIndent(n.getDepth());
    System.out.println(n.i + ": " + n.t);
  }

  public void visit(True n) {
	PrintIndent(n.getDepth());
    System.out.println("true" + ": " + n.t);
  }

  public void visit(False n) {
	PrintIndent(n.getDepth());
    System.out.println("false" + ": " + n.t);
  }

  // String s;
  public void visit(IdentifierExp n) {
	PrintIndent(n.getDepth());
    System.out.println(n.s + ": " + n.t);
  }

  public void visit(This n) {
	PrintIndent(n.getDepth());
    System.out.println("this" + ": " + n.t);
  }

  // Exp e;
  public void visit(NewArray n) {
	PrintIndent(n.getDepth());
    System.out.println("NewArray" + ": " + n.t);
    n.e.accept(this);
  }

  // Identifier i;
  public void visit(NewObject n) {
	PrintIndent(n.getDepth());
    System.out.println(n.i.s + ": " + n.t);
  }

  // Exp e;
  public void visit(Not n) {
	PrintIndent(n.getDepth());
    System.out.println("Not" + ": " + n.t);
    n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
	PrintIndent(n.getDepth());
    System.out.println(n.s + ": " + n.t);
    
  }
  
  // Prints the the line number of the AST node n 
  private void Println(ASTNode n) {
    System.out.println("@" + n.getLineNumber() + "");
  }
  
  // private method used for indentation - num specifies
  // the number of tabs (3 spaces) to print out 
  private void PrintIndent(int num) {
    for(int i = 0; i < num; i++) {
      System.out.print("   ");
    }
  }
}