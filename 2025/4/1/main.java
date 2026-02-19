import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public String[] parseElfString(String a) {
    }

    public void mySol(String[][] input) {
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String b = Files.readString(Path.of("2025/3/2/input.txt"));
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
