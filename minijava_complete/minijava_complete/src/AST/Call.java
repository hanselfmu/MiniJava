package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class Call extends Exp {
	public Exp e;
	public Identifier i;
	public ExpList el;

	public Call(Exp ae, Identifier ai, ExpList ael, int ln) {
		super(ln);
		e = ae;
		i = ai;
		el = ael;
	}

	public void accept(Visitor v) {
		e.depth = i.depth = el.depth = this.depth + 1;
		for (int i = 0; i < el.size(); i++)
			el.get(i).depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
