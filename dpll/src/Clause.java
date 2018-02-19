import java.util.ArrayList;
import java.util.Arrays;

public class Clause {

  private ArrayList<String> literals;

  /**
   * Constructs a new clause using a set of literals
   * @param literals
   */
  public Clause(ArrayList<String> literals) {
    this.literals = literals;
  }

  /**
   * Constructs a new clause using an array of literals
   * @param literals
   */
  public Clause(String[] literals) {
    this.literals = new ArrayList<>(Arrays.asList(literals));
  }

  /**
   * true, if this clause is equal to {}
   * @return
   */
  public boolean containsLiterals() {
    return literals.size() != 0;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Clause) {
      Clause other = (Clause) obj;
      if(other.literals.size() == literals.size()) {
        if(literals.size() == 0) {
          //System.out.println(toString() + " cmp " + other + "-> true");
          return true;
        }
        for(String lit : literals) {
          if(!other.containsLiteral(lit)) {
            //System.out.println(toString() + " cmp " + other + "-> false");
            return false;
          }
        }
        //System.out.println(toString() + " cmp " + other + "-> true");
        return true;
      }
    }
    //System.out.println(toString() + " cmp " + obj + "-> false");
    return false;
  }

  /**
   * Returns the clause as a string representing the set of literals
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for(String literal : getLiterals()) {
      sb.append(literal);
      if(getLiterals().indexOf(literal) < getLiterals().size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * Returns the clause as a string representing a disjuntion
   * Can be used to construct a conjunctive normal form
   * @return
   */
  public String toStringDisjunction() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for(String literal : getLiterals()) {
      sb.append(literal);
      if(getLiterals().indexOf(literal) < getLiterals().size() - 1) {
        sb.append(" ∨ ");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  /**
   * Returns whether the clause contains a given literal
   * @param literal
   * @return
   */
  public boolean containsLiteral(String literal) {
    return literals.contains(literal);
  }

  /**
   * Removes a literal from a clause
   * @param literal
   */
  public void removeLiteral(String literal) {
    literals.remove(literal);
  }

  /**
   * Returns the literals of the clause as an ArrayList
   * @return
   */
  public ArrayList<String> getLiterals() {
    return literals;
  }

}
