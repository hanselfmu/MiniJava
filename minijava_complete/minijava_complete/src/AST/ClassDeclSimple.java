package AST;

import code_generation.CGVisitor;
import main.GlobalTableVisitor;
import main.SymbolTableVisitor;
import main.TableP3Visitor;
import main.TypeVisitor;
import Type.ClassType;
import AST.Visitor.Visitor;

public class ClassDeclSimple extends ClassDecl {
	public Identifier i;
	public VarDeclList vl;
	public MethodDeclList ml;

	public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml,
			int ln) {
		super(ln);
		i = ai;
		vl = avl;
		ml = aml;
		i.t = new ClassType(null, i.toString(), null);
	}

	public void accept(Visitor v) {
		i.depth = vl.depth = ml.depth = this.depth + 1;
		for (int i = 0; i < vl.size(); i++)
			vl.get(i).depth = this.depth + 1;
		for (int j = 0; j < ml.size(); j++)
			ml.get(j).depth = this.depth + 1;
		v.visit(this);
	}

	@Override
	public void accept(SymbolTableVisitor v) {
		for (int i = 0; i < vl.size(); i++)
			vl.get(i).depth = this.depth + 1;
		for (int j = 0; j < ml.size(); j++)
			ml.get(j).depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	@Override
	public void accept(GlobalTableVisitor v) {
		v.visit(this);

	}

	@Override
	public void accept(TableP3Visitor v) {
		v.visit(this);
	}

	public void accept(CGVisitor v) {
		v.visit(this);
	}
}
