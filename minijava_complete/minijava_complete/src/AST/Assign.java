package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class Assign extends Statement {
	public Identifier i;
	public Exp e;

	public Assign(Identifier ai, Exp ae, int ln) {
		super(ln);
		i = ai;
		e = ae;
	}

	public void accept(Visitor v) {
		i.depth = e.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
