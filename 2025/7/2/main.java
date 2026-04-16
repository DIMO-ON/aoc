import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// System.out.println("======< >======\n");
// this.print(layers, rows, cols);
class Main {
	private Long followbeam(int col, int row, char[][] mat, Long[][] counts, int rows, int cols) {
		if (col < 0 || col >= cols) return 1l;
		if (row < 0 || row >= rows) return 1l;
		
		if (mat[row][col] == '^') {
			if (counts[row][col] <= 0) {
				counts[row][col]  = followbeam(col - 1, row, mat, counts, rows, cols);
			    counts[row][col] += followbeam(col + 1, row, mat, counts, rows, cols);
			}

			return counts[row][col];
		}

		return followbeam(col, row + 1, mat, counts, rows, cols);
	}

	public void print(char[][] s, int rows, int cols) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				System.out.printf("%c", s[i][j]);

			System.out.println();
		}
	}

    public Long mySol(String input) {
		String copy = new String(input);
		String[] layers = copy.split("\n");
		// for (String l: layers) System.out.println(l.length());
		// System.exit(0);
		int rows = layers.length;
		int cols = layers[0].length();

		char[][] mat = new char[layers.length][];
		Long[][] counts = new Long[layers.length][];
		for (int i = 0; i < layers.length; i++) {
			mat[i] = layers[i].toCharArray();
			counts[i] = new Long[cols];
			for (int j = 0; j < cols; j++) counts[i][j] = 0l;
		}
		
		// find seed
		int seed = 0;
		for (;seed < cols && mat[0][seed] != 'S'; seed++);
		Long res = followbeam(seed, 0, mat, counts, rows, cols);

		// System.out.println("======< >======");
		// this.print(mat, rows, cols);
		return res;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/7/2/input.txt"));
        String example = "";
		example += ".......S.......\n";
		example += "...............\n";
		example += ".......^.......\n";
		example += "...............\n";
		example += "......^.^......\n";
		example += "...............\n";
		example += ".....^.^.^.....\n";
		example += "...............\n";
		example += "....^.^...^....\n";
		example += "...............\n";
		example += "...^.^...^.^...\n";
		example += "...............\n";
		example += "..^...^.....^..\n";
		example += "...............\n";
		example += ".^.^.^.^.^...^.\n";
		example += "...............\n";


        Long totcount  = sol.mySol(example);
		totcount  = sol.mySol(input);
        System.out.printf("the particle ends up on %d different timelines", totcount);
    }
}
