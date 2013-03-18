package AST;

import code_generation.CGVisitor;
import main.GlobalTableVisitor;
import main.SymbolTableVisitor;
import main.TableP3Visitor;
import main.TypeVisitor;
import AST.Visitor.Visitor;

public abstract class ClassDecl extends ASTNode{
  public ClassDecl(int ln) {
    super(ln);
  }
  public abstract void accept(Visitor v);
  
  public abstract void accept(SymbolTableVisitor buildTablesVisitor);
  public abstract void accept(TypeVisitor typeCheckVisitor);
  public abstract void accept(GlobalTableVisitor v);
  public abstract void accept(TableP3Visitor v);
  public abstract void accept(CGVisitor v); 
}
