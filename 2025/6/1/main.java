import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	class Matrix {
		public ArrayList<ArrayList<Long>> matrix;
		Matrix(String s) {
			this.matrix = new ArrayList<ArrayList<Long>>();
			String copy = s;
			copy = copy.replaceAll("\n", ";");
			copy = copy.replaceAll("[^\\d;]", "-");
			copy = copy.replaceAll("-+", "-");
			copy = copy.replaceAll("^-", "");
			copy = copy.replaceAll("-;", ";");
			copy = copy.replaceAll("-$", "");
			String[] m = s.plit(";");
			for (String s: m) {
				String[] nrs = s.split("-");
				ArrayList<Long> row = new ArrayList<Long>();
				for (String n: nrs) {
					row.append(Long.parseLong(n));
				}
				this.matrix.append(row);
			}

		}

		public void transpose() {
			ArrayList<ArrayList<Long>> newm = new ArrayList<ArrayList<Long>>();
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					newm[j][i] = this.matrix[i][j];
				}
			}
			
			this.matrix = newm;
		}
	}
	
	public enum OP {
		PLUS("+"),
		MOLTIPLICATION("*");

		public final String symbol;

		OP(String symbol) {
			this.symbol = symbol;
		}
	}

	class Operators {
		public ArrayList<OP> ops;

		Operators(String input) {
			String input2 = input.split("\n");
			String operators = "";
			for (String s: input2) 
				if (!s.matches(".*\\d.*")) operators = s;

			operators = operators.replaceAll("[^*+]", "-");
			operators = operators.replaceAll("-+", "-");
			operators = operators.replaceAll("^-", "");
			operators = operators.replaceAll("-;", ";");
			operators = operators.replaceAll("-$", "");

			this.ops = new ArrayList<OP>();
			for (String o: operators)
				this.ops.append(OP(o));
		}
	}

	class Operation {
		private ArrayList<Long> factors;
		public Long result;
		private OP operator;

		Operation(ArrayList<Long> f, OP o) {
			this.operator = o;
			this.result = 0l;
			this.factors = f;
			this.computeAll();
		}

		Long compute(Long a, Long b) {
			switch(this.operator.symbol) {
				case OP.PLUS: return a + b;
				case OP.MOLTIPLICATION: return a * b;
			}
		}

		public void computeAll() {
			for (Long n: this.factors)
				this.result += n;
		}
	}
    
    public Long mySol(String input) {
		Long finalres = 0l;
		Matrix m = new Matrix(input);
		m.transpose();

		Operators operators = new Operators(input);

		ArrayList<Operation> operations;

		for (int i = 0; i < m.length(); i++)
			operations.append(new Operation(m.matrix[i], operators.ops));

		for (Operation o: operations) finalres += o.result;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String input = Files.readString(Path.of("2025/5/2/input.txt"));
        // String input = Files.readString(Path.of("2025/5/2/input2.txt"));
        String example = """
         3-5
         10-14
         16-20
         12-18
         
         1
         5
         8
         11
         17
         32
        """;
        // Long totcount  = sol.mySol(example);
        Long totcount  = sol.mySol(input);
        System.out.printf("%d of the available ingredient IDs are fresh\n", totcount);
    }
}
