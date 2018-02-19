/*
  Simple Java implementation of the Davis–Putnam–Logemann–Loveland (DPLL) algorithm
  ( https://en.wikipedia.org/wiki/DPLL_algorithm )
  including OLR (one-literal-rule) and PLR (pure-literal-rule)

  Checks whether a given formula is satisfiable or not

  https://github.com/yoshc
  18.2.2018

  examples:
    Checking if the formula
      (¬p ∨ q ∨ ¬r ∨ s) ∧ (¬q ∨ ¬r ∨ s) ∧ (r) ∧ (¬p ∨ ¬s) ∧ (¬p ∨ r)
    is satisfiable can be done by using
      String[][] formula = {{"-p", "q", "-r", "s"}, {"-q", "-r", "s"}, {"r"}, {"-p", "-s"}, {"-p", "r"}};
      DpllResult result = Dpll.solve(new Formula(formula));

    Disabling the output can be done by using
      Dpll.disableLog()

    Note that '-' is used instead of ¬

 */

import java.util.ArrayList;
import java.util.HashMap;

public class Dpll {

  private static boolean logEnabled = true;

  /**
   * Solves a given formula using the DPLL algorithm
   * @param formula the formula
   * @return the Result of the solved formula
   */
  public static DpllResult solve(Formula formula) {
    log(0, "Formula CNF: " + formula.toStringCnf());
    final long timeNow = System.currentTimeMillis();
    HashMap<String, Boolean> settings = new HashMap<>();
    int orderSize = formula.getLiteralsLex().size();
    DpllResult result = solve(formula, settings, orderSize, 0);
    if(result != null) {
      result.setTimeTaken(System.currentTimeMillis()-timeNow);
    }
    return result;
  }

  /**
   * internal recursive method for the DPLL algorithm
   * @param formula
   * @param variableSetting
   * @param orderSize
   * @param tabs
   * @return
   */
  private static DpllResult solve(Formula formula, HashMap<String, Boolean> variableSetting, int orderSize, int tabs) {
    log(tabs,"Solving formula with " + formula.getClauses().size() + " clauses and " + formula.getLiteralsLex().size() + " literals:");
    log(tabs,"As set of clauses: \t" + formula.toString());

    // {}
    if(formula.getClauses().size() == 0) {
      return new DpllResult(variableSetting);
    }

    // {{}}
    if(formula.isOnlyEmptyClauses()) {
      log(tabs,"Only empty clauses! Going up");
      return null;
    }

    ArrayList<String> literals = formula.getLiteralsLex();

    // First: Check if OLR is usable
    int rule = 0; // 0 = none, 1 = left/true, 2 = right/false
    String lit = null;
    olrLoop:for(Clause clause : formula.getClauses()) {
      if(clause.getLiterals().size() == 1) {
        lit = clause.getLiterals().get(0);
        if(lit.startsWith("-")) {
          lit = lit.replaceFirst("-","");
          rule = 2;
        } else {
          rule = 1;
        }
        log(tabs, "OLR: " + lit + " => " + (rule == 1 ? "true" : "false"));
        break olrLoop;
      }
    }

    if(rule == 0) {
      // Only look for PLR if OLR is not usable
      // since OLR has a higher priority than OLR
      plrLoop:for(Clause c : formula.getClauses()) {
        for(String literal : c.getLiterals()) {
          String opposite = literal.startsWith("-") ? literal.replaceFirst("-","") : "-" + literal;
          if(!formula.containsLiteral(opposite)) {
            // found PLR
            if(literal.startsWith("-")) {
              lit = literal.replaceFirst("-","");
              rule = 2;
            } else {
              lit = literal;
              rule = 1;
            }
            log(tabs, "PLR: " + lit + " => " + (rule == 1 ? "true" : "false"));
            break plrLoop;
          }
        }
      }
    }

    if(lit == null) {
      lit = literals.get(0);
    }

    // Don't do this if OLR result was 2
    // since that would mean we skip the true case and set x := false
    if(rule == 0 || rule == 1) {
      // Left part ( x:= true)
      Formula newFormulaL = Formula.copy(formula);
      HashMap<String, Boolean> newSettingsL = (HashMap<String, Boolean>) variableSetting.clone();

      StringBuffer sb = new StringBuffer();
      sb.append("[L] " + lit + ":= true (");
      for (String key : newSettingsL.keySet()) {
        sb.append(" " + key + "=" + newSettingsL.get(key));
      }
      sb.append(" )");
      log(tabs, sb.toString());
      newSettingsL.put(lit, true);
      newFormulaL.set(lit, true);

      DpllResult resultL = solve(newFormulaL, newSettingsL, orderSize, tabs + 1);

      if (resultL != null) {
        return resultL;
      }
    }
    // Don't do this if OLR result was 1
    // since that would mean we skip the false case and set x := true
    if(rule == 0 || rule == 2) {
      // Right part ( x:= false )
      Formula newFormulaR = Formula.copy(formula);
      HashMap<String, Boolean> newSettingsR = (HashMap<String, Boolean>) variableSetting.clone();

      StringBuilder sb = new StringBuilder();
      sb.append("[R] " + lit + ":= false (");
      for (String key : newSettingsR.keySet()) {
        sb.append(" " + key + "=" + newSettingsR.get(key));
      }
      sb.append(" )");
      log(tabs, sb.toString());
      newSettingsR.put(lit, false);
      newFormulaR.set(lit, false);

      DpllResult resultR = solve(newFormulaR, newSettingsR, orderSize, tabs + 1);
      if (resultR != null) {
        return resultR;
      }
    }

    return null;
  }

  public static void disableLog() {
    Dpll.logEnabled = false;
  }

  public static void enableLog() {
    Dpll.logEnabled = true;
  }

  private static void log(int tabs, String msg) {
    if(!logEnabled) {
      return;
    }
    for(int i = 0; i < tabs; i++) {
      System.out.print("\t");
    }
    System.out.println(msg.replaceAll("-", "¬"));
  }

}
