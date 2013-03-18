package main;

import Table.Binding;
import Table.SymbolTable;
import Type.ClassType;
import Type.MethodType;

/*
 *  This is another "pass" which doesn't use the visitor pattern, but
 *  computes the offsets of classes whose super type is not Object. This
 *  pass is crucial for the construction of the method dispatch tables.
 *  @Author Oleg Godunok, Changhao Han
 */
public class ComputeOffset {
	// the global SymbolTable
	private SymbolTable top;
	// the current SymbolTable (current namespace)
	private SymbolTable namespace;

	public ComputeOffset(SymbolTable namespace) {
		// this class uses the SymbolTable from previous passes
		this.namespace = namespace;
		this.top = namespace;
	}

	// This function is called to update the offsets (i.e. to finish
	// up computing the offsets of all of the variables and methods
	// in the program)
	public void updateOffsets() {
		// iterate through the Bindings of each class
		for (Binding cid : top.table.values()) {
			// change to the namespace of class "cid"
			namespace = top.scopes.get(cid.id);
			ClassType class_t = (ClassType) cid.type;
			// we update the offsets if the base class is not Object,
			// which is repersented as null
			if (class_t.baseType != null)
				updateOffsets(class_t.baseType);
			namespace = namespace.prev;
		}
	}

	// Updates the offsets in the namespace specified by the
	// SymbolTable "namespace"
	// base : the super class id of the class
	private void updateOffsets(String base) {
		// the offset the fields of the class will start at
		int var_offset = varOffset(base);
		// the offset the methods of the class will start at
		int method_offset = methodOffset(base);

		// iterate through the fields and methods contained in the
		// SymbolTable of the class
		for (Binding cid_b : namespace.table.values()) {
			// if the offset is -1, which indicates that the offset,
			// hasn't been set yet, then set the offsets
			if (cid_b.offset == -1) {
				if (cid_b.type instanceof MethodType) {
					cid_b.offset = method_offset;
					method_offset += 8;
				} else {
					cid_b.offset = var_offset;
					var_offset += 8;
				}
			}
		}
	}

	// Returns the offset that the fields of the class, with base class 
	// "cid", will start at
	private int varOffset(String cid) {
		// retrieve the SymbolTable of the base class
		SymbolTable base_table = top.scopes.get(cid);
		// "offset" will represent the offset that the fields of the
		// class will start at
		int offset = 0;
		// iterate through the fields and methods of the base class
		for (Binding b : base_table.table.values()) {
			if (!(b.type instanceof MethodType)) {
				if (namespace.get(b.id).offset != -1) {
					offset += 8;
				} else {
					namespace.get(b.id).offset = b.offset;
				}
			}
		}
		// + 8 because of the 8-bytes for the superclass vtable pointer
		return offset + 8;
	}

	// Returns the offset that the methods of the class, with base class 
	// "cid", will start at
	private int methodOffset(String cid) {
		// retrieve the SymbolTable of the base class
		SymbolTable base_table = top.scopes.get(cid);
		// "offset" will represent the offset that the fields of the
		// class will start at
		int offset = 0;
		// iterate through the fields and methods of the base class
		for (Binding b : base_table.table.values()) {
			if (b.type instanceof MethodType) {
				if (namespace.get(b.id).offset != -1) {
					offset += 8;
				} else {
					namespace.get(b.id).offset = b.offset;
				}
			}
		}
		// + 8 because of the 8-bytes for the superclass vtable pointer
		return offset + 8;
	}
}
