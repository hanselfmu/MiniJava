package main;

import AST.*;

/*
 * A Visitor interace for constructing the method symbol tables, 
 * updating reference types (which where assigned during the 
 * construction of the abstract tree, and a little type checking
 * @Author Oleg Godunok, Changhao Han
 */
public interface SymbolTableVisitor {
	public void visit(Program n);
	public void visit(MainClass n);
	public void visit(ClassDeclSimple n);
	public void visit(ClassDeclExtends n);
	public void visit(VarDecl n);
	public void visit(MethodDecl n);
	public void visit(Formal n);
}
