import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    private String[] input;

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
            Long actual = this.largestValue(this.input[i], 4);
            res += actual; 
            System.out.println(actual);
        }

        System.out.printf("::: res = %d :::\n", res);
    }

    public Long largestValue(String a, int joltsize) {
        int  idxmaxval = a.length() - joltsize;

        // find latest max value starting from len - joltsize to left
        for (int i = a.length() - joltsize; i >= 0; i--)
            if (a.charAt(i) >= a.charAt(idxmaxval))
                idxmaxval = i;

        StringBuilder max= new StringBuilder();
        // starting from the index of maxb value to right
        // build string maxb if a[idxmaxbval + i] > a[len - joltsize + j]
        for (int i = idxmaxval; i < a.length() - joltsize; i++) {
            if (max.length() > 0 && max.charAt(max.length() - 1) < a.charAt(i))
                this.replace(max, a.charAt(i));

            if (max.length() < joltsize && a.charAt(i) >= a.charAt(a.length() - joltsize))
                max.append(a.charAt(i));

            // System.out.printf("%c %s\n", a.charAt(i), max.toString());

        }


        int count_subs = max.length();
        for (int i = a.length() - joltsize; i < a.length(); i++) {
            if (count_subs > 0 && max.length() > 0 && max.charAt(max.length() - 1) < a.charAt(i)) {
                max.deleteCharAt(max.length() - 1);
                count_subs--;
            }

            if (max.length() < joltsize)
                max.append(a.charAt(i));

            // System.out.printf("%c %s\n", a.charAt(i), max.toString());

        }
        

        // System.out.println(max);
        // System.exit(0);
        return Long.parseLong(max.toString());
    }

    void replace(StringBuilder s, char v) {
        for (int i = s.length() - 1; s.charAt(i) < v && i >= 0; i--) {
            s.deleteCharAt(i);
        }
    }


    public static void main(String[] args) throws IOException {
        Main ex = new Main("""
                987654321111111
                811111111111119
                234234234234278
                818181911112111""");

        ex.mySol();

        // Main input = new Main(Files.readString(Path.of("2025/3/2/input.txt")));
        // input.mySol();
    }
}
