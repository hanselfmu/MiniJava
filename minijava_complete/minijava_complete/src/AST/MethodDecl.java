package AST;

import java.util.ArrayList;
import java.util.List;

import code_generation.CodeGenVisitor;

import main.SymbolTableVisitor;
import main.TypeVisitor;

import Type.MethodType;
import Type.Type;
import AST.Visitor.Visitor;

public class MethodDecl extends ASTNode {
	public AST.Type type;
	public Identifier i;
	public FormalList fl;
	public VarDeclList vl;
	public StatementList sl;
	public Exp e;

	public MethodDecl(AST.Type at, Identifier ai, FormalList afl,
			VarDeclList avl, StatementList asl, Exp ae, int ln) {
		super(ln);
		type = at;
		i = ai;
		fl = afl;
		vl = avl;
		sl = asl;
		e = ae;
		List<Type> params = new ArrayList<Type>();
		for (int i = 0; i < fl.size(); i++)
			params.add(fl.get(i).i.t);
		i.t = new MethodType(i.toString(), type.t, params);
	}

	public void accept(Visitor v) {
		type.depth = i.depth = fl.depth = vl.depth = sl.depth = e.depth = this.depth + 1;
		for (int i = 0; i < fl.size(); i++)
			fl.get(i).depth = this.depth + 1;
		for (int i = 0; i < vl.size(); i++)
			vl.get(i).depth = this.depth + 1;
		for (int i = 0; i < sl.size(); i++)
			sl.get(i).depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(SymbolTableVisitor v) {
		for (int i = 0; i < vl.size(); i++)
			vl.get(i).depth = this.depth + 1;
		v.visit(this);
	}

	public void accept(TypeVisitor v) {
		v.visit(this);
	}

	public void accept(CodeGenVisitor v) {
		v.visit(this);
	}
}
