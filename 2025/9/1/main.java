import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Math;
import java.util.TreeSet;
import java.util.Set;

class Main {
    public Long mySol(String input) {
		ArrayList<ArrayList<Long>> vertices = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.collect(Collectors.toCollection(ArrayList::new));

		vertices.stream().forEach(i -> System.out.println(i));

		Long maxarea = 0l;
		

		for (int i = 0; i < vertices.size(); i++) {
			ArrayList<Long> pointa = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j++) {
				ArrayList<Long> pointb = vertices.get(j);
				Long height = Math.abs(pointa.get(1) - pointb.get(1) + 1);
				Long base   = Math.abs(pointa.get(0) - pointb.get(0) + 1);
				Long actualarea = height * base;
				maxarea = maxarea < actualarea? actualarea: maxarea; 
			}
		}


		return maxarea;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/9/input.txt"));
        String example = "";
		example += "7,1\n";
		example += "11,1\n";
		example += "11,7\n";
		example += "9,7\n";
		example += "9,5\n";
		example += "2,5\n";
		example += "2,3\n";
		example += "7,3\n";

        Long totcount = 0l;
        totcount = sol.mySol(example);
		totcount = sol.mySol(input);
        System.out.printf("max area: %d", totcount);
    }
}
