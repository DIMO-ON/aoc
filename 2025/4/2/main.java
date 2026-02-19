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

        for (String l: grid) System.out.println(l);
        System.out.println("=========================");
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


    public int mySol(String[] grid) {
        int totRemoves = 0;
        StringBuilder[] gridtrack = new StringBuilder[grid.length];
        for (int i = 0; i < gridtrack.length; i++) gridtrack[i] = new StringBuilder(grid[i]);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length(); j++) {
                if (grid[i].charAt(j) == this.roll) {
                    int actualremove = this.checkAccess(grid, j, i);
                    totRemoves += actualremove;
                    if (actualremove >= 1) {
                        gridtrack[i].setCharAt(j, 'x');
                    }
                }
            }
        }

        for (StringBuilder l: gridtrack) System.out.println(l.toString());
        System.out.println("=========================");

        String[] newgrid = new String[grid.length];
        for (int i = 0; i < gridtrack.length; i++)
            newgrid[i] = gridtrack[i].toString().replaceAll("x",".");

        if (totRemoves <= 0) return 0;
        return totRemoves + mySol(newgrid);
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String a = Files.readString(Path.of("2025/4/1/input.txt"));
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
        String[] grid = sol.parseElfString(a);
        int totcount  = sol.mySol(grid);
        System.out.printf("\nthere are %d rolls of paper that can be accessed by a forklift\n", totcount);
    }
}
