package main;

import Type.ClassType;
import Type.Type;
import AST.*;
import Table.SymbolTable;
import Table.Binding;

/*
 * Visitor implementation which constructs the global, class, and
 * method symbol tables only (i.e. symbol tables for other scopes
 * are not created)
 * @Author Oleg Godunok, Changhao Han
 */

public class BuildGlobalTableVisitor implements GlobalTableVisitor {
	// global symbol table : class id -> ClassType, ...,
	// : class id -> class symbol table
	private SymbolTable global;

	// MainClass m
	// ClassDeclList cl
	@Override
	public void visit(Program n) {
		// construct a global symbol table: class id -> ClassType, ...
		global = new SymbolTable(null);
		global.name = "global";
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}

	// Identifier i1, i2
	@Override
	public void visit(MainClass n) {
		ConstructClassTable(n.i1.s, n.i1.t);
	}

	// Identifier i;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclSimple n) {
		ConstructClassTable(n.i.s, n.i.t);
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclExtends n) {
		ConstructClassTable(n.i.s, n.i.t);
	}

	// private helper method for constructing the global
	// class method.
	private void ConstructClassTable(String cid, Type ct) {
		// class symbol table: var or method id -> Type, ...
		SymbolTable classtable = new SymbolTable(global);
		classtable.name = cid;
		((ClassType) ct).thisType = classtable;

		// map class name to class table and to information about it
		global.put(cid, new Binding(cid, global.name, ct));
		global.put(cid, classtable);
	}

	// Returns the global Symbol table
	public SymbolTable getGlobalTable() {
		return global;
	}
}
