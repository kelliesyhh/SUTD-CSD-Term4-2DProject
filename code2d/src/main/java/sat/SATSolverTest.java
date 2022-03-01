package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // https://people.sc.fsu.edu/~jburkardt/data/cnf/cnf.html


	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
//    public static void main(String[] args) {
////        String filepath = args[0];
//        String filepath = "/Users/kellie/Documents/50.001/2DProject/code2d/src/main/java/sat/s8Sat.cnf";
//        System.out.println(filepath);
//
////        // include this part for the first time you run the cnf, to obtain the desired output (just check if it's working)
////        PrintStream out = null;
////        try {
////            out = new PrintStream(new FileOutputStream("/Users/kellie/Documents/50.001/2DProject/code2d/src/main/java/sat/cnf-output.txt"));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        };
//
//        try {
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filepath));
//            int data = bufferedInputStream.read();
//            ImList<Literal> literalImList = new EmptyImList<>();
//            ImList<Clause> clauseImList = new EmptyImList<>();
//            // if data is -1, that means that it's the end of the file
//            while (data != -1) {
////                System.out.println((char) data + ": " + String.valueOf(data));
//                data = bufferedInputStream.read();
//
////                // include this part for the first time you run the cnf, to obtain the desired output (just check if it's working)
////                String output = (char) data + ": " + data + "\n";
////                out.print(output);
//
//                // if data is 32 (ASCII), it means a space
//                // if data is 10 (ASCII), it means a line break (\n)
//                // if data is 48 (ASCII), it means a 0.  clause is terminated by '0'
//                // if data is 99 (ASCII), it means c. c in cnf refers to comment lines
//                // if data is 112 (ASCII), it means p. p in cnf refers to problem lines
//
//                /* pseudocode:
//                we will first detect the presence of a line break.
//                if a line break is present, we will track the next thing that occurs right after the line break (i.e. p? or some other number)
//                if the next thing is a p, we carry on reading. else, we start to process it into the formula
//                */
//                while (data != 48) {
//                    data = bufferedInputStream.read();
//                    if (data == 99 || data == 112) {
//                        while (data != 32) {
//                            data = bufferedInputStream.read();
//                        }
//                    }
//                    else if (data != 99 && data != 112) {
//                        StringBuilder temp = new StringBuilder();
//                        // if data is 32, it means a space (signifies OR)
//                        // while you haven't reached a space, keep reading (eg: -16, -2, -55)
//                        while (data != 32) {
//                            temp.append((char) (data));
//                            data = bufferedInputStream.read();
//                        }
//                        if (data == 32) {
//                            data = bufferedInputStream.read();
//                            // if data is 45, it means '-' aka a negative literal.
//                            System.out.println((char) data + ": " + String.valueOf(data));
//                            if (data == 45) {
//                                System.out.println("-" + temp.toString());
//                                Variable variable = new Variable(temp.toString());
//                                Literal negLiteral = NegLiteral.make(variable);
//                                literalImList.add(negLiteral);
//                            } else if (data >= 49 && data <= 57) {
//                                System.out.println(temp.toString());
//                                Variable variable = new Variable(temp.toString());
//                                Literal posLiteral = PosLiteral.make(variable);
//                                literalImList.add(posLiteral);
//                            }
//                        }
//                    }
//                }
//
//
//                if (data == 48) {
//                    int i = 0;
//                    Literal[] literals = new Literal[literalImList.size()];
//                    while (literalImList.iterator().hasNext()) {
//                        literals[i] = literalImList.iterator().next();
//                        i += 1;
//
//                        System.out.println("LITERAL");
//                        System.out.println(literalImList.iterator().next());
//                    }
//
//                    clauseImList.add(makeCl(literals));
//                }
//            }
//            int i = 0;
//            Clause[] clauses = new Clause[clauseImList.size()];
//            while (clauseImList.iterator().hasNext()) {
//                clauses[i] = clauseImList.iterator().next();
//                i += 1;
//
//                System.out.println("CLAUSE");
//                System.out.println(clauseImList.iterator().next());
//            }
//            Formula formula = makeFm(clauses);
////            SATSolver.solve(formula);
//
//            }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
//        String filepath = args[0];
        String filepath = "/Users/kellie/Documents/50.001/2DProject/code2d/src/main/java/sat/s8Sat.cnf";
        System.out.println(filepath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                String line = reader.readLine();
                System.out.println(line);
                line = line.trim();
                while (line.length() >= 1) {
                    char firstChar = line.charAt(0);
                    if (firstChar == '-' || (firstChar >= 0 && firstChar <= 9)) {
                        // form a new literal
                        // add literal to list of literals
                        // form clause using makeCl
                        // add clause to list of clauses
                }
                    // else if first char is p or c, then do something else
                }
                // makeFm at the end of the line by using the list of clauses (eg: when 0 is detected)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}