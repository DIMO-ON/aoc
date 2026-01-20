/*
 * data una lista di tot numeri, cerca 2 numeri in sequenza che formano il valore piu' grande
 * ex. [81119] -> 89
 * ex. [12345] -> 45
 * trovo il massimo, poi trovo il secondo ma tengo sempre conto dell'ordine
 * 89 8 stava a destra di 9: cerco il massimo da 2 posizioni contemporanee (destr/sinistra del primo massimo trovato)
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
        int res = 0;
        for (int i = 0; i < this.input.length; i++) {
            int actual = this.largestValue(this.input[i]); 
            System.out.println(actual);
            res += actual;
        }

        System.out.printf("::: res = %d :::\n", res);
    }

    public int largestValue(String a) {
        // find max value
        int exclude = 0;
        for (int i = 0; i < a.length(); i++)
            if (a.charAt(exclude) < a.charAt(i)) exclude = i;

        // System.out.println(a.charAt(exclude));
        
        // find the biggest 2 digits value
        int max = 0;
        for (int i = 0; i < a.length(); i++) {
            int actual = this.twodigitdecimal(a, exclude, i);
            if (i != exclude && actual > max)
                max = actual;
        }

        // System.exit(0);
        return max;

    }

    public int twodigitdecimal(String a, int i, int j) {
        if (i < j) {
            return (int) (a.charAt(i) - 48) * 10 + (int) (a.charAt(j) - 48);
        }

        return (int) (a.charAt(j) - 48) * 10 + (int) (a.charAt(i) - 48);
    }
    

    public static void main(String[] args) throws IOException {
        Main ex = new Main("""
                987654321111111
                811111111111119
                234234234234278
                818181911112111""");

        ex.mySol();

        Main input = new Main(Files.readString(Path.of("2025/3/1/input.txt")));
        input.mySol();
    }
}
