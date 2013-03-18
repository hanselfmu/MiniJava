package AST;

import Type.BaseType;
import AST.Visitor.Visitor;

public class IntegerType extends Type {
	public IntegerType(int ln) {
		super(ln);
		t = BaseType.INTEGER;
		super.t = t;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
