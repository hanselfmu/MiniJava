/*import java_cup.runtime.Symbol;
import Parser.sym;
import Scanner.scanner;

public class TestScanner {
    public static void main(String [] args) {
        try {
            // create a scanner on the input file
            scanner s = new scanner(System.in);
            Symbol t = s.next_token();
            while (t.sym != sym.EOF){ 
                // print each token that we scan
                System.out.print(s.symbolToString(t) + " ");
                t = s.next_token(); 
            }
            System.out.print("\nLexical analysis completed"); 
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                        e.toString());
            // print out a stack dump
            e.printStackTrace();
        }
   }
}*/
//import java.io.*;
//import java.util.*;

import Scanner.*;
import Parser.sym;
import java_cup.runtime.Symbol;

public class TestScanner {
    public static void main(String [] args) {
        try {
        	
        	/*
        	if(args.length != 2) {
        		System.err.println("Format: ./TestScanner java_source output_file");
        		System.exit(1);
        	}
            Reader fileIn  = new BufferedReader(new FileReader(args[0]));
            PrintWriter  fileOut = new PrintWriter(new BufferedWriter(new FileWriter(args[1]))); 
            // create a scanner on the input file
            scanner s = new scanner(fileIn); */
        	
            scanner s = new scanner(System.in);
            Symbol t = s.next_token();
            while (t.sym != sym.EOF){ 
                // print each token that we scan
                System.out.print(s.symbolToString(t) + " ");
                //fileOut.print(s.symbolToString(t) + " ");
                t = s.next_token(); 
            }
            //fileIn.close();
            //fileOut.close();
            System.out.print("\nLexical analysis completed"); 
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                        e.toString());
            // print out a stack dump
            e.printStackTrace();
        }
   }
}



