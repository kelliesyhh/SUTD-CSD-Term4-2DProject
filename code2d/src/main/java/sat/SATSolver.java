package sat;

import immutable.EmptyImList;
import immutable.ImList;
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
        System.out.println("SAT solver starts!!!");

        Environment environment = new Environment();

        ImList<Clause> clauses = formula.getClauses();
        solve(clauses, environment);

        return environment;
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
        if (clauses.isEmpty()){
            return env;
        } else {
            //check if got empty clause in list of clauses
            Clause shortestClause = clauses.first();
            for (Clause clause : clauses){
                if (clause.isEmpty()){
                    return null;
                } else { //shun bian identify the shortest clause for the recursive part later
                    if (clause.size() < shortestClause.size()){
                        shortestClause = clause;
                    }
                }
            }
            //the recursive code starts here
            Literal l = shortestClause.chooseLiteral();
            env = env.putTrue(l.getVariable());
            clauses = SATSolver.substitute(clauses,l);
            Environment result = solve(clauses, env);
            if (result == null && shortestClause.size() > 1){
                env = env.putFalse(l.getVariable());
                clauses = SATSolver.substitute(clauses,l);
                return solve(clauses, env);
            } else {
                return result;
            }
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
        // for each clause in list, check if it contains literal
        // delete clause if literal be found
        ImList<Clause> newClauses = new EmptyImList<>();
        for (Clause clause : clauses) {
            if (clause.contains(l)){
                if (clause.reduce(l) != null) {
                    newClauses = newClauses.add(clause.reduce(l));
                }
            } else {
                newClauses = newClauses.add(clause);
            }
        }
        return newClauses;
    }

}
