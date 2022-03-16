package sat;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    public static void main(String[] args) {
        String filepath = args[0];

        try {
            // Read information in the file by using BufferedReader
            InputStream inputStream = new FileInputStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // NOTE: start parsing process
            // clauses LinkedList stores our clauses parsed from the file
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

                                // In case a and ~a are added, Clause returns null, so we have to handle this case
                                if (currentClause == null) break;
                            }
                            if (currentClause != null) {
                                clauses.add(currentClause);    // Add the clause to our list of clauses
                            }

                            currentClause = new Clause();   // Reset the clause for the next set of literals
                            literals.clear();               // Reset the literals LL for the next set of literals

                        } else if (i == '-') {  // Literal starts with -, treat it as a negative literal
                            literals.add(NegLiteral.make(item.substring(1)));
                        } else {                // Treat it as a positive literal
                            literals.add(PosLiteral.make(item));
                        }
                    }
                }
            }

            Clause[] temp = clauses.toArray(new Clause[0]); // Convert to standard array to use makeFm

            reader.close(); // Close BufferedReader stream and release all the system resources associated with the stream operations

            Formula fm = makeFm(temp);
            long started = System.nanoTime();
            Environment e = SATSolver.solve(fm);
            long time = System.nanoTime();

            if (e == null) {
                System.out.println("Unsatisfiable");
            } else {
                System.out.println("Satisfiable");
                writeToFile(e); // Call method to write result of solver to a txt file
            }

            long timeTaken = time - started; System.out.println("Time:" + timeTaken/1000000.0 + "ms");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Takes an Environment object and writes the assignment of variables to values
     * bound to the env object to a txt file. The method does not return anything,
     * but writes directly to the txt file.
     *
     * @param env assignment of some or all variables in clauses to true or
     *            false values
     */
    private static void writeToFile(Environment env) {
        // Use the string format of our env to write to our file
        String[] truncatedEnv = env.toString().substring(13, env.toString().length()-1).split(", ");

        try {
            FileWriter file = new FileWriter("BoolAssignment.txt");
            for (String i : truncatedEnv) {
                file.write(i.replaceAll("->", ":") + "\n");
            }
            file.close();
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