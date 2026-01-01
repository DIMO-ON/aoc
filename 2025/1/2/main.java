import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public void mySol(int[] moves) {
        int actual = 50;
        int zeros = 0;

        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            zeros += move/100 + Math.abs
            actual = (actual + move) % 100;
            System.out.printf("%d\n", actual);
        }

        System.out.printf(":: count %d zeros ::\n", zeros);
    }

    public int[] parseElfString(String a) {
        a = a.replaceAll("L", "-");
        a = a.replaceAll("R", "+");
        String b[] = a.split("\n");
        int c[] = new int[b.length];

        for (int i = 0; i < b.length; i++)
            c[i] = Integer.parseInt(b[i]);

        return c;
    }
    
    public static void main(String[] args) throws IOException {
        String a = "L68\nL30\nR48\nL5\nR60\nL55\nL1\nL99\nR14\nL82";

        Main test = new Main();
        int[] c = test.parseElfString(a);
        test.mySol(c);

        // String input = Files.readString(Path.of("2025/1/2/input.txt"));
        // int[] d = test.parseElfString(input);
        // test.mySol(d);
    }
}
