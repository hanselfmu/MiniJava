package main;

import java.util.ArrayList;

import Table.Binding;
import Table.SymbolTable;
import Type.MethodType;
import Type.Type;
import Type.Undefined;
import Type.VoidType;
import Type.ClassType;
import Type.ArrayType;
import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.Formal;
import AST.MainClass;
import AST.MethodDecl;
import AST.Program;
import AST.VarDecl;

/*
 * We utilize this Visitor in another pass to update the types
 * of variables in the parameters of methods and variable declarations
 * in the symbol tables (class and method) and to construct the
 * method symbol tables
 * @Author Oleg Godunok, Changhao Han
 */
public class BuildTableP2Visitor implements SymbolTableVisitor {
	// global symbol table : class id -> ClassType, ...,
	// : class id -> class symbol table
	private SymbolTable global;
	private SymbolTable top;
	// indicates if a type error is present
	private boolean error;

	// Constructor accepts the global symbol table which contains
	// links to the class and method tables
	public BuildTableP2Visitor(SymbolTable global) {
		this.global = global;
		top = global;
	}

	// Returns if whether or not a type error was discovered (in this pass)
	public boolean error() {
		return error;
	}

	// MainClass m
	// ClassDeclList cl
	@Override
	public void visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}

	// Identifier i1, i2
	@Override
	public void visit(MainClass n) { // no type checking is needed
		global = global.scopes.get(n.i1.s); // down to class table

		ClassType class_type = (ClassType) global.get(n.i1.s).type;
		SymbolTable class_t = class_type.thisType;
		// method symbol table for the special "public static void main..."
		// method
		SymbolTable method_t = new SymbolTable(class_t);
		method_t.name = "main";
		// the MethodType of main has return type of void, and since the
		// parameter
		// is of type String[] (so not used), the parameter list just empty
		MethodType method_type = new MethodType("main", VoidType.VOID,
				new ArrayList<Type>());
		// map main method to method table (empty) and to information about it
		class_t.put("main", new Binding("main", n.i1.s, method_type));
		class_t.put("main", method_t);
		// ... main(String[] args) { ...
		method_t.put(n.i2.s, new Binding(n.i2.s, "main", new ArrayType(1,
				new ClassType(null, "String", null))));

		global = global.prev; // back up to global
	}

	// Identifier i;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclSimple n) { // No type checking is need in this
											// function
		global = global.scopes.get(n.i.s); // down to class table

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
			// set the offsets of the class fields
			global.get(n.vl.get(i).i.s).offset = 8+8*i;
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
			// set the offsets of the methods
			global.get(n.ml.get(i).i.s).offset = 8+8*i;
		}

		global = global.prev; // back up to global
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclExtends n) {
		// Class is of the type c1 extends c2; we need to check
		// that c2 is contained in the global symbol table

		// only type checking ///////////////////////
		Binding super_c = global.get(n.j.s);
		if (super_c.getType().same(Undefined.UNDEFINED)) {
			System.out.println("Error at line " + n.getLineNumber() + ": "
					+ n.j.s + " is not defined");
			error = true;
		}
		// ///////////////////////////////////////////

		global = global.scopes.get(n.i.s); // down to class table

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}

		global = global.prev; // back up to global
	}

	// Identifier i
	// FormalList fl
	// VarDeclList vl
	@Override
	public void visit(MethodDecl n) {
		// method symbol table: var id -> Type, ...
		SymbolTable methodtable = new SymbolTable(global);
		String method_name = n.i.s;
		methodtable.name = method_name;

		// change from class scope to method scope
		global = methodtable;
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.get(i).accept(this);
			// set the offsets of the parameters
			global.get(n.fl.get(i).i.s).offset = -(8 + 8*n.fl.size() - 8*i);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.get(i).accept(this);
			// set the offsets of the method variables
			global.get(n.vl.get(i).i.s).offset = 8+8*i;
		}
		// update the return type of the method
		// update(n.type.t);
		MethodType method_t = (MethodType) n.i.t;
		update(method_t);

		// change back to class scope
		global = global.prev;

		// map main method to method table (empty) and to information about it
		global.put(method_name, new Binding(method_name, global.name, n.i.t));
		global.put(method_name, methodtable);
	}

	// Identifier i
	@Override
	public void visit(VarDecl n) {
		// we need to update n.i.t if is of type ClassType
		// update(n.i.t);
		if (n.i.t instanceof ClassType) {
			ClassType nit_ = (ClassType) n.i.t;
			Binding class_b = top.get(nit_.type);
			n.i.t = class_b.type;
		}
		// merely maps variable id -> type of id
		global.put(n.i.s, new Binding(n.i.s, global.name, n.i.t));
		SymbolTable null_t = null;
		global.put(n.i.s, null_t);
	}

	// Identifier i
	@Override
	public void visit(Formal n) {
		// we need to update n.i.t if is of type ClassType
		if (n.i.t instanceof ClassType) {
			ClassType nit_ = (ClassType) n.i.t;
			Binding class_b = top.get(nit_.type);
			n.type.t = class_b.type;
			n.i.t = class_b.type;
		}
		// map parameter id -> type of id
		global.put(n.i.s, new Binding(n.i.s, global.name, n.i.t));
	}

	// Updates a Type if  a reference type is a constituent
	private void update(Type t) {
		if (t instanceof MethodType) {
			MethodType method_t = (MethodType) t;
			// update return type
			if (method_t.rType instanceof ClassType) {
				ClassType nit_ = (ClassType) method_t.rType;
				Binding class_b = top.get(nit_.type); // retrieve class type
														// from global
				method_t.rType = class_b.type; // re-assign class type
			}
			// update parameter types
			for (int i = 0; i < method_t.params.size(); i++) {
				if (method_t.params.get(i) instanceof ClassType) {
					ClassType nit_ = (ClassType) method_t.params.get(i);
					Binding class_b = top.get(nit_.type); // retrieve class type
															// from global

					method_t.params.set(i, class_b.type); // re-assign class
															// type
				}
			}
		} else if (t instanceof ArrayType) {
			ArrayType array_t = (ArrayType) t;
			// update type of the array
			if (array_t.elementType instanceof ClassType) {
				ClassType c_type = (ClassType) array_t.elementType;
				Binding class_b = top.get(c_type.type);
				array_t.elementType = class_b.type;
			}
		}
	}

	// Returns the global Symbol table
	public SymbolTable getGlobalTable() {
		return global;
	}
}
