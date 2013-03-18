package AST;

import code_generation.CodeGenVisitor;
import main.SymbolTableVisitor;
import AST.Visitor.Visitor;

public class VarDecl extends ASTNode {
	public AST.Type type;
	public Identifier i;

	public VarDecl(Type at, Identifier ai, int ln) {
		super(ln);
		type = at;
		i = ai;
		i.t = type.t;
	}

	public void accept(Visitor v) {
		type.depth = i.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(SymbolTableVisitor v) {
		v.visit(this);
	}

	public void accept(CodeGenVisitor v) {
		v.visit(this);
	}

}
