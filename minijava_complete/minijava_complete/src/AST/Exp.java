package AST;
import code_generation.CGVisitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public abstract class Exp extends ASTNode {
	public boolean sense;
	public boolean used; // true if other is used
	public String target; 
	public String other; 
	
    public Exp(int ln) {
        super(ln);
    }
    
    public abstract void accept(Visitor v);
	public abstract void accept(TypeVisitor v);
	public abstract void accept(CGVisitor v);
}
