/*
 * togliere i miniori per formare il numero piu' grande da 12 cifre
 * 987654321111111 -> [987654321111] 111 
 *
 * trasformo la stringa in numero e tolgo tutte le combinazioni da 3 cifre finche non trovo il valore maggiore
 * oppure provo tutte le combinazioni da 12 cifre dato che il puzzle input contiene codici da 100 cifre
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

        int[] combo = {0,1,2,3,4,5,6,7,8,9,10,11};

        for (int i = combo.length - 1; i >= 0; i--) {
            for (; i + 1 < combo.length && combo[i] < combo[i + 1] || combo[i] < a.length(); combo[i]++) {
                Long actual = this.combineLong(a, combo);
                if (actual > max) max = actual; 
            }
            combo[i] = combo[i - 1] + 2;
        }

        // System.exit(0);
        return max;
    }

    public Long combineLong(String a, int[] idx) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < idx.length; i++)
            b.append(a.charAt(idx[i]));

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
