package code_generation;

import java.util.HashMap;
import java.util.Map;
import Table.Binding;
import Table.SymbolTable;
import Type.ClassType;
import Type.MethodType;
import AST.And;
import AST.ArrayAssign;
import AST.ArrayLength;
import AST.ArrayLookup;
import AST.Assign;
import AST.Block;
import AST.Call;
import AST.ClassDeclExtends;
import AST.ClassDeclSimple;
import AST.Display;
import AST.Exp;
import AST.ExpList;
import AST.False;
import AST.Formal;
import AST.Identifier;
import AST.IdentifierExp;
import AST.If;
import AST.IntegerLiteral;
import AST.LessThan;
import AST.MainClass;
import AST.MethodDecl;
import AST.Minus;
import AST.NewArray;
import AST.NewObject;
import AST.Not;
import AST.Plus;
import AST.Print;
import AST.Program;
import AST.This;
import AST.Times;
import AST.True;
import AST.VarDecl;
import AST.While;

/* 
 * Visitor which generates x86-64 assembly language code suitable for 
 * input to the GNU assembly and writes the compiled code to standard 
 * input.
 * @Author Oleg Godunok, Changhao Han
 */

public class CodeGenVisitor implements CGVisitor {
	// true if the stack is 16-byte aligned else false
	private boolean align; 
	
	private SymbolTable top; // the global SymbolTable
	
	private SymbolTable namespace; 	// the current SymbolTable
	
	// mapping: label -> (# of times used)
	private Map<String,Integer> labels = new HashMap<String,Integer>();
	
	// the number of bytes allocated on the stack
	private int stack_mem;
	
	public CodeGenVisitor(SymbolTable namespace) {
		this.namespace = namespace;
		this.top = namespace;
		this.stack_mem = 0;
		// stack_mem %16 = 0 => stack is 16-byte aligned
		this.align = false; 
	}
	
	// Display added for toy example language. Not used in regular MiniJava
	public void visit(Display n) {
		n.e.accept(this);
		pushq("%rdi");
		gen("movq", "%rax","%rdi");
		align("%rax");
	    call("put");
		align("%rdx");
		popq("%rdi");
	}

	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		gen("\t" + ".text");
		gen("\t" + ".globl asm_main");
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.get(i).accept(this);
		}
	}

	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {		
		// main method label
		gen("asm_main:");
		prologue();
		n.s.accept(this);
		epilogue();
		
		// dispatch table for the main class
		System.out.println();
		gen("\t" + ".data");
		gen(n.i1.s + "$$: .quad 0");
		System.out.println();
	}
	
	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {	
		namespace = namespace.scopes.get(n.i.s);		
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		}
		vtable("0"); // no superclass (null pointer)
		namespace = namespace.prev;
	}
	
	// Generates assembly for the virtual method table for the class
	// specified by the current "namespace". 
	// base: the base class 
	private void vtable(String base) {
		// the number of methods and fields this class contains
		int num_methods = 0;
		int num_vars = 0;
		for(Binding element : namespace.table.values()) {
			if(element.type instanceof MethodType) {
				num_methods++;
			} else {
				num_vars++;
			}
		}
		// Used to place the methods and fields in the "correct order"
		// (i.e. used for O(1) method dispatch 
		Binding[] methods = new Binding[num_methods];
		Binding[] fields = new Binding[num_vars];
		
		for(Binding class_b : namespace.table.values()) {
			if(!(class_b.type instanceof MethodType)) { // for fields 
				// fields start at offset 8
				if(num_vars > 0) fields[(int)((class_b.offset-8)/8)] = class_b;
			} else { // for methods
				// method pointers start at offset 8 (in vtable)
				if(num_methods > 0) methods[(int)((class_b.offset-8)/8)] = class_b;
			}
		}
		
		// generate the constructor
		constructor(8 + 8*num_vars, namespace.name, base);
		System.out.println();
		
		// generate the methods for the method dispatch table
		gen("\t" + ".data");
		String after = base.equals("0") ? "" : "$$";
		genTableLabel(namespace.name, base + after);
		gen("\t" + ".quad " + namespace.name + "$" + namespace.name);
		for(Binding method_b : methods) {
			gen("\t" + ".quad " +  method_b.belonging + "$" + method_b.getID());
		}
		
		System.out.println();
	}
	
	// Generates the label for the virtual method table of class i with 
	// base class j (j will be "0" if Object is the base class)
	private void genTableLabel(String i, String j) {
		gen(i + "$$: .quad " + j);
	}

	// Generates assembly for the constructor of the class "cid"
	// base : the base class of cid
	// total_mem: the amount of bytes to allocate for an object of type
	// "cid"
	private void constructor(int total_mem, String cid, String base) {
		// generate the label for the constructor 
		gen(cid + "$" + cid + ":");
		prologue();
		// save the contents of %rdi
		pushq("%rdi");
		gen("movq", total_mem, "%rdi");
	
		align("%rax");
		call("mjmalloc"); // allocate 8*(e+1) space. pointer to object in %rax
		undo("%rdx");
		popq("%rdi");
		// set pointer to base class dispatch table. 
		gen("leaq", cid + "$$", "%rdx");
		gen("movq", "%rdx", "(%rax)");
		
		// set all of the fields to zero (initialize the object)
		for(int i = 8; i < total_mem; i += 8) { 
			gen("movq", 0, i + "(%rax)");
		}
		epilogue(); 
	}
	
	/* Generates code for the prologue of a method */
	private void prologue() {
		pushq("%rbp");
		gen("movq", "%rsp", "%rbp");
	}
	
	/* Generates code for the epilogue of a method */
	private void epilogue() {
		gen("movq", "%rbp", "%rsp");
		popq("%rbp");
		gen("ret", "");
	}
	
	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
		namespace = namespace.scopes.get(n.i.s); // down to class scope
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.get(i).accept(this);
		} 
		vtable(n.j.s); // generate the method dispatch table for this class
		namespace = namespace.prev; // back upto global scope

	}

	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public void visit(MethodDecl n) {
		namespace = namespace.scopes.get(n.i.s); // down to method scope
		
		// generate method label
		gen(namespace.prev.name + "$" + namespace.name + ":"); // class$method: 
		prologue();
		// allocate space for the variable on the stack
		if(n.vl.size() > 0) gen("subq", 8*n.vl.size(), "%rsp");
	
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.get(i).accept(this);
		}
		// "return" value of the method will be in %rax
		n.e.accept(this); 
		epilogue();

		namespace = namespace.prev; // back up to class scope
	}

	// Type t;
	// Identifier i;
	public void visit(Formal n) {}

	// StatementList sl;
	public void visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.get(i).accept(this);
		}
	}

	// Exp e;
	// Statement s1,s2;
	public void visit(If n) { 
		// generate the labels ////////////////////////
		String else_ = label("else");
		String done_ = label("done");
		String other = label("other");
		//////////////////////////////////////////////
		
		// generate and propagate the information for control flow 
		// to the expression 
		n.e.sense = false;
		n.e.target = else_;
		n.e.other = other;
		// IF ( 
		n.e.accept(this);
		boolJump(n.e, else_);
		if(n.e.used) gen(other + ":");
		// ) { 
		n.s1.accept(this);
		// } ELSE {
		gen("jmp", done_);
		gen(else_ + ":");
		n.s2.accept(this);
		// }
		gen(done_ + ":");
	}

	// Exp e;
	// Statement s;
	public void visit(While n) {
		// generate the labels /////////////////////////
		String done_ = label("done");
		String test_ = label("test");	
		String body = label("body");
		///////////////////////////////////////////////
		
		gen(test_ + ":");
		
		// generate and propagate the information for control flow 
		// to the expression 
		n.e.sense = false;
		n.e.target = done_;
		n.e.other = body;
		
		// TEST /////////////////////////////////////
		n.e.accept(this);
		boolJump(n.e, done_);
		if(n.e.used) gen(body + ":");
		// end TEST //////////////////////////////////
		
		// BODY /////////////////////////////////////
		n.s.accept(this);
		// end BODY /////////////////////////////////
		
		gen("jmp", test_); // test if can terminate the loop
		gen(done_ + ":");
	}
	
	// Generates code for jumping to "jump" given that e is a "simple"
	// boolean expression - doesn't contain "<" or "&&" - if the expression
	// evaluates to 0
	private void boolJump(Exp e, String jump) {
		if(e instanceof True          || 
		   e instanceof False         || 
		   e instanceof IdentifierExp || 
		   e instanceof Call ||
		   e instanceof Not) {
			if(e instanceof Not && e.used == true) return;
			gen("cmpq", 0, "%rax");
			gen("je", jump);
		} 
	}

	// Exp e;
	public void visit(Print n) { 
		n.e.accept(this);
		// store the contents of %rdi 
		pushq("%rdi");
		gen("movq", "%rax","%rdi");
		align("%rax");
		// System.out.println(R[%rax]);
	    call("put"); 
		align("%rdx");
		popq("%rdi");
	}

	    // Identifier i;
		// Exp e;
		public void visit(Assign n) { // i = e;
			// Since the variable indicated by i is declared, it is present in 
			// some SymbolTable. We can extract the offset of it, from the 
			// its Binding
			Binding i_binding = namespace.get(n.i.s);
			// the offset of i
			int offset = i_binding.offset;
			String sign = (offset < 0) ? "" : "-";
			offset = Math.abs(offset);
			
			// generate and propagate the information for control flow to the 
			// expression (relevant only if its a boolean expression)
			String genfalse = label("genfalse");
			String storeit = label("storeit");
			n.e.sense = false;
			n.e.target = genfalse;
			n.e.other = storeit;
			n.e.accept(this);
			
			// we jump to the proper label if e is a boolean expression
			if(bexp(n.e)) assignJump(storeit, genfalse);
			
			// if the expression is true then it belongs to the current SymbolTable 
			// (class namespace), else it belongs to the  namespace of a class (i.e. 
			// it's a field)
			  if(namespace.table.get(n.i.s) == null) {
					  gen("movq", "%rax", offset + "(%rdi)"); // movq e,-offset(%rbp)
			  } else { 
					gen("movq", "%rax", sign + offset + "(%rbp)");
			  }	 
		}
		
		// We use this method to handle the assignment of a variable to 
		// a boolean expressions. For the assingnment "i = e;", we propagate 
		// these labels (along with a "sense") to the (boolean) expression.
		// We do not want to store 0s and 1s, but rather use boolean expressions 
		// for control flow (so we jump to genfalse to store a 1, ...)
		// - storeit:the label which we jump to assign the value to 
		// the variable
		// - genfalse: the label which generates a 0
		private void assignJump(String storeit, String genfalse) {
			gen("movq", 1, "%rax");
			gen("jmp", storeit);
			gen(genfalse + ":");
			gen("movq", 0, "%rax");
			gen(storeit + ":");
		}
	
	// Identifier i;
	// Exp e1,e2;
	public void visit(ArrayAssign n) { 	// i[e1] = e2
		// the offset of the variable 
		int offset = namespace.get(n.i.s).offset;
		String sign = (offset <= 0) ? "" : "-";
		offset = Math.abs(offset);
		
		n.e1.accept(this);
		pushq("%rax");
		n.e2.accept(this); 
		popq("%rdx"); // r-expression in %rax, index in %rdx
		
		// if the expression is true then it belongs to the current SymbolTable 
		// (class namespace), else it belongs to the  namespace of a class (i.e. 
		// it's a field)
		if(namespace.get(n.i.s).belonging.equals(namespace.name)) {
			gen("movq", sign + offset + "(%rbp)", "%rcx");
		} else {
			gen("movq", offset + "(%rdi)", "%rcx");
		}
		gen("movq", "%rax", "8(%rcx,%rdx,8)"); 
	}

	// Exp e1,e2;
	public void visit(And n) {
		boolean not = n.used;

		// propagate the states to e1 and e2 ///////////
		n.e1.sense = n.e2.sense = n.sense;
		n.e1.target = n.e2.target = n.target;
		n.e1.other = n.e2.other = n.other;
		n.e1.used = n.e2.used = not;
		//////////////////////////////////////////////
		
		n.e1.accept(this);
		// boolean not the contents of %rax if the parent is Not
		if(not) gen("xor", 1, "%rax");
		
		gen("cmpq", 0, "%rax"); 
		String jump = n.sense ? "jne" : "je";
		gen(jump, n.target);
		
		n.e2.accept(this); 
		// boolean not the contents of %rax if the parent is Not
		if(not) gen("xor", 1, "%rax");
		
		n.used = not;
		gen("cmpq", 0, "%rax");
        gen(jump, n.target);   
        // if the parent is Not, then "this" boolean expression
        // is equivalent to an boolean or 
		if(not) gen("jmp", n.other);
	}
	
	
	// Exp e1,e2;
	public void visit(LessThan n) { //  cmp s2, s1 == s1-s2
		n.e1.accept(this);
		pushq("%rax");
		n.e2.accept(this);
		popq("%rdx");
		gen("cmpq", "%rax", "%rdx"); // %rdx-%rax = e1-e2
		// if the sense is true then the jump instruction is jl
		// else it is jge
		String jump = n.sense ? "jl" : "jge";
		gen(jump, n.target);
	}

	// Exp e1,e2;
	public void visit(Plus n) { // e1 + e2
		n.e1.accept(this);
		pushq("%rax");
		n.e2.accept(this); // pop e1 off the stack into %rdx
		popq("%rdx");
		gen("addq", "%rdx", "%rax");
	}

	// Exp e1,e2;
	
	public void visit(Minus n) { // e1 - e2
		n.e1.accept(this);		
		pushq("%rax");
		n.e2.accept(this); // pop e1 off the stack into %rdx
		popq("%rdx");
		gen("negq", "%rax"); // negate e2 (which is in %rax)
		gen("addq", "%rdx", "%rax"); // gen(-e2+e1) 
	}

	// Exp e1,e2;
	
	public void visit(Times n) { // e1 * e2
		n.e1.accept(this);
		pushq("%rax");
		n.e2.accept(this);
		popq("%rdx");
		// signed multiplication of R[%rdx] and R[%rax] and store in %rax
		gen("imulq", "%rdx", "%rax"); 
	}

	// Exp e1,e2;
	
	public void visit(ArrayLookup n) { // e1[e2] 
		n.e1.accept(this);
		pushq("%rax");
		n.e2.accept(this);
		popq("%rdx");
		gen("movq", "8(%rdx,%rax,8)", "%rax"); // M[8 + offset[e1] + 8*e2]
	}
	
	// Exp e;
	public void visit(ArrayLength n) { // e.length
		n.e.accept(this);
		// the expression specified by e may be a variable or an r-value
		// since the length of the array is stored at offset 0 from the 
		// pointer, we don't have to worry about that
		gen("movq", "(%rax)", "%rax");
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public void visit(Call n) { // e.i(el);
		// save a copy of "this"
		pushq("%rdi");
		// we store "this" into %rcx in case an argument is "this"
		gen("movq", "%rdi", "%rcx");
		
		n.e.accept(this);
		if(!(n.e instanceof This)) gen("movq", "%rax", "%rdi");
	
		// aligns the stack BEFORE pushing the arguments onto the stack
		if((stack_mem + 8*n.el.size())%16 != 0) align("%rax"); 
		
		pushArguments(n.el);
		
		ClassType e_type = (ClassType)n.e.t;
		// obtain the method Binding from the correct class SymbolTable
		Binding mb = top.scopes.get(e_type.type).get(n.i.s);
		// move vtable pointer to %rax 
		gen("movq", "(%rdi)", "%rax"); 
		gen("lea", 8 + mb.offset + "(%rax)", "%rax");
		// call the  correct method
		gen("call", "*(%rax)");  
		
		popArguments(n.el, "%rdx");
		
		undo("%rdx");
		popq("%rdi");
	}

	// Pushes the arguments onto the stack from left to right
	private void pushArguments(ExpList el) {
		for (int i = 0; i < el.size(); i++) {
			el.get(i).accept(this);
			String register = (el.get(i) instanceof This) ? "%rcx" : "%rax";
			pushq(register);
		}
	}
	
	// Pops all the arguments of the stack into the register
	// "register"
	private void popArguments(ExpList el, String register) {
		for (int i = 0; i < el.size(); i++) popq(register);
	}
	
	// int i;
	public void visit(IntegerLiteral n) { 
		// optimization: if constant is zero 
		if(n.i == 0) {
			gen("xor", "%rax", "rax");
		} else {
			gen("movq", n.i, "%rax");
		}
	}

	public void visit(True n) {
		 // false will be represent as the constant 0
       gen("movq", 1, "%rax");
}

   public void visit(False n) {
	 // false will be represent as the constant 0
     gen("movq", 0, "%rax");
   }
	
	// String s;
	public void visit(IdentifierExp n) { 
		// the offset of the variable 
		int offset = namespace.get(n.s).offset;
		
		String sign = (offset <= 0) ? "" : "-";
		offset = Math.abs(offset);
		
		// if the expression is true then it belongs to the current SymbolTable 
		// (class namespace), else it belongs to the  namespace of a class (i.e. 
		// it's a field)
		if(namespace.get(n.s).belonging.equals(namespace.name)) {
			gen("movq", sign + offset + "(%rbp)", "%rax");
		} else {
			gen("movq", offset + "(%rdi)", "%rax");
		}
	}

	public void visit(This n) {
		// "this" is always contained in the register %rdi
		gen("movq", "%rdi", "%rax");
	}

	// Exp e;
	public void visit(NewArray n) { // new int[e];	
		n.e.accept(this); 
		newArray("%rax");
	} 
	
	// Creates a new array which, on return, is contained in the register 
	// "register". At offset zero of the pointer is the  the length of the 
	// array; at offset 8+ are the contents of the array
	private void newArray(String register) {
		pushq("%rax");
		gen("incq", "%rax"); // e+1
		
		// acquire a pointer to e+1 bytes of memory
		allocateArray("%rax");
		
		// set up for loop
		popq("%rdx"); // the length of the array
		gen("movq", "%rdx", "(%rax)"); // set the length of the array
		gen("movq", 8, "%rcx"); // current index = 1
		pushq("%rax"); // push the pointer to the array unto the stack
		
		initializeArray("%rdx", "%rcx", "%rax");
		
		popq(register); // pop array pointer back into "register"
	}
	
	// Allocates memory corresponding to the number of bytes to be allocated
	// in the register "bytes_r" and stores the pointer to the memory in %rax
	// (e.g. if 10 is in %rax and allocateArray("%rax") is called, then a pointer 
	// to 80 bytes will be in %rax when the function returns)
	private void allocateArray(String bytes_r) {
		gen("shlq" , 3, "%rax"); // 8*(e+1)
		pushq("%rdi");
		gen("movq", "%rax","%rdi");
		align("%rax"); // align if not stack is not 16-bytes aligned
		call("mjmalloc"); // allocate 8*(e+1) space. pointer to array in %rax
		undo("%rdx");
		popq("%rdi");
	}
	
	
	// Initializes the array to all zero values (array[i] = 0 for 0 <= i < array.length)
	// - length_r: the register which contains the length of the array
	// - index_r: the register which contains the initial index
	// - array_r: the register containing the pointer to the array
	private void initializeArray(String length_r, String index_r, String array_r) {
		String done = label("done");
		String test = label("test");
		gen(test + ":");
		
		// TEST
		gen("testq", 0, length_r); // compare length to 0
		// end TEST
		
		gen("je", done); // checks that 0 = length
		
		// BODY
		gen("addq", index_r, array_r);
		gen("movq", 0, "(" + array_r + ")");  // sets arr[index] = 0
		gen("shlq", 2, index_r); // index = index + 1
		gen("decq", length_r);	// length = length - 1
		// end BODY
		
		gen("jmp", test);
		gen(done + ":");
	}

	// Identifier i;
	public void visit(NewObject n) { // new i();
		align("%rax");
		// call appropriate constructor
		call(n.i.s, n.i.s); // object pointer will be in %rax
		undo("%rdx");
	}
	
	// Exp e;
	public void visit(Not n) { 
		// reverse the sense
		n.e.sense = !n.sense;
		
		// propagate the targets to the children; reverse if 
		// the expression is And
		n.e.target = (n.e instanceof And || n.e instanceof Not) ? n.other : n.target;
		n.e.other =  (n.e instanceof And || n.e instanceof Not) ? n.target : n.other;
		
		// sets "used" of the expression to true if it's And indicating that the
		// "other" label will be used
		n.e.used = (n.e instanceof And) ? true : false;
		n.e.accept(this);
		n.used = n.e.used;
		
		// simple expressions do not jump, so Not has to boolean not 
		// the value of the expression 
		if(n.e instanceof True          || 
		   n.e instanceof False         ||
           n.e instanceof IdentifierExp || 
           n.e instanceof Call) {
			gen("xor", 1, "%rax");
		}
	}
	
	// String s;
	public void visit(Identifier n) {}
	
	// private function which aligns the stack if it is not 16-bytes aligned
	private void align(String register) {
		if(stack_mem%16 != 0) {
			gen("pushq", register);
			stack_mem += 8;
			align = true;
		}
	}
	
	// The complement function of of align, which undoes the alignment
	// this is primarily called after a call to a function
	private void undo(String register) {
		if(align) {
			gen("popq", register);
			stack_mem -= 8;
			align = false;
		}
	}
	
	// Returns true if e is a complex boolean expression
	// else false
	private boolean bexp(Exp e) {
		return 
		(e instanceof And   	|| 
		 e instanceof Not   	|| 
		 e instanceof LessThan);
	}
	
	// Generates code for "call class$method"
	private void call(String class_, String method_) {
		gen("call", class_ + "$" + method_);
	}
	
	// Generates code for "call method"
	private void call(String method) {
		gen("call", method);
	}
	
	// Function which generates code for pushq and increments
	// the total stack memory by 8
	private void pushq(String register) {
		gen("pushq", register);
		stack_mem += 8;
	}
	
	// Function which generates code for popq and decrements
	// the total stack memory by 8
	private void popq(String register) {
		gen("popq", register);
		stack_mem -= 8;
	}
	
	/////////////////////// functions for generating the assembly ////////////////////
	
	// Writes compiled code to standard output
	private void gen(String s) {
		System.out.println(s);
	}
	
	// Generates code for "instruction i,j"
	private void gen(String instruction, String i, String j) {
		gen("\t" + instruction + " " + i + "," + j);
	}
	
	// Generates code for "instruction $constant,j"
	private void gen(String instruction, int constant, String j) {
		gen("\t" + instruction + " " + "$" + constant + "," + j);
	}
	
	// Generates code for "instruction i"
	private void gen(String instruction, String i) {
		gen("\t" + instruction + " " + i);
	}
 		
    //////////////////////////////////////////////////////////////////////////////////
	
	// function which generates a label for "s" of the form "si" where i indicates
	// the number of times the label has been "generated"
	private String label(String s) {
		// if the label specified by s, "label", is present 
		// than increment it's count, i, and return "labeli"
		if(labels.containsKey(s)) {
			int count = labels.get(s);
			labels.put(s, count+1);
			return s + (count + 1);
		}
		
		// the label is not present so map it to 1 and return
		// "label1"
		labels.put(s, 1);
		return s + 1;
	}
}