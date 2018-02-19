# DpllSatSolver
Simple Java implementation of the Davis–Putnam–Logemann–Loveland (DPLL) algorithm ( https://en.wikipedia.org/wiki/DPLL_algorithm ) including OLR (one-literal-rule) and PLR (pure-literal-rule)

## Example
```
f := (¬p ∨ q ∨ ¬r ∨ s) ∧ (¬q ∨ ¬r ∨ s) ∧ (r) ∧ (¬p ∨ ¬s) ∧ (¬p ∨ r)
```
Checking if the formula f is satisfiable can be done by using
```java
String[][] formula = {{"-p", "q", "-r", "s"}, {"-q", "-r", "s"}, {"r"}, {"-p", "-s"}, {"-p", "r"}};
DpllResult result = Dpll.solve(new Formula(formula));

if(result != null) {
  System.out.println("Satisfiable! Took " + result.getTimeTaken() + "ms.");
  System.out.println(result);
} else {
  System.out.println("Not satisfiable!");
}

```
Disabling the output can be done by using
```java
Dpll.disableLog()
```

## Output
Executing the above code results in the following output

```
Formula CNF: (¬p ∨ q ∨ ¬r ∨ s) ∧ (¬q ∨ ¬r ∨ s) ∧ (r) ∧ (¬p ∨ ¬s) ∧ (¬p ∨ r)
Solving formula with 5 clauses and 4 literals:
As set of clauses: 	{ {¬p, q, ¬r, s}, {¬q, ¬r, s}, {r}, {¬p, ¬s}, {¬p, r} }
OLR: r => true
[L] r:= true ( )
	Solving formula with 3 clauses and 3 literals:
	As set of clauses: 	{ {¬p, q, s}, {¬q, s}, {¬p, ¬s} }
	PLR: p => false
	[R] p:= false ( r=true )
		Solving formula with 1 clauses and 2 literals:
		As set of clauses: 	{ {¬q, s} }
		PLR: q => false
		[R] q:= false ( p=false r=true )
			Solving formula with 0 clauses and 0 literals:
			As set of clauses: 	{  }
Satisfiable! Took 6ms.
p -> 0, q -> 0, r -> 1
```

## Note
'-' is used instead of '¬'
