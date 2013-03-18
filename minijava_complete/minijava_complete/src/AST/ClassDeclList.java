package AST;
import java.util.List;
import java.util.ArrayList;

public class ClassDeclList extends ASTNode{
   private List<ClassDecl> list;

   public ClassDeclList(int ln) {
      super(ln);
      list = new ArrayList<ClassDecl>();
   }

   public void add(ClassDecl n) {
      list.add(n);
   }

   public ClassDecl get(int i)  { 
      return list.get(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
