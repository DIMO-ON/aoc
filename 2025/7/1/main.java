import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	private int followbeam(int beam, int row, char[][] layers, int rows, int cols) {
		if (row < 0 || row >= rows) return 0;
		if (beam < 0 || beam >= cols || layers[row][beam] == '|') return 0;
		int i  = row;
		for (; i < rows && layers[i][beam] != '^'; i++) {
			if (layers[i][beam] == '|') return 0;
			layers[i][beam] = '|';
		}

		
		if (i >= rows) return 0;
		
		// System.out.println("======< >======");
		// this.print(layers, rows, cols);
		return 1 + followbeam(beam - 1, i, layers, rows, cols) +
			     + followbeam(beam + 1, i, layers, rows, cols);
	}

	public void print(char[][] s, int rows, int cols) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				System.out.printf("%c", s[i][j]);

			System.out.println();
		}
	}

    public int mySol(String input) {
		String copy = new String(input);
		String[] layers = copy.split("\n");
		// for (String l: layers) System.out.println(l.length());
		// System.exit(0);
		int rows = layers.length;
		int cols = layers[0].length();

		char[][] mat = new char[layers.length][];
		for (int i = 0; i < layers.length; i++) {
			mat[i] = layers[i].toCharArray();
		}
		
		// find seed
		int seed = 0;
		for (;seed < cols && mat[0][seed] != 'S'; seed++);
		int res = followbeam(seed, 0, mat, rows, cols);

		// System.out.println("======< >======");
		// this.print(mat, rows, cols);
		return res;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/7/1/input.txt"));
        // String example = "";
		// example += ".......S.......\n";
		// example += "...............\n";
		// example += ".......^.......\n";
		// example += "...............\n";
		// example += "......^.^......\n";
		// example += "...............\n";
		// example += ".....^.^.^.....\n";
		// example += "...............\n";
		// example += "....^.^...^....\n";
		// example += "...............\n";
		// example += "...^.^...^.^...\n";
		// example += "...............\n";
		// example += "..^...^.....^..\n";
		// example += "...............\n";
		// example += ".^.^.^.^.^...^.\n";
		// example += "...............\n";
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


        int totcount  = sol.mySol(example);
        totcount  = sol.mySol(input);
        System.out.printf("tachyon beam is split a total of %d times", totcount);
    }
}
