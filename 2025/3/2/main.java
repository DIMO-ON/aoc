import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    private String[] input;
    public int joltsize = 12;

    public Main(String input) {
        this.input = parseElfString(input);
    }

    public String[] parseElfString(String a) {
        String b[] = a.replaceAll("\n", ",").split(",");
        return b;
    }

    public void mySol() {
        Long res = 0l;
        for (int i = 0; i < this.input.length; i++) {
            Long actual = this.largestValue(this.input[i], this.joltsize);
            res += actual; 
            System.out.println(actual);
        }

        System.out.printf("::: res = %d :::\n", res);
    }

    public Long largestValue(String a, int joltsize) {
        int idxmaxval = 0;
        int i = idxmaxval;

        StringBuilder max = new StringBuilder();
        // starting from the index of max value to right
        // check max values in the subset [idxactualmaxvalue + 1, a.len - joltsize + max.len]
        // while max.len < joltsize
        while (max.length() < joltsize) {
            if (i >= a.length() - joltsize + 1 + max.length()) {
                max.append(a.charAt(idxmaxval));
                idxmaxval += 1;
                i = idxmaxval;
            }

            if (idxmaxval < a.length() && a.charAt(idxmaxval) < a.charAt(i)) {
                idxmaxval = i;
            }

            i++;

            // System.out.printf("%c %s\n", a.charAt(i), max.toString());

        }

        // System.out.println(max);
        // System.exit(0);
        return Long.parseLong(max.toString());
    }


    public static void main(String[] args) throws IOException {
        Main ex = new Main("""
                987654321111111
                811111111111119
                234234234234278
                818181911112111""");

        ex.mySol();

        Main input = new Main(Files.readString(Path.of("2025/3/2/input.txt")));
        input.mySol();
    }
}
