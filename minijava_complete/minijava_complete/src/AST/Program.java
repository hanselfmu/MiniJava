package AST;

import code_generation.CodeGenVisitor;
import main.GlobalTableVisitor;
import main.SymbolTableVisitor;
import main.TableP3Visitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class Program extends ASTNode {
	public MainClass m;
	public ClassDeclList cl;

	public Program(MainClass am, ClassDeclList acl, int ln) {
		super(ln);
		m = am;
		cl = acl;
	}

	public void accept(Visitor v) {
		m.depth = cl.depth = this.depth + 1;
		for (int i = 0; i < cl.size(); i++)
			cl.get(i).depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(GlobalTableVisitor v) {
		v.visit(this);
	}

	public void accept(SymbolTableVisitor v) {
		v.visit(this);
	}

	public void accept(TableP3Visitor v) {
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CodeGenVisitor v) {
		v.visit(this);
	}
}
