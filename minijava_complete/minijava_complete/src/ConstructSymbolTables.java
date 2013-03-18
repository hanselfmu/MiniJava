import main.BuildGlobalTableVisitor;
import main.BuildTableP2Visitor;
import main.BuildTableP3Visitor;
import Table.SymbolTable;
import AST.Program;

/*
 * Wrapper for Visitor classes which construct the Symbol Tables
 * @Author Oleg Godunok, Changhao Han
 */
public class ConstructSymbolTables {
	private Program program;
	private boolean error;

	public ConstructSymbolTables(Program program) {
		this.program = program;
		this.error = false;
	}

	// Calling this constructs the symbol tables.
	public SymbolTable Construct() {
		// pass 1 - construct global symbol table
		BuildGlobalTableVisitor v = new BuildGlobalTableVisitor();
		program.accept(v);

		// pass 2 - construct method symbol table, updates types assigned from
		// construction of abstract tree phase, and does a little type checking
		BuildTableP2Visitor pass2 = new BuildTableP2Visitor(v.getGlobalTable());
		program.accept(pass2);

		// pass 3 - deals with class inheritance - updates accordingly, and does
		// a little type checking
		BuildTableP3Visitor pass3 = new BuildTableP3Visitor(
				pass2.getGlobalTable());
		program.accept(pass3);
		error = pass2.error() || pass3.error();
		return pass3.getGlobalTable();
	}

	// If an error was discovered during the construction of them, then
	// true is returned, else false is
	public boolean error() {
		return error;
	}
}
