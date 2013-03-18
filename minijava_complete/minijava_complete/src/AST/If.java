package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class If extends Statement {
	public Exp e;
	public Statement s1, s2;

	public If(Exp ae, Statement as1, Statement as2, int ln) {
		super(ln);
		e = ae;
		s1 = as1;
		s2 = as2;
	}

	public void accept(Visitor v) {
		e.depth = s1.depth = s2.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
