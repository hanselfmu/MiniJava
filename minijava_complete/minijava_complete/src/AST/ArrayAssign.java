package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class ArrayAssign extends Statement {
	public Identifier i;
	public Exp e1, e2;

	public ArrayAssign(Identifier ai, Exp ae1, Exp ae2, int ln) {
		super(ln);
		i = ai;
		e1 = ae1;
		e2 = ae2;
	}

	public void accept(Visitor v) {
		i.depth = e1.depth = e2.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
