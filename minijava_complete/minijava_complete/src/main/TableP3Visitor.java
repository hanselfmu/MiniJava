package main;

import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.Program;

/*
 * Interface for visitor for pass three, which finishes up creating 
 * the symbol tables.
 */
public interface TableP3Visitor {
	public void visit(Program n);
	public void visit(ClassDeclSimple n);
	public void visit(ClassDeclExtends n);

}
