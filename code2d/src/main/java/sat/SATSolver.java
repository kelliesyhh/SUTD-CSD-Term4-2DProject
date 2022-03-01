package sat;

import com.sun.tools.doclint.Env;

import java.util.Iterator;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(formula);
        long time = System.nanoTime();

        Environment environment = new Environment();

        ImList<Clause> clauses = formula.getClauses();
        solve(clauses, environment);

        long timeTaken= time - started; System.out.println("Time:" + timeTaken/1000000.0 + "ms");

        return environment;
//        throw new RuntimeException("not yet implemented.");
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
        if (clauses.isEmpty()) {
            Clause clause = new Clause();
            // returns an empty clause
            env.putFalse(clause.chooseLiteral().getVariable());
        }
        else {
            Clause clause;
            while (clauses.iterator().hasNext()) {
                // current clause
                clause = clauses.iterator().next();

                // if the clause only has 1 literal
                if (clause.isUnit()) {
                    env.putFalse(clause.chooseLiteral().getVariable());
                    while (env.get(clause.chooseLiteral().getVariable()) == Bool.FALSE) {
                        substitute(clauses, clause.chooseLiteral());
                        solve(clauses, env);
                    }
                }
                else {
                    clause = clauses.iterator().next();
                    // arbitarily picking a literal from the clause
                    Literal literal = clause.chooseLiteral();
                    try {
                        // set literal to true
                        env.putTrue(literal.getVariable());
                        // substitute for it in all the clauses
                        substitute(clauses, literal);
                        solve(clauses, env);
                    }
                    catch (Exception e){
                        env.putFalse(literal.getVariable());
                        substitute(clauses, literal);
                        solve(clauses, env);
                    }
                }
            }
        }
        return env;
//        throw new RuntimeException("not yet implemented.");
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        ImList<Clause> newClauses = new EmptyImList<>();
        while (clauses.iterator().hasNext()) {
            // if it contains l - remove l because true doesn't matter
            if (clauses.iterator().next().contains(l)) {
                newClauses.remove(clauses.iterator().next());
            }
        }


        throw new RuntimeException("not yet implemented.");
    }
}
