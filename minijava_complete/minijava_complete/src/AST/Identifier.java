package AST;

import code_generation.CodeGenVisitor;
import AST.Visitor.Visitor;

public class Identifier extends ASTNode {
	public String s;

	public Identifier(String as, int ln) {
		super(ln);
		s = as;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public String toString() {
		return s;
	}

	public void accept(CodeGenVisitor v) {
		v.visit(this);
	}
}
