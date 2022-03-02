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
import java.text.ParseException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import immutable.EmptyImList;
import immutable.ImList;
import immutable.ImListIterator;
import immutable.NonEmptyImList;
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
        String filepath = "C:\\Users\\Razer\\OneDrive - Singapore University of Technology and Design\\SUTD\\Y1\\Term 4\\50.002 Computation Structures\\2D\\50001_Project-2D-starting\\sampleCNF\\s8Sat.cnf";

        try {
            InputStream inputStream = new FileInputStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // clauses LL stores our clauses parsed from the file
            LinkedList<Clause> clauses = new LinkedList<>();

            // literals stores each literal until we hit a EOL character '0', then it is
            // added to a Clause, and added to clauses LL
            LinkedList<Literal> literals = new LinkedList<>();

            // Stores the current line we are looking at
            String line;

            // Used to store the literals in our literals LL once we hit a EOL char
            Clause currentClause = new Clause();

            // Read until EOF
            while ((line = reader.readLine()) != null) {
                // Check that the line is a valid clause (not empty, not problem statement, not comment)
                if (line.length() > 0 && line.charAt(0) != 'p' && line.charAt(0) != 'c') {
                    // Remove any potential extra whitespace and split
                    String[] splitLine = line.trim().split(" ");

                    /*
                    - If the item has a 0, we consider it as EOL, and purge our literals LL to a clause
                    - Else if the item starts with a 0, we add a negative literal named after that term
                    - Else we add a positive literal named after that term
                     */
                    for (String item : splitLine) {
                        char i = item.charAt(0);
                        if (i == '0') {
                            for (Literal l : literals) {
                                currentClause = currentClause.add(l);   // Add all our temporary literals to our clause
                            }
                            clauses.add(currentClause);     // Add the clause to our list of clauses
                            currentClause = new Clause();   // Reset the clause for the next set of literals
                            literals.clear();               // Reset the literals LL for the next set of literals
                        } else if (i == '-') {
                            literals.add(NegLiteral.make(item.substring(1)));
                        } else {
                            literals.add(PosLiteral.make(item));
                        }
                    }
                }
            }

            Clause[] temp = clauses.toArray(new Clause[0]); // Convert to standard array to use makeFm
            Formula fm = makeFm(temp);
            System.out.println(fm);
//            SATSolver.solve(fm);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void testSATSolver1() {
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())
    			|| Bool.TRUE == e.get(b.getVariable())	);

*/
    }


    public void testSATSolver2() {
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