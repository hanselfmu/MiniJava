package AST;

import java.util.List;
import java.util.ArrayList;

public class VarDeclList extends ASTNode {
   private List<VarDecl> list;

   public VarDeclList(int ln) {
      super(ln);
      list = new ArrayList<VarDecl>();
   }

   public void add(VarDecl n) {
      list.add(n);
   }

   public VarDecl get(int i)  { 
      return list.get(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
