import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public char roll  = '@';
    public char empty = '.';

    public String[] parseElfString(String a) {
        String[] grid = a.split("\n");
        for (int i = 0; i < grid.length; i++) {
            grid[i] = grid[i].trim();
        }
        return grid;
    }

    public int checkAccess(String[] grid, int x, int y) {
        char e  = x + 1 >=grid[y].length()? this.empty: grid[y].charAt(x + 1);
        char w  = x - 1 < 0               ? this.empty: grid[y].charAt(x - 1);
        char n  = y - 1 < 0               ? this.empty: grid[y - 1].charAt(x);
        char s  = y + 1 >=grid.length     ? this.empty: grid[y + 1].charAt(x);
        
        char ne = x + 1 >=grid[y].length()
               || y - 1 < 0               ? this.empty: grid[y - 1].charAt(x + 1);

        char nw = x - 1 < 0               
               || y - 1 < 0               ? this.empty: grid[y - 1].charAt(x - 1);

        char se = x + 1 >=grid[y].length()
               || y + 1 >=grid.length     ? this.empty: grid[y + 1].charAt(x + 1);

        char sw = x - 1 < 0               
               || y + 1 >=grid.length     ? this.empty: grid[y + 1].charAt(x - 1);

        int rolls = (e  == this.roll? 1: 0) +
                    (w  == this.roll? 1: 0) + 
                    (n  == this.roll? 1: 0) + 
                    (s  == this.roll? 1: 0) + 
                    (ne == this.roll? 1: 0) + 
                    (nw == this.roll? 1: 0) + 
                    (se == this.roll? 1: 0) + 
                    (sw == this.roll? 1: 0);

        if (rolls < 4) return 1;
        else           return 0;

    }


    public void mySol(String[] grid) {
        int totAccess = 0;
        for (int i = 0; i < grid.length; i++) {
            System.out.println(grid[i]);
            for (int j = 0; j < grid[i].length(); j++) {
                if (grid[i].charAt(j) == this.roll)
                    totAccess += this.checkAccess(grid, j, i);
            }
        }

        System.out.printf("\nthere are %d rolls of paper that can be accessed by a forklift\n", totAccess);

    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String b = Files.readString(Path.of("2025/4/1/input.txt"));
        // String a = """
        //             ..@@.@@@@.
        //             @@@.@.@.@@
        //             @@@@@.@.@@
        //             @.@@@@..@.
        //             @@.@@@@.@@
        //             .@@@@@@@.@
        //             .@.@.@.@@@
        //             @.@@@.@@@@
        //             .@@@@@@@@.
        //             @.@.@@@.@.
        // """;
        // String[] gridA = sol.parseElfString(a);
        // sol.mySol(gridA);
        
        String[] gridB = sol.parseElfString(b);
        sol.mySol(gridB);
    }
}
