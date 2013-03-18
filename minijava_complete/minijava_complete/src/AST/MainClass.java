package AST;

import code_generation.CodeGenVisitor;
import main.GlobalTableVisitor;
import main.SymbolTableVisitor;
import main.TypeVisitor;
import Type.ClassType;
import AST.Visitor.Visitor;

public class MainClass extends ASTNode {
	public Identifier i1, i2;
	public Statement s;

	public MainClass(Identifier ai1, Identifier ai2, Statement as, int ln) {
		super(ln);
		i1 = ai1;
		i2 = ai2;
		s = as;
		ai1.t = new ClassType(null, i1.toString(), null);
	}

	public void accept(Visitor v) {
		i1.depth = i2.depth = s.depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(SymbolTableVisitor v) {
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(GlobalTableVisitor v) {
		v.visit(this);
	}

	public void accept(CodeGenVisitor v) {
		v.visit(this);
	}
}
