package Table;

import java.util.*;
import Type.Undefined;

/*
 * ADT for a symbol table - maps identifiers to properties such as 
 * type, size, location (potentially offset), etc. A SymbolTable is 
 * built and used during semantics pass and may be retained for later 
 * use if desired.
 * @Author Oleg Godunok, Changhao Han
 */
public class SymbolTable {
	public String name;
	
	// mapping M: id -> Binding[information of id]
	public Map<String, Binding> table;
	
	// mapping M: id -> [SymbolTable of the id]
	public Map<String, SymbolTable> scopes;
	
	// reference to a higher scope
	public SymbolTable prev;
	
	// the depth of the symbol table
	private int depth;
	
	// true indicates that it has received all the fields
	// and methods from its supertype
	public boolean updated;

	public SymbolTable(SymbolTable prev) {
		table = new HashMap<String, Binding>();
		scopes = new HashMap<String, SymbolTable>();
		this.prev = prev;
		updated = false;
		this.depth = (prev == null) ? 0 : prev.depth + 1;
	}

	// This function bindings information to an identifier s
	// An error is printing if a binding already exists for s
	public void put(String s, Binding b) {
		if (table.get(s) != null) {
			System.out.println(s + " is already declared!");
		}
		table.put(s, b);
	}

	// Binds the SymbolTable 'table' to s. An error is printed
	// if a binding already exists
	public void put(String s, SymbolTable table) {
		if (scopes.get(s) != null) {
			System.out.println(s + " is already declared!");
		}
		scopes.put(s, table);
	}

	// Retrieves a Binding object for s which contains information
	// about it. If it isn't bound to anything, a binding is created
	// and the type of s is set to undefined (suppress error messages)
	public Binding get(String s) {
		for (SymbolTable t = this; t != null; t = t.prev) {
			Binding found = t.table.get(s);
			if (found != null)
				return found;
		}
		Binding undef = new Binding(s, null, Undefined.UNDEFINED);
		table.put(s, undef);
		return undef;
	}

	// returns the depth of the symbol table
	public int returnDepth() {
		return depth;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		if (depth == 0)
			res.append("Global:\n");
		for (String s : table.keySet()) { // s -> Binding
			res.append(indent(depth + 1) + s + "-> Binding=[" + table.get(s)
					+ "]\n");
			SymbolTable child = scopes.get(s);
			if (child != null) {
				res.append(child + "\n");
			}
		}
		return res.toString();
	}

	// private helper method to toString(): used for indentation
	private String indent(int n) {
		String res = "";
		for (int i = 0; i < n; i++)
			res += "\t";
		return res;
	}
}