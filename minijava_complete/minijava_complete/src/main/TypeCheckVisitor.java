package main;

import Table.Binding;
import Table.SymbolTable;
import Type.ClassType;
import Type.MethodType;
import Type.Type;
import Type.ArrayType;
import Type.BaseType;
import Type.Undefined;
import AST.*;

/*
 * Class for error/type checking which verifies the following properties:
 * i. Every name is properly declared.
 * ii. Components of expressions have appropriate types (e.g., + is only 
 * applied to values of type int, && is only applied to values of type 
 * boolean, the expression in parentheses in an if or while statement is 
 * boolean, etc.).
 * iii. If a method is selected from a value with a reference type, then 
 * that name is defined as a member of that type.
 * iv. Methods are called with the correct number of arguments.
 * v. In assignment statements and method call parameter lists, the values 
 * being assigned have appropriate types (i.e., the value either has the same 
 * type as the variable or is a subclass of the variable's class).
 * vi. If a method in a subclass overrides one in any of its superclasses, 
 * the overriding method has the same parameter list as the original method, 
 * and the result type of the method is the same as the result type of the 
 * original one, or a subclass if the original result type is a reference type.
 * @Author Oleg Godunok, Changhao Han
 */
public class TypeCheckVisitor implements TypeVisitor {
	private SymbolTable global;
	private SymbolTable topmost;
	private boolean error;

	public TypeCheckVisitor(SymbolTable global) {
		this.global = global;
		topmost = global;
	}

	public boolean error() {
		return error;
	}

	// Display added for toy example language. Not used in regular MiniJava
	public void visit(Display n) {
		n.e.accept(this);
		if (n.e.t == null) {
			System.out.println("null in 'Display' at " + n.e.getLineNumber());
			return;
		}
		if (n.e.t.same(Undefined.UNDEFINED)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": cannot be displayed");
			error = true;
		}
	}

	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}

	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
		global = global.scopes.get(n.i1.s); // down to class table
		n.s.accept(this);
		global = global.prev; // back up to global symbol table
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {
		global = global.scopes.get(n.i.s); // down to class table
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
		global = global.prev; // back up to global symbol table
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
		global = global.scopes.get(n.i.s); // down to class table
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
		global = global.prev; // back up to global symbol table
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public void visit(MethodDecl n) {
		// not valid so return
		if (global.get(n.i.s).type.same(Undefined.UNDEFINED)) {
			return;
		}

		// retrieve method type from symbol table
		MethodType method_t = (MethodType) global.get(n.i.s).type;

		global = global.scopes.get(n.i.s); // down to method scope
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.get(i).accept(this);
		}
		n.e.accept(this);
		if (n.e.t == null) {
			System.out
					.println("null in 'MethodDecl' at " + n.e.getLineNumber());
		}
		if (!method_t.rType.assignable(n.e.t)) {
			System.out.println("Error at " + n.i.getLineNumber()
					+ ": return type does not match");
			error = true;
		}
		global = global.prev; // back up to class symbol table
	}

	// StatementList sl;
	public void visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.get(i).accept(this);
		}
	}

	// Exp e;
	// Statement s1,s2;
	public void visit(If n) {
		n.e.accept(this);
		if (n.e.t == null) {
			System.out.println("null in 'if' at " + n.e.getLineNumber());
			return;
		}
		if (!n.e.t.same(BaseType.BOOLEAN)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": the expression must be of type boolean");
			error = true;
		}
		n.s1.accept(this);
		n.s2.accept(this);
	}

	// Exp e;
	// Statement s;
	public void visit(While n) {
		n.e.accept(this);
		if (n.e.t == null) {
			System.out.println("null in 'While' at " + n.e.getLineNumber());
			return;
		}
		// the expression must be a boolean
		if (!n.e.t.same(BaseType.BOOLEAN)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": the expression must be of type boolean");
			error = true;
		}
		n.s.accept(this);
	}

	// Exp e;
	public void visit(Print n) {
		n.e.accept(this);
		if (n.e.t == null) {
			System.out.println("null in 'Print' at " + n.e.getLineNumber());
			return;
		}
		if (n.e.t.same(Undefined.UNDEFINED)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": cannot be displayed");
			error = true;
		}
	}

	// Identifier i;
	// Exp e;
	public void visit(Assign n) { // i = e;
		// n.i.accept(this);
		n.e.accept(this);
		n.i.t = n.e.t;
		if (n.e.t == null) {
			System.out.println("null in 'Assign' at " + n.e.getLineNumber());
			return;
		}
		if (global.get(n.i.s).type == null) {
			System.out.println("null in 'Assign' at " + n.i.getLineNumber());
			return;
		}
		if (!global.get(n.i.s).type.assignable(n.e.t)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": the expression is not assignable to '" + n.i.s + "'");
			error = true;
		}
	}

	// Identifier i;
	// Exp e1,e2;
	public void visit(ArrayAssign n) { // i[e1] = e2;
		n.e1.accept(this);
		if (n.e1.t == null) {
			System.out.println("null in 'ArrayAssign' at "
					+ n.e1.getLineNumber());
			return;
		}
		if (!n.e1.t.same(BaseType.INTEGER)) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": index type is not integer");
			error = true;
		}
		n.e2.accept(this);
		if (n.e2.t == null) {
			System.out.println("null in 'ArrayAssign' at "
					+ n.e2.getLineNumber());
			return;
		}
		ArrayType arr = (ArrayType) global.get(n.i.s).type;
		if (!arr.getType().assignable(n.e2.t)) {
			System.out.println("Error at " + n.e2.getLineNumber()
					+ ": the array '" + n.i.s
					+ "' cannot be assigned the expression");
			error = true;
		}
	}

	// Exp e1,e2;
	public void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
		if (!(n.e1.t.same(BaseType.BOOLEAN) && n.e2.t.same(BaseType.BOOLEAN))) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": both expressions must be booleans");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = BaseType.BOOLEAN;
	}

	// Exp e1,e2;
	public void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		if (n.e1.t == null) {
			System.out.println("null in 'LessThan' at " + n.e1.getLineNumber());
			return;
		}
		if (n.e2.t == null) {
			System.out.println("null in 'LessThan' at " + n.e2.getLineNumber());
			return;
		}

		if (!(n.e1.t.same(BaseType.INTEGER) && n.e2.t.same(BaseType.INTEGER))) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": both expression must be integers");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = BaseType.BOOLEAN;
	}

	// Exp e1,e2;
	public void visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		if (n.e1.t == null) {
			System.out.println("null in 'Plus' at " + n.e1.getLineNumber());
			return;
		}
		if (n.e2.t == null) {
			System.out.println("null in 'Plus' at " + n.e2.getLineNumber());
			return;
		}

		if (!(BaseType.INTEGER.assignable(n.e1.t) && BaseType.INTEGER
				.assignable(n.e2.t))) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": both expression must be integers");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = BaseType.INTEGER;
	}

	// Exp e1,e2;
	public void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		if (n.e1.t == null) {
			System.out.println("null in 'Minus' at " + n.e1.getLineNumber());
			return;
		}
		if (n.e2.t == null) {
			System.out.println("null in 'Minus' at " + n.e2.getLineNumber());
			return;
		}
		if (!(BaseType.INTEGER.assignable(n.e1.t) && BaseType.INTEGER
				.assignable(n.e2.t))) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": both expression must be integers");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = BaseType.INTEGER;
	}

	// Exp e1,e2;
	public void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
		if (n.e1.t == null) {
			System.out.println("null in 'Times' at " + n.e1.getLineNumber());
			return;
		}
		if (n.e2.t == null) {
			System.out.println("null in 'Times' at " + n.e2.getLineNumber());
			return;
		}
		if (!(BaseType.INTEGER.assignable(n.e1.t) && BaseType.INTEGER
				.assignable(n.e2.t))) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": both expression must be integers");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = BaseType.INTEGER;
	}

	// Exp e1,e2;
	public void visit(ArrayLookup n) { // e1[e2]
		n.e1.accept(this);
		if (!(n.e1.t instanceof ArrayType)) {
			System.out.println("Error at " + n.e1.getLineNumber()
					+ ": not an array ");
			n.t = Undefined.UNDEFINED;
		} else {
			n.t = ((ArrayType) n.e1.t).elementType;
		}
		n.e2.accept(this);
		if (!n.e2.t.same(BaseType.INTEGER)) {
			System.out.println("Error at " + n.e2.getLineNumber()
					+ ": array must be of type int (for MiniJava)");
			error = true;
			n.t = Undefined.UNDEFINED;
		}
	}

	// Exp e;
	public void visit(ArrayLength n) {
		n.e.accept(this);
		if (n.e.t.same(Undefined.UNDEFINED)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": not an array ");
			error = true;
		}
		if (!(n.e.t instanceof ArrayType)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": not an array");
			error = true;
		} else {
			n.t = BaseType.INTEGER;
			return;
		}
		n.t = Undefined.UNDEFINED;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public void visit(Call n) { // e.i(el_0, el_1, ...)
		n.e.accept(this);
		// the expression e must be an object
		if (!(n.e.t instanceof ClassType)) {
			System.out.println("Error at " + n.e.getLineNumber() + ": "
					+ " expression is not of class types");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		// the expression is an object
		ClassType ct = (ClassType) n.e.t;
		// get its class symbol table
		SymbolTable ctable = topmost.scopes.get(ct.type);
		// retrieve the associated Binding[id, type]
		Binding b_method = ctable.get(n.i.s);
		n.i.t = b_method.type;
		// if not found, the type will be set to undefined
		if (b_method.getType().same(Undefined.UNDEFINED)) {
			System.out.println("Error at " + n.i.getLineNumber()
					+ ": class doesn't have the method");
			n.t = Undefined.UNDEFINED;
			return;
		}

		// paranoid testing
		if (!(b_method.getType() instanceof MethodType)) {
			System.out.println("Error at " + n.i.getLineNumber()
					+ ": Not a method");
			n.t = Undefined.UNDEFINED;
			return;
		}

		// the identifier i is a method
		MethodType method = (MethodType) b_method.getType();
		n.t = method.rType;
		//
		if (method.numParams() != n.el.size()) {
			System.out.println("Error at " + n.el.getLineNumber()
					+ ": incorrect number of arguments");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		} else {
			for (int i = 0; i < n.el.size(); i++) {
				n.el.get(i).accept(this);
				Type ei_t = n.el.get(i).t;
				// check to see that argument i is assignable to parameter i
				if (!method.params.get(i).assignable(ei_t)) {
					System.out.println("Error at "
							+ n.el.get(i).getLineNumber()
							+ ": parameter and argument types do not match");
					n.t = Undefined.UNDEFINED;
				}
			}
		}
	}

	// int i;
	public void visit(IntegerLiteral n) {
		n.t = BaseType.INTEGER;
	}

	public void visit(True n) {
		n.t = BaseType.BOOLEAN;
	}

	public void visit(False n) {
		n.t = BaseType.BOOLEAN;
	}

	// String s;
	public void visit(IdentifierExp n) {
		if (global == null) { // should always be false (for debugging)
			System.out.println("null in IdentifierExp at " + n.getLineNumber());
			return;
		}
		Binding binding = global.get(n.s);
		// if n.s is not in the symbol table
		if (binding.getType().same(Undefined.UNDEFINED)) {
			System.out.println("Error at " + n.getLineNumber() + ": '" + n.s
					+ "' is not declared");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		// assign the type retrieved from the symbol table to this
		n.t = binding.getType();
	}

	public void visit(This n) {
		// for debugging
		if (global.prev == null) {
			System.out.println("null in 'This' at " + n.getLineNumber());
		}
		SymbolTable temp = global;
		while (global.prev != null) {
			// get the previous symbol table (should be the global one) of the
			// current one (should be a class table)
			Type type = global.prev.table.get(global.name).type;
			if (type instanceof ClassType) {
				n.t = type;
				global = temp;
				// for debugging
				if (n.t == null)
					System.out
							.println("null in 'This' at " + n.getLineNumber());
				return;
			}
			global = global.prev;
		}
		global = temp;
		n.t = Undefined.UNDEFINED;
	}

	// Exp e;
	public void visit(NewArray n) { // new int[e];
		n.e.accept(this);
		if (n.e.t == null) {
			System.out.println("null in 'NewArray' at " + n.e.getLineNumber());
			return;
		}
		if (!n.e.t.same(BaseType.INTEGER)) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": size is not of type int ");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = new ArrayType(1, BaseType.INTEGER);
	}

	// Identifier i;
	public void visit(NewObject n) { // new i();
		Binding b = topmost.get(n.i.s);
		// for debugging
		if (b == null) {
			System.out.println("null in 'NewObject at' " + n.i.getLineNumber());
		}

		// Identifier has to be of type ClassType:
		if (!(b.type instanceof ClassType)) {
			System.out.println("Error at " + n.i.getLineNumber() + ": '"
					+ n.i.s + "' is not a valid class");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = b.type;
		if (n.t == null) {
			System.out.println("null in 'NewObject at' " + n.i.getLineNumber());
		}
	}

	// Exp e;
	public void visit(Not n) {
		n.e.accept(this);
		// for debugging
		if (n.e.t == null) {
			System.out.println("null in 'Not' at " + n.e.getLineNumber());
			return;
		}
		if (!(n.e.t.same(BaseType.BOOLEAN))) {
			System.out.println("Error at " + n.e.getLineNumber()
					+ ": not of type boolean");
			error = true;
			n.t = Undefined.UNDEFINED;
			return;
		}
		n.t = n.e.t;
	}
}