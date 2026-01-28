/*
 * togliere i miniori per formare il numero piu' grande da 12 cifre
 * 987654321111111 -> [987654321111] 111 
 *
 * trasformo la stringa in numero e tolgo tutte le combinazioni da 3 cifre finche non trovo il valore maggiore
 *
 */

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
            Long actual = this.largestValue(this.input[i]);
            res += actual; 
            System.out.println(actual);
        }

        System.out.printf("::: res = %d :::\n", res);
    }

    public Long largestValue(String a) {
        Long max = 0l;
        for (int first = 0; first < a.length(); first++) {
            for (int second = first + 1; second < a.length(); second++) {
                for (int third = second + 1; third < a.length(); third++) {
                    Long actual = this.exclude(a, first, second, third);
                    if (actual > max) max = actual; 
                }
            }
        }

        // System.exit(0);
        return max;
    }

    public Long exclude(String a, int first, int second, int third) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < a.length(); i++)
            if (i != first && i != second && i != third)
                b.append(a.charAt(i));

        // System.exit(0);
        return Long.parseLong(b.toString());
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
