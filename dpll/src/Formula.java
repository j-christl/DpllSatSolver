import java.lang.reflect.Array;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Formula {

  private ArrayList<Clause> clauses;

  /**
   * Constructs a new formula from a given set of clauses
   * @param formula
   */
  public Formula(String[][] formula) {
    ArrayList<Clause> clauses = new ArrayList<>();
    for(int i = 0; i < formula.length; i++) {
      String[] clause = formula[i];
      clauses.add(new Clause(new ArrayList<>(Arrays.asList(clause))));
    }
    this.clauses = clauses;
  }

  /**
   * Created a new Formula by copying another given Formula
   * @param other
   */
  public Formula(Formula other) {
    this.clauses = new ArrayList<Clause>();
    for(Clause c : other.getClauses()) {
      this.clauses.add(c);
    }
  }

  /**
   * Checks whether a formula contains a literal, meaning
   * one of it's clauses contains the given literal
   * @param literal
   * @return
   */
  public boolean containsLiteral(String literal) {
    for(Clause clause : clauses) {
      if(clause.containsLiteral(literal)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Surely not the most efficient method, but it's bug-free
   * @param f
   * @return
   */
  public static Formula copy(Formula f) {
    ArrayList<Clause> newClauses = new ArrayList<>();
    for(Clause c : f.getClauses()) {
      ArrayList<String> literals = new ArrayList<>();
      for(String lit : c.getLiterals()) {
        literals.add(new String(lit));
      }
      Clause cNew = new Clause(literals);
      newClauses.add(cNew);
    }
    Formula formula = new Formula(newClauses);
    return formula;
  }

  public boolean isOnlyEmptyClauses() {
    for(Clause clause : clauses) {
      if(clause.containsLiterals()) {
        return false;
      }
    }
    return true;
  }

  /**
   * set literal to value and change clauses according to the result
   * @param literal
   * @param value
   */
  public void set(String literal, boolean value) {
    if(value) {
      // x := true
      removeClausesContainingLiteral(literal);
      removeLiteralFromClauses("-" + literal);
    } else {
      // x := false
      removeClausesContainingLiteral("-" + literal);
      removeLiteralFromClauses(literal);
    }
    removeDuplicateClauses();
  }

  private void removeDuplicateClauses() {
    //ArrayList<Clause> noDuplicates = new ArrayList<>(new LinkedHashSet<>(clauses));
    //this.clauses = noDuplicates;
   ArrayList<Clause> set = new ArrayList<>();
   for(Iterator<Clause> iterator = clauses.iterator(); iterator.hasNext();) {
     Clause c = iterator.next();
     if(set.contains(c)) {
       iterator.remove();
     }
     set.add(c);
   }
  }

  /**
   * Removes a literal in every clause containing it
   * note: the clause itself does NOT get removed!
   * @param literal
   */
  public void removeLiteralFromClauses(String literal) {
    for(Clause clause : clauses) {
      if(clause.containsLiteral(literal)) {
        //System.out.println("removed " + literal + " from " + clause.toString());
        clause.removeLiteral(literal);
      }
    }
  }

  /**
   * Removes all clauses which contain a given literal
   * @param literal
   */
  public void removeClausesContainingLiteral(String literal) {
    for(Iterator<Clause> iterator = clauses.iterator(); iterator.hasNext();) {
      Clause clause = iterator.next();
      if(clause.containsLiteral(literal)) {
        //System.out.println("Removed " + clause);
        iterator.remove();
      }
    }
  }

  /**
   * Returns all literals, ordered lexicographically
   * @return
   */
  public ArrayList<String> getLiteralsLex() {
    LinkedHashSet<String> set = new LinkedHashSet<>();
    for(Clause c : clauses) {
      for(String lit : c.getLiterals()) {
        set.add(lit.startsWith("-") ? lit.replaceFirst("-","") : lit);
      }
    }
    ArrayList<String> literals = new ArrayList<>(set);
    Collections.sort(literals);
    /*ArrayList<String> completeList = new ArrayList<>(literals);
    for(String l : literals) {
      completeList.add("-" + l);
    }
    */
    return literals;
  }

  /**
   * Constructs a new formula using a set of clauses
   * @param clauses
   */
  public Formula(ArrayList<Clause> clauses) {
    this.clauses = clauses;
  }

  /**
   * Returns the formula represented by a set of clauses
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    for(Clause clause : clauses) {
      sb.append(clause.toString());
      if(clauses.indexOf(clause) < clauses.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append(" }");

    return sb.toString();
  }

  /**
   * Returns the formula as conjunctive normal form
   * @return
   */
  public String toStringCnf() {
    StringBuilder sb = new StringBuilder();
    for(Clause clause : clauses) {
      sb.append(clause.toStringDisjunction());
      if(clauses.indexOf(clause) < clauses.size() - 1) {
        sb.append(" ∧ ");
      }
    }

    return sb.toString();
  }

  /**
   * Removes a clause from the formula
   * @param clause
   */
  public void removeClause(Clause clause) {
    clauses.remove(clause);
  }

  public ArrayList<Clause> getClauses() {
    return clauses;
  }

}
