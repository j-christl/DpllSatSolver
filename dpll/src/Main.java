public class Main {

    public static void main(String[] args) {
        Formula f = new Formula(getTestFormula2());
        DpllResult result = Dpll.solve(f);
        if(result != null) {
            System.out.println("Satisfiable! Took " + result.getTimeTaken() + "ms.");
            System.out.println(result);
        } else {
            System.out.println("Not satisfiable!");
        }
    }

    /*
       (b ∨ ¬d) ∧ (d ∨ ¬c) ∧ (f ∨ ¬d) ∧ (f ∨ a ∨ c ∨ ¬b) ∧
       (¬f ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (f ∨ c ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (a ∨ c ∨ d ∨ ¬f) ∧
       (f ∨ b ∨ ¬a ∨ ¬d) ∧ (f ∨ ¬a ∨ ¬b ∨ ¬c ∨ ¬d)
        Satisfiable with a -> 1, b -> 1, c -> 0, d -> 0
     */
    public static String[][] getTestFormula1() {
        return new String[][] {{"b","-d"}, {"d","-c"},{"f","-d"},{"f","a","c","-b"},
            {"-f","-a","-b","-d"},{"f","c","-a","-b","-d"},
            {"a","c","d","-f"},{"f","b","-a","-d"},{"f","-a","-b","-c","-d"}};
    }

    /*
        (¬p ∨ q ∨ ¬r ∨ s) ∧ (¬q ∨ ¬r ∨ s) ∧ (r) ∧ (¬p ∨ ¬s) ∧ (¬p ∨ r)
        Satisfiable with p -> 0, q -> 0, r -> 1
     */
    public static String[][] getTestFormula2() {
        return new String[][] {{"-p", "q", "-r", "s"}, {"-q", "-r", "s"}, {"r"}, {"-p", "-s"}, {"-p", "r"}};
    }

    /*
        (¬q ∨ s) ∧ (¬p ∨ q ∨ s) ∧ (p) ∧ (r ∨ ¬s) ∧ (¬p ∨ ¬r ∨ ¬s)
        Not satisfiable
     */
    public static String[][] getTestFormula3() {
        return new String[][] {{"-q", "s"},{"-p", "q", "s"},{"p"},{"r", "-s"},{"-p", "-r", "-s"}};
    }

    /*
        Sample Output:


Formula CNF: (b ∨ ¬d) ∧ (d ∨ ¬c) ∧ (f ∨ ¬d) ∧ (f ∨ a ∨ c ∨ ¬b) ∧ (¬f ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (f ∨ c ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (a ∨ c ∨ d ∨ ¬f) ∧ (f ∨ b ∨ ¬a ∨ ¬d) ∧ (f ∨ ¬a ∨ ¬b ∨ ¬c ∨ ¬d)
Solving formula with 9 clauses and 5 literals:
As set of clauses: 	{ {b, ¬d}, {d, ¬c}, {f, ¬d}, {f, a, c, ¬b}, {¬f, ¬a, ¬b, ¬d}, {f, c, ¬a, ¬b, ¬d}, {a, c, d, ¬f}, {f, b, ¬a, ¬d}, {f, ¬a, ¬b, ¬c, ¬d} }
[L] a:= true ( )
	Solving formula with 7 clauses and 4 literals:
	As set of clauses: 	{ {b, ¬d}, {d, ¬c}, {f, ¬d}, {¬f, ¬b, ¬d}, {f, c, ¬b, ¬d}, {f, b, ¬d}, {f, ¬b, ¬c, ¬d} }
	[L] b:= true ( a=true )
		Solving formula with 5 clauses and 3 literals:
		As set of clauses: 	{ {d, ¬c}, {f, ¬d}, {¬f, ¬d}, {f, c, ¬d}, {f, ¬c, ¬d} }
		[L] c:= true ( a=true b=true )
			Solving formula with 3 clauses and 2 literals:
			As set of clauses: 	{ {d}, {f, ¬d}, {¬f, ¬d} }
			OLL: d => true
			[L] d:= true ( a=true b=true c=true )
				Solving formula with 2 clauses and 1 literals:
				As set of clauses: 	{ {f}, {¬f} }
				OLL: f => true
				[L] f:= true ( a=true b=true c=true d=true )
					Solving formula with 1 clauses and 0 literals:
					As set of clauses: 	{ {} }
					Only empty clauses! Going up
		[R] c:= false ( a=true b=true )
			Solving formula with 2 clauses and 2 literals:
			As set of clauses: 	{ {f, ¬d}, {¬f, ¬d} }
			PLL: ¬d => false
			[R] d:= false ( a=true b=true c=false )
				Solving formula with 0 clauses and 0 literals:
				As set of clauses: 	{  }
Satisfiable! Took 12ms.
a -> 1, b -> 1, c -> 0, d -> 0

     */

}
