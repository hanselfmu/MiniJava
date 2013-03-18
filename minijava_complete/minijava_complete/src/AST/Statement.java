package AST;
import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public abstract class Statement extends ASTNode {
    public Statement(int ln) {
        super(ln);
    }
    public abstract void accept(Visitor v);
	public abstract void accept(TypeVisitor v);
	public abstract void accept(CGVisitor v);
}
