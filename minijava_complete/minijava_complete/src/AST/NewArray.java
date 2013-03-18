package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class NewArray extends Exp {
	public Exp e;

	public NewArray(Exp ae, int ln) {
		super(ln);
		e = ae;
	}

	public void accept(Visitor v) {
		e.depth = this.depth + 1;
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
