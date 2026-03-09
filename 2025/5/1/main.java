import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;

class Main {
    class Range {
        public Long left;
        public Long right;

        public Range(String s) {
            s = s.trim();
            // assert regex /d+-/d+
            String[] values = s.split("-");
            values[0] = values[0].replaceAll("^[0]*", "").trim();
            values[1] = values[1].replaceAll("^[0]*", "").trim();

            this.left  = Long.parseLong(values[0]);
            this.right = Long.parseLong(values[1]);
        }

        public boolean include(Long v) {
            return v >= this.left && v <= this.right;
        }
    }

    class Reciept {
        public ArrayList<Range> ranges;
        public ArrayList<Long> ids;

        Reciept(String input) {
            String[] parse = input.split("\n");

            this.ranges = new ArrayList<Range>();
            this.ids    = new ArrayList<Long>();

            // parse input
            for (int i = 0; i < parse.length; i++) {
                parse[i] = parse[i].trim();
                System.out.println(parse[i]);
                if (parse[i].contains("-")) this.ranges.add(new Range(parse[i]));
                else if (parse[i].length() > 0) this.ids.add(Long.parseLong(parse[i]));
            }
        }

        public void print() {
            System.out.println("==================================");
            System.out.println("\t\t\tprint reciept\t\t\t");
            System.out.println("\t\t\tPRINT RANGES\t\t\t");
            for (Range r: this.ranges) System.out.printf("%d-%d\n", r.left, r.right);
            System.out.println("\t\t\tPRINT IDS\t\t\t");
            for (Long i: this.ids) System.out.printf("%d\n", i);
            System.out.println("==================================");
        }
    }
    
    public int mySol(String input) {
        Reciept r = new Reciept(input);
        r.print();
        int count = 0;
        for (Long id: r.ids) {
            for (Range range: r.ranges) {
                boolean check = range.include(id); 
                // System.out.printf("%d-%d: includes %d -> %b\n", range.left, range.right, id, check);
                if (check) {
                    count += 1;
                    break;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String input = Files.readString(Path.of("2025/5/1/input.txt"));
        String example = """
         3-5
         10-14
         16-20
         12-18
         
         1
         5
         8
         11
         17
         32
        """;
        int totcount  = sol.mySol(input);
        // int totcount  = sol.mySol(example);
        System.out.printf("%d of the available ingredient IDs are fresh\n", totcount);
    }
}
