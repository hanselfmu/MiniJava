import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;

public class TestAST {
	public static void main(String[] args) {
		try {
			// create a scanner on the input file
			scanner s = new scanner(System.in);
			parser p = new parser(s);
			Symbol root;
			// replace p.parse() with p.debug_parse() in next line to see trace
			// of
			// parser shift/reduce actions during parse
			root = p.parse();
			System.out.println("done\n");
			Program program = (Program) root.value;
			program.accept(new PrintVisitor());
			System.out.println("Parsing completed");
		} catch (Exception e) {
			// yuck: some kind of error in the compiler implementation
			// that we're not expecting (a bug!)
			System.err.println("Unexpected internal compiler error: "
					+ e.toString());
			// print out a stack dump
			e.printStackTrace();
			System.exit(1);
		}
	}
}
