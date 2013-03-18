import code_generation.CGVisitor;
import code_generation.CodeGenVisitor;
import main.ComputeOffset;
import main.TypeCheckVisitor;
import java_cup.runtime.Symbol;
import AST.Program;
import Parser.parser;
import Scanner.scanner;
import Table.SymbolTable;

/*
 * MiniJava compiler which takes in a java source program from standard
 * input and writes the compiled x86-64 assembly language code on standard
 * output
 * @Author Oleg Godunok, Changhao Han
 */
public class MiniJava {
	public static void main(String[] args) {
		try {
			// create a scanner which takes input from System.in
			scanner s = new scanner(System.in);
			parser p = new parser(s);
			Symbol root;
			// replace p.parse() with p.debug_parse() in next line to see trace
			// of
			// parser shift/reduce actions during parse
			root = p.parse(); // parses the program
			Program program = (Program) root.value;

			ConstructSymbolTables construct = new ConstructSymbolTables(program);
			// construct the symbol tables
			SymbolTable global = construct.Construct();

			// Finish calculating the offsets of the methods and variables
			// of the classes
			ComputeOffset comp = new ComputeOffset(global);
			comp.updateOffsets();

			// perform semantic analysis on the program errors will merely
			// be printed to output
			TypeCheckVisitor type_check = new TypeCheckVisitor(global);
			program.accept(type_check);

			boolean err = type_check.error() || construct.error();

			// an error was discovered during the type checking phase
			// so exit with a non-zero return code
			if (err)
				System.exit(1);

			// Generate the assembly which is written to standard output
			CGVisitor codegen = new CodeGenVisitor(global);
			codegen.visit(program);
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
