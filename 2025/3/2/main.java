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
            Long actual = this.largestValue(this.input[i], 12);
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

        StringBuilder left = new StringBuilder();
        // starting from the index of max value to right
        // build string max if a[idxmaxval + i] > a[len - joltsize + j]
        for (int i = idxmaxval; i < a.length() - joltsize; i++) {
            if (left.length() > 0 && left.charAt(left.length() - 1) < a.charAt(i))
                left.deleteCharAt(left.length() - 1);

            if (a.charAt(i) >= a.charAt(a.length() - joltsize))
                left.append(a.charAt(i));
        }
        
        StringBuilder right = new StringBuilder(a.substring(a.length() - joltsize));

        for (int i = left.length(); i > 0; i--)
            this.deleteMin(right);

        String max = left.append(right.toString()).toString();
        // System.out.println(max);

        // System.exit(0);
        return Long.parseLong(max);
    }

    void deleteMin(StringBuilder s) {
        if (s.length() <= 0) return;
        int idxminval = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) < s.charAt(idxminval)) idxminval = i;

        s.deleteCharAt(idxminval);
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
