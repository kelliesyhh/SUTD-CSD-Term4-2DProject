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
        if (clauses.isEmpty()) {
            return env;
        } else {
            //check for empty clause in list of clauses and find shortest clause
            Clause shortestClause = clauses.first();

            // Check for empty clauses as well as the shortest clause
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
            if (shortestClause.isUnit()) { // Check if the clause has only 1 literal
                env = set(env, l, true);
                result = solve(SATSolver.substitute(clauses, l), env);
            } else { // Set any random literal to be true first
                env = set(env, l, true);
                result = solve(SATSolver.substitute(clauses, l), env);
                if (result == null) { // If your previous guess of literal's value == True was wrong, try literal's value = False
                    env = set(env, l, false);
                    result = solve(SATSolver.substitute(clauses, l.getNegation()), env);
                }
            }
            return result;
        }
    }

    // Convenience method to neaten main solver code
    private static Environment set(Environment env, Literal l, Boolean bool) {
        if (l instanceof PosLiteral) {
            // If l=x is PosLiteral, l.getVariable returns x
            // Therefore we can set the underlying variable to T / F accordingly
            if (bool) {
                env = env.putTrue(l.getVariable());
            } else {
                env = env.putFalse(l.getVariable());
            }
        } else {
            // But if l=~x is NegLiteral, l.getVariable returns x
            // Therefore we have to set the variable to the opposite of the intended bool value
            if (bool) {
                env = env.putFalse(l.getVariable());
            } else {
                env = env.putTrue(l.getVariable());
            }
        }
        return env;
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
                newClauses = newClauses.add(clause.reduce(l));
            } else {
            }
        }
        return newClauses;
    }

}
