/*
  Simple Java implementation of the Davis–Putnam–Logemann–Loveland (DPLL) algorithm
  ( https://en.wikipedia.org/wiki/DPLL_algorithm )
  including OLL (one-literal-rule) and PLL (pure-literal-rule)

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

    // First: Check if OLL is usable
    int oll = 0; // 0 = none, 1 = left/true, 2 = right/false
    String lit = null;
    ollLoop:for(Clause clause : formula.getClauses()) {
      if(clause.getLiterals().size() == 1) {
        lit = clause.getLiterals().get(0);
        if(lit.startsWith("-")) {
          lit = lit.replaceFirst("-","");
          oll = 2;
        } else {
          oll = 1;
        }
        log(tabs, "OLL: " + lit + " => " + (oll == 1 ? "true" : "false"));
        break ollLoop;
      }
    }

    if(oll == 0) {
      // Only look for PLL if OLL is not usable
      // since OLL has a higher priority than OLL
      pllLoop:for(Clause c : formula.getClauses()) {
        for(String literal : c.getLiterals()) {
          String opposite = literal.startsWith("-") ? literal.replaceFirst("-","") : "-" + literal;
          if(!formula.containsLiteral(opposite)) {
            // found PLL
            if(literal.startsWith("-")) {
              lit = literal.replaceFirst("-","");
              oll = 2;
            } else {
              lit = literal;
              oll = 1;
            }
            log(tabs, "PLL: " + literal + " => " + (oll == 1 ? "true" : "false"));
            break pllLoop;
          }
        }
      }
    }

    if(lit == null) {
      lit = literals.get(0);
    }

    // Don't do this if OLL result was 2
    // since that would mean we skip the true case and set x := false
    if(oll == 0 || oll == 1) {
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
    // Don't do this if OLL result was 1
    // since that would mean we skip the false case and set x := true
    if(oll == 0 || oll == 2) {
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
