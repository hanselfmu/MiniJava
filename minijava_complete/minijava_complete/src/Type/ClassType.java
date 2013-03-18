package Type;

import Table.Binding;
import Table.SymbolTable;

/*
 * A ClassType in MiniJava
 * @Author Oleg Godunok, Changhao Han
 */
public class ClassType extends Type {
	// fields
	public String baseType; // base class name
	public String type; // class name
	// thisType is a class Symbol Table for type
	public SymbolTable thisType;

	public ClassType(SymbolTable table, String t, String base) {
		thisType = table;
		type = t;
		baseType = base;
	}

	// Classes are name equivalent in Java (and MiniJava)
	@Override
	public boolean same(Type other) {
		if (!(other instanceof ClassType))
			return false;
		ClassType o = (ClassType) other;
		return type.equals(o.type);
	}

	// other is assignable only if it is of the same type or a
	// subtype of this
	@Override
	public boolean assignable(Type other) {
		// must be a ClassType object
		if (!(other instanceof ClassType))
			return false;
		ClassType o = (ClassType) other;
		// is of the same "type" - classes have name equivalence
		if (type.equals(o.type))
			return true;

		// May or may not be a subclass
		String sup = o.baseType;
		while (sup != null) {
			Binding b = thisType.prev.get(sup);
			ClassType superOfo = (ClassType) b.getType();
			if (sup.equals(type))
				return true;
			sup = superOfo.baseType;
		}
		return false;
	}

	@Override
	public String toString() {
		String base = (baseType == null) ? "Object" : baseType;
		String result = "ClassType-(id: " + type + ", base: " + base;
		return result + ")";
	}
}
