package AST;

import Type.BaseType;
import AST.Visitor.Visitor;

public class BooleanType extends Type {
	public BooleanType(int ln) {
		super(ln);
		t = BaseType.BOOLEAN;
		super.t = t;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
