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

// GUARDA LA CONSEGNA: fare solo le prime n giunzioni
class Main {
	private class ThreeDpt {
		private Long x, y, z;
		public Integer idxorigin; 

		ThreeDpt(Integer idx, Long x, Long y, Long z) {
			Long[] c = {x, y, z};
			this.set(idx, c);
		}

		ThreeDpt(Integer idx, Long[] c) {
			this.set(idx, c);
		}

		private void set(Integer idx, Long[] c) {
			assert c != null && c.length == 3 : "array of Long must have 3 element"; 
			this.idxorigin = idx;
			this.x = c[0];
			this.y = c[1];
			this.z = c[2];
		}


		public Long distance(ThreeDpt o) {
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

		public void print(String endl) {
			if (endl == null) endl = "\n";
			System.out.printf("%d\t%d\t%d%s", this.x, this.y, this.z, endl);
		}

	}

	private class Line {
		private ThreeDpt a;
		private ThreeDpt b;

		public Long distance() {
			return a.distance(b);
		}

		public Line(ThreeDpt a, ThreeDpt b) {
			this.a = a;
			this.b = b;
		}

		public void print() {
			a.print(" - > ");
			b.print(" : ");
			System.out.printf("%d\n", this.distance());
		}

		public void printIdxs() {
			System.out.printf("%d -> %d: %d\n", a.idxorigin, b.idxorigin, this.distance());

		}

		public void printABidxs() {
			System.out.println(this.a.idxorigin);
			System.out.println(this.b.idxorigin);
		}

	}

	public Integer findSet(ArrayList<TreeSet<Integer>> sets, int idx) {
		for (int i = 0; i < sets.size(); i++)
			if (sets.get(i).contains(idx))
				return i;

		return -1;
		
	}


    public Long mySol(String input, int ptslimit) {
		String[] copy = input.split("\n");
		ArrayList<Long[]> parse_coordinates = Arrays.stream(copy)
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong).toArray(Long[]::new)
			)
			.collect(Collectors.toCollection(ArrayList::new));

		ArrayList<ThreeDpt> coordinates = IntStream.range(0, parse_coordinates.size())
			.mapToObj(i -> new ThreeDpt(i, parse_coordinates.get(i)))
			.collect(Collectors.toCollection(ArrayList::new));

		// kruskal
		ArrayList<Line> distances = new ArrayList<Line>();
		for (int i = 0; i < coordinates.size(); i++) {
			for (int j = i + 1; j < coordinates.size(); j++) {
				distances.add(new Line(coordinates.get(i), coordinates.get(j)));
			}
		}

		distances.sort((a, b) -> {
			return a.distance().compareTo(b.distance());
		});

		ArrayList<TreeSet<Integer>> circuits = new ArrayList<TreeSet<Integer>>();
		for (Integer i = 0; i < coordinates.size(); i++) {
			circuits.add(new TreeSet<Integer>());
			circuits.get(i).add(i);
		}


		int count = 0;
		for (Line l: distances) {
			count += 1;
			if (count > ptslimit) break;
			int idxseta = findSet(circuits, l.a.idxorigin);
			int idxsetb = findSet(circuits, l.b.idxorigin);
			if (idxseta < 0 || idxsetb < 0 || idxseta == idxsetb) continue;

			circuits.get(idxseta).addAll(circuits.get(idxsetb));
			circuits.remove(idxsetb);
		}

		circuits.sort((a, b) -> {return (new Integer(b.size())).compareTo(a.size());});

		Long re = 1l;
		for (int i = 0; i < circuits.size() && i < 3; i++) {
			re *= circuits.get(i).size();
			System.out.println(circuits.get(i));
		}

		return re;

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

        Long totcount = 0l;
        totcount = sol.mySol(example, 10);
		totcount = sol.mySol(input, 1000);
        System.out.printf("mult three largest circuits: %d", totcount);
    }
}
