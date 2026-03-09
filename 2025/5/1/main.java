import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Main {
    class Range {
        public Long left;
        public Long right;

        public Range(String s) {
            // assert regex /d+-/d+
            String[] values = s.split("-");
            values[0] = values[0].replaceAll("^[0]*", "").trim();
            values[1] = values[1].replaceAll("^[0]*", "").trim();

            this.left  = Long.parseLong(values[0]);
            this.right = Long.parseLong(values[1]);
        }

        public boolean include(Long v) {
            return v >= left && v <= right;
        }
    }

    class Reciept {
        public List<Range> ranges;
        public List<Long> ids;

        Reciept(String input) {
            String[] parse = input.split("\n");

            this.ranges = new List<Range>[];
            this.ids    = new List<Long>[];

            int i = 0;

            // parse input
            for (int i = 0; i < parse.length(); i++) {
                System.out.println(parse[i]);
                if (parse[i].contains("-")) this.ranges.add(new Range(parse[i]));
                else if (parse[i].length > 0) this.ids.add(Long.parseLong(parse[i]));
            }
        }
    }
    
    public int mySol(String input) {
        Reciept r = new Reciept(input);
        int count = 0;
        for (Range r: r.ranges) {
            for (int id: r.ids) {
                if (r.include(id)) count += 1;
            }
        }

        return count;
    }

    public void mySol(String[] ranges) {
        Long res = 0L;
        int totCount = 0;
        for (int i = 0; i < ranges.length; i += 2) {
            int count   = 0;
            
            while (start <= end) {
                int idlen = this.idLen(start);
                if ((idlen % 2) == 0) {
                    Long check = this.checkId(start);
                    
                    if (check.compareTo(0L) > 0) {
                        res   += check;
                        count += 1;
                        // System.out.println(start);
                    }
                    start += 1;

                }
                else {
                    start = (long) Math.pow(10, idlen);
                }
            }

            totCount += count;

            System.out.printf("%s-%s: %d\n", ranges[i], ranges[i + 1], count);
        }

        System.out.printf("::: sum of nonIds is %d summing %d longs :::\n", res, totCount);
    }

    int idLen(Long code) {
        String id = code.toString();
        return id.length();
    }

    Long checkId(Long code) {
        String id = code.toString();
        int len = id.length();
        String l = id.substring(0, len/2);
        String r = id.substring(len/2, len);

        if (l.compareTo(r) == 0)
            return code;
        else
            return 0L;
    }


    public String[] parseElfString(String a) {
        String b[] = a.replaceAll("-", ",").split(",");
        return b;
    }
    

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
        // String a = Files.readString(Path.of("2025/4/1/input.txt"));
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
        // String[] grid = sol.parseElfString(a);
        // int totcount  = sol.mySol(grid);
        int totcount  = sol.mySol(ranges, ids);
        System.out.printf("\nthere are %d rolls of paper that can be accessed by a forklift\n", totcount);
    }
}
