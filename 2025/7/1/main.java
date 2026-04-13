import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	class Tree {
		private class Beam {
			private int row;
			private int col;
			Beam(int row, int col) {
				this.row = row;
				this.col = col;
			}
			
			public int extend(String layer) {
				return 0;
			}
		}

		private ArrayList<Beam> beams;
		private int splits;

		Tree(int seedcol) {
			this.beams = new ArrayList<Beam>();
			Beam seed = new Beam(col, 0);
			this.beams.add(seed);
		}

		public void growRoot(String layer) {
			for (Beam b: this.beams) this.splits += b.extend(layer);
		}

	}
    
    public Long mySol(String input) {
		Long finalres = 0l;
		String copy = new String(input);
		String[] layers = copy.split("\n");
		
		// find seed
		int seed = 0;
		while (seed < layers[0].length() && layers[0].charAt(seed++) == 'S');

		Tree t = new Tree(seed);

		for (int i = 0; i < layers.length; i++)
			t.growRoot(layers[i]);

		return t.splits;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		// String input = Files.readString(Path.of("2025/7/1/input.txt"));
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
        System.out.printf("tachyon beam is split a total of %d times", totcount);
        // totcount  = sol.mySol(input);
        // System.out.printf("the grand total is %d of the available ingredient IDs are fresh\n", totcount);
    }
}
