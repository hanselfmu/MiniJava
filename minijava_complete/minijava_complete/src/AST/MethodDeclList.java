package AST;
import java.util.List;
import java.util.ArrayList;

public class MethodDeclList extends ASTNode {
   private List<MethodDecl> list;

   public MethodDeclList(int ln) {
      super(ln);
      list = new ArrayList<MethodDecl>();
   }

   public void add(MethodDecl n) {
      list.add(n);
   }

   public MethodDecl get(int i)  { 
      return list.get(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
