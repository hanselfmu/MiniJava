package AST;

import Type.ArrayType;
import Type.BaseType;
import AST.Visitor.Visitor;

public class IntArrayType extends Type {
	public IntArrayType(int ln) {
		super(ln);
		t = new ArrayType(1, BaseType.INTEGER);
		super.t = t;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
