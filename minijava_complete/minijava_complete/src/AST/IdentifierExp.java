package AST;
import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public class IdentifierExp extends Exp {
  public String s;
  public IdentifierExp(String as, int ln) { 
    super(ln);
    s=as;
  }

  public void accept(Visitor v) {
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

