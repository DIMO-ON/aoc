import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public void mySol(String[] ranges) {
        for (int i = 0; i < ranges.length; i += 2) {
            String start = ranges[i];
            String end   = ranges[i+1];
            System.out.println(start + "-" + end);
        }
    }


    public String[] parseElfString(String a) {
        String b[] = a.replaceAll("-", ",").split(",");
        return b;
    }
    
    public static void main(String[] args) throws IOException {
        String a = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

        Main test = new Main();
        String[] c = test.parseElfString(a);
        test.mySol(c);

        // String input = Files.readString(Path.of("2025/1/1/input.txt"));
        // int[] d = test.parseElfString(input);
        // test.mySol(d);
    }
}
