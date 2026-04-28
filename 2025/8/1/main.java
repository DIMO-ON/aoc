import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.lang.Math;
import java.util.TreeSet;
import java.util.Set;


class Main {
	private class Coordinate {
		private Long x, y, z;

		Coordinate(Long x, Long y, Long z) {
			Long[] c = {x, y, z};
			this.set(c);
		}

		Coordinate(Long[] c) {
			this.set(c);
		}

		private void set(Long[] c) {
			assert c != null && c.length == 3 : "array of Long must have 3 element"; 
			this.x = c[0];
			this.y = c[1];
			this.z = c[2];
		}


		public Long distance(Coordinate o) {
			assert this != o: "same element";
			return this.distance(o.x, o.y, o.z);
		}

		public Long distance(Long x, Long y, Long z) {
			return (long) Math.sqrt(
						Math.pow(this.x - x, 2) +
						Math.pow(this.y - y, 2) +
						Math.pow(this.z - z, 2)
						);
		}

		public void print() {
			System.out.printf("%d\t%d\t%d\n", this.x, this.y, this.z);
		}

	}

	public TreeSet<Integer> minDistance(ArrayList<Coordinate> all) {
		if (all.size() <= 1) return null;
		Long min = 99999999999999999l;
		Integer aindex = 0;
		Integer bindex = 0;

		for (int i = 0; i < all.size(); i++) {
			for (int j = i + 1; j < all.size(); j++) {
				Long actualdist = 0l;
				actualdist = all.get(i).distance(all.get(j));
				if (actualdist < min) {
					min = actualdist;
					aindex = i;
					bindex = j;
				}
			}
		}

		return new TreeSet<Integer>(Set.of(aindex, bindex));
	}

    public Long mySol(String input) {
		String[] copy = input.split("\n");
		ArrayList<Long[]> parse_coordinates = Arrays.stream(copy)
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong).toArray(Long[]::new)
			)
			.collect(Collectors.toCollection(ArrayList::new));

		ArrayList<Coordinate> coordinates = parse_coordinates.stream()
			.map(Coordinate::new)
			.collect(Collectors.toCollection(ArrayList::new));

		ArrayList<Set<Integer>> graphs = new ArrayList<Set<Integer>>();

		while (coordinates.size() > 0) {
			TreeSet<Integer> s = minDistance(coordinates);
			if (s == null) break;
			for (Set<Integer> g: graphs) {
				Set<Integer> diff = new TreeSet<>(s);
				if (diff.removeAll(g)) {
					g.addAll(s);
					break;
				}
			}
			coordinates.remove((int) s.last());

			if (s.size() >= 2) graphs.add(s);
		}


		// graphs.sort(Comparator.comparingInt((TreeSet<Integer> s) -> s.size()).reversed());
		for (Set g: graphs) System.out.println(g.size());

		Integer mul = 0;

		for (int i = 0; i < 3; i++) mul *= graphs.get(i).size();

		return 0l;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/8/1/input.txt"));
        String example = "";
		example += "162,817,812\n";
		example += "57,618,57\n";
		example += "906,360,560\n";
		example += "592,479,940\n";
		example += "352,342,300\n";
		example += "466,668,158\n";
		example += "542,29,236\n";
		example += "431,825,988\n";
		example += "739,650,466\n";
		example += "52,470,668\n";
		example += "216,146,977\n";
		example += "819,987,18\n";
		example += "117,168,530\n";
		example += "805,96,715\n";
		example += "346,949,466\n";
		example += "970,615,88\n";
		example += "941,993,340\n";
		example += "862,61,35\n";
		example += "984,92,344\n";
		example += "425,690,689\n";

        Long totcount  = sol.mySol(example);
		// totcount  = sol.mySol(input);
        System.out.printf("mult three largest circuits: %d", totcount);
    }
}
