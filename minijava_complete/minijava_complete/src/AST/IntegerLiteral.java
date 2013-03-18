package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class IntegerLiteral extends Exp {
	public int i;

	public IntegerLiteral(int ai, int ln) {
		super(ln);
		i = ai;
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
