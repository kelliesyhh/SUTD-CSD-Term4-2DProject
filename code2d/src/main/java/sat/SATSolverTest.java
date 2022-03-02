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

    public static void main(String[] args) {
//        String filepath = args[0];
        String filepath = "C:\\Users\\Razer\\OneDrive - Singapore University of Technology and Design\\SUTD\\Y1\\Term 4\\50.002 Computation Structures\\2D\\50001_Project-2D-starting\\sampleCNF\\RCAout.cnf";

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

            reader.close();
            clauses = null;
            literals = null;

            Formula fm = makeFm(temp);
            long started = System.nanoTime();
            Environment e = SATSolver.solve(fm);
            long time = System.nanoTime();

            if (e == null) {
                System.out.println("Unsatisfiable");
            } else {
                System.out.println("Satisfiable");
            }

            long timeTaken= time - started; System.out.println("Time:" + timeTaken/1000000.0 + "ms");

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