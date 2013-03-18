package main;

import Table.SymbolTable;
import Type.MethodType;
import Type.Type;
import Type.ClassType;
import Type.Undefined;
import Type.VoidType;
import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.Program;

/*
 * This is the last pass before performing the typical type
 * checking (Expression types, etc.). This class deals with
 * class inheritance. Subclass symbol tables are augmented 
 * to include information about their supertype
 * @Author Oleg Godunok, Changhao Han
 */
public class BuildTableP3Visitor implements TableP3Visitor {
	// global symbol table : class id -> ClassType, ...,
	// : class id -> class symbol table
	private SymbolTable global;
	private SymbolTable top;

	// indicates if a type error is present
	private boolean error;

	// Constructor accepts the global symbol table which contains
	// links to the class and method tables
	public BuildTableP3Visitor(SymbolTable global) {
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
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}

	// Identifier i;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclSimple n) {/* superclass is "Object" */}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl
	// MethodDeclList ml
	@Override
	public void visit(ClassDeclExtends n) {
		global = global.scopes.get(n.i.s); // down to class table
		SymbolTable base_table = top.scopes.get(n.j.s); // table of super class
		if (base_table == null)
			return; // will be of type Undefined
		String base_c = n.j.s;
		while (base_c != null) { // break when super class "Object" is reached
			for (String id : base_table.table.keySet()) {
				// if the child class doesn't contain the method or field then
				// add it to it
				if (!global.table.containsKey(id)) {
					// handles the case where a class extends the MainClass
					if (base_table.get(id).getType() instanceof MethodType) {
						MethodType method_t = (MethodType) base_table.get(id)
								.getType();
						if (method_t.rType == VoidType.VOID)
							continue;
					}
					global.put(id, base_table.get(id));
					global.updated = true;
					continue;
				}
				// does contain it. If it is a method we check that they are
				// "assignable"
				
				Type id_type_b = base_table.get(id).type;
				if (id_type_b instanceof MethodType) {
					id_type_b = (MethodType) id_type_b;
					MethodType id_type = (MethodType) global.get(id).type;
					if (!id_type_b.assignable(id_type)) {
						System.out
								.println("Error at "
										+ n.getLineNumber()
										+ ":  the class method "
										+ id
										+ " is not assignable to the base class's (same) method");
						error = true;
						// set the child class's method to Undefined 
						(global.table.get(id)).type = Undefined.UNDEFINED;;
						continue;
					}
				}
			}
			// if the super class is updated, then it has retrieved all missing
			// fields/methods from its supertype...
			if (base_table.updated == true)
				break;
			ClassType super_t = (ClassType) top.table.get(base_c).type;
			base_c = super_t.baseType; // may be null; what terminates the loop
			base_table = top.scopes.get(super_t.baseType);
		}

		global = global.prev; // back up to global
	}
	

	// Returns the global Symbol table
	public SymbolTable getGlobalTable() {
		return global;
	}
}
