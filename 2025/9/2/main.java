import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Math;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

class Main {
	private static class Pt2D implements Comparable<Pt2D> {
		public Long x, y;

		@Override
		public int compareTo(Pt2D o) {
			int cmp = this.x.compareTo(o.x);
			return (cmp != 0) ? cmp : this.y.compareTo(o.y);
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Pt2D)) return false;
			Pt2D o = (Pt2D) obj;
			return x.equals(o.x) && y.equals(o.y);
		}

		@Override
		public int hashCode() {
			return 31 * x.hashCode() + y.hashCode();
		}

		public Pt2D(Pt2D o) {
			this.y = o.y;
			this.x = o.x;
		}

		public Pt2D(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public Pt2D(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		public boolean rayCast(ArrayList<Pt2D> polygonperimeter) {
			// true se dentro, flase se fuori
			int count = 0;
			int size  = polygonperimeter.size();
			for (int i = 0; i < size; i++) {
				Pt2D p = polygonperimeter.get(i);
				int previdx = (i - 1) % size;
				if (previdx < 0)
				{
					previdx += size;
				}
				Pt2D prev = polygonperimeter.get(previdx);
				Pt2D next = polygonperimeter.get((i+1) % size);
				if (p == this) return true;
				if (p.y.equals(this.y) && p.x.compareTo(this.x) > 0) {
					if (prev.y.compareTo(p.y) <= 0 && p.y.compareTo(next.y) < 0) {
						count += 1;
				    }
				}
			}

			// System.out.println(count);

			return (count % 2) == 1;
		}

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + "}";
		}
	}


	public Map<Pt2D, Pt2D> coordCompression(ArrayList<Pt2D> pts) {
		ArrayList<Long> xset = new ArrayList<>(pts.stream()
			.map(p -> p.x)
			.collect(Collectors.toCollection(TreeSet::new)));

		ArrayList<Long> yset = new ArrayList<>(pts.stream()
			.map(p -> p.y)
			.collect(Collectors.toCollection(TreeSet::new)));

		// System.out.println(xset);
		// System.out.println(yset);

		Map<Pt2D, Pt2D> compressed = new HashMap<>();
		for (Pt2D p: pts) {
			Long compx = (long) xset.indexOf(p.x);
			Long compy = (long) yset.indexOf(p.y);
			compressed.put(p, new Pt2D(compx, compy));
		}
		return compressed;
	}

	public void printArea(List<Pt2D> polygon, List<Pt2D> rectangle) {
		// System.out.println("\n" + rectangle + "\n");
		Long width  = polygon.stream().mapToLong(i -> i.x).max().getAsLong() + 3;
		Long height = polygon.stream().mapToLong(i -> i.y).max().getAsLong() + 3;
		List<StringBuilder> area = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < height; j++) {
				row.append('.');
			}
			area.add(row);
		}

		for (Pt2D p: polygon) {
			area.get(p.y.intValue())
				.setCharAt(p.x.intValue(), '#');
		}

		if (rectangle != null) {
			for (Pt2D p: rectangle) {
				area.get(p.y.intValue())
					.setCharAt(p.x.intValue(), '@');
			}
		}


		area.forEach(System.out::println);
	}

	public static ArrayList<Pt2D> perimeter(List<Pt2D> compressed_points) {
		// System.out.println(compressed_points);
		// return null;
		ArrayList<Pt2D> compressed_perimeter_points = new ArrayList<>();
		for (int i = 0; i < compressed_points.size(); i++) {
			Pt2D a = compressed_points.get(i);
			Pt2D b = compressed_points.get((i+1) % compressed_points.size());
			Pt2D c = new Pt2D(a);

			int xinc = a.y.equals(b.y)? (a.x.compareTo(b.x) <= 0? 1: -1): 0;
			int yinc = a.x.equals(b.x)? (a.y.compareTo(b.y) <= 0? 1: -1): 0;
			// System.out.println(xinc + " " + yinc);
			// break;

			while (!c.equals(b)) {
				compressed_perimeter_points.add(new Pt2D(c));
				c.x += xinc;
				c.y += yinc;
				// break;
			}

		}

		return compressed_perimeter_points;
	}

    public Long mySol(String input) {
		ArrayList<Pt2D> points = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(Pt2D::new)
			.collect(Collectors.toCollection(ArrayList::new));

		// comprimere l'input
		ArrayList<Pt2D> compressed_points = new ArrayList<>();

		// mappa per comprimere
		Map<Pt2D, Pt2D> origTocomp = this.coordCompression(points);
		// mappa per decomprimere
		Map<Pt2D, Pt2D> compToorig = new HashMap<>();

		for (Pt2D p: points) {
			Pt2D compressed = origTocomp.get(p);
			// estrarre solo i punti compressi (valori) dalla mappa
			compressed_points.add(compressed);
			compToorig.put(compressed, p);
		}


		// System.out.println("vertices: " + vertices.size());
		// printArea(compressed_points, null);


		// costruire il perimetro compresso sotto forma di punti
		ArrayList<Pt2D> compressed_perimeter_points = perimeter(compressed_points);

		// System.out.println("sides: " + compressed_perimeter_points.size());
		Pt2D prova = new Pt2D(1l, 2l);
		printArea(compressed_perimeter_points, List.of(prova));
		System.out.println(prova.rayCast(compressed_perimeter_points));

		// accumulare i vertici (compressi) per formare tutti possibili rettangoli
		// TODO: mappare ad ogni coppia di punti orig l'area rispetto al perimetro compresso
		ArrayList<List<Pt2D>> rectvertices = new ArrayList<>();
		for (int i = 0; i < compressed_points.size(); i++) {
		 	for (int j = i + 1; j < compressed_points.size(); j++) {
				Pt2D v1 = compressed_points.get(i);
				Pt2D v3 = compressed_points.get(j % compressed_points.size());
				Pt2D v2 = new Pt2D(v1.x, v3.y);
				Pt2D v4 = new Pt2D(v3.x, v1.y);
		 		rectvertices.add(List.of(v1,v2,v3,v4));
			}
		}
		// System.out.println("rectvertices: " + rectangles.size());
		// printArea(compressed_perimeter_points, rectvertices.get(0));


		// costruire tutti i perimetri di tutti i rettangoli possibili sotto forma di pt2d
		List<List<Pt2D>> rectperimeters = rectvertices.stream().map(l -> perimeter(l)).collect(Collectors.toList());
		rectperimeters.sort((a,b) -> b.size() - a.size());
		// printArea(compressed_perimeter_points, rectperimeters.get(0));


		// rectangles.forEach(r -> r.perimeter());
		// filtrare i rettangoli che non sono dentro il perimetro
		// rectangles.removeIf(r -> !r.isInscribed(sides));
		// System.out.println("inscribed rectangles: " + rectangles.size());

		// ordino (decrescente) i rettangoli per area (compressa)
		// rectangles.sort((a, b) -> b.area().compareTo(a.area()));
		// rectangles.forEach(System.out::println);

		System.out.println("\n");

		// printArea(compressed_points, rectangles.get(0));
		// System.out.println(rectangles.get(0).perimeter());
		// printArea(compressed_points, new Rectangle(new Pt2D(0l, 0l), new Pt2D(0l,0l)));
		// Pt2D v1 = new Pt2D(9l, 5l);
		// Pt2D v2 = new Pt2D(2l, 3l);
		// Pt2D v1c = origTocomp.get(v1);
		// Pt2D v2c = origTocomp.get(v2);
		// Rectangle prova = new Rectangle(v1c, v2c);
		// printArea(compressed_points, prova);
		// System.out.println(sides);	
		// System.out.println(prova.isInscribed(sides));	
		// System.out.println("\n");


		// prendo i vertici originali del rettangolo compresso più grande
		// decomprimendo i vertici e ricalcolo il rettangolo (quindi l'area originale) con i vertici originali
		Long maxarea = 0l;
		// Rectangle biggest = rectangles.get(0);
		// Rectangle biggestcompressed = rectangles.get(0);
		// for (Rectangle r: rectangles) {
		// 	Rectangle decompressed = new Rectangle(
		// 		compToorig.get(r.vertix1),
		// 		compToorig.get(r.vertix2)
		// 	);

		// 	// maxarea = maxarea.compareTo(decompressed.area()) >= 0? maxarea: decompressed.area();
		// 	if (maxarea.compareTo(decompressed.area()) < 0) {
		// 		maxarea = decompressed.area();
		// 		biggestcompressed = r;
		// 		biggest = decompressed;
		// 	}
		// }

		// {x=217, y=122}
		// System.out.println(biggest);
		// printArea(compressed_points, new Rectangle(new Pt2D(216l,122l), new Pt2D(216l, 122l)));
		// printArea(sides, biggestcompressed);
		// printArea(sides);
		return maxarea;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/9/input.txt"));
		String example = Files.readString(Path.of("2025/9/example.txt"));
		// String example2 = Files.readString(Path.of("2025/9/example2.txt"));

        System.out.printf("max area: %d\n\n", sol.mySol(example));
        // System.out.printf("max area: %d\n\n", sol.mySol(example2));
        // System.out.printf("max area: %d\n", sol.mySol(input));
    }
}
