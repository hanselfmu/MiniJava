package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class Display extends Statement {
	public Exp e;

	public Display(Exp re, int ln) {
		super(ln);
		e = re;
	}

	public void accept(Visitor v) {
		e.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
