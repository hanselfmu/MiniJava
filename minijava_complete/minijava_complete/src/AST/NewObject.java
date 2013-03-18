package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class NewObject extends Exp {
	public Identifier i;

	public NewObject(Identifier ai, int ln) {
		super(ln);
		i = ai;
	}

	public void accept(Visitor v) {
		i.depth = this.depth + 1;
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
