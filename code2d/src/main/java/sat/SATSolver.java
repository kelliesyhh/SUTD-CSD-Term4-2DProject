package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.*;

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
     * null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        System.out.println("SAT solver starts!!!");

        Environment environment = new Environment();

        ImList<Clause> clauses = formula.getClauses();

        return solve(clauses, environment);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses formula in conjunctive normal form
     * @param env     assignment of some or all variables in clauses to true or
     *                false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     * or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        //System.out.println(" ");
        System.out.println(clauses.size());
        if (clauses.isEmpty()) {
            return env;
        } else {
            //check for empty clause in list of clauses and find shortest clause
            Clause shortestClause = clauses.first();

            for (Clause clause : clauses) {
                if (clause.isEmpty()) {
                    return null;
                } else if (clause.size() < shortestClause.size()) {
                        shortestClause = clause;
                }
            }

            // Pick any literal l and set it to true
            Literal l = shortestClause.chooseLiteral();
            Environment result;
            if (shortestClause.isUnit()) { //check if the clause has only 1 literal
                if (l instanceof PosLiteral) {
                    env = env.putTrue(l.getVariable());
                } else {
                    env = env.putFalse(l.getVariable());
                }
                result = solve(SATSolver.substitute(clauses, l), env);
            } else { //set any random literal to be true first
                env = env.putTrue(l.getVariable());
                result = solve(SATSolver.substitute(clauses, l), env);
                if (result.equals(null)) { //if your previous guess of literal's value == True was wrong, try literal's value = False
                    env = env.putFalse(l.getVariable());
                    result = solve(SATSolver.substitute(clauses, l.getNegation()), env);
                }
            }
            return result;
        }
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

        ImList<Clause> newClauses = new EmptyImList<>();
        // If the clause doesn't reduce to null (true), add it to our new clause list

        for (Clause clause : clauses) {
            if (clause.reduce(l) != null) {
                //System.out.println(clause + " became " + clause.reduce(l));
                newClauses = newClauses.add(clause.reduce(l));
            } else {
                //System.out.println("a null clause was found " + clause);
            }
        }
        return newClauses;
    }

}
