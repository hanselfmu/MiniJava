package AST;

import Type.ClassType;
import AST.Visitor.Visitor;

public class IdentifierType extends AST.Type {
	public String s;

	public IdentifierType(String as, int ln) {
		super(ln);
		s = as;
		t = new ClassType(null, as, null);
		super.t = t;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
