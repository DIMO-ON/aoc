import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public String[] parseElfString(String a) {
        String[] grid = a.split("\n");
        // for (String w : grid) {
        for (int i = 0; i < grid.length; i++) {
            grid[i] = grid[i].trim();
        }
        return grid;
    }

    public void mySol(String[] grid) {
        for (String w : grid) {
            System.out.println(w);
        }
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        // String b = Files.readString(Path.of("2025/3/2/input.txt"));
        String a = """
                    ..@@.@@@@.
                    @@@.@.@.@@
                    @@@@@.@.@@
                    @.@@@@..@.
                    @@.@@@@.@@
                    .@@@@@@@.@
                    .@.@.@.@@@
                    @.@@@.@@@@
                    .@@@@@@@@.
                    @.@.@@@.@.
        """;
        String[] gridA = sol.parseElfString(a);
        sol.mySol(gridA);
    }
}
