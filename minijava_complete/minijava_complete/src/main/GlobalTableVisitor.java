package main;

import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.MainClass;
import AST.Program;

/* Interface for visitor which constructs the global symbol 
 * table (class id -> Binding[class id, ClassType of id])
 */
public interface GlobalTableVisitor {
	void visit(Program n);
	void visit(MainClass n);
	void visit(ClassDeclSimple n);
	void visit(ClassDeclExtends n);
}
