import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
    class Range {
        public Long left; // left < right
        public Long right;

        public Range(String s) {
            s = s.trim();
            // assert regex /d+-/d+
            String[] values = s.split("-");
            values[0] = values[0].replaceAll("^[0]*", "").trim();
            values[1] = values[1].replaceAll("^[0]*", "").trim();
            Long a = Long.parseLong(values[0]);
            Long b = Long.parseLong(values[1]);

            this.left  = a.compareTo(b) <= 0? a: b;
            this.right = a.compareTo(b) <= 0? b: a;
        }

        public boolean include(Long v) {
            return v >= this.left && v <= this.right;
        }
    }

    class Receipt {
        public ArrayList<Range> ranges;
        public ArrayList<Long> ids;

        Receipt(String input) {
            String[] parse = input.split("\n");

            this.ranges = new ArrayList<Range>();
            this.ids    = new ArrayList<Long>();

            // parse input
            for (int i = 0; i < parse.length; i++) {
                parse[i] = parse[i].trim();
                System.out.println(parse[i]);
                if (parse[i].contains("-")) this.ranges.add(new Range(parse[i]));
                // else if (parse[i].length() > 0) this.ids.add(Long.parseLong(parse[i]));
            }
        }

        public void sortRanges() {
            Collections.sort(ranges, new Comparator<Range>() {
                @Override
                public int compare(Range a, Range b) {
                    return a.left.compareTo(b.left);
                }
            });
        }

        public void print() {
            System.out.println("==================================");
            System.out.println("\t\t\tprint receipt\t\t\t");
            System.out.println("\t\t\tPRINT RANGES\t\t\t");
            for (Range r: this.ranges) System.out.printf("%d-%d\n", r.left, r.right);
            System.out.println("\t\t\tPRINT IDS\t\t\t");
            for (Long i: this.ids) System.out.printf("%d\n", i);
            System.out.println("==================================");
        }
    }
    
    public Long mySol(String input) {
        Receipt receipt = new Receipt(input);
        receipt.sortRanges();
        receipt.print();
        Long count = 0l;
        Long left  = 0l;
        Long right = 0l;
        for (Range r: receipt.ranges) {
            // if (r.left.compareTo(r.right) >= 0) System.out.printf("%d-%d\n", r.left, r.right);
            // if (r.left.compareTo(r.right) >= 0) System.out.printf("%d - %d left >= right\n", r.left, r.right);
            // collect overlaped ranges
            if (r.left.compareTo(right) > 0) {
                count += (right - left + 1);
                left   = r.left;
                right  = r.right;
            }
           
            else if (right.compareTo(r.right) <= 0 && left.compareTo(r.left) <= 0) {
                right = r.right;
            }

            // System.out.printf("%d %d -> %d %d\n", r.left, r.right, left, right);
        }
        count += (right - left);
        return count;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        String input = Files.readString(Path.of("2025/5/2/input.txt"));
        // String input = Files.readString(Path.of("2025/5/2/input2.txt"));
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
        // Long totcount  = sol.mySol(example);
        Long totcount  = sol.mySol(input);
        System.out.printf("%d of the available ingredient IDs are fresh\n", totcount);
    }
}
