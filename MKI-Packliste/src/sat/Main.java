package sat;

import java.util.ArrayList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class Main {

	final int MAXVAR = 1000000;
	final int NBCLAUSES = 500000;
	private ArrayList<Atom> atoms = new ArrayList<Atom>();

	public Main() {
		aufgabe();
	}

	private void solveAll() throws ContradictionException, TimeoutException {
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(3600);
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);

		int cold = 1;
		int warm = 3;
		int wet = 7;
		int dry = 9;

		for (int i = 0; i < NBCLAUSES; i++) {
			int[] clause = { -1, -3, -7, 9 };
			int[] clause2 = { -1 };
			int[] clause3 = { 3 };
			solver.addClause(new VecInt(clause));
			solver.addClause(new VecInt(clause2));
			solver.addClause(new VecInt(clause3));

		}

		IProblem problem = solver;
		boolean unsat = true;
		while (problem.isSatisfiable()) {
			unsat = false;
			int[] model = problem.model();
			for (int i = 0; i < model.length; i++) {
				System.out.println("Value " + i + " is " + model[i]);
			}
		}

		Reader reader = new DimacsReader(solver);

		// if(problem.isSatisfiable()){
		// unsat=false;
		// int [] model = problem.model();
		// for(int i = 0; i<model.length; i++){
		// System.out.println("Value " + i + " is " + model[i]);
		// }
		// System.out.println(reader.decode(model));
		// }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

	public void aufgabe() {

		// ============================================================== init
		// ==========================================
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(3600);
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);

		// =================================================================
		// knowledge base ===============================
		// All atoms in my program
		generateAtom("Berge");
		generateAtom("Wald");
		generateAtom("Wüste");
		generateAtom("Meer");
		generateAtom("Weltall");
		generateAtom("Hotel");
		generateAtom("Zahnbürste");
		generateAtom("Zahnpasta");

		// add all atoms to solver with positive and negative value because they
		// can be any of both
		int[] allAtoms = new int[atoms.size() * 2];
		for (int i = 0; i < atoms.size(); i++) {
			allAtoms[i] = atoms.get(i).id;
			allAtoms[atoms.size() + i] = atoms.get(i).id * -1;
		}
		try {
			solver.addClause(new VecInt(allAtoms));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ============================================================= Given
		// Atoms =======================================
		// Add the Atoms with a given Value
		ArrayList<Atom> given = new ArrayList<Atom>();
		// e.g. addAtom(given, "Berge", false);
		addAtom(given, "Berge", false);
		addAtom(given, "Wald", false);
		addAtom(given, "Wüste", true);
		addAtom(given, "Meer", false);

		for (int i = 0; i < given.size(); i++) {
			int[] clause = { given.get(i).id };
			try {
				solver.addClause(new VecInt(clause));
			} catch (ContradictionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Could not add clause: " + given.get(i).id);
			}
		}

		// ============================================================
		// implications ================================

		// ============================================================= solve
		// ===========================================
		IProblem problem = solver;
		boolean unsat = true;
		Reader reader = new DimacsReader(solver);

		try {
			if (problem.isSatisfiable()) {
				unsat = false;
				int[] model = problem.model();
				for (int i = 0; i < model.length; i++) {
					System.out.print("Value " + i + " is " + model[i] + " | ");
				}
				System.out.println("\n" + reader.decode(model));
			} else {
				System.out.println("There is no solution!");
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addAtom(ArrayList<Atom> list, String name, boolean value) {
		for (int i = 0; i < atoms.size(); i++) {
			if (atoms.get(i).name.equals(name)) {
				Atom natom = new Atom(name, value, (i + 1));
				if (!natom.value)
					natom.id = natom.id * (-1);
				list.add(natom);
			}
		}
	}

	/*
	 * Send a number of atoms and a negation list if you wantgivenAtoms:
	 * {"Book","Paper","Pencil"} negationList: {0,1,1} => no book, paper, pencil
	 */
	public int[] getIntArray(String[] givenAtoms, int[] negationList) {
		int[] returnArray = new int[givenAtoms.length];
		for (int j = 0; j < givenAtoms.length; j++) {
			for (int i = 0; i < atoms.size(); i++) {
				if (atoms.get(i).name.equals(givenAtoms[j])) {

					int id = atoms.get(i).id;
					if (negationList != null
							&& negationList.length == givenAtoms.length
							&& negationList[j] == 0)
						id = id * (-1);
					returnArray[j] = id;
				}
			}
		}
		return returnArray;
	}

	public void generateAtom(String name) {
		Atom a = new Atom(name, false, (atoms.size() + 1));
		atoms.add(a);
	}

	
	/*
	 * Makes the solution easy readable by converting numbers to strings accordingly
	 */
	public String resolveArray(int[] array) {
		String answer = "";
		for (int j = 0; j < array.length; j++) {
			for (int i = 0; i < atoms.size(); i++) {
				if(array[j]==0){
					//eof
					answer+="ende.";
				}
				else{
					if (atoms.get(i).id == array[j]) {
						if (!atoms.get(i).value)
							answer+= "!";
							answer+=atoms.get(i).name + " and ";
					}
				}
			}
		}
		return answer;
	}
}
