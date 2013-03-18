package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class While extends Statement {
	public Exp e;
	public Statement s;

	public While(Exp ae, Statement as, int ln) {
		super(ln);
		e = ae;
		s = as;
	}

	public void accept(Visitor v) {
		e.depth = s.depth = this.depth + 1;
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
