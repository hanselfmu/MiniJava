package main;

import AST.And;
import AST.ArrayAssign;
import AST.ArrayLength;
import AST.ArrayLookup;
import AST.Assign;
import AST.Block;
import AST.Call;
import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.Display;
import AST.False;
import AST.IdentifierExp;
import AST.If;
import AST.IntegerLiteral;
import AST.LessThan;
import AST.MainClass;
import AST.MethodDecl;
import AST.Minus;
import AST.NewArray;
import AST.NewObject;
import AST.Not;
import AST.Plus;
import AST.Print;
import AST.Program;
import AST.This;
import AST.Times;
import AST.True;
import AST.While;

/*
 * Visitor interface for type checking and static analysis.
 */
public interface TypeVisitor {
	// Display added for toy example language. Not used in MiniJava AST
	public void visit(Display n);
	public void visit(Program n);
	public void visit(MainClass n);
	public void visit(ClassDeclSimple n);
	public void visit(ClassDeclExtends n);
	public void visit(MethodDecl n);
	public void visit(Block n);
	public void visit(If n);
	public void visit(While n);
	public void visit(Print n);
	public void visit(Assign n);
	public void visit(ArrayAssign n);
	public void visit(And n);
	public void visit(LessThan n);
	public void visit(Plus n);
	public void visit(Minus n);
	public void visit(Times n);
	public void visit(ArrayLookup n);
	public void visit(ArrayLength n);
	public void visit(Call n);
	public void visit(IntegerLiteral n);
	public void visit(True n);
	public void visit(False n);
	public void visit(IdentifierExp n);
	public void visit(This n);
	public void visit(NewArray n);
	public void visit(NewObject n);
	public void visit(Not n);
}