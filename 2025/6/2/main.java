import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	class StringMatrix {
		public ArrayList<ArrayList<String>> matrix;

		StringMatrix(String s) {
			this.matrix = new ArrayList<ArrayList<String>>();
			String copy = new String(s);
			copy = copy.replaceAll("\n", ";");
			copy = copy.replaceAll("[^\\d;]", "-");
			copy = copy.replaceAll("-+", "-");
			copy = copy.replaceAll("^-", "");
			copy = copy.replaceAll("-;", ";");
			copy = copy.replaceAll(";-", ";");
			copy = copy.replaceAll("-$", "");
			copy = copy.replaceAll(";;", ";");
			String[] m = copy.split(";");
			for (String sr: m) {
				String[] nrs = sr.split("-");
				ArrayList<String> row = new ArrayList<String>();

				for (String n: nrs) row.add(n);
				this.matrix.add(row);
			}

			this.transpose();
			this.rightmost();
		}

		public void rightmost() {
			ArrayList<ArrayList<String>> newm = new ArrayList<ArrayList<String>>();

			for (int i = 0; i < this.matrix.size(); i++) {
				for (int j = 0; j < this.matrix.get(i).size(); j++) {
					System.out.println(this.matrix.get(i).get(j));
				}
			}
		}

		ArrayList<Long> getParseArrayLong(int rowindex) {
			ArrayList<Long> a = new ArrayList<Long>();

			for (String n: this.matrix.get(rowindex)) a.add(Long.parseLong(n));

			return a;
		}

		public void transpose() {
			ArrayList<ArrayList<String>> newm = new ArrayList<ArrayList<String>>();
			int cols = this.matrix.size();
			int rows = this.matrix.get(0).size();
			
			for (int i = 0; i < rows; i++) {
				newm.add(new ArrayList<String>());
				for (int j = 0; j < cols; j++) {
					newm.get(i).add(this.matrix.get(j).get(i));
					// System.out.printf("%d ", newm.get(i).get(j));
				}
				// System.out.println();
			}
			// System.exit(0);
			
			this.matrix = newm;
		}
	}
	
	public class Op {
		public final char symbol;

		Op(String s) {
			this(s.charAt(0));
		}

		Op(char symbol) {
			assert symbol != '+' || symbol != '*';
			this.symbol = symbol;
		}
	}

	class Operators {
		public ArrayList<Op> ops;

		Operators(String input) {
			String[] input2 = input.split("\n");
			String operators = "";
			for (String s: input2) 
				if (!s.matches(".*\\d.*")) operators = s;

			operators = operators.replaceAll("[^*+]", "-");
			operators = operators.replaceAll("-+", "-");
			operators = operators.replaceAll("^-", "");
			operators = operators.replaceAll("-;", ";");
			operators = operators.replaceAll("-$", "");
			

			this.ops = new ArrayList<Op>();
			for (String o: operators.split("-"))
				this.ops.add(new Op(o));

		}
	}

	class Operation {
		private ArrayList<Long> factors;
		public Long result;
		private Op operator;

		Operation(ArrayList<Long> f, Op o) {
			this.operator = o;
			this.factors = f;
			this.resetResult();
			this.computeAll();
			this.print();
		}

		Long compute(Long a, Long b) {
			switch (this.operator.symbol) {
				case '+': return a + b;
				case '*': return a * b;
			}
			return 0l;
		}

		public void resetResult() {
			switch (this.operator.symbol) {
				case '+':
					this.result = 0l;
					break;
				case '*':
					this.result = 1l;
					break;
			}
		}
		public void computeAll() {
			this.resetResult();
			for (Long n: this.factors)
				this.result = compute(this.result, n);
		}

		public void print() {
			for (Long n: this.factors) {
				System.out.printf("%d %c ", n, this.operator.symbol);
			}
			
			System.out.printf("   = %d\n", this.result);
		}
	}
    
    public Long mySol(String input) {
		Long finalres = 0l;
		StringMatrix m = new StringMatrix(input);
		System.exit(0);

		Operators operators = new Operators(input);

		ArrayList<Operation> operations = new ArrayList<Operation>();

		for (int i = 0; i < m.matrix.size(); i++) {
			operations.add(new Operation(m.getParseArrayLong(i), operators.ops.get(i)));
		}

		for (Operation o: operations) {
			finalres += o.result;
		}

		return finalres;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/6/1/input.txt"));
        String example = """
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   + 
        """;
        Long totcount  = sol.mySol(example);
        // Long totcount  = sol.mySol(input);
        System.out.printf("the grand total is %d of the available ingredient IDs are fresh\n", totcount);
    }
}
