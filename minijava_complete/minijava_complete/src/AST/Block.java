package AST;

import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class Block extends Statement {
	public StatementList sl;

	public Block(StatementList asl, int ln) {
		super(ln);
		sl = asl;
	}

	public void accept(Visitor v) {
		sl.depth = this.depth + 1;
		for (int i = 0; i < sl.size(); i++)
			sl.get(i).depth += this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
