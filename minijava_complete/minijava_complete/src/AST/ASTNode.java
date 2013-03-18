package AST;
import Type.Type;

abstract public class ASTNode {
  // Instance variables
  
  // The line number where the node is in the source file, for use
  // in printing error messages about this AST node
  protected int line_number;

  // The depth of this node 
  protected int depth;
  
  public Type t;
    
  // Constructor
  public ASTNode(int ln) {
    line_number = ln;
    depth = 0;
  }
  
  // Returns the line number of this node
  public int getLineNumber() {
    return line_number;
  }
  
  // Returns the depth of this node
  public int getDepth() {
    return depth;
  }
}
