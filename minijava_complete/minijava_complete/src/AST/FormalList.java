package AST;
import java.util.List;
import java.util.ArrayList;

public class FormalList extends ASTNode {
   private List<Formal> list;

   public FormalList(int ln) {
      super(ln);
      list = new ArrayList<Formal>();
   }

   public void add(Formal n) {
      list.add(n);
   }

   public Formal get(int i)  { 
      return list.get(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
