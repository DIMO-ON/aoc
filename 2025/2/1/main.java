import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    public void mySol(String[] ranges) {
        Long res = 0;
        for (int i = 0; i < ranges.length; i += 2) {
            ranges[i  ] = ranges[i  ].replaceAll("^[0]*", "").trim();
            ranges[i+1] = ranges[i+1].replaceAll("^[0]*", "").trim();
            Long start = Long.parseLong(ranges[i  ]);
            Long end   = Long.parseLong(ranges[i+1]);
            
            while (start <= end) {
                res += this.checkId(start);
                int idlen = this.idLen(start);
                if (!idlen % 2) start += 1;
            }

            System.out.printf("%s-%s\n", ranges[i], ranges[i + 1]);
        }
    }

    int idLen(static Long code) {
        String id = code.toString();
        return id.length();
    }

    Long checkId(static Long code) {
        String id = code.toString();
        int len = id.length();
        String l = id.substring(0, len/2);
        String r = id.substring(len/2, len);

        if (l.compareTo(r) == 0)
            return code;
        else
            return 0;
    }


    public String[] parseElfString(String a) {
        String b[] = a.replaceAll("-", ",").split(",");
        return b;
    }
    
    public static void main(String[] args) throws IOException {
        String a = "011-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

        Main test = new Main();
        String[] c = test.parseElfString(a);
        test.mySol(c);

        // String input = Files.readString(Path.of("2025/1/1/input.txt"));
        // int[] d = test.parseElfString(input);
        // test.mySol(d);
    }
}
