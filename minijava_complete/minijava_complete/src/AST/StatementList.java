package AST;

import java.util.List;
import java.util.ArrayList;

public class StatementList extends ASTNode {
   private List<Statement> list;

   public StatementList(int ln) {
      super(ln);
      list = new ArrayList<Statement>();
   }

   public void add(Statement n) {
      list.add(n);
   }

   public Statement get(int i)  { 
      return list.get(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
