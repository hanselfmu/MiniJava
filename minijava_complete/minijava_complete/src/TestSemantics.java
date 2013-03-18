import java_cup.runtime.Symbol;
import main.TypeCheckVisitor;
import AST.Program;
import Parser.parser;
import Scanner.scanner;
import Table.SymbolTable;

public class TestSemantics {
	public static void main(String[] args) {
        try {
            // create a scanner which takes input from System.in
            scanner s = new scanner(System.in);
            parser p = new parser(s);
            Symbol root;
	         // replace p.parse() with p.debug_parse() in next line to see trace of
	         // parser shift/reduce actions during parse
            root = p.parse(); // parses the program
            Program program = (Program)root.value;
            
            ConstructSymbolTables construct = new ConstructSymbolTables(program);
            // construct the symbol tables
            SymbolTable global = construct.Construct();
            
            // perform semantic analysis on the program
            // errors will merely be printed to output 
            TypeCheckVisitor type_check = new TypeCheckVisitor(global);
            program.accept(type_check);
            boolean err = type_check.error() || construct.error();
            String result = err ? "an error(s) was found" : "no errors";
            System.out.println("Semantic Analysis complete: " + result);
            
            // print out the symbol tables 
            System.out.println("Symbol Tables:");
            System.out.println();
            System.out.println(global);
            // an error was discovered during the type checking phase 
            // so exit with a non-zero return code
            if(err) System.exit(1);
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                               e.toString());
            // print out a stack dump
            e.printStackTrace();
            System.exit(1);
        }
	}
} 