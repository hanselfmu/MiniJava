package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class True extends Exp {
	public True(int ln) {
		super(ln);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	@Override
	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
