import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	String MAIN_OPERATORS = "*+";

	public Long resetResult(char operator) {
		if (operator == '*') return 1l;
		return 0l;
	}

	public Long compute(char op, Long a, Long b) {
		assert op != '*' || op != '+';
		if (op == '+') return a + b;
		return a * b;
	}

    
    public Long mySol(String input) {
		Long finalres = 0l;
		String copy = new String(input);
		String[] l = copy.split("\n");
		int rows = copy.split("\n").length - 1;
		int cols = copy.split("\n")[0].length() + 1;
		char op = '\0';
		Long actualres = 0l;

		for (int c = 0; c < cols - 1; c++) {
			char readop = input.charAt(rows * cols + c);
			String operation = "";
			operation += readop;
			if (this.MAIN_OPERATORS.contains(operation)) {
				finalres += actualres;
				op = readop;
				actualres = this.resetResult(op);
			}
			
			String actualnr = "";
			for (int r = 0; r < rows; r++) {
				actualnr += input.charAt(r * cols + c);
			}
			actualnr = actualnr.trim();
			if (actualnr.length() > 0) {
				actualres = this.compute(op, actualres, Long.parseLong(actualnr));
				// System.out.printf("%c: %s -> %d -> %d\n", op, actualnr, actualres, finalres);
			}
		}
		finalres += actualres;
		
		return finalres;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/6/2/input.txt"));
        String example = "";
		example += "123 328  51 64 \n";
		example += " 45 64  387 23 \n";
		example += "  6 98  215 314\n";
		example += "*   +   *   +  \n";

        Long totcount  = sol.mySol(example);
        System.out.printf("the grand total is %d of the available ingredient IDs are fresh\n", totcount);
        totcount  = sol.mySol(input);
        System.out.printf("the grand total is %d of the available ingredient IDs are fresh\n", totcount);
    }
}
